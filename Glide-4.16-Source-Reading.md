# Glide 4.16.0 源码研读

> 本文所有代码均来自本项目实际依赖的 `com.github.bumptech.glide:glide:4.16.0` 官方 sources jar，
> 已解压到本机 `$GLIDE_SRC = C:/Users/LTY/AppData/Local/Temp/glide-src-4.16.0`。
> 文中 `startLine:endLine:filepath` 引用均基于该目录，可对照查看。

项目中的使用点：

- `library_base/src/main/java/com/libase/utils/GlideUtils.java`
- `library_base/src/main/java/com/libase/adapter/commonBindingAdapter.java`
- `feature_user/.../changeInfo/ChangeInfoActivity.java`
- `feature_piazza/.../adapter/piazzaAdapter.java`

依赖声明：`gradle/libs.versions.toml:17` → `glide = "4.16.0"`，`library_base/build.gradle:54` → `api libs.glide`。

---

## 0. 全景：一次 `into()` 到底走了几层

```
Glide.with(ctx)                 ① 生命周期层     → RequestManager（绑定 Activity/Fragment/App）
     .load(url)                 ② 配置层         → RequestBuilder（模型 + RequestOptions）
     .apply(options)            ②                → 占位图/变换/缓存策略/尺寸
     .into(imageView)           ③ 目标层         → Target（尺寸测量 + 占位图 + 展示）
         └─ SingleRequest.begin()→ onSizeReady()
              └─ Engine.load()  ④ 缓存调度层     → 活动资源 → 内存缓存 → 磁盘/网络
                   └─ EngineJob.start(DecodeJob) ⑤ 线程与状态机层
                        └─ DecodeJob.run()       ⑥ 数据获取 + 解码 + 变换 + 转码 + 落盘
                             └─ Registry         ⑦ 注册表：ModelLoader/Decoder/Transcoder
                                  └─ BitmapPool  ⑧ Bitmap 复用池
```

一句话概括架构：**上层（with/load/into）负责"描述一个请求并绑定到生命周期"，中层（Engine）负责"用 Key 去重并从各级缓存取"，下层（DecodeJob）负责"取不到就按状态机去磁盘/网络取，然后解码-变换-转码-回写缓存"**。

---

## 1. 单例初始化：`Glide.get()` 装了哪些东西

### 1.1 双检锁 + 防递归

```126:141:$GLIDE_SRC/com/bumptech/glide/Glide.java
  @NonNull
  // Double checked locking is safe here.
  @SuppressWarnings("GuardedBy")
  public static Glide get(@NonNull Context context) {
    if (glide == null) {
      GeneratedAppGlideModule annotationGeneratedModule =
          getAnnotationGeneratedGlideModules(context.getApplicationContext());
      synchronized (Glide.class) {
        if (glide == null) {
          checkAndInitializeGlide(context, annotationGeneratedModule);
        }
      }
    }

    return glide;
  }
```

两个细节值得注意：

1. **`isInitializing` 标志位**（Glide.java:143-159）：初始化过程中若有代码回调 `Glide.get()`，会直接抛 `IllegalStateException("Glide has been called recursively")`，防止无限递归。
2. **注解处理器生成类**（Glide.java:271-301）：反射加载 `com.bumptech.glide.GeneratedAppGlideModuleImpl`。本项目**没有**引入 `com.github.bumptech.glide:compiler`，也没有 `@GlideModule` 类，所以这里返回 `null`，走的是"纯默认配置 + Manifest 解析"路径（4.16 里 Manifest 解析也基本是空集）。

### 1.2 `GlideBuilder.build()` 的默认值一览

```534:565:$GLIDE_SRC/com/bumptech/glide/GlideBuilder.java
    if (bitmapPool == null) {
      int size = memorySizeCalculator.getBitmapPoolSize();
      if (size > 0) {
        bitmapPool = new LruBitmapPool(size);
      } else {
        bitmapPool = new BitmapPoolAdapter();
      }
    }

    if (arrayPool == null) {
      arrayPool = new LruArrayPool(memorySizeCalculator.getArrayPoolSizeInBytes());
    }

    if (memoryCache == null) {
      memoryCache = new LruResourceCache(memorySizeCalculator.getMemoryCacheSize());
    }

    if (diskCacheFactory == null) {
      diskCacheFactory = new InternalCacheDiskCacheFactory(context);
    }

    if (engine == null) {
      engine =
          new Engine(
              memoryCache,
              diskCacheFactory,
              diskCacheExecutor,
              sourceExecutor,
              GlideExecutor.newUnlimitedSourceExecutor(),
              animationExecutor,
              isActiveResourceRetentionAllowed);
    }
```

| 组件 | 默认实现 | 默认大小/线程数 |
|---|---|---|
| `BitmapPool` | `LruBitmapPool` | `MemorySizeCalculator.getBitmapPoolSize()`（见下） |
| `ArrayPool` | `LruArrayPool` | 4 MB（`ARRAY_POOL_SIZE_BYTES`） |
| `MemoryCache` | `LruResourceCache` | `getMemoryCacheSize()` |
| `DiskCache` | `DiskLruCacheWrapper`（底层 `DiskLruCache`） | 250 MB，目录 `image_manager_disk_cache`，位于 `context.getCacheDir()` |
| `sourceExecutor` | 固定线程池，`source` 前缀 | `min(4, CPU 核数)` |
| `diskCacheExecutor` | 固定线程池，`disk-cache` 前缀 | **1** 个线程 |
| `sourceUnlimitedExecutor` | 缓存线程池（`SynchronousQueue`，0 核心线程） | 无上限 |
| `animationExecutor` | 固定线程池，`animation` 前缀 | `bestThreadCount >= 4 ? 2 : 1` |

线程池常量：

```55:59:$GLIDE_SRC/com/bumptech/glide/load/engine/executor/GlideExecutor.java
  /** The default keep alive time for threads in our cached thread pools in milliseconds. */
  private static final long KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10);

  // Don't use more than four threads when automatically determining thread count..
  private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
```

```316:325:$GLIDE_SRC/com/bumptech/glide/load/engine/executor/GlideExecutor.java
  public static int calculateBestThreadCount() {
    if (bestThreadCount == 0) {
      bestThreadCount =
          Math.min(MAXIMUM_AUTOMATIC_THREAD_COUNT, RuntimeCompat.availableProcessors());
    }
    return bestThreadCount;
  }
```

> **磁盘缓存为什么只有 1 个线程？** 磁盘是顺序 IO，多线程并发读反而增加寻道；而且磁盘缓存读取极快，单线程足够喂饱解码流水线。网络才需要并发。

### 1.3 内存额度怎么算

```131:145:$GLIDE_SRC/com/bumptech/glide/load/engine/cache/MemorySizeCalculator.java
  public static final class Builder {
    @VisibleForTesting static final int MEMORY_CACHE_TARGET_SCREENS = 2;

    /**
     * On Android O+, we use {@link android.graphics.Bitmap.Config#HARDWARE} for all reasonably
     * sized images unless we're creating thumbnails for the first time. As a result, the Bitmap
     * pool is much less important on O than it was on previous versions.
     */
    static final int BITMAP_POOL_TARGET_SCREENS =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? 4 : 1;

    static final float MAX_SIZE_MULTIPLIER = 0.4f;
    static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;
    // 4MB.
    static final int ARRAY_POOL_SIZE_BYTES = 4 * 1024 * 1024;
```

- 一屏像素数 = `屏幕宽 × 屏幕高 × 4 字节`（ARGB_8888）。
- **内存缓存** = 2 屏；**Bitmap 池** = Android O 以下 4 屏、O 及以上 1 屏（因为 O 起默认用 `HARDWARE` Bitmap，内存分配在 native 层，复用收益变小，见注释）。
- 两者之和还要再被 `maxSizeMultiplier`（普通设备 0.4、低内存设备 0.33）约束，即**不得超过 `memoryClass` 的 40%**。

### 1.4 `Glide` 构造里最关键的一步

```338:354:$GLIDE_SRC/com/bumptech/glide/Glide.java
    GlideSupplier<Registry> registry =
        RegistryFactory.lazilyCreateAndInitializeRegistry(
            this, manifestModules, annotationGeneratedModule);

    ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
    glideContext =
        new GlideContext(
            context,
            arrayPool,
            registry,
            imageViewTargetFactory,
            defaultRequestOptionsFactory,
            defaultTransitionOptions,
            defaultRequestListeners,
            engine,
            experiments,
            logLevel);
```

