package com.featuremediaplay.ui.categoryVideoList;

import com.example.video_data.bean.CancelBody;
import com.example.video_data.bean.CollectionAddBody;
import com.example.video_data.bean.CommentBody;
import com.example.video_data.bean.DeleteCmdBody;
import com.example.video_data.bean.LikeAddBody;
import com.example.video_data.bean.ResAddComment;
import com.example.video_data.bean.ResComment;
import com.example.video_data.bean.ResLike;
import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo;
import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.libase.base.IRequestCallBack;
import com.libase.base.list.BaseListModel;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.bean.ResList;
import com.network.config.NetworkStatusConfig;

import retrofit2.Call;

public class CategoryVideoListModel extends BaseListModel {
    private int mChannel_id;  //分类id
    private int mType;   //类型
    private int mCommentPage; //评论列表的请求页数

    public void setmChannel_id(int mChannel_id) {
        this.mChannel_id = mChannel_id;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    /**
     * 发起请求分类视频的请求
     *
     * @param isFirst
     */
    @Override
    public void requestDatas(boolean isFirst) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        if (isFirst) {
            mPage = 1;  //第一次加载重置页数为1,否则加载下一页
        } else {
            mPage++;
        }
        Call<ResBase<ResList<ResCategoryVideo>>> call = apiService.getCategoryVideoList(UserManager.getInstance().getUserToken(),
                mChannel_id, mPage, mLimit, mType);
        ApiCall.enqueueList(call, new ApiCall.ApiListCallback<ResCategoryVideo>() {
            @Override
            public void GetLiseSuccess(ResList<ResCategoryVideo> result) {
                iListListener.GetSuccess(result, isFirst);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                iListListener.GetFailed(errorCode);
            }
        });
    }



    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    /**
     * 进行点赞
     *
     * @param id              要点赞的东西的id
     * @param type            事件类型
     * @param requestCallBack
     */
    public void requestLike(String id, String type, IRequestCallBack<ResLike> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResLike> call = apiService.Like(UserManager.getInstance().getUserToken(), new LikeAddBody(id, type));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void GetLiseSuccess(ResLike result) {
                if (result.getCode() == 1001 && result.getMsg() != null) //判断服务器是否正常
                {
                    requestCallBack.RequestSuccess(result);
                } else {
                    requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, result.getMsg());
                }
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }

    /**
     * 取消点赞
     *
     * @param aid             要取消点赞的video
     * @param requestCallBack
     */
    public void cancelLike(String aid, IRequestCallBack<ResBase<Object>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.cancelLike(UserManager.getInstance().getUserToken(), new CancelBody(aid));
//        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Object>>() {
//            @Override
//            public void GetLiseSuccess(ResBase<Object> result) {
//                requestCallBack.RequestSuccess(result);
//            }
//
//            @Override
//            public void GetLiseFailed(int errorCode, String message) {
//                requestCallBack.RequestFailed(errorCode, message);
//            }
//        });
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                if (result.getCode() == 1 && result.getMsg() != null) //判断服务器是否正常
                {
                    requestCallBack.RequestSuccess(result);
                } else {
                    requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, result.getMsg());
                }
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }

    /**
     * 发起添加收藏请求
     *
     * @param type            事件类型
     * @param aid             要收藏的视频id
     * @param requestCallBack
     */
    public void addCollection(String type, String aid, IRequestCallBack<ResBase<Object>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.
                addCollection(UserManager.getInstance().getUserToken(),
                        new CollectionAddBody(type, aid));
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }

    /**
     * 发起取消收藏
     *
     * @param aid             要取消收藏的视频id
     * @param requestCallBack
     */
    public void cancelCollection(String aid, IRequestCallBack<ResBase<Object>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.cancelCollection(UserManager.getInstance().getUserToken(),
                new CancelBody(aid));
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    /**
     * 发送评论
     *
     * @param content         评论内容
     * @param aid             评论资讯的aid
     * @param requestCallBack
     */
    public void sendComment(String content, String aid, IRequestCallBack<ResBase<ResAddComment>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<ResAddComment>> Call = apiService.sendComment(UserManager.getInstance().getUserToken(), new CommentBody(content, aid));
        ApiCall.enqueue(Call, new ApiCall.ApiCallback<ResBase<ResAddComment>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResAddComment> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    /**
     * 获取评论列表
     *
     * @param aid             视频id
     * @param requestCallBack
     */
    public void getCommentList(boolean isFirst, int aid, IRequestCallBack<ResList<ResComment>> requestCallBack) {
        if (isFirst) {
            mCommentPage = 1;   //重置页数
        } else {
            mCommentPage++;     //页数加一
        }
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<ResList<ResComment>>> call = apiService.getCommentList(UserManager.getInstance().getUserToken(),
                aid, mCommentPage);
        ApiCall.enqueueList(call, new ApiCall.ApiListCallback<ResComment>() {
            @Override
            public void GetLiseSuccess(ResList<ResComment> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    /**
     * 删除评论
     * @param id 要删除的评论的id
     * @param requestCallBack
     */

    public void deleteComment(int id, IRequestCallBack<ResBase<Object>> requestCallBack) {
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.deleteComment(UserManager.getInstance().getUserToken(), new DeleteCmdBody(String.valueOf(id)));
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }
}
