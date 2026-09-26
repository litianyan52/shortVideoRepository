package com.featuremediaplay.ui.mediaplay.kotlinVersion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.video_data.bean.ArchivesInfo
import com.example.video_data.bean.ResComment
import com.example.video_data.bean.ResVideoAllInfo
import com.featuremediaplay.api.apiKotlinVersion.MediaApiServiceProvider

import com.libase.base.BaseViewmodel
import com.libase.manager.UserManager
import com.network.config.NetworkStatusResult
import kotlinx.coroutines.launch


class MediaPlayViewModelKotlinVersion : BaseViewmodel() {

    companion object {
        private const val TAG: String = "MediaPlayViewModel";
    }


    private var mModel: MediaPlayModelKotlinVersion = MediaPlayModelKotlinVersion()

    private val _mResVideoAllInfo = MutableLiveData<ResVideoAllInfo>()
    val mResVideoAllInfo: LiveData<ResVideoAllInfo> get() = _mResVideoAllInfo


     val mIsLike = MutableLiveData<Int>(0) //用户是否点赞
     val mIsCollection = MutableLiveData<Int>(0) //用户是否收藏
     val mIsComment = MutableLiveData<Int>(0) //用户是否评论
     val mIsShared = MutableLiveData<Int>(0) //用户是否分享

     val mLikes = MutableLiveData<String>();  //点赞数
     val mCollections = MutableLiveData<String>();  //收藏数
     val mComments = MutableLiveData<String>();  //评论数
     val mAuthorName = MutableLiveData<String>();  //作者名字
     val mAuthorAvatar = MutableLiveData<String>();  //作者头像


     val mArchivesInfo = MutableLiveData<ArchivesInfo>(); //视频数据
     val mChannel = MutableLiveData<String>();//渠道,表示这个视频来源
     val mDescription = MutableLiveData<String>();  //视频描述
     val mTitle = MutableLiveData<String>();  //视频标题

     val mShares = MutableLiveData<String>();  //分享数

     val mCommentList = MutableLiveData<List<ResComment>>(null);  //评论列表

    //是否允许继续加载评论列表,设置为true确保第一次能够加载
     val isEnableLoadMore = MutableLiveData<Boolean>(true);  //默认允许下拉加载

     val mToastForLikeCollect = MutableLiveData<String>()  //用于触发点赞收藏的弹窗


    private val ERRORLIKES: Int = -100

    private val ERRORCOLLECTIONS: Int = -100

    fun requestVideoInfo(id: String) {
        viewModelScope.launch {
            showLoading(true)
            val result = mModel.RequestVideoInfo(id)
            Log.d(TAG,"result: " + result + "token: " + MediaApiServiceProvider.getInstance().getVideoInfo(id, UserManager.getInstance().getUserToken()))
            when (result) {
                is NetworkStatusResult.Empty -> showToastText(result.msg ?: "没有数据了")
                is NetworkStatusResult.Error -> showToastText(result.errorMessage ?: "网络异常")
                is NetworkStatusResult.Success -> {_mResVideoAllInfo.value =
                    result.data
                    mResVideoAllInfo.value?.archivesInfo?.let {
                        it -> loadData(it)
                    }

                    InsertBrowseRecord(mArchivesInfo.value?.id ?: 0
                        ,mArchivesInfo.value?.image?:"",
                        mArchivesInfo.value?.channel?.name?:"",
                        mArchivesInfo.value?.title?:"",
                        mArchivesInfo.value?.duration ?: ""
                    )
                }
            }
            showLoading(false)

        }
    }