注意 `lazilyCreateAndInitializeRegistry` —— **Registry 是懒加载的**（`GlideSupplier`）。因为 Registry 装配时需要 `BitmapPool`，而 `BitmapPool` 又由 Glide 持有，存在循环依赖，所以用一个惰性 supplier 打破循环。首次调用 `Glide.getRegistry()` 时才真正装配。

`Glide` 还实现了 `ComponentCallbacks2`，注册到 Application：

```672:685:$GLIDE_SRC/com/bumptech/glide/Glide.java
  @Override
  public void onTrimMemory(int level) {
    trimMemory(level);
  }
  ...
  @Override
  public void onLowMemory() {
    clearMemory();
  }
```

---

## 2. `with()`：生命周期是怎么绑上去的

> ⚠️ **这一节与网上大多数中文文章不同。** 很多文章还在讲"Glide 往 Activity 里塞一个空白 `RequestManagerFragment`"——那是 **4.11 以前**的实现。4.16 已经全面改为基于 **AndroidX `Lifecycle`**，不再插入 Fragment。

### 2.1 入口分发

```543:546:$GLIDE_SRC/com/bumptech/glide/Glide.java
  public static RequestManager with(@NonNull Context context) {
    return getRetriever(context).get(context);
  }
```

```86:103:$GLIDE_SRC/com/bumptech/glide/manager/RequestManagerRetriever.java
  @NonNull
  public RequestManager get(@NonNull Context context) {
    if (context == null) {
      throw new IllegalArgumentException("You cannot start a load on a null Context");
    } else if (Util.isOnMainThread() && !(context instanceof Application)) {
      if (context instanceof FragmentActivity) {
        return get((FragmentActivity) context);
      } else if (context instanceof ContextWrapper
          // Only unwrap a ContextWrapper if the baseContext has a non-null application context.
          // Context#createPackageContext may return a Context without an Application instance,
          // in which case a ContextWrapper may be used to attach one.
          && ((ContextWrapper) context).getBaseContext().getApplicationContext() != null) {
        return get(((ContextWrapper) context).getBaseContext());
      }
    }

    return getApplicationManager(context);
  }
```

三条分支：

| 场景 | 结果 |
|---|---|
| 主线程 + 传入 `FragmentActivity` | 走 `get(FragmentActivity)`，绑定该 Activity 的 `Lifecycle` |
| 主线程 + 传入 `ContextWrapper`（如 `ContextThemeWrapper`） | **递归剥壳**直到露出 Activity，再判 `instanceof FragmentActivity` |
| 非主线程 / 传入 Application / 剥不出 Activity | 退化为 **Application 级** `RequestManager`（`ApplicationLifecycle`，永不暂停） |

```106:120:$GLIDE_SRC/com/bumptech/glide/manager/RequestManagerRetriever.java
  @NonNull
  public RequestManager get(@NonNull FragmentActivity activity) {
    if (Util.isOnBackgroundThread()) {
      return get(activity.getApplicationContext());
    }
    assertNotDestroyed(activity);
    frameWaiter.registerSelf(activity);
    boolean isActivityVisible = isActivityVisible(activity);
    Glide glide = Glide.get(activity.getApplicationContext());
    return lifecycleRequestManagerRetriever.getOrCreate(
        activity,
        glide,
        activity.getLifecycle(),
        activity.getSupportFragmentManager(),
        isActivityVisible);
  }
```

### 2.2 核心：以 `Lifecycle` 为 Key 的映射表

```32:70:$GLIDE_SRC/com/bumptech/glide/manager/LifecycleRequestManagerRetriever.java
  RequestManager getOrCreate(
      Context context,
      Glide glide,
      final Lifecycle lifecycle,
      FragmentManager childFragmentManager,
      boolean isParentVisible) {
    Util.assertMainThread();
    RequestManager result = getOnly(lifecycle);
    if (result == null) {
      LifecycleLifecycle glideLifecycle = new LifecycleLifecycle(lifecycle);
      result =
          factory.build(
              glide,
              glideLifecycle,
              new SupportRequestManagerTreeNode(childFragmentManager),
              context);
      lifecycleToRequestManager.put(lifecycle, result);
      glideLifecycle.addListener(
          new LifecycleListener() {
            @Override
            public void onStart() {}

            @Override
            public void onStop() {}

            @Override
            public void onDestroy() {
              lifecycleToRequestManager.remove(lifecycle);
            }
          });
      // This is a bit of hack, we're going to start the RequestManager, but not the
      // corresponding Lifecycle. It's safe to start the RequestManager, but starting the
      // Lifecycle might trigger memory leaks. See b/154405040
      if (isParentVisible) {
        result.onStart();
      }
    }
    return result;
  }
```

读这段要抓住 4 个点：

1. **`Map<Lifecycle, RequestManager>`**：一个 Activity/Fragment 一个 `RequestManager`，复用。用 `Lifecycle` 对象当 Key 天然避免了 Fragment 那套"tag 查找 + 事务提交"的时序问题。
2. **`onDestroy` 时从 Map 移除**：这是防止内存泄漏的关键——`RequestManager` 不再被静态结构持有，可以随 Activity 一起被回收。
3. **`LifecycleLifecycle` 是适配器**：把 AndroidX `LifecycleObserver` 的 `ON_START/ON_STOP/ON_DESTROY` 转成 Glide 自己的 `LifecycleListener` 回调。Glide 内部所有代码只依赖自己的 `Lifecycle` 接口，便于测试。
4. **`isParentVisible` 时手动 `onStart()`**：Activity 已经可见但 `Lifecycle` 还没分发 `ON_START` 的边界情况（例如刚 `get()` 时），注释明确说"直接启动 Lifecycle 可能导致内存泄漏，所以只启动 RequestManager"。

### 2.3 `RequestManager` 的状态机

```362:396:$GLIDE_SRC/com/bumptech/glide/RequestManager.java
  @Override
  public synchronized void onStart() {
    resumeRequests();
    targetTracker.onStart();
  }

  /**
   * Lifecycle callback that unregisters for connectivity events (if the
   * android.permission.ACCESS_NETWORK_STATE permission is present) and pauses in progress loads
   * and clears all resources if {@link #clearOnStop()} is called.
   */
  @Override
  public synchronized void onStop() {
    targetTracker.onStop();
    if (clearOnStop) {
      clearRequests();
    } else {
      pauseRequests();
    }
  }

  /**
   * Lifecycle callback that cancels all in progress requests and clears and recycles resources for
   * all completed requests.
   */
  @Override
  public synchronized void onDestroy() {
    targetTracker.onDestroy();
    clearRequests();
    requestTracker.clearRequests();
    lifecycle.removeListener(this);
    lifecycle.removeListener(connectivityMonitor);
    Util.removeCallbacksOnUiThread(addSelfToLifecycle);
    glide.unregisterRequestManager(this);
  }
```

`onStop` 默认只是 **暂停**（`pauseRequests`），不是清除——这样回到前台时能立刻 `resumeRequests` 把没完成的请求重新跑起来，避免闪一下占位图。

暂停期间新来的请求会进 `pendingRequests` 队列：

```40:49:$GLIDE_SRC/com/bumptech/glide/manager/RequestTracker.java
    if (!isPaused) {
      request.begin();
    } else {
      request.clear();
      if (Log.isLoggable(TAG, Log.VERBOSE)) {
        Log.v(TAG, "Paused, delaying request");
      }
      pendingRequests.add(request);
    }
  }
```

`resumeRequests` 时统一 `begin()`：

```107:119:$GLIDE_SRC/com/bumptech/glide/manager/RequestTracker.java
  /** Starts any not yet completed or failed requests. */
  public void resumeRequests() {
    isPaused = false;
    for (Request request : Util.getSnapshot(requests)) {
      // We don't need to check for cleared here. Any explicit clear by a user will remove the
      // Request from the tracker, so the only way we'd find a cleared request here is if we cleared
      // it. As a result it should be safe for us to resume cleared requests.
      if (!request.isComplete() && !request.isRunning()) {
        request.begin();
      }
    }
    pendingRequests.clear();
  }
```

还有一个容易忽略的：**断网重连自动重试**。构造时注册了 `ConnectivityMonitor`：

