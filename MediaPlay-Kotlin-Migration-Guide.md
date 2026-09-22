# MediaPlay 模块 Kotlin 改造指南

> `feature_mediaplay` 目前为 Java 实现，`library_base` 已配置好 Kotlin，`kotlinVersion` 包下已有部分 Kotlin 雏形但存在编译错误。以下为标准改造方案（自底向上：Model → ViewModel → Manager → Adapter → Activity）。

---

## 一、第 0 步：Gradle 配置（必做，否则 .kt 不参与编译）

`library_base/build.gradle` 已配好 Kotlin，但 **`featureConfig.build.gradle`（feature_mediaplay 引用的公共配置）没有 Kotlin 插件**：

```groovy
// featureConfig.build.gradle
apply plugin: 'com.android.library'
apply plugin: 'org.jetbrains.kotlin.android'    // ← 新增

android {
    kotlinOptions { jvmTarget = '11' }          // 与 library_base 一致
}

dependencies {
    implementation libs.viewmodel.kt   // viewModelScope；library_base 是 implementation 不传递，需自己加
    implementation libs.core.ktx
    // 若 toml 无 coroutines 条目则补：org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1
}
```

`libs.versions.toml` 已具备 `kotlin-android` 插件（kotlin 2.0.21）、`core-ktx`、`viewmodel-kt`，无需重复声明。

---

## 二、Activity 标准写法（对照 MediaPlayActivity.java）

```kotlin
@Route(path = ARouterPath.ACTIVITY_MEDIAPLAY)
class MediaPlayActivity : BaseActivity<MediaPlayViewModel, ActivityMediaPlayBinding>() {

    companion object {
        private const val TAG = "MediaPlayActivity"
        fun start(videoId: String) {
            ARouter.getInstance().build(ARouterPath.ACTIVITY_MEDIAPLAY)
                .withString("videoId", videoId).navigation()
        }
    }

    @Autowired(name = "videoId")
    @JvmField
    var mVideoId: String? = null

    private lateinit var mPlayer: MediaPlayerManager
    private lateinit var mTabMediator: TabLayoutMediator

    override fun getLayoutId() = R.layout.activity_media_play

    override fun getViewModel(): MediaPlayViewModel =
        ViewModelProvider(this)[MediaPlayViewModel::class.java]

    override fun initView() {
        ARouter.getInstance().inject(this)
        initTab()
        initPlayer()
    }

    override fun initData() {
        mVideoId?.let { id -> mViewModel.requestVideoInfo(id) }
        mViewModel.videoInfo.observe(this) { info ->
            info ?: return@observe
            mdataBinding.setVariable(BR.viewModel, info)
        }
        mViewModel.comments.observe(this) { /* 通知评论 Fragment 刷新 */ }
    }

    private fun initTab() {
        val fragments = listOf(MediaPlayInfoFragment(), MediaPlayCommentFragment())
        val titles = listOf("简介", "评论")
        mdataBinding.vpMediaPlay.adapter = MediaPlayFragmentAdapter(this, fragments)
        mTabMediator = TabLayoutMediator(mdataBinding.tabMediaPlay, mdataBinding.vpMediaPlay) { tab, pos ->
            tab.text = titles[pos]
        }
        mTabMediator.attach()
    }

    private fun initPlayer() {
        mPlayer = MediaPlayerManager.getInstance(this)
        mPlayer.setPlayerListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> dismissLoading()
                    Player.STATE_BUFFERING -> showLoading()
                    else -> Unit
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                showToastText("播放出错：${error.message}")
            }
        })
        mPlayer.initPlayer()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) { /* 与 Java 版逻辑一致 */ }

    override fun onPause()   { super.onPause();   mPlayer.pausePlayer() }
    override fun onResume()  { super.onResume();  mPlayer.resumePlayer() }
    override fun onDestroy() { super.onDestroy(); mPlayer.releasePlayer() }

    override fun onBackPressed() {
        if (mPlayer.isPlayerPlaying) mPlayer.pausePlayer() else super.onBackPressed()
    }
}
```

**关键点：**

