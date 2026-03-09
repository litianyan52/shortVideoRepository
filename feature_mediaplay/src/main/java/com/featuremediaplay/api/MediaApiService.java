package com.featuremediaplay.api;

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
import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo;
import com.example.video_data.bean.searchVideoBean.ResSearch;
import com.example.video_data.bean.searchVideoBean.SearchBody;
import com.featuremediaplay.netAddress.NetAddress;
import com.network.bean.ResBase;
import com.network.bean.ResList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MediaApiService {
    @GET(NetAddress.RE_ADDRESS)
        //获取推荐页信息
    Call<ResBase<ResList<ResVideo>>> getRecommend(@Query("page") int page, @Query("limit") int limit);

    @GET(NetAddress.DAILY_ADDRESS)
        //获取日报页信息
    Call<ResBase<ResList<ResVideo>>> getDaily(@Query("page") int page, @Query("limit") int limit);

    @GET(NetAddress.VIDEO_INFO_ADDRESS)
        //请求视频信息
    Call<ResBase<ResVideoAllInfo>> getVideoInfo(@Query("id") String id, @Header("token") String token);

    @POST(NetAddress.VIDEO_LIKE)
        //点赞
    Call<ResLike> Like(@Header("token") String token, @Body LikeAddBody body);

    @POST(NetAddress.VIDEO_CANCEL_LIKE)
        //取消点赞
    Call<ResBase<Object>> cancelLike(@Header("token") String token, @Body CancelBody body);

    @POST(NetAddress.VIDEO_ADD_COLLECTION)
        //添加收藏
    Call<ResBase<Object>> addCollection(@Header("token") String token, @Body CollectionAddBody body);

    @POST(NetAddress.VIDEO_CANCEL_COLLECTION)
        //取消收藏
    Call<ResBase<Object>> cancelCollection(@Header("token") String token, @Body CancelBody body);

    @POST(NetAddress.VIDEO_COMMENT)
        //评论
    Call<ResBase<ResAddComment>> sendComment(@Header("token") String token, @Body CommentBody body);

    @GET(NetAddress.GET_COMMENT_LIST)
        //获取评论列表
    Call<ResBase<ResList<ResComment>>> getCommentList(@Header("token") String token, @Query("aid") int aid, @Query("page") int page);

    @POST(NetAddress.DELETE_COMMENT)
        //删除评论
    Call<ResBase<Object>> deleteComment(@Header("token") String token, @Body DeleteCmdBody body);

    @GET(NetAddress.GET_CATEGORY_VIDEO_LIST)
        //获取评论列表
    Call<ResBase<ResList<ResCategoryVideo>>> getCategoryVideoList(@Header("token") String token, @Query("channel_id") int channel_id, @Query("page") int page, @Query("limit") int limit, @Query("type") int type);

    @POST(NetAddress.SEARCH_VIDEO)
        //搜索
    Call<ResBase<List<ResSearch>>> getSearchResult(@Body SearchBody body);


}