```129:147:$GLIDE_SRC/com/bumptech/glide/RequestManager.java
    connectivityMonitor =
        factory.build(
            context.getApplicationContext(),
            new RequestManagerConnectivityListener(requestTracker));

    // Order matters, this might be unregistered by teh listeners below, so we need to be sure to
    // register first to prevent both assertions and memory leaks.
    glide.registerRequestManager(this);

    // If we're the application level request manager, we may be created on a background thread.
    // In that case we cannot risk synchronously pausing or resuming requests, so we hack around the
    // issue by delaying adding ourselves as a lifecycle listener by posting to the main thread.
    // This should be entirely safe.
    if (Util.isOnBackgroundThread()) {
      Util.postOnUiThread(addSelfToLifecycle);
    } else {
      lifecycle.addListener(this);
    }
    lifecycle.addListener(connectivityMonitor);
```

---

## 3. `load()` / `apply()`：配置体系

### 3.1 `load()` 除了存模型，还会偷偷改配置

```536:543:$GLIDE_SRC/com/bumptech/glide/RequestBuilder.java
  private RequestBuilder<TranscodeType> loadGeneric(@Nullable Object model) {
    if (isAutoCloneEnabled()) {
      return clone().loadGeneric(model);
    }
    this.model = model;
    isModelSet = true;
    return selfOrThrowIfLocked();
  }
```

**`load(null)` 也会把 `isModelSet` 置为 `true`** —— 这一点很重要，`SingleRequest.begin()` 里对 `model == null` 会走 `onLoadFailed()` 从而显示 `fallback` 图。本项目 `commonBindingAdapter.java:51-59` 正是靠这个行为在 uri 为空时兜底显示占位图。

不同 `load()` 重载会隐式套用不同策略，例如 `load(byte[])`：

```766:778:$GLIDE_SRC/com/bumptech/glide/RequestBuilder.java
  public RequestBuilder<TranscodeType> load(@Nullable byte[] model) {
    RequestBuilder<TranscodeType> result = loadGeneric(model);
    if (!result.isDiskCacheStrategySet()) {
      result = result.apply(diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }
    if (!result.isSkipMemoryCacheSet()) {
      result = result.apply(skipMemoryCacheOf(true /*skipMemoryCache*/));
    }
    return result;
  }
```

### 3.2 `autoClone`：`RequestOptions` 的写时复制

这是 Glide 配置系统里最巧的设计。看任意一个 setter：

```222:230:$GLIDE_SRC/com/bumptech/glide/request/BaseRequestOptions.java
  public T diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
    if (isAutoCloneEnabled) {
      return clone().diskCacheStrategy(strategy);
    }
    this.diskCacheStrategy = Preconditions.checkNotNull(strategy);
    fields |= DISK_CACHE_STRATEGY;

    return selfOrThrowIfLocked();
  }
```

- 平时（`isAutoCloneEnabled == false`）：直接改自己，返回 `this`，零分配。
- 一旦调用过 `autoClone()`（如 `RequestManager` 的默认 options、`RequestOptions` 常量）：**每次 setter 都先 `clone()` 再改**，原对象永远不可变。

好处：一份"模板 options"可以在任意线程共享，任何一次修改都自动产生副本，调用方无需关心。这就是为什么 `RequestManager.setRequestOptions()` 里写 `toSet.clone().autoClone()`：

```154:156:$GLIDE_SRC/com/bumptech/glide/RequestManager.java
  protected synchronized void setRequestOptions(@NonNull RequestOptions toSet) {
    requestOptions = toSet.clone().autoClone();
  }
```

同时 `fields` 用**位掩码**记录"哪些字段被显式设置过"（`fields |= DISK_CACHE_STRATEGY`），用于 `load(byte[])` 那种"仅在用户没设置时才套默认值"的判断，以及 `placeholder(Drawable)` 与 `placeholder(int)` 互斥（`fields &= ~PLACEHOLDER_ID`）。

---

## 4. `into()`：从 View 到 Request

### 4.1 `into(ImageView)` 会根据 `scaleType` 自动加变换

```884:922:$GLIDE_SRC/com/bumptech/glide/RequestBuilder.java
  public ViewTarget<ImageView, TranscodeType> into(@NonNull ImageView view) {
    Util.assertMainThread();
    Preconditions.checkNotNull(view);

    BaseRequestOptions<?> requestOptions = this;
    if (!requestOptions.isTransformationSet()
        && requestOptions.isTransformationAllowed()
        && view.getScaleType() != null) {
      // Clone in this method so that if we use this RequestBuilder to load into a View and then
      // into a different target, we don't retain the transformation applied based on the previous
      // View's scale type.
      switch (view.getScaleType()) {
        case CENTER_CROP:
          requestOptions = requestOptions.clone().optionalCenterCrop();
          break;
        ...
```

**源码级解释了那个经典面试题**：`ImageView` 的 `android:scaleType` 会影响 Glide 的缓存 Key（因为变换进 Key）。同一个 URL 在 `centerCrop` 和 `fitCenter` 的 ImageView 上会生成**两份不同的缓存**。这也是为什么这里要 `clone()`——防止这个 Builder 之后被复用到别的 View 上，把上一个 View 的 scaleType 变换带过去。

### 4.2 请求复用：避免 RecyclerView 重复加载

```828:861:$GLIDE_SRC/com/bumptech/glide/RequestBuilder.java
  private <Y extends Target<TranscodeType>> Y into(
      @NonNull Y target,
      @Nullable RequestListener<TranscodeType> targetListener,
      BaseRequestOptions<?> options,
      Executor callbackExecutor) {
    Preconditions.checkNotNull(target);
    if (!isModelSet) {
      throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    Request request = buildRequest(target, targetListener, options, callbackExecutor);

    Request previous = target.getRequest();
    if (request.isEquivalentTo(previous)
        && !isSkipMemoryCacheWithCompletePreviousRequest(options, previous)) {
      // If the request is completed, beginning again will ensure the result is re-delivered,
      // triggering RequestListeners and Targets. If the request is failed, beginning again will
      // restart the request, giving it another chance to complete. If the request is already
      // running, we can let it continue running without interruption.
      if (!Preconditions.checkNotNull(previous).isRunning()) {
        // Use the previous request rather than the new one to allow for optimizations like skipping
        // setting placeholders, tracking and un-tracking Targets, and obtaining View dimensions
        // that are done in the individual Request.
        previous.begin();
      }
      return target;
    }

    requestManager.clear(target);
    target.setRequest(request);
    requestManager.track(target, request);

    return target;
  }
```

这是列表滑动流畅的关键：**一个 View 上如果新请求和旧请求等价（同 URL、同尺寸、同变换……），直接复用旧的 `Request` 并 `begin()`，而不去 `clear()` 旧请求**。否则列表每次 `onBindViewHolder` 都会取消+重发请求，必然闪烁。

等价性判断在 `buildRequest` 之后、`clear` 之前——注意顺序是先建新请求、比对、不复用才 `clear(target)`。

`requestManager.clear(target)` 最终会走到 `untrack` → `requestTracker.clearAndRemove(request)` → `request.clear()`，从而释放旧资源（引用计数 -1，可能回落到内存缓存）。

### 4.3 `SingleRequest` 的生命周期

```214:269:$GLIDE_SRC/com/bumptech/glide/request/SingleRequest.java
  @Override
  public void begin() {
    synchronized (requestLock) {
      assertNotCallingCallbacks();
      stateVerifier.throwIfRecycled();
      startTime = LogTime.getLogTime();
      if (model == null) {
        if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
          width = overrideWidth;
          height = overrideHeight;
        }
        // Only log at more verbose log levels if the user has set a fallback drawable, because
        // fallback Drawables indicate the user expects null models occasionally.
        int logLevel = getFallbackDrawable() == null ? Log.WARN : Log.DEBUG;
        onLoadFailed(new GlideException("Received null model"), logLevel);
        return;
      }

      if (status == Status.RUNNING) {
        throw new IllegalArgumentException("Cannot restart a running request");
      }

      // If we're restarted after we're complete (usually via something like a notifyDataSetChanged
      // that starts an identical request into the same Target or View), we can simply use the
      // resource and size we retrieved the last time around and skip obtaining a new size, starting
      // a new load etc. This does mean that users who want to restart a load because they expect
      // that the view size has changed will need to explicitly clear the View or Target before
      // starting the new load.
      if (status == Status.COMPLETE) {
        onResourceReady(
            resource, DataSource.MEMORY_CACHE, /* isLoadedFromAlternateCacheKey= */ false);
        return;
      }
      ...
      cookie = GlideTrace.beginSectionAsync(TAG);
      status = Status.WAITING_FOR_SIZE;
      if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
        onSizeReady(overrideWidth, overrideHeight);
      } else {
        target.getSize(this);
      }

      if ((status == Status.RUNNING || status == Status.WAITING_FOR_SIZE)
          && canNotifyStatusChanged()) {
        target.onLoadStarted(getPlaceholderDrawable());
      }
```

