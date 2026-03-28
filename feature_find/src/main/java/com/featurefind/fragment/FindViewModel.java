package com.featurefind.fragment;

import androidx.lifecycle.MutableLiveData;

import com.featurefind.bean.ResFind;
import com.featurefind.bean.Topic;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.network.bean.ResBase;

import java.util.List;

public class FindViewModel extends BaseViewmodel {
    private FindModel mModel;
    private static final String TAG = "FindViewModel";
    private MutableLiveData<ResFind> resFindData = new MutableLiveData<>();
    private MutableLiveData<List<Topic>> topicDataList = new MutableLiveData<>();


    public MutableLiveData<List<Topic>> getTopicDataList() {
        return topicDataList;
    }

    public MutableLiveData<ResFind> getResFindData() {
        return resFindData;
    }

    public FindViewModel() {
        mModel = new FindModel();
    }

    /**
     * 请求发现页数据
     */
    public void requestData() {
        mModel.requestData(new IRequestCallBack<ResBase<ResFind>>() {
            @Override
            public void RequestSuccess(ResBase<ResFind> result) {
                resFindData.setValue(result.getData());
                topicDataList.setValue(resFindData.getValue().getTopic());
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
            }
        });
    }

}
