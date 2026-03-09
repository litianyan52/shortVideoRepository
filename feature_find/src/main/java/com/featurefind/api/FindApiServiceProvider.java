package com.featurefind.api;

import com.network.RetrofitProvider;

import retrofit2.Retrofit;
public class FindApiServiceProvider {
    private static FindApiService mFindApiService;
    public static FindApiService provider()
    {
        if (mFindApiService==null)
        {
            Retrofit retrofit = RetrofitProvider.provider();
            mFindApiService = retrofit.create(FindApiService.class);
            return mFindApiService;
        }
        else
        {
            return mFindApiService;
        }
    }
}
