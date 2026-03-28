package com.featuremediaplay.ui.categoryVideoList;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.ResLike;
import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo;
import com.libase.base.IRequestCallBack;
import com.libase.base.list.BaseListViewmodel;
import com.network.bean.ResBase;

public class CategoryVideoListViewModel extends BaseListViewmodel<ResCategoryVideo, CategoryVideoListModel> {
//    private MutableLiveData<Integer> mChannel_id = new MutableLiveData<>();
//    private MutableLiveData<Integer> mType = new MutableLiveData<>();

    public CategoryVideoListViewModel() {
        super(new CategoryVideoListModel());  //传给BaseListViewModel,因为Model的实现不是同一的,请求的时候一定要有一个具体的Model
    }


    //    private MutableLiveData<ResCategoryVideo> mResCategoryVideo = new MutableLiveData<>();
//
    private MutableLiveData<Boolean> mIsInteract = new MutableLiveData<>(); //用户是否触发点赞,收藏,评论
//    protected MutableLiveData<Integer> mIsCollection = new MutableLiveData<>(0); //用户是否收藏
//    protected MutableLiveData<Integer> mIsComment = new MutableLiveData<>(0); //用户是否评论
//    protected MutableLiveData<Integer> mIsShared = new MutableLiveData<>(0); //用户是否分享
//
//    protected MutableLiveData<String> mLikes = new MutableLiveData<>();  //点赞数
//    protected MutableLiveData<String> mCollections = new MutableLiveData<>();  //收藏数
//    protected MutableLiveData<String> mComments = new MutableLiveData<>();  //评论数


    public MutableLiveData<Boolean> getIsInteract() {
        return mIsInteract;
    }

    /**
     * 请求视频数据所需的值,需在发起视频请求之前完成这个操作
     *
     * @param channel_id
     * @param type
     */
    public void LoadData(int channel_id, int type) {
        mModel.setmChannel_id(channel_id);
        mModel.setmType(type);
    }

    /**
     * 点赞关联方法
     */
    public void onLikeClick(ResCategoryVideo resCategoryVideo) {
        if (!mModel.isLogin())  //判断是否登录
        {
            showToastText("请先登录");
            return;
        }
        if (resCategoryVideo.getIslike() == 1)  //说明已经点赞,发起取消点赞
        {
            mModel.cancelLike(String.valueOf(resCategoryVideo.getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    showToastText(result.getMsg());
                    Log.d("TAG", "RequestSuccess: " + result.getMsg());
                    resCategoryVideo.setIslike(0);//取消点赞成功,同步一下本地数据
                    String likes = resCategoryVideo.getLikes();
                    Integer value = Integer.valueOf(likes);
                    resCategoryVideo.setLikes(String.valueOf(--value));  //更新一下点赞数
                    mIsInteract.setValue(true);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showToastText(errorMsg);
                }
            });
        } else if (resCategoryVideo.getIslike() == 0) {
            mModel.requestLike(String.valueOf(resCategoryVideo.getId()), "like", new IRequestCallBack<ResLike>() {
                @Override
                public void RequestSuccess(ResLike result) {
                    showToastText(result.getMsg());
                    resCategoryVideo.setIslike(1); //点赞成功,同步一下本地数据
                    int value = Integer.valueOf(resCategoryVideo.getLikes());
                    resCategoryVideo.setLikes(String.valueOf(++value));  //更新一下点赞数
                    mIsInteract.setValue(true);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showToastText(errorMsg);
                }
            });
        }
    }


    /**
     * 收藏关联方法
     */
    public void onClickCollect(ResCategoryVideo resCategoryVideo) {
        if (!mModel.isLogin()) {
            showToastText("请先登录");
            return;
        }

        if (resCategoryVideo.getIscollection() == 1)  //说明已经收藏发起取消收藏
        {
            mModel.cancelCollection(String.valueOf(resCategoryVideo.getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    showToastText(result.getMsg());
                    resCategoryVideo.setIscollection(0);  //取消收藏成功
                    Integer value = Integer.valueOf(resCategoryVideo.getCollection());
                    resCategoryVideo.setCollection(String.valueOf(--value));//更新收藏数
                    mIsInteract.setValue(true);  //表明用户进行了互动要进行数据更新
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showToastText(errorMsg);
                }
            });
        } else if (resCategoryVideo.getIscollection() == 0) {   //未收藏,要发起收藏
            mModel.addCollection("archives", String.valueOf(resCategoryVideo.getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    showToastText(result.getMsg());
                    resCategoryVideo.setIscollection(1);  //收藏成功
                    Integer value = Integer.valueOf(resCategoryVideo.getCollection());
                    resCategoryVideo.setCollection(String.valueOf(++value));  //更新收藏数
                    mIsInteract.setValue(true); //表明用户进行了互动要进行数据更新
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showToastText(errorMsg);
                }
            });
        }

    }

}