六个状态：`PENDING / RUNNING / WAITING_FOR_SIZE / COMPLETE / FAILED / CLEARED`。注意 **`status == COMPLETE` 时直接把上次拿到的 `resource` 再投递一次**，完全不碰尺寸测量和 `Engine`。

### 4.4 尺寸测量：为什么有时要等一帧

```372:390:$GLIDE_SRC/com/bumptech/glide/request/target/ViewTarget.java
    void getSize(@NonNull SizeReadyCallback cb) {
      int currentWidth = getTargetWidth();
      int currentHeight = getTargetHeight();
      if (isViewStateAndSizeValid(currentWidth, currentHeight)) {
        cb.onSizeReady(currentWidth, currentHeight);
        return;
      }

      // We want to notify callbacks in the order they were added and we only expect one or two
      // callbacks to be added a time, so a List is a reasonable choice.
      if (!cbs.contains(cb)) {
        cbs.add(cb);
      }
      if (layoutListener == null) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        layoutListener = new SizeDeterminerLayoutListener(this);
        observer.addOnPreDrawListener(layoutListener);
      }
    }
```

优先级：

```435:448:$GLIDE_SRC/com/bumptech/glide/request/target/ViewTarget.java
    private int getTargetDimen(int viewSize, int paramSize, int paddingSize) {
      // We consider the View state as valid if the View has non-null layout params and a non-zero
      // layout params width and height. This is imperfect. We're making an assumption that View
      // parents will obey their child's layout parameters, which isn't always the case.
      int adjustedParamSize = paramSize - paddingSize;
      if (adjustedParamSize > 0) {
        return adjustedParamSize;
      }

      // Since we always prefer layout parameters with fixed sizes, even if waitForLayout is true,
      // we might as well ignore it and just return the layout parameters above if we have them.
      // Otherwise we should wait for a layout pass before checking the View's dimensions.
      if (waitForLayout && view.isLayoutRequested()) {
        return PENDING_SIZE;
```

1. **`LayoutParams` 的固定值 - padding**（最高优先级，即使 View 还没布局完也能算出来）
2. `waitForLayout == true` 且正在 `requestLayout` → 返回 `PENDING_SIZE`，等布局
3. `View.getWidth()` - padding
4. 都不行 → 挂 `OnPreDrawListener` 等下一帧

**实践含义**：给 ImageView 写死 `android:layout_width="100dp"` 能让 Glide 立即得到尺寸、跳过一帧等待；用 `wrap_content`/`match_parent` 则要等预绘制回调。

---

## 5. `Engine`：三级缓存的调度中枢

### 5.1 缓存 Key 由 8 个字段决定

```44:58:$GLIDE_SRC/com/bumptech/glide/load/engine/EngineKey.java
  @Override
  public boolean equals(Object o) {
    if (o instanceof EngineKey) {
      EngineKey other = (EngineKey) o;
      return model.equals(other.model)
          && signature.equals(other.signature)
          && height == other.height
          && width == other.width
          && transformations.equals(other.transformations)
          && resourceClass.equals(other.resourceClass)
          && transcodeClass.equals(other.transcodeClass)
          && options.equals(other.options);
    }
    return false;
  }
```

`model, signature, width, height, transformations, resourceClass, transcodeClass, options` 全相等才算同一个请求。

**实践含义（本项目直接相关）**：项目里的 URL 如果带了**会变化的 token/时间戳**，`model` 就每次不同 → 每次都是新 Key → 内存缓存和磁盘缓存全部失效。这时要么让服务端把签名参数固定，要么自定义 `GlideUrl` 覆写 `getCacheKey()` 只返回稳定部分。

### 5.2 `load()` 主流程

```155:223:$GLIDE_SRC/com/bumptech/glide/load/engine/Engine.java
  public <R> LoadStatus load(
      ... ) {
    long startTime = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0;

    EngineKey key =
        keyFactory.buildKey(
            model,
            signature,
            width,
            height,
            transformations,
            resourceClass,
            transcodeClass,
            options);

    EngineResource<?> memoryResource;
    synchronized (this) {
      memoryResource = loadFromMemory(key, isMemoryCacheable, startTime);

      if (memoryResource == null) {
        return waitForExistingOrStartNewJob(
            ... );
      }
    }

    // Avoid calling back while holding the engine lock, doing so makes it easier for callers to
    // deadlock.
    cb.onResourceReady(
        memoryResource, DataSource.MEMORY_CACHE, /* isLoadedFromAlternateCacheKey= */ false);
    return null;
  }
```

注意最后的注释：**回调是在 `synchronized` 块外面做的**，避免持锁回调造成死锁。

内存两级：

```295:342:$GLIDE_SRC/com/bumptech/glide/load/engine/Engine.java
  @Nullable
  private EngineResource<?> loadFromMemory(
      EngineKey key, boolean isMemoryCacheable, long startTime) {
    if (!isMemoryCacheable) {
      return null;
    }

    EngineResource<?> active = loadFromActiveResources(key);
    if (active != null) {
      ...
      return active;
    }

    EngineResource<?> cached = loadFromCache(key);
    ...
  }

  private EngineResource<?> loadFromActiveResources(Key key) {
    EngineResource<?> active = activeResources.get(key);
    if (active != null) {
      active.acquire();
    }

    return active;
  }

  private EngineResource<?> loadFromCache(Key key) {
    EngineResource<?> cached = getEngineResourceFromCache(key);
    if (cached != null) {
      cached.acquire();
      activeResources.activate(key, cached);
    }
    return cached;
  }
```

以及 `getEngineResourceFromCache` 里是 **`cache.remove(key)` 而不是 `get`**：

```344:363:$GLIDE_SRC/com/bumptech/glide/load/engine/Engine.java
  private EngineResource<?> getEngineResourceFromCache(Key key) {
    Resource<?> cached = cache.remove(key);
    ...
```

**这就是"活动资源"和"内存缓存"的分工**：

- `ActiveResources`：**正在被至少一个 Target 使用**的资源（弱引用 Map）。命中不消耗 LruCache 额度，也不会被 LRU 淘汰。
- `LruResourceCache`：**没人用但还值得留着**的资源。命中时**移出** LruCache 并转入 ActiveResources。

为什么这么设计？如果只有 LruCache，正在屏幕上显示的图片可能因为 LRU 满而被淘汰回收，直接导致**正在显示的图崩掉/变黑**。Activity 资源层把"正在用"和"可淘汰"隔离开了。

### 5.3 请求去重

```248:292:$GLIDE_SRC/com/bumptech/glide/load/engine/Engine.java
    EngineJob<?> current = jobs.get(key, onlyRetrieveFromCache);
    if (current != null) {
      current.addCallback(cb, callbackExecutor);
      if (VERBOSE_IS_LOGGABLE) {
        logWithTimeAndKey("Added to existing load", startTime, key);
      }
      return new LoadStatus(cb, current);
    }

    EngineJob<R> engineJob =
        engineJobFactory.build(
            key,
            isMemoryCacheable,
            useUnlimitedSourceExecutorPool,
            useAnimationPool,
            onlyRetrieveFromCache);

    DecodeJob<R> decodeJob = decodeJobFactory.build(... );
    ...
    jobs.put(key, engineJob);

    engineJob.addCallback(cb, callbackExecutor);
    engineJob.start(decodeJob);

    if (VERBOSE_IS_LOGGABLE) {
      logWithTimeAndKey("Started new load", startTime, key);
    }
    return new LoadStatus(cb, engineJob);
  }
```

**同一个 Key 的多个请求（比如列表里 5 个 item 同一张头像）只会真跑一次网络**，其余 4 个只是往同一个 `EngineJob` 注册回调。这是 Glide 最重要的性能优化之一。

