# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile




# 保留 Entity 类和 Dao 接口
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.Dao
-keepclassmembers class * {
    @androidx.room.* *;
}

# 保留 ARouter 生成的类和方法
-keep class com.alibaba.android.arouter.** { *; }
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe { *; }
-keep class * implements com.alibaba.android.arouter.facade.template.IInterceptor { *; }
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider { *; }
-keep public class * extends com.alibaba.android.arouter.routes.** { *; }

# 保留 javax 相关类（解决 Element 缺失问题）
-dontwarn javax.lang.model.**
-keep class javax.lang.model.element.Element { *; }


# 保留 Retrofit 接口和注解
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp 规则
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class okio.** { *; }
-dontwarn okio.**

# 保留 Glide 和资源加载类
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.** { *; }
-keep class * implements com.bumptech.glide.load.resource.bitmap.BitmapTransformation { *; }

# 保留 EventBus 事件类和订阅方法
-keep class org.greenrobot.eventbus.** { *; }
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# 保留自定义 View 的构造方法
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
}


# 保留通过反射调用的类和方法
-keep class com.example.demo20shvideoproject.** { *; }  # 替换为你的主包名






-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}