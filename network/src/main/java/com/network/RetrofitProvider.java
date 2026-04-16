package com.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static volatile Retrofit mretrofit;
    private static final String BASE_URL = "https://titok.fzqq.fun";

    public static Retrofit provider() {
        if (mretrofit == null) {

            synchronized (RetrofitProvider.class){
                if (mretrofit == null){
                    mretrofit = new Retrofit
                            .Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClientProvider.provide())
                            .build();
                }
            }

            return mretrofit;
        } else {
            return mretrofit;
        }
    }
}
