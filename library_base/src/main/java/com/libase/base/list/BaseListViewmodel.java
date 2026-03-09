package com.libase.base.list;


import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;
import com.network.bean.ResList;

import java.util.List;

public class BaseListViewmodel<T, V extends BaseListModel> extends BaseViewmodel implements IListListener<T> {
    protected V mModel;
    private static final String TAG = "BaseListViewmodel";

    public BaseListViewmodel(V Model) {

        this.mModel = Model;
        mModel.iListListener = this;
    }

    protected MutableLiveData<ResList<T>> mData = new MutableLiveData<>();
    protected MutableLiveData<Boolean> enableLoadMore = new MutableLiveData<>(true);

    public MutableLiveData<ResList<T>> getmData() {
        return mData;
    }

    /**
     * 请求列表数据
     * @param isFirst 判断是否是第一次加载
     */
    public void requestListData(boolean isFirst) {

        if (isFirst) {
            enableLoadMore.setValue(true);
            mModel.requestDatas(isFirst);
        } else {
            mModel.requestDatas(isFirst);  //这里不确定
        }
    }

    public MutableLiveData<Boolean> getEnableLoadMore() {
        return enableLoadMore;
    }

    @Override
    public void GetSuccess(ResList<T> datas, boolean isFirst) {

        if (isFirst) {
          //  Log.d(TAG, "requestData: " + datas);
            mData.setValue(datas);
        } else {
            // Log.d(TAG, "GetSuccess: " + mData.getValue());
            if (mData.getValue() != null)  //如果第一次请求还未发起就加载更多此时的data是null
            {
                List<T> list = mData.getValue().getList();
                list.addAll(datas.getList());
                mData.setValue(mData.getValue());
            }
        }

        if (mData.getValue().getList().size() >= datas.getCount()) {
            enableLoadMore.setValue(false);  //在每次请求完成后,如果data中的数据已经超过或等于了服务器中数据数量则不允许再进行请求

        }
    }

    @Override
    public void GetFailed(int status_code) {
        mErrorCode.setValue(status_code);
    }
}
