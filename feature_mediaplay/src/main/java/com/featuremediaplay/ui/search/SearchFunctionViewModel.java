package com.featuremediaplay.ui.search;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.searchVideoBean.ResSearch;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.network.bean.ResBase;

import java.util.List;

public class SearchFunctionViewModel extends BaseViewmodel {
    private MutableLiveData<List<ResSearch>> mData = new MutableLiveData<>(); //搜索结果

    private final SearchFunctionModel mModel;

    public SearchFunctionViewModel() {
        mModel = new SearchFunctionModel();
    }

    public MutableLiveData<List<ResSearch>> getData() {
        return mData;
    }

    public void getSearchResult(String keyword)
    {
        mModel.getSearchResult(keyword, new IRequestCallBack<ResBase<List<ResSearch>>>() {
            @Override
            public void RequestSuccess(ResBase<List<ResSearch>> result) {
                mData.setValue(result.getData());
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
            }
        });
    }
}
