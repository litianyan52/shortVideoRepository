package com.featurepiazza.api;

import com.network.RetrofitProvider;
import retrofit2.Retrofit;
public class PiazzaApiServiceProvider {
    private static PiazzaApiService mPiazzaApiService;
    public static PiazzaApiService provider()
    {
        if (mPiazzaApiService==null)
        {
            Retrofit retrofit = RetrofitProvider.provider();
            mPiazzaApiService = retrofit.create(PiazzaApiService.class);
            return mPiazzaApiService;
        }
        else
        {
            return mPiazzaApiService;
        }
    }
}
