package com.featuremediaplay.ui.search;

import com.example.video_data.bean.searchVideoBean.ResSearch;
import com.example.video_data.bean.searchVideoBean.SearchBody;
import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class SearchFunctionModel {
    public void getSearchResult(String keyword, IRequestCallBack<ResBase<List<ResSearch>>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<List<ResSearch>>> call = apiService.getSearchResult(new SearchBody(keyword));
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResSearch>>>() {
            @Override
            public void GetLiseSuccess(ResBase<List<ResSearch>> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }
}
