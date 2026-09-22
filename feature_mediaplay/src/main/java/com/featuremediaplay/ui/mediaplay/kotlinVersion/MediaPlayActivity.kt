package com.featuremediaplay.ui.mediaplay.kotlinVersion

import android.content.Intent
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
import com.featuremediaplay.player.MediaPlayerManager
import com.featuremediaplay.ui.commend.CommendFragment
import com.featuremediaplay.ui.introdution.IntroductionFragment
import com.featuremediaplay.ui.mediaplay.MediaPlayViewModel
import com.libase.base.BaseActivity
import com.libase.config.ArouterPath
import com.libase.utils.StatusBarUtils

@Route(path = ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
class MediaPlayActivity : BaseActivity<MediaPlayViewModelKotlinVersion, ActivityMediaPlayBinding>() {
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
    }

    override fun getViewModel(): MediaPlayViewModelKotlinVersion? {
        return ViewModelProvider(this).get(MediaPlayViewModelKotlinVersion::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_media_play
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
        initViewPager()
    }

    override fun initData() {
        mViewModel.RequestVideoInfo(mVideoId.toString())
        mViewModel.archivesInfo.observe(this) { it ->
            mPlayer.play(it.video_file)
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


    fun initMediaPlayer(): Unit {
        mPlayer = MediaPlayerManager.getInstance(this)
    }
}