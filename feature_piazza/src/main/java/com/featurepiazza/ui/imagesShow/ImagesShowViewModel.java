package com.featurepiazza.ui.imagesShow;

import androidx.lifecycle.MutableLiveData;

import com.featurepiazza.bean.ResPiazza;
import com.libase.base.BaseViewmodel;

import java.util.List;

public class ImagesShowViewModel extends BaseViewmodel {
    private MutableLiveData<ResPiazza.ResPiazzaDetail> mResPiazzaDetail = new MutableLiveData<>();
    private MutableLiveData<String> mTitle = new MutableLiveData<>();
    private MutableLiveData<String> mAuthor = new MutableLiveData<>();
    private MutableLiveData<String> mAvatar = new MutableLiveData<>();
    private MutableLiveData<List<String>> mImages = new MutableLiveData<>();
    //是否关闭显示图片的Activity
    private MutableLiveData<Boolean> mIsFinishCurrentImageActivity = new MutableLiveData<>();

    private MutableLiveData<String> mIndex = new MutableLiveData<>();

    public MutableLiveData<ResPiazza.ResPiazzaDetail> getmResPiazzaDetail() {
        return mResPiazzaDetail;
    }

    public MutableLiveData<String> getTitle() {
        return mTitle;
    }

    public MutableLiveData<String> getAuthor() {
        return mAuthor;
    }

    public MutableLiveData<List<String>> getImages() {
        return mImages;
    }

    public MutableLiveData<String> getIndex() {
        return mIndex;
    }

    public MutableLiveData<String> getAvatar() {
        return mAvatar;
    }

    public MutableLiveData<Boolean> getIsFinishCurrentImageActivity() {
        return mIsFinishCurrentImageActivity;
    }

    /**
     * 加载数据
     * @param resPiazzaDetail
     */
    public void LoadData(ResPiazza.ResPiazzaDetail resPiazzaDetail) {
        mResPiazzaDetail.setValue(resPiazzaDetail);
        mTitle.setValue(mResPiazzaDetail.getValue().getTitle());
        mAuthor.setValue(mResPiazzaDetail.getValue().getAuthor());
        mImages.setValue(mResPiazzaDetail.getValue().getImages());
        mIndex.setValue("1/" + mImages.getValue().size());
        mAvatar.setValue(mResPiazzaDetail.getValue().getAvatar());
    }

    /**
     * 关闭ImageShowActivity专用退出动画,与退出按钮关联
     */
    public void FinishImageActivity(){
        mIsFinishCurrentImageActivity.setValue(true);
    }
}