### 5.4 引用计数与回收闭环

```88:118:$GLIDE_SRC/com/bumptech/glide/load/engine/EngineResource.java
  synchronized void acquire() {
    if (isRecycled) {
      throw new IllegalStateException("Cannot acquire a recycled resource");
    }
    ++acquired;
  }
  ...
  void release() {
    boolean release = false;
    synchronized (this) {
      if (acquired <= 0) {
        throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
      }
      if (--acquired == 0) {
        release = true;
      }
    }
    if (release) {
      listener.onResourceReleased(key, this);
    }
  }
```

`acquired` 归零 → 回调 `Engine`：

```397:405:$GLIDE_SRC/com/bumptech/glide/load/engine/Engine.java
  @Override
  public void onResourceReleased(Key cacheKey, EngineResource<?> resource) {
    activeResources.deactivate(cacheKey);
    if (resource.isMemoryCacheable()) {
      cache.put(cacheKey, resource);
    } else {
      resourceRecycler.recycle(resource, /* forceNextFrame= */ false);
    }
  }
```

完整闭环图：

```
Engine.load 命中 Active  ──acquire──▶ 给 Target
Engine.load 命中 LruCache ──remove──▶ activate 进 Active ──▶ 给 Target
DecodeJob 完成          ───────────▶ activate 进 Active ──▶ 给 Target

Target.clear/新图替换     ──release──▶ acquired-1
acquired == 0            ───────────▶ 出 Active ──▶ put 进 LruCache
LruCache 满了被 LRU 淘汰  ───────────▶ onResourceRemoved ──▶ recycle ──▶ Bitmap 回 BitmapPool
```

`ActiveResources` 内部是**弱引用 + ReferenceQueue**，防的是"Target 泄漏没调 release"的情况：

```106:125:$GLIDE_SRC/com/bumptech/glide/load/engine/ActiveResources.java
  @Synthetic
  void cleanupActiveReference(@NonNull ResourceWeakReference ref) {
    synchronized (this) {
      activeEngineResources.remove(ref.key);

      if (!ref.isCacheable || ref.resource == null) {
        return;
      }
    }

    EngineResource<?> newResource =
        new EngineResource<>(
            ref.resource,
            /* isMemoryCacheable= */ true,
            /* isRecyclable= */ false,
            ref.key,
            listener);
    listener.onResourceReleased(ref.key, newResource);
  }
```

有一个常驻后台线程 `monitorClearedResourcesExecutor` 在 `ReferenceQueue.remove()` 上阻塞，被 GC 掉的 `EngineResource` 会被捞出来转存到 LruCache（而不是直接丢弃）。

---

## 6. `EngineJob` + `DecodeJob`：线程模型与状态机

### 6.1 选线程池

```128:133:$GLIDE_SRC/com/bumptech/glide/load/engine/EngineJob.java
  public synchronized void start(DecodeJob<R> decodeJob) {
    this.decodeJob = decodeJob;
    GlideExecutor executor =
        decodeJob.willDecodeFromCache() ? diskCacheExecutor : getActiveSourceExecutor();
    executor.execute(decodeJob);
  }
```

```195:199:$GLIDE_SRC/com/bumptech/glide/load/engine/EngineJob.java
  private GlideExecutor getActiveSourceExecutor() {
    return useUnlimitedSourceGeneratorPool
        ? sourceUnlimitedExecutor
        : (useAnimationPool ? animationExecutor : sourceExecutor);
  }
```

### 6.2 `DecodeJob` 的状态机

```272:303:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodeJob.java
  private void runWrapped() {
    switch (runReason) {
      case INITIALIZE:
        stage = getNextStage(Stage.INITIALIZE);
        currentGenerator = getNextGenerator();
        runGenerators();
        break;
      case SWITCH_TO_SOURCE_SERVICE:
        runGenerators();
        break;
      case DECODE_DATA:
        decodeFromRetrievedData();
        break;
      default:
        throw new IllegalStateException("Unrecognized run reason: " + runReason);
    }
  }

  private DataFetcherGenerator getNextGenerator() {
    switch (stage) {
      case RESOURCE_CACHE:
        return new ResourceCacheGenerator(decodeHelper, this);
      case DATA_CACHE:
        return new DataCacheGenerator(decodeHelper, this);
      case SOURCE:
        return new SourceGenerator(decodeHelper, this);
      case FINISHED:
        return null;
      default:
        throw new IllegalStateException("Unrecognized stage: " + stage);
    }
  }
```

Stage 推进由 `DiskCacheStrategy` 决定：

```351:370:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodeJob.java
  private Stage getNextStage(Stage current) {
    switch (current) {
      case INITIALIZE:
        return diskCacheStrategy.decodeCachedResource()
            ? Stage.RESOURCE_CACHE
            : getNextStage(Stage.RESOURCE_CACHE);
      case RESOURCE_CACHE:
        return diskCacheStrategy.decodeCachedData()
            ? Stage.DATA_CACHE
            : getNextStage(Stage.DATA_CACHE);
      case DATA_CACHE:
        // Skip loading from source if the user opted to only retrieve the resource from cache.
        return onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
      case SOURCE:
      case FINISHED:
        return Stage.FINISHED;
      default:
        throw new IllegalArgumentException("Unrecognized stage: " + current);
    }
  }
```

```290:327:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodeJob.java
  private void runGenerators() {
    currentThread = Thread.currentThread();
    startFetchTime = LogTime.getLogTime();
    boolean isStarted = false;
    while (!isCancelled
        && currentGenerator != null
        && !(isStarted = currentGenerator.startNext())) {
      stage = getNextStage(stage);
      currentGenerator = getNextGenerator();

      if (stage == Stage.SOURCE) {
        reschedule(RunReason.SWITCH_TO_SOURCE_SERVICE);
        return;
      }
    }
    // We've run out of stages and generators, give up.
    if ((stage == Stage.FINISHED || isCancelled) && !isStarted) {
      notifyFailed();
    }

    // Otherwise a generator started a new load and we expect to be called back in
    // onDataFetcherReady.
  }
```

**关键的一手**：当推进到 `Stage.SOURCE`（要发网络请求）时，先 `reschedule` —— 把自己从 **disk-cache 线程** 丢回 **source 线程池**再执行。这样网络请求不会占用那唯一的 1 个磁盘缓存线程，磁盘缓存读取不会被网络 IO 阻塞。

`DecodeJob.run()` 还有一个细节：捕获 `Throwable`（包括 OOM），保证任何情况下都 `notifyFailed()`，避免请求永久挂起：

```240:261:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodeJob.java
    } catch (Throwable t) {
      // Catch Throwable and not Exception to handle OOMs. Throwables are swallowed by our
      // usage of .submit() in GlideExecutor so we're not silently hiding crashes by doing this. We
      // are however ensuring that our callbacks are always notified when a load fails. Without this
      // notification, uncaught throwables never notify the corresponding callbacks, which can cause
      // loads to silently hang forever, a case that's especially bad for users using Futures on
      // background threads.
      ...
      // When we're encoding we've already notified our callback and it isn't safe to do so again.
      if (stage != Stage.ENCODE) {
        throwables.add(t);
        notifyFailed();
      }
```

### 6.3 `SourceGenerator`：为什么网络图片要"先落盘再解码"

```202:218:$GLIDE_SRC/com/bumptech/glide/load/engine/SourceGenerator.java
  @Synthetic
  void onDataReadyInternal(LoadData<?> loadData, Object data) {
    DiskCacheStrategy diskCacheStrategy = helper.getDiskCacheStrategy();
    if (data != null && diskCacheStrategy.isDataCacheable(loadData.fetcher.getDataSource())) {
      dataToCache = data;
      // We might be being called back on someone else's thread. Before doing anything, we should
      // reschedule to get back onto Glide's thread. Then once we're back on Glide's thread, we'll
      // get called again and we can write the retrieved data to cache.
      cb.reschedule();
    } else {
      cb.onDataFetcherReady(
          loadData.sourceKey,
          data,
          loadData.fetcher,
          loadData.fetcher.getDataSource(),
          originalKey);
    }
  }
```

