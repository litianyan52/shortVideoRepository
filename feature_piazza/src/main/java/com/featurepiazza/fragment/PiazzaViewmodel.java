package com.featurepiazza.fragment;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.featurepiazza.bean.ResPiazza;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;

import java.util.List;

public class PiazzaViewmodel extends BaseViewmodel implements IRequestCallBack<List<ResPiazza>> {
    public MutableLiveData<List<ResPiazza>> getmData() {
        return mData;
    }

    private static final String TAG = "PiazzaViewmodel";
    private final PiazzaModel mModel;
    private MutableLiveData<List<ResPiazza>> mData = new MutableLiveData<>();

    public PiazzaViewmodel() {
        mModel = new PiazzaModel();
    }

    public void RequestData() {
        mModel.requestData(this);
    }

    @Override
    public void RequestSuccess(List<ResPiazza> result) {
        mData.setValue(result);
        Log.d(TAG, "RequestSuccess: " + result.get(0).getLists().get(1).getUrl());
    }

    @Override
    public void RequestFailed(int codeOfError, String errorMsg) {
        mErrorCode.setValue(codeOfError);  //BaseFragment会处理
    }
}
