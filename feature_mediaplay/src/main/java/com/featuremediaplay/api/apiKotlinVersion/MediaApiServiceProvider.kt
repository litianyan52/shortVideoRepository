package com.featuremediaplay.api.apiKotlinVersion

import com.network.RetrofitProvider

object MediaApiServiceProvider {
    private val mMediaApiService : MediaApiService by lazy {
        val retrofit = RetrofitProvider.provider()
        retrofit.create(MediaApiService::class.java)
    }

    fun provider(): MediaApiService = mMediaApiService
}


//private static MediaApiService mMediaApiServiceApiService;
//public static MediaApiService provider()
//{
//    if (mMediaApiServiceApiService ==null)
//    {
//        Retrofit retrofit = RetrofitProvider.provider();
//        mMediaApiServiceApiService = retrofit.create(MediaApiService.class);
//        return mMediaApiServiceApiService;
//    }
//    else
//    {
//        return mMediaApiServiceApiService;
//    }
//}