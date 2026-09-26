package com.featuremediaplay.ui.mediaplay.kotlinVersion


import com.example.video_data.bean.CancelBody
import com.example.video_data.bean.CollectionAddBody
import com.example.video_data.bean.CommentBody
import com.example.video_data.bean.DeleteCmdBody
import com.example.video_data.bean.LikeAddBody
import com.example.video_data.bean.ResAddComment
import com.example.video_data.bean.ResComment
import com.example.video_data.bean.ResLike
import com.example.video_data.bean.ResVideoAllInfo
import com.featuremediaplay.api.apiKotlinVersion.MediaApiService
import com.featuremediaplay.api.apiKotlinVersion.MediaApiServiceProvider
import com.libase.base.BaseApplication
import com.libase.bean.ResUserData
import com.libase.bean.ResUserInfo
import com.libase.db.UserLookRecord
import com.libase.db.UserLookRecordOperation
import com.libase.manager.UserManager
import com.network.bean.ResBase
import com.network.bean.ResList
import com.network.config.NetworkStatusResult
import com.network.kotlinVersion.ApiCallKotlinVersion



class MediaPlayModelKotlinVersion {

    var  mPage : Int = 1//获取评论列表中要用到的页数

    /**
     * 判断是否登录
     *
     * @return
     */
    fun  isLogin() : Boolean {
        return UserManager.getInstance().isLogin()
    }

    /**
     * 请求视频数据
     */
    suspend fun RequestVideoInfo(id: String): NetworkStatusResult<ResVideoAllInfo> {
        return ApiCallKotlinVersion.enqueue ({
            MediaApiServiceProvider.getInstance().getVideoInfo(id, UserManager.getInstance().getUserToken())
        }) { it -> it.data }
    }



    /**
     * 进行点赞
     *
     * @param id              要点赞的东西的id
     * @param type            事件类型
     * @param requestCallBack
     */
    suspend fun requestLike(id: String,  type :String) : NetworkStatusResult<ResLike> {
      return  ApiCallKotlinVersion.enqueue ({ MediaApiServiceProvider.getInstance().Like(UserManager.getInstance().getUserToken(),
            LikeAddBody(id, type)) }, successCode = 1001)
        {it -> it}
    }


    /**
     * 取消点赞
     *
     * @param aid             要取消点赞的video
     * @param requestCallBack
     */
    suspend fun cancelLike( aid :String): NetworkStatusResult<ResBase<Any>> {

        return ApiCallKotlinVersion.enqueue({ MediaApiServiceProvider.getInstance().cancelLike(UserManager.getInstance().getUserToken(),
            CancelBody(aid)) }) {it -> it}

    }


    /**
     * 发起添加收藏请求
     *
     * @param type            事件类型
     * @param aid             要收藏的视频id
     * @param requestCallBack
     */
    suspend fun addCollection( type:String,  aid:String): NetworkStatusResult<ResBase<Any>> {

        return ApiCallKotlinVersion.enqueue ({ MediaApiServiceProvider.getInstance().addCollection(UserManager.getInstance().getUserToken(),CollectionAddBody(type, aid)) })
        {it -> it}
    }


    /**
     * 发起取消收藏
     *
     * @param aid             要取消收藏的视频id
     * @param requestCallBack
     */
    suspend fun cancelCollection( aid:String): NetworkStatusResult<ResBase<Any>> {

       return ApiCallKotlinVersion.enqueue({
            MediaApiServiceProvider.getInstance()
                .cancelCollection(UserManager.getInstance().getUserToken(), CancelBody(aid))
        }) { it -> it }

    }



    /**
     * 发送评论
     *
     * @param content         评论内容
     * @param aid             评论资讯的aid
     * @param requestCallBack
     */
    suspend fun sendComment( content:String,  aid:String) : NetworkStatusResult<ResBase<ResAddComment>> {

        return ApiCallKotlinVersion.enqueue ({ MediaApiServiceProvider
            .getInstance()
            .sendComment(UserManager.getInstance().getUserToken(),  CommentBody(content, aid))}){ it -> it }

    }




    /**
     * 获取评论列表
     *
     * @param aid             视频id
     * @param requestCallBack
     */
    suspend fun getCommentList(isFirst: Boolean, aid :Int ): NetworkStatusResult<ResBase<ResList<ResComment>>>{
        if (isFirst){
            mPage = 1
        }
        else{
            mPage++
        }

         return ApiCallKotlinVersion.enqueue({ MediaApiServiceProvider.getInstance().getCommentList(UserManager.getInstance().getUserToken(),aid,mPage) }) {
            it -> it
        }
    }


    /**
     * 删除评论
     * @param id 要删除的评论的id
     * @param requestCallBack
     */

    suspend fun deleteComment( id :Int) : NetworkStatusResult<ResBase<Any>>{
       return ApiCallKotlinVersion.enqueue({MediaApiServiceProvider.getInstance().deleteComment(UserManager.getInstance().getUserToken(),DeleteCmdBody(id.toString()))}){
            it -> it
        }

    }


    /**
     * 插入浏览记录
     * @param video_id
     * @param cover
     * @param label
     * @param title
     * @param duration
     */
     fun InsertData(video_id: Int,  cover:String,  label:String,  title:String,  duration:String)
    {
         var user_id:String
        val operation :UserLookRecordOperation =  UserLookRecordOperation(BaseApplication.getContext());
        if (isLogin())
        {
            val userInfo : ResUserData<ResUserInfo> = UserManager.getInstance().getUserInfo()
            user_id =  userInfo.getUser().getId()

        }
        else
        {
            user_id = "0" //没登录id设置为:"0"
        }

        val view_time :Long = System.currentTimeMillis() //获取当前时间戳
        val record :UserLookRecord = operation.getUserLookRecord(user_id, video_id, cover, label, title, duration, view_time)
        operation.Insert(record);
    }
}