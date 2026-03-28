package com.featuremediaplay.ui.mediaplay;

import com.featuremediaplay.api.MediaApiService;
import com.featuremediaplay.api.MediaApiServiceProvider;
import com.example.video_data.bean.CancelBody;
import com.example.video_data.bean.CollectionAddBody;
import com.example.video_data.bean.CommentBody;
import com.example.video_data.bean.DeleteCmdBody;
import com.example.video_data.bean.LikeAddBody;
import com.example.video_data.bean.ResAddComment;
import com.example.video_data.bean.ResComment;
import com.example.video_data.bean.ResLike;
import com.example.video_data.bean.ResVideo;
import com.example.video_data.bean.ResVideoAllInfo;
import com.libase.base.BaseApplication;
import com.libase.base.IRequestCallBack;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.db.UserLookRecord;
import com.libase.db.UserLookRecordOperation;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.bean.ResList;
import com.network.config.NetworkStatusConfig;

import retrofit2.Call;

public class MediaPlayModel {

    private int mPage; //获取评论列表中要用到的页数

    /**
     * 发起获取视频相关信息的请求
     *
     * @param id 视频id
     * @param requestCallBack
     */
    public void RequestVideoInfo(String id, IRequestCallBack<ResBase<ResVideoAllInfo>> requestCallBack) {
        MediaApiService service = MediaApiServiceProvider.provider();
        Call<ResBase<ResVideoAllInfo>> call = service.getVideoInfo(id, UserManager.getInstance().getUserToken());
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResVideoAllInfo>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResVideoAllInfo> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
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
            mPage = 1;   //重置页数
        } else {
            mPage++;     //页数加一
        }
        MediaApiService apiService = MediaApiServiceProvider.provider();
        Call<ResBase<ResList<ResComment>>> call = apiService.getCommentList(UserManager.getInstance().getUserToken(),
                aid, mPage);
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


    /**
     * 插入浏览记录
     * @param video_id
     * @param cover
     * @param label
     * @param title
     * @param duration
     */
    public void InsertData(int video_id,String cover,String label,String title,String duration)
    {
        String user_id;
        UserLookRecordOperation operation = new UserLookRecordOperation(BaseApplication.getContext());
        if (isLogin())
        {
            ResUserData<ResUserInfo> userInfo = UserManager.getInstance().getUserInfo();
            user_id =  userInfo.getUser().getId();

        }
        else
        {
            user_id = "0"; //没登录id设置为:"0"
        }

        long view_time = System.currentTimeMillis();  //获取当前时间戳
        UserLookRecord record = operation.getUserLookRecord(user_id, video_id, cover, label, title, duration, view_time);
        operation.Insert(record);
    }
}
