package com.featuremediaplay.api;


import com.network.RetrofitProvider;

import retrofit2.Retrofit;

public class MediaApiServiceProvider {
    private static MediaApiService mMediaApiServiceApiService;
    public static MediaApiService provider()
    {
        if (mMediaApiServiceApiService ==null)
        {
            Retrofit retrofit = RetrofitProvider.provider();
            mMediaApiServiceApiService = retrofit.create(MediaApiService.class);
            return mMediaApiServiceApiService;
        }
        else
        {
            return mMediaApiServiceApiService;
        }
    }
}
