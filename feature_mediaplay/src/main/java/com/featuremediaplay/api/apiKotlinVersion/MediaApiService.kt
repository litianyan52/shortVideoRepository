package com.featuremediaplay.api.apiKotlinVersion

import com.example.video_data.bean.CancelBody
import com.example.video_data.bean.CollectionAddBody
import com.example.video_data.bean.CommentBody
import com.example.video_data.bean.DeleteCmdBody
import com.example.video_data.bean.LikeAddBody
import com.example.video_data.bean.ResAddComment
import com.example.video_data.bean.ResCollection
import com.example.video_data.bean.ResComment
import com.example.video_data.bean.ResLike
import com.example.video_data.bean.ResVideo
import com.example.video_data.bean.ResVideoAllInfo
import com.example.video_data.bean.categoryVideoBean.ResCategoryVideo
import com.example.video_data.bean.searchVideoBean.ResSearch
import com.example.video_data.bean.searchVideoBean.SearchBody
import com.example.video_data.bean.themPlayListBean.ResThemePlayList
import com.featuremediaplay.netAddress.NetAddress
import com.network.bean.ResBase
import com.network.bean.ResList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MediaApiService {

    @GET(NetAddress.RE_ADDRESS)
    //获取推荐页信息
    suspend fun getRecommend(@Query("page") page: Int, @Query("limit") limit: Int): ResBase<ResList<ResVideo>>

    @GET(NetAddress.DAILY_ADDRESS)
    //获取日报页信息
    suspend fun getDaily(@Query("page") page: Int, @Query("limit") limit: Int): ResBase<ResList<ResVideo>>

    @GET(NetAddress.VIDEO_INFO_ADDRESS)
    //请求视频信息
    suspend fun getVideoInfo(@Query("id") id: String, @Header("token") token: String): ResBase<ResVideoAllInfo>

    @POST(NetAddress.VIDEO_LIKE)
    //点赞
    suspend fun Like(@Header("token") token: String, @Body body: LikeAddBody): ResLike

    @POST(NetAddress.VIDEO_CANCEL_LIKE)
    //取消点赞
    suspend fun cancelLike(@Header("token") token: String, @Body body: CancelBody): ResBase<Any>

    @POST(NetAddress.VIDEO_ADD_COLLECTION)
    //添加收藏
    suspend fun addCollection(@Header("token") token:String, @Body body:CollectionAddBody):ResBase<Any>

    @POST(NetAddress.VIDEO_CANCEL_COLLECTION)
    // 取消收藏
    suspend fun cancelCollection(
        @Header("token") token: String,
        @Body body: CancelBody
    ): ResBase<Any>

    @POST(NetAddress.VIDEO_COMMENT)
    // 评论
    suspend fun sendComment(
        @Header("token") token: String,
        @Body body: CommentBody
    ): ResBase<ResAddComment>

    @GET(NetAddress.GET_COMMENT_LIST)
    // 获取评论列表
    suspend fun getCommentList(
        @Header("token") token: String,
        @Query("aid") aid: Int,
        @Query("page") page: Int
    ): ResBase<ResList<ResComment>>

    @POST(NetAddress.DELETE_COMMENT)
    // 删除评论
    suspend fun deleteComment(
        @Header("token") token: String,
        @Body body: DeleteCmdBody
    ): ResBase<Any>

    @GET(NetAddress.GET_CATEGORY_VIDEO_LIST)
    // 获取分类视频列表
    suspend fun getCategoryVideoList(
        @Header("token") token: String,
        @Query("channel_id") channelId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: Int
    ): ResBase<ResList<ResCategoryVideo>>

    @POST(NetAddress.SEARCH_VIDEO)
    // 搜索
    suspend fun getSearchResult(
        @Body body: SearchBody
    ): ResBase<List<ResSearch>>

    @GET(NetAddress.GET_THEME_LIST)
    // 获取主题列表
    suspend fun getThemeListResult(): ResBase<List<ResThemePlayList>>

    @GET(NetAddress.COLLECTION_LIST)
    // 获取收藏列表
    suspend fun getCollectionList(
        @Header("token") token: String,
        @Query("page") page: Int
    ): ResBase<ResCollection>
}