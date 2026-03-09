package com.featurefind.fragment;

import com.featurefind.api.FindApiService;
import com.featurefind.api.FindApiServiceProvider;
import com.featurefind.bean.BodyCategoryDataOne;
import com.featurefind.bean.ResCategoryDataOne;
import com.featurefind.bean.ResFind;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;

import retrofit2.Call;

public class FindModel {
    /**
     * 获取发现页数据
     *
     * @param requestCallBack
     */
    public void requestData(IRequestCallBack<ResBase<ResFind>> requestCallBack) {
        FindApiService apiService = FindApiServiceProvider.provider();
        Call<ResBase<ResFind>> call = apiService.getFindData();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResFind>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResFind> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


}
