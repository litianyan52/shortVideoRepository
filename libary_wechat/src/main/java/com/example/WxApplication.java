package com.example;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxApplication extends Application {
    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wx9cd1208e8ddd6deb";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private static IWXAPI api;

    public static IWXAPI getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        regToWx();
        super.onCreate();
    }

    private  void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(WxApplication.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }
}