```131:190:$GLIDE_SRC/com/bumptech/glide/load/engine/SourceGenerator.java
  private boolean cacheData(Object dataToCache) throws IOException {
    long startTime = LogTime.getLogTime();
    boolean isLoadingFromSourceData = false;
    try {
      DataRewinder<Object> rewinder = helper.getRewinder(dataToCache);
      Object data = rewinder.rewindAndGet();
      Encoder<Object> encoder = helper.getSourceEncoder(data);
      DataCacheWriter<Object> writer = new DataCacheWriter<>(encoder, data, helper.getOptions());
      DataCacheKey newOriginalKey = new DataCacheKey(loadData.sourceKey, helper.getSignature());
      DiskCache diskCache = helper.getDiskCache();
      diskCache.put(newOriginalKey, writer);
      ...
      if (diskCache.get(newOriginalKey) != null) {
        originalKey = newOriginalKey;
        sourceCacheGenerator =
            new DataCacheGenerator(Collections.singletonList(loadData.sourceKey), helper, this);
        // We were able to write the data to cache.
        return true;
      } else {
        ...
        isLoadingFromSourceData = true;
        cb.onDataFetcherReady(...);
      }
```

流程是：`HttpUrlFetcher` 拿到 `InputStream`（还在网络线程的回调里）→ `reschedule()` 切回 Glide 线程 → `rewindAndGet()` **把流 rewind 到开头** → 写磁盘 → **重新生成一个 `DataCacheGenerator` 从磁盘文件再读一遍来解码**。

为什么绕这一圈？

1. `InputStream` 是一次性的，写盘和解码不能共用同一个流（rewind 需要 `DataRewinder`，对网络流要借助 `ArrayPool` 缓冲）。
2. 写盘后从文件解码，等于统一了"网络首次加载"和"磁盘缓存命中"两条路径的解码逻辑，代码只有一份。
3. 落盘失败时（如磁盘满）会直接拿 rewind 后的原始流解码兜底，不会白跑一趟网络。

### 6.4 解码 → 变换 → 转码

```52:62:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodePath.java
  public Resource<Transcode> decode(
      DataRewinder<DataType> rewinder,
      int width,
      int height,
      @NonNull Options options,
      DecodeCallback<ResourceType> callback)
      throws GlideException {
    Resource<ResourceType> decoded = decodeResource(rewinder, width, height, options);
    Resource<ResourceType> transformed = callback.onResourceDecoded(decoded);
    return transcoder.transcode(transformed, options);
  }
```

三步非常清晰：

1. **`decodeResource`**：遍历注册的 `ResourceDecoder`，用 `decoder.handles(data, options)` 判断能不能解，能解就 `decode`。
2. **`onResourceDecoded`**：应用变换（`Transformation`），同时在这里决定要不要写 RESOURCE 磁盘缓存。
3. **`transcode`**：`Bitmap` → `BitmapDrawable`（`BitmapDrawableTranscoder`），因为默认 `RequestManager.load()` 返回的是 `RequestBuilder<Drawable>`。

### 6.5 先回调还是先落盘

```446:480:$GLIDE_SRC/com/bumptech/glide/load/engine/DecodeJob.java
  private void notifyEncodeAndRelease(
      Resource<R> resource, DataSource dataSource, boolean isLoadedFromAlternateCacheKey) {
    GlideTrace.beginSection("DecodeJob.notifyEncodeAndRelease");
    try {
      if (resource instanceof Initializable) {
        ((Initializable) resource).initialize();
      }

      Resource<R> result = resource;
      LockedResource<R> lockedResource = null;
      if (deferredEncodeManager.hasResourceToEncode()) {
        lockedResource = LockedResource.obtain(resource);
        result = lockedResource;
      }

      notifyComplete(result, dataSource, isLoadedFromAlternateCacheKey);

      stage = Stage.ENCODE;
      try {
        if (deferredEncodeManager.hasResourceToEncode()) {
          deferredEncodeManager.encode(diskCacheProvider, options);
        }
      } finally {
        if (lockedResource != null) {
          lockedResource.unlock();
        }
      }
      // Call onEncodeComplete outside the finally block so that it's not called if the encode
      // process throws.
      onEncodeComplete();
```

**先把结果回调给 UI，再写磁盘**。写盘是纯 IO，不该阻塞图片显示。用 `LockedResource` 包一层是为了防止在回调过程中资源被 `recycle()`（回调里用户可能同步 `clear()`），写盘完再 `unlock()`。

### 6.6 结果回调与线程池切换

```229:265:$GLIDE_SRC/com/bumptech/glide/load/engine/EngineJob.java
  @Synthetic
  void notifyCallbacksOfResult() {
    ResourceCallbacksAndExecutors copy;
    Key localKey;
    EngineResource<?> localResource;
    synchronized (this) {
      stateVerifier.throwIfRecycled();
      if (isCancelled) {
        // TODO: Seems like we might as well put this in the memory cache instead of just recycling
        // it since we've gotten this far...
        resource.recycle();
        release();
        return;
      } else if (cbs.isEmpty()) {
        throw new IllegalStateException("Received a resource without any callbacks to notify");
      } else if (hasResource) {
        throw new IllegalStateException("Already have resource");
      }
      engineResource = engineResourceFactory.build(resource, isCacheable, key, resourceListener);
      // Hold on to resource for duration of our callbacks below so we don't recycle it in the
      // middle of notifying if it synchronously released by one of the callbacks. Acquire it under
      // a lock here so that any newly added callback that executes before the next locked section
      // below can't recycle the resource before we call the callbacks.
      hasResource = true;
      copy = cbs.copy();
      incrementPendingCallbacks(copy.size() + 1);

      localKey = key;
      localResource = engineResource;
    }

    engineJobListener.onEngineJobComplete(this, localKey, localResource);

    for (final ResourceCallbackAndExecutor entry : copy) {
      entry.executor.execute(new CallResourceReady(entry.cb));
    }
    decrementPendingCallbacks();
  }
```

- 用 `pendingCallbacks` 原子计数器保证"回调期间资源不被回收"。
- 每个回调有自己的 `Executor`（就是 `into()` 时传入的 `Executors.mainThreadExecutor()`），所以**结果一定回主线程**，这里也是"子线程解码 → 主线程显示"的切换点。

---

## 7. `Registry`：数据流的插件表

### 7.1 注册了什么

```319:326:$GLIDE_SRC/com/bumptech/glide/RegistryFactory.java
    registry
        .append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
        .append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory<Uri>())
        .append(String.class, InputStream.class, new StringLoader.StreamFactory())
        .append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
        .append(
            String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
```

```193:198:$GLIDE_SRC/com/bumptech/glide/RegistryFactory.java
    registry
        .append(ByteBuffer.class, new ByteBufferEncoder())
        .append(InputStream.class, new StreamEncoder(arrayPool))
        /* Bitmaps */
        .append(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
        .append(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder);
```

```226:241:$GLIDE_SRC/com/bumptech/glide/RegistryFactory.java
        .append(
            Registry.BUCKET_BITMAP_DRAWABLE,
            ByteBuffer.class,
            BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder))
        .append(
            Registry.BUCKET_BITMAP_DRAWABLE,
            InputStream.class,
            BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, streamBitmapDecoder))
```

### 7.2 项目里 `load(String url)` 的完整链路

```
model = "https://xxx/a.jpg"  (String)
  │
  ├─ StringLoader.buildLoadData()   → parseUri：首字符 '/' 当文件路径，否则 Uri.parse；scheme 为空也当文件
  │      ↓  Uri("https://xxx/a.jpg")
  ├─ MultiModelLoaderFactory.build(Uri.class, InputStream.class)
  │      ↓  UriLoader / UrlUriLoader → HttpGlideUrlLoader
  ├─ HttpGlideUrlLoader.buildLoadData()  → new LoadData<>(url, new HttpUrlFetcher(url, timeout))
  │      ↓  超时默认 2500ms
  ├─ HttpUrlFetcher.loadData()  → HttpURLConnection，自己处理 3xx 重定向（最多 5 次）
  │      ↓  InputStream
  ├─ StreamBitmapDecoder(Downsampler)  → Bitmap
  ├─ Transformation（CircleCrop / 按 scaleType 自动加的 FitCenter 等）
  └─ BitmapDrawableTranscoder  → BitmapDrawable → DrawableImageViewTarget → ImageView
```

```28:29:$GLIDE_SRC/com/bumptech/glide/load/model/stream/HttpGlideUrlLoader.java
  public static final Option<Integer> TIMEOUT =
      Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500);
```

