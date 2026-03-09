package com.featurepiazza.fragment;

import android.util.Log;

import com.featurepiazza.api.PiazzaApiService;
import com.featurepiazza.api.PiazzaApiServiceProvider;
import com.featurepiazza.bean.ResPiazza;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class PiazzaModel {
    private static final String TAG = "PiazzaModel";

    public void requestData(IRequestCallBack requestCallBack) {
        PiazzaApiService piazzaApiService = PiazzaApiServiceProvider.provider();
        Call<ResBase<List<ResPiazza>>> call = piazzaApiService.getDatas();

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResPiazza>>>() {
            @Override
            public void GetLiseSuccess(ResBase<List<ResPiazza>> result) {
                Log.d(TAG, "requestData: ");
                requestCallBack.RequestSuccess(result.getData());
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });


    }
}
