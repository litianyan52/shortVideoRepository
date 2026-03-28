package com.featurefind.ui.categoryDetail;

import com.featurefind.api.FindApiService;
import com.featurefind.api.FindApiServiceProvider;
import com.featurefind.bean.BodyCategoryDataOne;
import com.featurefind.bean.ResCategoryDataOne;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;

import retrofit2.Call;

public class CategoryDetailModel {


    public void getCategoryDetailOne(String channel_id, IRequestCallBack<ResBase<ResCategoryDataOne>> requestCallBack) {
        FindApiService apiService = FindApiServiceProvider.provider();
        Call<ResBase<ResCategoryDataOne>> call = apiService.getCategoryDetailOne(new BodyCategoryDataOne(channel_id));
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResCategoryDataOne>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResCategoryDataOne> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }

}