1. **`@JvmField` 不能省**：ARouter 反射直接读字段，Kotlin 属性默认是 private field + getter，`@JvmField` 才暴露为 public 字段。
2. **`getViewModel()` 收窄为非空**：Java 基类签名是平台类型，Kotlin 覆写直接声明非空，调用处免判空。
3. **匿名内部类 → `object : Player.Listener { }`**：`Player.Listener` 有多个带默认实现的方法，不能用 SAM lambda。
4. **`switch` → `when`**：无 fall-through、无 break。
5. **`observe(this) { }`**：`Observer<T>` 是 Java SAM 接口，直接传 lambda。
6. **`mdataBinding.getRoot()` → `mdataBinding.root`**：getter 按属性访问。
7. **空安全**：`mVideoId?.let { }` 代替 `if (mVideoId != null)`。
8. **`lateinit var`**：用于 onCreate 必然初始化的成员；时序不确定时改可空 `var x: T? = null` + `?.`。
9. **EventBus `@Subscribe` 方法不能是 private**，保持默认 public。

---

## 三、ViewModel 标准写法（对照 MediaPlayViewModel.java）

```kotlin
class MediaPlayViewModel : BaseViewmodel<MediaPlayModel>() {

    private val mModel = MediaPlayModel()

    // LiveData 标准封装：私有可变 + 公开只读
    private val _videoInfo = MutableLiveData<ResVideoAllInfo>()
    val videoInfo: LiveData<ResVideoAllInfo> get() = _videoInfo

    private val _comments = MutableLiveData<MutableList<ResComment>>(mutableListOf())
    val comments: LiveData<MutableList<ResComment>> get() = _comments

    fun requestVideoInfo(videoId: String) {
        showLoading()
        viewModelScope.launch {
            when (val result = mModel.requestVideoInfo(videoId, token)) {
                is NetworkStatusResult.Success -> {
                    _videoInfo.value = result.data
                    getVideoComments(videoId, 0, 10, false)
                }
                is NetworkStatusResult.Empty -> showToastText(result.msg ?: "暂无数据")
                is NetworkStatusResult.Error -> showToastText(result.errorMessage ?: "请求失败")
            }
            dismissLoading()
        }
    }

    fun getVideoComments(videoId: String, page: Int, size: Int, isLoadMore: Boolean) {
        viewModelScope.launch {
            when (val result = mModel.getVideoComments(videoId, page, size, token)) {
                is NetworkStatusResult.Success -> {
                    val newList = result.data?.list ?: emptyList()
                    val current = _comments.value ?: mutableListOf()
                    if (isLoadMore) current.addAll(newList)
                    else { current.clear(); current.addAll(newList) }
                    _comments.value = current
                }
                is NetworkStatusResult.Empty -> if (!isLoadMore) _comments.value = mutableListOf()
                is NetworkStatusResult.Error -> showToastText(result.errorMessage ?: "评论加载失败")
            }
        }
    }

    // 点赞/关注/收藏等操作类请求同一模式
    fun requestLikeVideo(videoId: String, isLike: Boolean) {
        viewModelScope.launch {
            when (val result = mModel.requestLikeVideo(videoId, isLike)) {
                is NetworkStatusResult.Success -> showToastText("操作成功")
                is NetworkStatusResult.Error -> showToastText(result.errorMessage ?: "操作失败")
                is NetworkStatusResult.Empty -> Unit
            }
        }
    }
}
```

**关键点：**

1. **构造逻辑写 `init { }`**：`fun MediaPlayViewModel()` 是错误写法（那只是普通函数）。
2. **LiveData 封装规范**：`private val _xxx = MutableLiveData()` + `val xxx: LiveData get() = _xxx`，UI 层只能 observe 不能 setValue。
3. **`MutableLiveData<List<T>>(null)` 是错的**：构造参数就是初始值，应传 `mutableListOf()`。
4. **`viewModelScope.launch { }`** 替代 `ApiCall.enqueue(..., callback)`：回调地狱变顺序代码，Activity 销毁时协程自动取消；Model 的 suspend 函数直接返回 `NetworkStatusResult`，`when` 解包即可，无需 `collect`、无需 Flow。
5. **`when` 对 sealed interface 穷尽分支**：写全 `Success/Empty/Error` 即可，无需 `else`；新增子类时编译器强制补分支。
6. **智能转换**：`is Success -> result.data` 自动转换类型，无需强转。
7. **`viewModelScope` 默认主线程**，`_xxx.value = ...` 直接安全；切子线程后用 `postValue`。

---

## 四、Model 标准写法（对照 MediaPlayModel.java）

