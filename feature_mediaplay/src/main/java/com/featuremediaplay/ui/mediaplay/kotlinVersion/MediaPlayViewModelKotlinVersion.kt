package com.featuremediaplay.ui.mediaplay.kotlinVersion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.video_data.bean.ArchivesInfo
import com.example.video_data.bean.ResComment
import com.example.video_data.bean.ResLike
import com.example.video_data.bean.ResVideoAllInfo

import com.libase.base.BaseViewmodel
import com.libase.manager.UserManager
import com.network.bean.ResBase
import com.network.config.NetworkStatusResult
import kotlinx.coroutines.launch


class MediaPlayViewModelKotlinVersion : BaseViewmodel() {

    companion object {
        private const val TAG: String = "MediaPlayViewModel";
    }


    private var mModel: MediaPlayModelKotlinVersion = MediaPlayModelKotlinVersion()

    private val _mResVideoAllInfo = MutableLiveData<ResVideoAllInfo>()
    val mResVideoAllInfo: LiveData<ResVideoAllInfo> get() = _mResVideoAllInfo


    protected val mIsLike = MutableLiveData<Int>(0) //用户是否点赞
    protected val mIsCollection = MutableLiveData<Int>(0) //用户是否收藏
    protected val mIsComment = MutableLiveData<Int>(0) //用户是否评论
    protected val mIsShared = MutableLiveData<Int>(0) //用户是否分享

    protected val mLikes = MutableLiveData<String>();  //点赞数
    protected val mCollections = MutableLiveData<String>();  //收藏数
    protected val mComments = MutableLiveData<String>();  //评论数
    protected val mAuthorName = MutableLiveData<String>();  //作者名字
    protected val mAuthorAvatar = MutableLiveData<String>();  //作者头像


    private val mArchivesInfo = MutableLiveData<ArchivesInfo>(); //视频数据
    private val mChannel = MutableLiveData<String>();//渠道,表示这个视频来源
    private val mDescription = MutableLiveData<String>();  //视频描述
    private val mTitle = MutableLiveData<String>();  //视频标题

    private val mShares = MutableLiveData<String>();  //分享数

    private val mCommentList = MutableLiveData<List<ResComment>>(null);  //评论列表

    //是否允许继续加载评论列表,设置为true确保第一次能够加载
    private val isEnableLoadMore = MutableLiveData<Boolean>(true);  //默认允许下拉加载

    private val mToastForLikeCollect = MutableLiveData<String>()  //用于触发点赞收藏的弹窗


    private val ERRORLIKES: Int = -100

    private val ERRORCOLLECTIONS: Int = -100

    fun RequestVideoInfo(id: String) {
        viewModelScope.launch {
            showLoading(true)
            val result = mModel.RequestVideoInfo(id)
            when (result) {
                is NetworkStatusResult.Empty -> showToastText(result.msg ?: "没有数据了")
                is NetworkStatusResult.Error -> showToastText(result.errorMessage ?: "网络异常")
                is NetworkStatusResult.Success<ResVideoAllInfo> -> _mResVideoAllInfo.value =
                    result.data
            }

        }
    }

    /**
     * 设置数据
     *
     * @param resVideoAllInfo 视频相关的数据
     */
    fun LoadData(resVideoAllInfo: ArchivesInfo) {
        mArchivesInfo.setValue(resVideoAllInfo)
        val id = mArchivesInfo.value?.id ?: -1
        mChannel.value = "#${resVideoAllInfo.channel?.name ?: "未知频道"}" //设置视频来源
        mDescription.value = resVideoAllInfo.description ?: ""//设置视频描述
        mTitle.value = resVideoAllInfo.title ?: "无标题" //设置视频标题
        mLikes.value = "${resVideoAllInfo.likes ?: 0}" //设置视频点赞数
        mCollections.value = "${resVideoAllInfo.collection ?: 0}"//设置视频收藏数
        mComments.value = "${resVideoAllInfo.comments ?: 0}"//设置视频评论数
        mShares.value = "${resVideoAllInfo.views ?: 0}"//设置视频浏览数
        mIsLike.value = resVideoAllInfo.islike ?: 0//设置当前用户是否点赞

        mIsCollection.value = resVideoAllInfo.iscollection ?: 0//设置当前用户是否收藏
        mIsComment.value = resVideoAllInfo.iscomment ?: 0//设置当前用户是否评论
        mIsShared.value = resVideoAllInfo.isguest ?: 0//设置当前用户是否浏览

        mAuthorName.value = mArchivesInfo.value?.user?.nickname ?: "未知作者"  //设置视频作者名字
        mAuthorAvatar.value = mArchivesInfo.value?.user?.avatar ?: ""//设置作者头像

    }


    /**
     * 专门用于显示点赞收藏的弹窗触发,解决多次弹窗的问题
     * @param text
     */
    fun showLikeCollectCommandToast(text: String) {
        mToastForLikeCollect.value = text
    }


    /**
     * 点赞关联方法
     */
    fun onLikeClick() {
        if (!mModel.isLogin())  //判断是否登录
        {
            showLikeCollectCommandToast("请先登录")
            return;
        }
        viewModelScope.launch {

            if (mIsLike.value == 1)  //说明已经点赞,发起取消点赞
            {
                val result = mModel.cancelLike(mArchivesInfo.value?.id.toString() ?: "")
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success<ResBase<Any>> -> {
                        showLikeCollectCommandToast(result.data.msg.toString())
                        mIsLike.value = 0 //表示取消点赞
                        val newLikes = mLikes.value?.toIntOrNull() ?: ERRORLIKES + 1
                        mLikes.value = newLikes.toString()
                        mArchivesInfo.value?.likes = newLikes
                    }
                }

            } else if (mIsLike.value == 0) {
                val result = mModel.requestLike(mArchivesInfo.value?.id.toString(), "like")
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success<ResLike> -> {
                        showLikeCollectCommandToast(result.data?.msg.toString())
                        mIsLike.value = 1  //表示点赞成功
                        val newLikes = mLikes.value?.toIntOrNull() ?: ERRORLIKES + 1  //计算点赞之后的点赞数
                        mLikes.value = newLikes.toString()  //更新点赞数触发观察者用
                        mArchivesInfo.value?.likes = newLikes  //可能要处理一下这里的为空的情况，这里的点赞数更新是因为绑定的是这个的值，每次初始化从这里取值

                    }
                }

            }

        }
    }

    /**
     * 收藏关联方法
     */
    fun onClickCollect() {
        if (!mModel.isLogin()) {
            //showToastText("请先登录");
            showLikeCollectCommandToast("请先登录")
            return
        }

        viewModelScope.launch {
            if (mIsCollection.value == 1) {
                val result = mModel.cancelCollection(mArchivesInfo.value?.id.toString())
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success<ResBase<Any>> -> {
                        showLikeCollectCommandToast(result.data.msg)
                        mIsCollection.value = 0
                        val newCollections = mCollections.value?.toInt() ?: ERRORCOLLECTIONS - 1
                        mCollections.value = newCollections.toString()
                        mArchivesInfo.value?.collection = newCollections

                    }
                }
            } else if (mIsCollection.value == 0) {
                val result = mModel.addCollection("archives", mArchivesInfo.value?.id.toString())
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success<ResBase<Any>> -> {
                        showLikeCollectCommandToast(result.data.msg)
                        mIsCollection.value = 1
                        val newCollections = mCollections.value?.toInt() ?: ERRORCOLLECTIONS + 1
                        mCollections.value = newCollections.toString()
                        mArchivesInfo.value?.collection = newCollections
                    }
                }
            }
        }
    }

}