```54:55:$GLIDE_SRC/com/bumptech/glide/load/model/stream/HttpGlideUrlLoader.java
    int timeout = options.get(TIMEOUT);
    return new LoadData<>(url, new HttpUrlFetcher(url, timeout));
```

`HttpUrlFetcher` 自己处理重定向，而不是交给 `HttpURLConnection`：

```160:163:$GLIDE_SRC/com/bumptech/glide/load/data/HttpUrlFetcher.java
    // Stop the urlConnection instance of HttpUrlConnection from following redirects so that
    // redirects will be handled by recursive calls to this method, loadDataWithRedirects.
    urlConnection.setInstanceFollowRedirects(false);
    return urlConnection;
```

---

## 8. `BitmapPool`：Bitmap 复用

### 8.1 解码时怎么复用

```409:413:$GLIDE_SRC/com/bumptech/glide/load/resource/bitmap/Downsampler.java
      // If this isn't an image, or BitmapFactory was unable to parse the size, width and height
      // will be -1 here.
      if (expectedWidth > 0 && expectedHeight > 0) {
        setInBitmap(options, bitmapPool, expectedWidth, expectedHeight);
      }
```

```899:924:$GLIDE_SRC/com/bumptech/glide/load/resource/bitmap/Downsampler.java
  private static void setInBitmap(
      BitmapFactory.Options options, BitmapPool bitmapPool, int width, int height) {
    @Nullable Bitmap.Config expectedConfig = null;
    // Avoid short circuiting, it appears to break on some devices.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      if (options.inPreferredConfig == Config.HARDWARE) {
        return;
      }
      // On API 26 outConfig may be null for some images even if the image is valid, can be decoded
      // and outWidth/outHeight/outColorSpace are populated (see b/71513049).
      expectedConfig = options.outConfig;
    }

    if (expectedConfig == null) {
      // We're going to guess that BitmapFactory will return us the config we're requesting. This
      // isn't always the case, even though our guesses tend to be conservative and prefer configs
      // of larger sizes so that the Bitmap will fit our image anyway. If we're wrong here and the
      // config we choose is too small, our initial decode will fail, but we will retry with no
      // inBitmap which will succeed so if we're wrong here, we're less efficient but still correct.
      expectedConfig = options.inPreferredConfig;
    }
    // BitmapFactory will clear out the Bitmap before writing to it, so getDirty is safe.
    options.inBitmap = bitmapPool.getDirty(width, height, expectedConfig);
  }
```

`inBitmap` 是 Android 的"用已有 Bitmap 的内存承接新解码结果"，避免新的大块内存分配和 GC。前置条件是已经知道目标宽高——这正是 `Downsampler` 要**先跑一遍 `inJustDecodeBounds = true`** 的原因。

复用失败（尺寸/格式不匹配会抛异常）时，把 `inBitmap` 还回池子并重试一次不复用：

```783:800:$GLIDE_SRC/com/bumptech/glide/load/resource/bitmap/Downsampler.java
      if (Log.isLoggable(TAG, Log.DEBUG)) {
        Log.d(
            TAG,
            "Failed to decode with inBitmap, trying again without Bitmap re-use",
            bitmapAssertionException);
      }
      if (options.inBitmap != null) {
        try {
          bitmapPool.put(options.inBitmap);
          options.inBitmap = null;
          return decodeStream(imageReader, options, callbacks, bitmapPool);
        } catch (IOException resetException) {
          throw bitmapAssertionException;
        }
      }
      throw bitmapAssertionException;
```

这是典型的"乐观优化 + 失败降级"，保证正确性不受影响。

### 8.2 池子的准入与淘汰

```103:141:$GLIDE_SRC/com/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool.java
  public synchronized void put(Bitmap bitmap) {
    if (bitmap == null) {
      throw new NullPointerException("Bitmap must not be null");
    }
    if (bitmap.isRecycled()) {
      throw new IllegalStateException("Cannot pool recycled bitmap");
    }
    if (!bitmap.isMutable()
        || strategy.getSize(bitmap) > maxSize
        || !allowedConfigs.contains(bitmap.getConfig())) {
      ... 拒绝入池
    }
    ...
    evict();
  }
```

三个准入条件：**必须 mutable**、**尺寸不超过池上限**、**Config 在白名单内**（4.16 起默认含 `HARDWARE`，但 `HARDWARE` Bitmap 不可复用只是允许入池占位）。

淘汰时直接 `recycle()`：

```258:279:$GLIDE_SRC/com/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool.java
  private synchronized void trimToSize(long size) {
    while (currentSize > size) {
      final Bitmap removed = strategy.removeLast();
      // TODO: This shouldn't ever happen, see #331.
      if (removed == null) {
        ...
        currentSize = 0;
        return;
      }
      tracker.remove(removed);
      currentSize -= strategy.getSize(removed);
      evictions++;
      ...
      removed.recycle();
    }
  }
```

`get()` 与 `getDirty()` 的区别：`get()` 会 `eraseColor(TRANSPARENT)` 清干净，`getDirty()` 不清（解码器反正会整体覆盖，省一次全图擦除）：

```147:171:$GLIDE_SRC/com/bumptech/glide/load/engine/bitmap_recycle/LruBitmapPool.java
  public Bitmap get(int width, int height, Bitmap.Config config) {
    Bitmap result = getDirtyOrNull(width, height, config);
    if (result != null) {
      // Bitmaps in the pool contain random data that in some cases must be cleared for an image
      // to be rendered correctly. we shouldn't force all consumers to independently erase the
      // contents individually, so we do so here. See issue #131.
      result.eraseColor(Color.TRANSPARENT);
    } else {
      result = createBitmap(width, height, config);
    }

    return result;
  }

  @NonNull
  @Override
  public Bitmap getDirty(int width, int height, Bitmap.Config config) {
    Bitmap result = getDirtyOrNull(width, height, config);
    if (result == null) {
      result = createBitmap(width, height, config);
    }
    return result;
  }
```

---

## 9. 磁盘缓存

### 9.1 接口与实现

```9:21:$GLIDE_SRC/com/bumptech/glide/load/engine/cache/DiskCache.java
public interface DiskCache {

  /** An interface for lazily creating a disk cache. */
  interface Factory {
    /** 250 MB of cache. */
    int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;

    String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
```

底层是 Glide 自己 fork 的 `DiskLruCache`（`com.github.bumptech.glide:disklrucache`），journal 日志 + LRU 淘汰。`DiskCacheWrapper` 对 Key 做 SHA-256 得到安全文件名：

```85:90:$GLIDE_SRC/com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper.java
  @Override
  public File get(Key key) {
    String safeKey = safeKeyGenerator.getSafeKey(key);
```

写盘是**带锁**的（`DiskCacheWriteLocker`），防止同一 Key 并发写。

### 9.2 `DiskCacheStrategy` 的五种策略

| 策略 | 缓存原始数据流 | 缓存变换后结果 | 读时解码 Resource | 读时解码 Data |
|---|---|---|---|---|
| `ALL` | 仅远程 | 是（非磁盘/内存来源） | ✅ | ✅ |
| `NONE` | 否 | 否 | ❌ | ❌ |
| `DATA` | 是（非磁盘/内存来源） | 否 | ❌ | ✅ |
| `RESOURCE` | 否 | 是 | ✅ | ❌ |
| `AUTOMATIC`（**默认**） | 仅远程 | 仅当（来自备用 Key 的 Data 缓存 ‖ 本地数据）且 `EncodeStrategy.TRANSFORMED` | ✅ | ✅ |

```120:144:$GLIDE_SRC/com/bumptech/glide/load/engine/DiskCacheStrategy.java
  public static final DiskCacheStrategy AUTOMATIC =
      new DiskCacheStrategy() {
        @Override
        public boolean isDataCacheable(DataSource dataSource) {
          return dataSource == DataSource.REMOTE;
        }

        @Override
        public boolean isResourceCacheable(
            boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy) {
          return ((isFromAlternateCacheKey && dataSource == DataSource.DATA_DISK_CACHE)
                  || dataSource == DataSource.LOCAL)
              && encodeStrategy == EncodeStrategy.TRANSFORMED;
        }
        ...
```