```kotlin
class MediaPlayModel {

    private val mApiService: MediaApiService =
        ApiRetrofit.getInstance().getApiService(MediaApiService::class.java)

    suspend fun requestVideoInfo(videoId: String, token: String): NetworkStatusResult<ResVideoAllInfo> =
        ApiCallKotlinVersion.enqueue { mApiService.getVideoInfo(videoId, token) }

    suspend fun getVideoComments(token: String, aid: Int, page: Int): NetworkStatusResult<ResList<ResComment>> =
        ApiCallKotlinVersion.enqueue { mApiService.getCommentList(token, aid, page) }

    suspend fun requestLikeVideo(token: String, body: LikeAddBody): NetworkStatusResult<ResLike> =
        ApiCallKotlinVersion.enqueue { mApiService.Like(token, body) }
}
```

**关键点：**

1. **不需要 Flow**：MediaPlay 的接口全是"一次请求 → 一次响应 → 结束"，`suspend fun` 直返 `NetworkStatusResult<T>` 就是最简方案。项目已有的 `ApiCallKotlinVersion.enqueue { }` 正是 suspend 包装（内部 try-catch 把异常归一化为 `Error`），直接复用。
2. **Retrofit 接口用原生 suspend**：`MediaApiService.kt` 已声明 `suspend fun getVideoInfo(...): ResBase<ResVideoAllInfo>`，Retrofit 2.6+ 原生支持，无需 RxJava Observable、无需任何 Flow/协程适配器。
3. **token 传递**：与 Java 版保持一致（`@Header("token")` 参数传入）；长期可在 OkHttp 拦截器统一处理。
4. **保持 `class` 而非 `object`**：Model 由 ViewModel 持有（与基类泛型 `BaseViewmodel<M>` 兼容）。

**Flow 什么时候才值得用？** 判断标准一句话：**一次发射用 suspend，多次发射才用 Flow**。

| 场景 | 方案 |
|---|---|
| 一次性请求（详情/分页/点赞/收藏） | `suspend fun` 直返 ← MediaPlay 全部属于此类 |
| 搜索框输入联想（需要 debounce） | `flow { }` + `debounce`/`flatMapLatest` |
| 持续观察数据源（Room 表变化、DataStore） | DAO 直接返回 `Flow<T>` |
| 轮询 / WebSocket / 倒计时 | `flow { while(true) { emit(...) } }` |
| 跨层事件推送（一次性事件总线） | `SharedFlow` |

MediaPlay 模块目前没有以上任何场景，**全链路不引入 Flow**；将来若做"搜索联想"或用 Room 观察浏览记录表变化，再局部引入即可，不必为迁移而全链路上 Flow。

---

## 五、MediaPlayerManager 标准写法（对照 MediaPlayerManager.java）

```kotlin
class MediaPlayerManager private constructor(context: Context) {

    private var mPlayer: ExoPlayer? = null

    fun initPlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayer.Builder(context.applicationContext).build()
        }
    }

    fun setPlayerListener(listener: Player.Listener) {
        mPlayer?.addListener(listener)
    }

    fun playVideo(url: String) {
        val player = mPlayer ?: return
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    fun pausePlayer()   { mPlayer?.pause() }
    fun resumePlayer()  { mPlayer?.play() }
    fun releasePlayer() { mPlayer?.release(); mPlayer = null }

    val isPlayerPlaying: Boolean
        get() = mPlayer?.isPlaying ?: false

    companion object {
        @Volatile
        private var instance: MediaPlayerManager? = null

        fun getInstance(context: Context): MediaPlayerManager =
            instance ?: synchronized(this) {
                instance ?: MediaPlayerManager(context).also { instance = it }
            }
    }
}
```

**关键点：**

1. **`private constructor` + `companion object` 双检锁** 替代 Java 的 `private static volatile` + `getInstance`；`@Volatile` 保留。
2. **`?: return` / `?.`** 替代 null 判断；`val isPlayerPlaying get() = ...` 替代 `isPlayerPlaying()` 方法（getter 语义）。
3. **`context.applicationContext`** 防止 Activity 泄漏（单例持有 Context 必须用应用级）。

---

## 六、BrowseRecordAdapter 标准写法（对照 BrowseRecordAdapter.java）

```kotlin
class BrowseRecordAdapter : BaseAdapter<AdapterBrowseRecordItemBinding, UserLookRecord>() {

    override fun convert(db: AdapterBrowseRecordItemBinding, position: Int, t: UserLookRecord) {
        db.tvTitle.text = t.title
        db.tvTime.text = t.lookTime
        // 图片加载与点击事件与 Java 版一致
        db.root.setOnClickListener { /* 跳转播放页 */ }
    }

    override fun convert(db: AdapterBrowseRecordItemBinding, t: UserLookRecord, payloads: List<Any>) {
        // 局部刷新，Java 版为空实现则同样留空
    }
}
```