    /**
     * 设置数据
     *
     * @param resVideoAllInfo 视频相关的数据
     */
    fun loadData(resVideoAllInfo: ArchivesInfo) {
        mArchivesInfo.value = resVideoAllInfo
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
                    is NetworkStatusResult.Success -> {
                        showLikeCollectCommandToast(result.data?.msg.toString())
                        mIsLike.value = 0 //表示取消点赞
                       // val newLikes = mLikes.value?.toIntOrNull()  ?: ERRORLIKES

                        val newLikes : Int = mLikes.value?.let{ likes ->  likes.toInt() - 1  } ?:ERRORLIKES
                        mLikes.value = newLikes.toString()
                        mArchivesInfo.value?.likes = newLikes
                    }
                }

            } else if (mIsLike.value == 0) {
                val result = mModel.requestLike(mArchivesInfo.value?.id.toString(), "like")
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success -> {
                        showLikeCollectCommandToast(result.data?.msg.toString())
                        mIsLike.value = 1  //表示点赞成功
                        //val newLikes = mLikes.value?.toIntOrNull() ?: ERRORLIKES + 1  //计算点赞之后的点赞数

                        val newLikes: Int = mLikes.value?.let { likes -> likes.toInt() + 1 } ?: ERRORLIKES  //计算点赞之后的点赞数

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
                    is NetworkStatusResult.Success -> {
                        showLikeCollectCommandToast(result.data.msg)
                        mIsCollection.value = 0
                        val newCollections = mCollections.value?.toInt() ?: ERRORCOLLECTIONS - 1
                        mCollections.value = newCollections.toString()
                       // mArchivesInfo.value?.collection = newCollections
                        mArchivesInfo.value?.let {
                            it -> it.collection = newCollections
                        }

                    }
                }
            } else if (mIsCollection.value == 0) {
                val result = mModel.addCollection("archives", mArchivesInfo.value?.id.toString())
                when (result) {
                    is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                    is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                    is NetworkStatusResult.Success -> {
                        showLikeCollectCommandToast(result.data.msg)
                        mIsCollection.value = 1
                        val newCollections = mCollections.value?.toInt() ?: ERRORCOLLECTIONS + 1
                        mCollections.value = newCollections.toString()
                        //mArchivesInfo.value?.collection = newCollections
                        mArchivesInfo.value?.let { it -> it.collection = newCollections }
                    }
                }
            }
        }
    }




    /**
     * 发送评论
     * @param comment 要发送的评论内容
     */
    fun sendComment( comment:String) {
        if (!mModel.isLogin()) {
            showLikeCollectCommandToast("请先登录")
            return
        }

        val id: Int = mArchivesInfo.value?.id ?: return

        viewModelScope.launch {
            val result = mModel.sendComment(comment, id.toString())
            when (result) {
                is NetworkStatusResult.Empty -> showLikeCollectCommandToast(result.msg.toString())
                is NetworkStatusResult.Error -> showLikeCollectCommandToast(result.errorMessage.toString())
                is NetworkStatusResult.Success -> {
                    showLikeCollectCommandToast(result.data.msg)
                    var valueOfComments: Int = mComments.value?.toInt() ?: 0  //如果为null默认给0
                    //Integer value = Integer.valueOf(mComments.getValue()); //评论条数增加
                    valueOfComments = valueOfComments + 1
                    mComments.value = valueOfComments.toString()
                    // mComments.setValue(String.valueOf(value));  //把加号的正确数据设置回去
                    val listValue: MutableList<ResComment>? = mCommentList.value?.toMutableList()
                    //在CommentFragment新增的评论插到列表头部,在简介页的时候不会请求评论列表，所以列表为null，不往里面添加数据，等请求的时候从服务器拿数据
                    listValue?.let {
                        list -> result.data?.data?.comment?.let { list.add(0, it) }
                        mCommentList.value = list//触发一下观察者
                    }
                }
            }

        }
    }






    /**
     * 获取评论列表
     */
    fun getCommentList( isFirst : Boolean) {
        val id:Int =  mArchivesInfo.value?.id ?:return
       viewModelScope.launch {
           val result = mModel.getCommentList(isFirst , id )
           when(result) {

               is NetworkStatusResult.Empty -> {
                   showToastText(result.msg.toString())
                   isEnableLoadMore.value = false
                   mCommentList.value = emptyList<ResComment>()  //本来就没有评论数据的情况,往里设置一个空列表，如果不设置后续添加新评论会空指针
               }

               is NetworkStatusResult.Error -> showToastText(result.errorMessage.toString())

               is NetworkStatusResult.Success -> {
                   if (isEnableLoadMore.value == false || isEnableLoadMore.value == null){
                       return@launch
                   }

                   val list = result.data.data?.list
                   if (list == null || list.size < 10) {
                       isEnableLoadMore.value = false //返回的数据条数不足10,说明后面没有更多的数据了，禁止加载更多
                   }
                   if (isFirst){
                       mCommentList.value = result.data.data?.list
                   }
                   else{
                       val list = result.data.data?.list
                       val newCommentList = mCommentList.value.orEmpty() + list.orEmpty()
                       mCommentList.value = newCommentList
                   }
               }

           }
       }

    }


    /**
     * 删除评论
     *
     * @param resComment
     */
    fun deleteComment( resComment :ResComment) {
        if (!mModel.isLogin()) {
            showToastText("请先登录")
            return
        }
        val nickname:String = UserManager.getInstance().userInfo.user.nickname
        //昵称不相同就返回
        if (nickname != resComment?.user?.nickname){
            return
        }

        mCommentList.value?.toMutableList()?.let { list -> list.remove(resComment)
            mCommentList.value = list //触发评论列表观察者
        }
        viewModelScope.launch {
            val result = mModel.deleteComment(resComment.id)
            when(result) {
                is NetworkStatusResult.Empty -> showToastText(result.msg.toString())
                is NetworkStatusResult.Error -> showToastText(result.errorMessage.toString())
                is NetworkStatusResult.Success -> mComments.value?.let { value -> value.toInt()
                    val newComments:Int = value.toInt() - 1 //评论条数减一
                    mComments.value = newComments.toString()  //触发评论数观察者
                }
            }
        }

    }



    /**
     * 插入浏览记录到数据库中
     */
    fun InsertBrowseRecord( video_id:Int, cover:String, label:String, title:String, duration:String)
    {
        mModel.InsertData(video_id,cover,label,title,duration);
    }

}