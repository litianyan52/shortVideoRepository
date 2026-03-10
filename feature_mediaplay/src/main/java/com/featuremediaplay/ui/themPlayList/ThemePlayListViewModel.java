package com.featuremediaplay.ui.themPlayList;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.themPlayListBean.ResThemePlayList;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;

import java.util.List;

public class ThemePlayListViewModel extends BaseViewmodel {
    private ThemePlayListModel model;

    private MutableLiveData<List<ResThemePlayList>>  mResThemePlayData =  new MutableLiveData<>();
    public ThemePlayListViewModel(){
        model = new ThemePlayListModel();
    }


    public MutableLiveData<List<ResThemePlayList>> getResThemePlayData() {
        return mResThemePlayData;
    }

    /**
     * 请求主题播单列表数据
     */
    public void RequestThemeListData(){
        model.requestThemeListData(new IRequestCallBack<List<ResThemePlayList>>() {
            @Override
            public void RequestSuccess(List<ResThemePlayList> result) {
                mResThemePlayData.setValue(result);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
            }
        });
    }


}
