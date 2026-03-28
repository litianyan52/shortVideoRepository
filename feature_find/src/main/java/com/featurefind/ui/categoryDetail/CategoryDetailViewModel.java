package com.featurefind.ui.categoryDetail;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.Category;
import com.featurefind.bean.ResCategoryDataOne;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.network.bean.ResBase;

public class CategoryDetailViewModel extends BaseViewmodel {

    private final CategoryDetailModel mModel;
    private MutableLiveData<String> mCover = new MutableLiveData<>();
    private MutableLiveData<String> mInteractionData = new MutableLiveData<>();
    private MutableLiveData<ResCategoryDataOne.InfoBean> mCategoryDetailOne = new MutableLiveData<>();

    public CategoryDetailViewModel() {
        mModel = new CategoryDetailModel();
    }

    private MutableLiveData<Category> mCategory = new MutableLiveData<>();
    private MutableLiveData<String> mCategoryName = new MutableLiveData<>();
    private MutableLiveData<String> mCategoryDescription = new MutableLiveData<>();


    public MutableLiveData<Category> getCategory() {
        return mCategory;
    }

    public MutableLiveData<String> getCategoryName() {
        return mCategoryName;
    }

    public MutableLiveData<String> getCategoryDescription() {
        return mCategoryDescription;
    }

    public MutableLiveData<String> getCover() {
        return mCover;
    }

    public MutableLiveData<String> getInteractionData() {
        return mInteractionData;
    }

    /**
     * 载入从发现页传过来的数据
     * @param category
     */
    public void LoadData(Category category) {
        if (category == null) {
            return;
        }
        mCategory.setValue(category);
        mCategoryName.setValue(mCategory.getValue().getName());
        mCategoryDescription.setValue(mCategory.getValue().getDescription());
    }


    /**
     * 请求分类详情页第一部分数据
     * @param channel_id
     */
    public void getCategoryDetailOne(String channel_id) {
        mModel.getCategoryDetailOne(channel_id, new IRequestCallBack<ResBase<ResCategoryDataOne>>() {
            @Override
            public void RequestSuccess(ResBase<ResCategoryDataOne> result) {
                mCategoryDetailOne.setValue(result.getData().getInfo());
                mCover.setValue(result.getData().getInfo().getImage());
                mInteractionData.setValue(mCategoryDetailOne.getValue().getPeople() + "万人参与 "
                        + mCategoryDetailOne.getValue().getBrowse()+ "万人浏览");
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {

            }
        });
    }
}
