package com.featureuser.api;

import com.network.RetrofitProvider;

import retrofit2.Retrofit;
public class UserApiServiceProvider {
    private static UserApiService mUserApiService;
    public static UserApiService provider()
    {
        if (mUserApiService ==null)
        {
            Retrofit retrofit = RetrofitProvider.provider();
            mUserApiService = retrofit.create(UserApiService.class);
            return mUserApiService;
        }
        else
        {
            return mUserApiService;
        }
    }
}
