package com.featuremediaplay.ui.mediaplay;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.video_data.bean.ArchivesInfo;
import com.example.video_data.bean.ResAddComment;
import com.example.video_data.bean.ResComment;
import com.example.video_data.bean.ResLike;
import com.example.video_data.bean.ResVideoAllInfo;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.bean.ResBase;
import com.network.bean.ResList;
import com.network.config.NetworkStatusConfig;

import java.util.ArrayList;
import java.util.List;

public class MediaPlayViewModel extends BaseViewmodel {
    private static final String TAG = "MediaPlayViewModel";
    public MediaPlayViewModel() {
        mModel = new MediaPlayModel();
    }
    private final MediaPlayModel mModel;
    private MutableLiveData<ResVideoAllInfo> mResVideoAllInfo = new MutableLiveData<>();

    protected MutableLiveData<Integer> mIsLike = new MutableLiveData<>(0); //用户是否点赞
    protected MutableLiveData<Integer> mIsCollection = new MutableLiveData<>(0); //用户是否收藏
    protected MutableLiveData<Integer> mIsComment = new MutableLiveData<>(0); //用户是否评论
    protected MutableLiveData<Integer> mIsShared = new MutableLiveData<>(0); //用户是否分享

    protected MutableLiveData<String> mLikes = new MutableLiveData<>();  //点赞数
    protected MutableLiveData<String> mCollections = new MutableLiveData<>();  //收藏数
    protected MutableLiveData<String> mComments = new MutableLiveData<>();  //评论数
    protected MutableLiveData<String> mAuthorName = new MutableLiveData<>();  //作者名字
    protected MutableLiveData<String> mAuthorAvatar = new MutableLiveData<>();  //作者头像


    private MutableLiveData<ArchivesInfo> mArchivesInfo = new MutableLiveData<>(); //视频数据
    private MutableLiveData<String> mChannel = new MutableLiveData<>();//渠道,表示这个视频来源
    private MutableLiveData<String> mDescription = new MutableLiveData<>();  //视频描述
    private MutableLiveData<String> mTitle = new MutableLiveData<>();  //视频标题

    private MutableLiveData<String> mShares = new MutableLiveData<>();  //分享数

    private MutableLiveData<List<ResComment>> mCommentList = new MutableLiveData<>(null);  //评论列表
    //是否允许继续加载评论列表,设置为true确保第一次能够加载
    private MutableLiveData<Boolean> isEnableLoadMore = new MutableLiveData<>(true);  //默认允许下拉加载

    private MutableLiveData<String> mToastForLikeCollect = new MutableLiveData<>();  //用于触发点赞收藏的弹窗


    public MutableLiveData<ArchivesInfo> getArchivesInfo() {
        return mArchivesInfo;
    }

    public MutableLiveData<String> getChannel() {
        return mChannel;
    }

    public MutableLiveData<String> getDescription() {
        return mDescription;
    }

    public MutableLiveData<String> getTitle() {
        return mTitle;
    }

    public MutableLiveData<String> getLikes() {
        return mLikes;
    }

    public MutableLiveData<String> getCollections() {
        return mCollections;
    }

    public MutableLiveData<String> getComments() {
        return mComments;
    }

    public MutableLiveData<String> getShares() {
        return mShares;
    }


    public MutableLiveData<Integer> getIsLike() {
        return mIsLike;
    }

    public MutableLiveData<Integer> getIsCollection() {
        return mIsCollection;
    }

    public MutableLiveData<Integer> getIsComment() {
        return mIsComment;
    }

    public MutableLiveData<Integer> getIsShared() {
        return mIsShared;
    }

    public MutableLiveData<List<ResComment>> getCommentsList() {
        return mCommentList;
    }

    public MutableLiveData<ResVideoAllInfo> getResVideoAllInfo() {
        return mResVideoAllInfo;
    }

    public MutableLiveData<Boolean> getIsEnableLoadMore() {
        return isEnableLoadMore;
    }

    public MutableLiveData<String> getAuthorName() {
        return mAuthorName;
    }

    public MutableLiveData<String> getAuthorAvatar() {
        return mAuthorAvatar;
    }

    public MutableLiveData<String> getToastForLikeCollect() {
        return mToastForLikeCollect;
    }

    /**
     * 专门用于显示点赞收藏的弹窗触发,解决多次弹窗的问题
     * @param text
     */
    public void showLikeCollectToast(String text){
        mToastForLikeCollect.setValue(text);
    }

