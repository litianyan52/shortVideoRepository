package com.libase.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;

import me.jessyan.autosize.AutoSizeConfig;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG, "onCreate: ");
        // 修复BuildConfig引用 - 使用应用的BuildConfig
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);

        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    /**
     * 生成一个全局可用的Context
     * @return
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
