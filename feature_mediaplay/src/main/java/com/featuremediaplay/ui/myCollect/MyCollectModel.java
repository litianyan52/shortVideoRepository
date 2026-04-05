package com.featuremediaplay.ui.myCollect;

import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.example.video_data.bean.ResCollection;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;

import retrofit2.Call;

public class MyCollectModel {

    /**
     * 获取收藏列表（一次性加载）
     *
     * @param requestCallBack 回调
     */
    public void getCollectionList(IRequestCallBack<ResBase<ResCollection>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<ResCollection>> call = apiService.getCollectionList(
                UserManager.getInstance().getUserToken(),
                1
        );
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResCollection>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResCollection> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }
}