    /**
     * 获取视频相关的数据
     *
     * @param id 视频id
     */
    public void RequestVideoInfo(String id) {
        showLoading(true);
        mModel.RequestVideoInfo(id, new IRequestCallBack<ResBase<ResVideoAllInfo>>() {
            @Override
            public void RequestSuccess(ResBase<ResVideoAllInfo> result) {
                showLoading(false);
                mResVideoAllInfo.setValue(result.getData());
                LoadData(mResVideoAllInfo.getValue().getArchivesInfo());
                InsertBrowseRecord(mArchivesInfo.getValue().getId()
                        ,mArchivesInfo.getValue().getImage(),
                        mArchivesInfo.getValue().getChannel().getName(),mArchivesInfo.getValue().getTitle(),
                        mArchivesInfo.getValue().getDuration()
                        );
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showLoading(false);
                showToastText(errorMsg);
            }
        });
    }


    /**
     * 设置数据
     *
     * @param resVideoAllInfo 视频相关的数据
     */
    public void LoadData(ArchivesInfo resVideoAllInfo) {
        mArchivesInfo.setValue(resVideoAllInfo);
        int id = mArchivesInfo.getValue().getId();
        mChannel.setValue("#" + resVideoAllInfo.getChannel().getName()); //设置视频来源
        mDescription.setValue(resVideoAllInfo.getDescription());  //设置视频描述
        mTitle.setValue(resVideoAllInfo.getTitle()); //设置视频标题
        mLikes.setValue(String.valueOf(resVideoAllInfo.getLikes()));  //设置视频点赞数
        mCollections.setValue(String.valueOf(resVideoAllInfo.getCollection()));//设置视频收藏数
        mComments.setValue(String.valueOf(resVideoAllInfo.getComments()));//设置视频评论数
        mShares.setValue(String.valueOf(resVideoAllInfo.getViews()));//设置视频浏览数
        mIsLike.setValue(resVideoAllInfo.getIslike());//设置当前用户是否点赞
        mIsCollection.setValue(resVideoAllInfo.getIscollection());//设置当前用户是否收藏
        mIsComment.setValue(resVideoAllInfo.getIscollection());//设置当前用户是否评论
        mIsShared.setValue(resVideoAllInfo.getIsguest());//设置当前用户是否浏览
        mAuthorName.setValue(mArchivesInfo.getValue().getUser().getNickname());  //设置视频作者名字
        mAuthorAvatar.setValue(mArchivesInfo.getValue().getUser().getAvatar());//设置作者头像

    }

