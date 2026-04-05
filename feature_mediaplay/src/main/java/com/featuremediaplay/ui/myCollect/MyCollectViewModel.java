package com.featuremediaplay.ui.myCollect;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.CollectionItem;
import com.example.video_data.bean.ResCollection;
import com.example.video_data.bean.ResCollectionList;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.network.bean.ResBase;

import java.util.ArrayList;
import java.util.List;

public class MyCollectViewModel extends BaseViewmodel {

    private MyCollectModel mModel;
    private MutableLiveData<List<CollectionItem>> mCollectionList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> mIsEmpty = new MutableLiveData<>(false);

    public MyCollectViewModel() {
        mModel = new MyCollectModel();
    }

    public MutableLiveData<List<CollectionItem>> getCollectionList() {
        return mCollectionList;
    }

    public MutableLiveData<Boolean> getIsEmpty() {
        return mIsEmpty;
    }

    /**
     * 加载收藏列表（一次性加载）
     */
    public void loadCollectionList() {
        showLoading(true);

        mModel.getCollectionList(new IRequestCallBack<ResBase<ResCollection>>() {
            @Override
            public void RequestSuccess(ResBase<ResCollection> data) {
                showLoading(false);
                if (data != null && data.getData() != null && data.getData().getCollectionList() != null) {
                    ResCollectionList list = data.getData().getCollectionList();
                    List<CollectionItem> items = list.getData();
                    if (items != null && !items.isEmpty()) {
                        mCollectionList.setValue(items);
                        mIsEmpty.setValue(false);
                    } else {
                        mCollectionList.setValue(new ArrayList<>());
                        mIsEmpty.setValue(true);
                    }
                } else {
                    mCollectionList.setValue(new ArrayList<>());
                    mIsEmpty.setValue(true);
                }
            }

            @Override
            public void RequestFailed(int errorCode, String message) {
                showLoading(false);
                showToastText(message);
                mIsEmpty.setValue(true);
            }
        });
    }
}
