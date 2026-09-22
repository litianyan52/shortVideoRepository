package com.featuremediaplay.ui.mediaplay.kotlinVersion


import com.example.video_data.bean.CancelBody
import com.example.video_data.bean.CollectionAddBody
import com.example.video_data.bean.LikeAddBody
import com.example.video_data.bean.ResLike
import com.example.video_data.bean.ResVideoAllInfo
import com.featuremediaplay.api.apiKotlinVersion.MediaApiServiceProvider
import com.libase.manager.UserManager
import com.network.bean.ResBase
import com.network.config.NetworkStatusResult
import com.network.kotlinVersion.ApiCallKotlinVersion



class MediaPlayModelKotlinVersion {

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
            MediaApiServiceProvider.provider().getVideoInfo(id, UserManager.getInstance().getUserToken())
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
      return  ApiCallKotlinVersion.enqueue ({ MediaApiServiceProvider.provider().Like(UserManager.getInstance().getUserToken(),
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

        return ApiCallKotlinVersion.enqueue({ MediaApiServiceProvider.provider().cancelLike(UserManager.getInstance().getUserToken(),
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

        return ApiCallKotlinVersion.enqueue ({ MediaApiServiceProvider.provider().addCollection(UserManager.getInstance().getUserToken(),CollectionAddBody(type, aid)) })
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
            MediaApiServiceProvider.provider()
                .cancelCollection(UserManager.getInstance().getUserToken(), CancelBody(aid))
        }) { it -> it }

    }
}