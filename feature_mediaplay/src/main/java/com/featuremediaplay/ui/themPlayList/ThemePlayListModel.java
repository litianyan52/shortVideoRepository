package com.featuremediaplay.ui.themPlayList;

import com.example.video_data.bean.themPlayListBean.ResThemePlayList;
import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class ThemePlayListModel {


    /**
     *  请求主题播单列表数据
     * @param callBack
     */
    public void requestThemeListData(IRequestCallBack<List<ResThemePlayList>> callBack){
        MediaApiService service = MediaApiServiceProvider.provider();
        Call<ResBase<List<ResThemePlayList>>> call = service.getThemeListResult();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResThemePlayList>>>() {
            @Override
            public void GetLiseSuccess(ResBase<List<ResThemePlayList>> result) {
                callBack.RequestSuccess(result.getData());
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                callBack.RequestFailed(errorCode, message);
            }
        });
    }
}
