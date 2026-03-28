package com.network;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class okHttpClientProvider {
    private static OkHttpClient mOkHttpClient;

    /**
     * 单例设计
     * @return
     */
    public static OkHttpClient provide() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {  //没有则创建一个
            mOkHttpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            return mOkHttpClient;
        } else {
            return mOkHttpClient; //有就直接返回
        }
    }

}