**关键点：**

1. 继承 Java 泛型基类时泛型参数照写；`convert` 覆写签名以 `BaseAdapter` 实际声明为准（`List<Object>` 在 Kotlin 里写 `List<Any>`，平台类型可按需收窄）。
2. **`db.tvTitle.text = t.title`**：ViewBinding 属性访问替代 `setText()`。
3. `addChildClickViewIds(...)`、`setOnItemChildClickListener { ... }` 用法与 Java 版相同，直接传 lambda。

---

## 七、现有 kotlinVersion 雏形的问题清单（改造时需修正）

| 文件 | 问题 | 修正 |
|---|---|---|
| `MediaPlayActivity.kt` | `mPlayer` 声明为 lateinit 但 `initPlayer()` 从未调用 → 运行时 `UninitializedPropertyAccessException` | `initView()` 中先 `initPlayer()` |
| `MediaPlayActivity.kt` | `getViewModel()` 返回可空 `MediaPlayViewModel?` | 收窄为非空返回 |
| `MediaPlayActivity.kt` | `mdataBinding.getRoot()` | 改为 `mdataBinding.root` |
| `MediaPlayActivity.kt` | 缺 `initTab()`、EventBus 注册/注销、onPause/onResume/onDestroy | 按本文第二节补全 |
| `MediaPlayViewModelKotlinVersion.kt` | `MutableStateFlow<ResVideoAllInfo>()` 缺初始值，编译不过 | 删掉 StateFlow——一次性请求场景不需要流；UI 状态用 LiveData（与项目现有架构一致） |
| `MediaPlayViewModelKotlinVersion.kt` | `fun MediaPlayViewModel()` 伪构造函数 | 删除，逻辑放 `init { }` |
| `MediaPlayViewModelKotlinVersion.kt` | `MutableLiveData<List<ResComment>>(null)` | 传 `mutableListOf()` |
| `MediaPlayViewModelKotlinVersion.kt` | 大量分号残留；`RequestVideoInfo` 的 collect 为空实现 | 去分号；collect 中按 sealed 分支处理并赋值 LiveData |
| `MediaPlayModelKotlinVersion.kt` | `import okhttp3.Dispatcher` 无用导入 | 删除 |
| `MediaPlayModelKotlinVersion.kt` | token 参数设计与 Java 版不一致 | 与 Java 版对齐，避免迁移时行为变化 |

---

## 八、Java → Kotlin 语法速查表

| Java | Kotlin |
|---|---|
| `public static final String TAG` | `companion object { const val TAG }` |
| `private static volatile X instance` + 双检锁 | `companion object { @Volatile private var instance }` |
| `new Player.Listener() { ... }` | `object : Player.Listener { ... }` |
| `switch (x) { case A: ...; break; }` | `when (x) { A -> ... }` |
| `if (x != null) x.foo()` | `x?.foo()` / `x?.let { }` |
| `X x = new X()` | `val x = X()` / `lateinit var x: X` |
| `(View v) -> {...}` / 匿名类 | `{ v -> ... }`（SAM） |
| `setText(s)` / `getText()` | `.text = s` / `.text` |
| `@Override` | `override`（必须显式） |
| `extends` / `implements` | `:` |
| `instanceof` | `is` |
| `(X) y` 强转 | `y as X` / 安全 `y as? X` |
| `String s`（可能为 null） | `var s: String? = null` |

---

## 九、注意事项

1. **注解照常使用**：`@Route`、`@Autowired`（配 `@JvmField`）、`@Subscribe`、Room 的 `@Entity/@Dao` 在 Kotlin 中写法不变。
2. **每改完一个类删除对应 Java 文件**：避免 ARouter 路由重复注册、EventBus 重复订阅、Room 双实现类冲突。
3. **混编期可见性**：Java 可直接调用 Kotlin 的 `@JvmStatic`/`@JvmField` 成员；companion object 的常量在 Java 侧是 `Xxx.Companion.TAG`，加 `@JvmStatic`/`const` 后才是 `Xxx.TAG`。
4. **混淆规则**：现有 keep 规则按类名匹配，Kotlin 重命名包（如 `kotlinVersion`）后同步更新 proguard 规则。
5. **验证方式**：每步改完执行 `gradlew :feature_mediaplay:compileDebugKotlin`（配置插件后）确认编译，再进行下一步。
