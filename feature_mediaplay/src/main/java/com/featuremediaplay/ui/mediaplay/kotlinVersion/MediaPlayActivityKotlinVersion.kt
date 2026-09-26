package com.featuremediaplay.ui.mediaplay.kotlinVersion

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.featuremediaplay.BR

import com.featuremediaplay.R
import com.featuremediaplay.databinding.ActivityMediaPlayBinding
import com.featuremediaplay.databinding.ActivityMediaPlayKotlinBinding
import com.featuremediaplay.player.MediaPlayerManager
import com.featuremediaplay.ui.commend.CommendFragment
import com.featuremediaplay.ui.introdution.IntroductionFragment
import com.libase.base.BaseActivity
import com.libase.config.ArouterPath
import com.libase.eventBus.MessageEvent
import com.libase.utils.StatusBarUtils
import com.zhengsr.tablib.view.adapter.TabFlowAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
class MediaPlayActivityKotlinVersion : BaseActivity<MediaPlayViewModelKotlinVersion, ActivityMediaPlayKotlinBinding>() {
    companion object {
        private const val TAG = "MediaPlayActivity"
    }

    @Autowired(name = ArouterPath.Video.KEY_VIDEO_ID)
    @JvmField
    var mVideoId: Int = -1   //要播放的视频id



    private lateinit var mVideoViewPager: ViewPager2

    private lateinit var mPlayer: MediaPlayerManager

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mVideoId = intent.getIntExtra(ArouterPath.Video.KEY_VIDEO_ID, 0)
        mViewModel.requestVideoInfo(mVideoId.toString()) //请求视频相关的数据
        mViewModel.isEnableLoadMore.value = true//因为上个视频可能已经因为数据不足十条的原因导致不能继续请求数据了,这里要重置使能请求数据
        //mdataBinding.nts.scrollTo(0,0);
        //mIntroductionFragment.refreshVideoListFragment()

    }

    override fun getViewModel(): MediaPlayViewModelKotlinVersion? {
        return ViewModelProvider(this).get(MediaPlayViewModelKotlinVersion::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_media_play_kotlin
    }

    override fun getViewModelId(): Int {
        return BR.viewModelOfVideoPlay
    }

    override fun initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot())
        initViewPager()
        initTab()
        initMediaPlayer()
    }

    override fun initData() {
        Log.d(TAG,"initData")
        mViewModel.requestVideoInfo(mVideoId.toString())

        mViewModel.mArchivesInfo.observe (this){ data -> mPlayer.play(data.video_file)
            mViewModel.getCommentList(true);  //点击下一个视频后重新请求评论列表
        }

        /**
         * 专门显示点赞收藏的弹窗,解决点赞收藏多次触发弹窗的问题
         */
        mViewModel.mToastForLikeCollect.observe (this){
                text -> Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
        }
    }


    fun initViewPager(): Unit {
        mVideoViewPager = mdataBinding.videoViewPager
        mVideoViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ARouter.getInstance()
                        .build(ArouterPath.Video.VIDEO_LIST_FRAGMENT_INTRODUCTION)
                        .navigation() as IntroductionFragment

                    1 ->
                        ARouter.getInstance()
                            .build(ArouterPath.Video.VIDEO_LIST_FRAGMENT_COMMEND)
                            .navigation() as CommendFragment

                    else -> throw IllegalArgumentException("Invalid position $position")
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
    }
    /**
     * 初始化指示器让其与ViewPager联动
     */
    fun initTab() {
        mdataBinding.tabLayout.setViewPager(mdataBinding.videoViewPager);
        val list : MutableList<String> = mutableListOf()
        list.add("简介");
        list.add("评论");
        mdataBinding.tabLayout.setAdapter( TabFlowAdapter(list))
    }


    /**
     * 初始化播放器
     */
    fun initMediaPlayer(): Unit {
        mPlayer = MediaPlayerManager.getInstance(this)
        mPlayer.bindPlayerView(mdataBinding.playView)
    }


    override fun onStart(){
        super.onStart()
        EventBus.getDefault().register(this) //注册EventBus
        mPlayer.playWhenReady(true);
    }

    override fun onPause(){
        super.onPause()
        mPlayer.playWhenReady(false)
    }

    override fun onStop(){
        super.onStop()
        mPlayer.playWhenReady(false)
        if (EventBus.getDefault().isRegistered(this))//判断是否注册了
        {
            EventBus.getDefault().unregister(this)
        }
    }


    override fun onDestroy(){
        super.onDestroy()
        mPlayer.destroy()

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)  //在主线程中处理,正好收到event时需要更新UI所以在主线程中更新正好
    fun MessageEvent( loginSuccess :MessageEvent.LoginEvent) {
        mViewModel.requestVideoInfo(mVideoId.toString()) //收到登录状态变更的消息,重新请求一次视频相关的数据
    }
}