    /**
     * 点赞关联方法
     */
    public void onLikeClick() {
        if (!mModel.isLogin())  //判断是否登录
        {
            showLikeCollectToast("请先登录");
            //showToastText("请先登录");
            return;
        }
        if (mIsLike.getValue() == 1)  //说明已经点赞,发起取消点赞
        {
            mModel.cancelLike(String.valueOf(mArchivesInfo.getValue().getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                   // showToastText(result.getMsg());
                    showLikeCollectToast(result.getMsg());
                    Log.d("TAG", "RequestSuccess: " + result.getMsg());
                    mIsLike.setValue(0); //取消点赞成功,同步一下本地数据
                    int value = Integer.valueOf(mLikes.getValue());
                    mLikes.setValue(String.valueOf(--value));
                    mArchivesInfo.getValue().setLikes(--value);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                  //  showToastText(errorMsg);
                    showLikeCollectToast(errorMsg);
                }
            });
        } else if (mIsLike.getValue() == 0) {
            mModel.requestLike(String.valueOf(mArchivesInfo.getValue().getId()), "like", new IRequestCallBack<ResLike>() {
                @Override
                public void RequestSuccess(ResLike result) {
                    showLikeCollectToast(result.getMsg());
                   // showToastText(result.getMsg());
                    mIsLike.setValue(1); //点赞成功,同步一下本地数据
                    int value = Integer.valueOf(mLikes.getValue());
                    mLikes.setValue(String.valueOf(++value));
                    mArchivesInfo.getValue().setLikes(++value);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    //showToastText(errorMsg);
                    showLikeCollectToast(errorMsg);
                }
            });
        }
    }


    /**
     * 收藏关联方法
     */
    public void onClickCollect() {
        if (!mModel.isLogin()) {
            //showToastText("请先登录");
            showLikeCollectToast("请先登录");
            return;
        }

        if (mIsCollection.getValue() == 1)  //说明已经收藏发起取消收藏
        {
            mModel.cancelCollection(String.valueOf(mArchivesInfo.getValue().getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    //showToastText(result.getMsg());
                    showLikeCollectToast(result.getMsg());
                    mIsCollection.setValue(0);  //取消收藏成功
                    Integer value = Integer.valueOf(mCollections.getValue());
                    mCollections.setValue(String.valueOf(--value));
                    mArchivesInfo.getValue().setCollection(--value);


                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                   // showToastText(errorMsg);
                    showLikeCollectToast(errorMsg);
                }
            });
        } else if (mIsCollection.getValue() == 0) {   //未收藏,要发起收藏
            mModel.addCollection("archives", String.valueOf(mArchivesInfo.getValue().getId()), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    showLikeCollectToast(result.getMsg());
                   // showToastText(result.getMsg());
                    mIsCollection.setValue(1);  //收藏成功
                    Integer value = Integer.valueOf(mCollections.getValue());
                    mCollections.setValue(String.valueOf(++value));
                    mArchivesInfo.getValue().setCollection(++value);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    //showToastText(errorMsg);
                    showLikeCollectToast(errorMsg);
                }
            });
        }

    }

    /**
     * 发送评论
     * @param comment 要发送的评论内容
     */
    public void sendComment(String comment) {
        if (!mModel.isLogin()) {
            showToastText("请先登录");
            return;
        }
        mModel.sendComment(comment, String.valueOf(mArchivesInfo.getValue().getId()), new IRequestCallBack<ResBase<ResAddComment>>() {
            @Override
            public void RequestSuccess(ResBase<ResAddComment> result) {
                showToastText(result.getMsg());
                Integer value = Integer.valueOf(mComments.getValue()); //评论条数增加
                value++;
                mComments.setValue(String.valueOf(value));  //把加号的正确数据设置回去
                List<ResComment> listValue = mCommentList.getValue();
                if (mCommentList.getValue() != null) {
                    //在CommentFragment新增的评论插到列表头部,在简介页因为还没请求评论列表所以无法往里加
                    mCommentList.getValue().add(0, result.getData().getComment());
                    mCommentList.setValue(mCommentList.getValue());  //触发一下观察者
                }

            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
            }
        });
    }


    /**
     * 获取评论列表
     */
    public void getCommentList(boolean isFirst) {
        int id = mArchivesInfo.getValue().getId();
        mModel.getCommentList(isFirst, mArchivesInfo.getValue().getId(), new IRequestCallBack<ResList<ResComment>>() {
            @Override
            public void RequestSuccess(ResList<ResComment> result) {
                Log.d(TAG, "RequestSuccess: " + result.getList().size());
                if (!isEnableLoadMore.getValue()) {
                    return;
                }
                if (result.getList().size() < 10)   //返回的数据条数不足10,说明后面没有更多的数据了
                {
                    isEnableLoadMore.setValue(false);
                }
                if (isFirst) {
                    mCommentList.setValue(result.getList());
                } else {
                    mCommentList.getValue().addAll(result.getList());
                    mCommentList.setValue(mCommentList.getValue());
                }
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
                Log.d(TAG, "RequestSuccess: " );
                if (errorCodeValue == NetworkStatusConfig.ERROR_STATUS_EMPTY && errorMsg.isEmpty()) {
                    isEnableLoadMore.setValue(false);  //本来就没有评论数据的情况,往里设置一个空列表,防止值为null引发空指针
                    mCommentList.setValue(new ArrayList<ResComment>());
                }
            }
        });
    }


    /**
     * 删除评论
     *
     * @param resComment
     */
    public void deleteComment(ResComment resComment) {
        if (!mModel.isLogin()) {
            showToastText("请先登录");
            return;
        }
        String nickname = UserManager.getInstance().getUserInfo().getUser().getNickname();
        if (!(resComment.getUser().getNickname().equals(UserManager.getInstance().getUserInfo().getUser().getNickname()))) {
            return;
        }
        mCommentList.getValue().remove(resComment);
        mCommentList.setValue(mCommentList.getValue());
        mModel.deleteComment(resComment.getId(), new IRequestCallBack<ResBase<Object>>() {
            @Override
            public void RequestSuccess(ResBase<Object> result) {
                showToastText(result.getMsg());
                Integer value = Integer.valueOf(mComments.getValue()); //评论条数增加
                value--;
                mComments.setValue(String.valueOf(value));  //把加号的正确数据设置回去
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showToastText(errorMsg);
            }
        });
    }




    public void InsertBrowseRecord(int video_id,String cover,String label,String title,String duration)
    {
        mModel.InsertData(video_id,cover,label,title,duration);
    }
}