默认 `AUTOMATIC` 的意图：**远程图只存原始数据流**（省空间，变换后可以随时重算），**本地图（如视频帧、资源文件）存变换后结果**（因为本地数据重新解码成本高，而且本地文件本来就在）。

---

## 10. 结果落地：`Target`

```12:26:$GLIDE_SRC/com/bumptech/glide/request/target/ImageViewTargetFactory.java
public class ImageViewTargetFactory {
  @NonNull
  @SuppressWarnings("unchecked")
  public <Z> ViewTarget<ImageView, Z> buildTarget(
      @NonNull ImageView view, @NonNull Class<Z> clazz) {
    if (Bitmap.class.equals(clazz)) {
      return (ViewTarget<ImageView, Z>) new BitmapImageViewTarget(view);
    } else if (Drawable.class.isAssignableFrom(clazz)) {
      return (ViewTarget<ImageView, Z>) new DrawableImageViewTarget(view);
    } else {
      throw new IllegalArgumentException(
          "Unhandled class: " + clazz + ", try .as*(Class).transcode(ResourceTranscoder)");
    }
  }
}
```

默认 `load()` 返回 `RequestBuilder<Drawable>` → `DrawableImageViewTarget`。

```100:107:$GLIDE_SRC/com/bumptech/glide/request/target/ImageViewTarget.java
  @Override
  public void onResourceReady(@NonNull Z resource, @Nullable Transition<? super Z> transition) {
    if (transition == null || !transition.transition(resource, this)) {
      setResourceInternal(resource);
    } else {
      maybeUpdateAnimatable(resource);
    }
  }
```

`SingleRequest` 里的最后一步（含监听器、动画工厂）：

```661:668:$GLIDE_SRC/com/bumptech/glide/request/SingleRequest.java
      anyListenerHandledUpdatingTarget |=
          targetListener != null
              && targetListener.onResourceReady(result, model, target, dataSource, isFirstResource);

      if (!anyListenerHandledUpdatingTarget) {
        Transition<? super R> animation = animationFactory.build(dataSource, isFirstResource);
        target.onResourceReady(result, animation);
      }
```

`animationFactory.build(dataSource, isFirstResource)` 决定了淡入动画：默认只有**非内存缓存来源且首次**才有过渡动画——所以从内存缓存命中时图片是瞬间出现、不闪的。

---

## 11. 回到本项目：几个可改进点

### 11.1 `Glide.with(imageView.getContext())` 是否安全？

`library_base/.../GlideUtils.java:12` 和 `commonBindingAdapter.java:19` 都是 `Glide.with(imageView.getContext())`。

对照 `RequestManagerRetriever.get(Context)` 的源码：主线程 + 非 Application → 若是 `ContextWrapper` 会**递归剥壳**到 baseContext。AppCompat 下 `ImageView.getContext()` 通常是 Activity 或包了一层 `ContextThemeWrapper` 的 Activity，最终都能剥出 `FragmentActivity`，从而绑定生命周期。

**所以能用，但有隐患**：

- 若在**子线程**调用，会直接退化为 Application 级 `RequestManager`，请求不再随页面销毁而取消。
- 若 View 还没 attach（`getContext()` 为包装的临时 context），同样可能退化。

更稳妥的写法是直接把 `Activity`/`Fragment` 传进去，或至少用 `imageView.getContext()` 前确保在主线程。建议 `GlideUtils` 增加一个接收 `Fragment`/`FragmentActivity` 的重载。

### 11.2 圆形头像：`CircleCrop` 会进缓存 Key

`commonBindingAdapter.java:48` 用 `RequestOptions.bitmapTransform(new CircleCrop())`。因为 transformation 参与 `EngineKey` 计算，同一 URL 的**圆形版和方形版是两份缓存**（这正是我们想要的，圆形图不必重复裁剪）。

但注意 `bitmapTransform()` 是**替换**整个变换集，且 `RequestOptions` 默认 `autoClone` 关闭，`bitmapTransform` 返回的是同一实例——如果在多处复用同一个静态 `RequestOptions` 对象，反复 `apply` 是安全的（`apply` 会 `clone` 合并），但直接在其上链式调用 setter 会污染模板。建议统一用 `RequestOptions.circleCropTransform()` 或 `.transform(...)`，并且**把 options 声明为 `static final` 常量复用**，避免每次 `onBindViewHolder` 都新建对象：

```java
private static final RequestOptions AVATAR_OPTIONS =
        RequestOptions.circleCropTransform()
                .placeholder(R.mipmap.icon_user_unlogin)
                .error(R.mipmap.icon_user_unlogin)
                .fallback(R.mipmap.icon_user_unlogin);
```

### 11.3 空 URL 分支的性能

`commonBindingAdapter.java:51-59` 在 uri 为空时仍然 `Glide.with(...).load(null)...into(view)`。由源码可知 `load(null)` 会置 `isModelSet=true`，`begin()` 里 `model == null` 立刻 `onLoadFailed()` 并显示 `fallback`。行为正确，但走了一遍完整的 `Request` 构建 + `RequestManager.track`，属于不必要的对象分配。更直接的写法是 `imageView.setImageResource(R.mipmap.icon_user_unlogin)`。

### 11.4 缺少 `@GlideModule` 配置

项目只引入了 `glide` 运行时，**没引入 `compiler`**，因此：

- 无法用 `GlideApp.with()` 的流式 API；
- 无法自定义 `MemoryCache` / `BitmapPool` / `DiskCache` 大小；
- 无法替换 `HttpUrlFetcher`（用 `HttpURLConnection`）为 OkHttp。

对于一个短视频类 App（图片量大、列表多），建议补一个 `@GlideModule` 把网络层换成 OkHttp（复用项目里已有的 `okhttp 4.12.0` 连接池），并根据机型调整内存缓存额度。

### 11.5 列表场景别忘了请求去重的前提

`Engine` 的去重依赖 `EngineKey` 相等，而 Key 里有 `width/height`。如果 RecyclerView 的 item  ImageView 用 `wrap_content` 导致每次测量出的尺寸不同，去重会失效。给头像、封面这类固定尺寸的图，**在 XML 里写死 dp** 既省一次测量等待，又能让去重生效。

---

## 12. 一图流总结

```
                        ┌────────────── 主线程 ──────────────┐
Glide.with(activity) ──▶ RequestManagerRetriever
                         └─ LifecycleRequestManagerRetriever
                              Map<Lifecycle, RequestManager>
                                     │ onStart/onStop/onDestroy
                                     ▼
                              RequestManager ── RequestTracker
                                     │
   .load(url).apply(opts) ───────────┤  RequestBuilder (model + BaseRequestOptions)
                                     │
   .into(imageView) ─────────────────┤  SizeDeterminer 测尺寸（LayoutParams > waitForLayout > PreDraw）
                                     ▼
                              SingleRequest.begin() ──▶ onSizeReady
                                     │
                                     ▼
                        ┌─────────── Engine.load(key) ───────────┐
                        │ 1. ActiveResources   (弱引用, 正在用)     │
                        │ 2. LruResourceCache  (LRU, 没人用)       │
                        │ 3. jobs.get(key)     请求去重            │
                        │ 4. 新建 EngineJob + DecodeJob            │
                        └────────────────────┬────────────────────┘
                                             │ 切到子线程
                                             ▼
                              DecodeJob 状态机
                        INITIALIZE → RESOURCE_CACHE → DATA_CACHE
                              → [reschedule 切 source 线程池] → SOURCE → FINISHED
                                             │
                        ResourceCacheGenerator / DataCacheGenerator / SourceGenerator
                                             │
                        ModelLoader → DataFetcher(HttpUrlFetcher)
                                             │
                        LoadPath → DecodePath: decode → transform → transcode
                              (Downsampler 用 BitmapPool 的 inBitmap 复用)
                                             │
                        notifyComplete（主线程回调显示） → 再写 RESOURCE 磁盘缓存
```

**三条主线记住就够了：**

1. **生命周期**：4.16 用 AndroidX `Lifecycle` 绑定，不再插空白 Fragment；`onStop` 只暂停不清除，`onDestroy` 才清空并从 Map 移除。
2. **缓存**：活动资源（弱引用）+ LruCache 引用计数分层，加上 `EngineJob` 请求去重，构成内存侧的两大优化。
3. **解码**：状态机 + 三级 Generator + `inBitmap` 复用，且**回调先于落盘**、**网络阶段主动换线程池**。
