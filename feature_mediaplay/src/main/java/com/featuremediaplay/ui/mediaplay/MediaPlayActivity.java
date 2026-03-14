package com.featuremediaplay.ui.mediaplay;


import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.example.video_data.bean.ArchivesInfo;
import com.featuremediaplay.databinding.ActivityMediaPlayBinding;
import com.featuremediaplay.player.MediaPlayerManager;
import com.featuremediaplay.ui.commend.CommendFragment;
import com.featuremediaplay.ui.introdution.IntroductionFragment;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.eventBus.MessageEvent;
import com.libase.manager.UserManager;
import com.libase.utils.StatusBarUtils;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

@Route(path = ArouterPath.Video.ACTIVITY_MEDIA_PLAY)
public class MediaPlayActivity extends BaseActivity<MediaPlayViewModel, ActivityMediaPlayBinding> {

    private static final String TAG = "MediaPlayActivity";
    private CommendFragment mCommendFragment;
    private IntroductionFragment mIntroductionFragment;
    @Autowired(name = ArouterPath.Video.KEY_VIDEO_ID)
    public int mVideoId;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mVideoId = intent.getIntExtra(ArouterPath.Video.KEY_VIDEO_ID, 0);
        mViewModel.RequestVideoInfo(String.valueOf(mVideoId)); //请求视频相关的数据
        mViewModel.getIsEnableLoadMore().setValue(true);   //因为上个视频可能已经因为数据不足十条的原因导致不能继续请求数据了,这里要重置使能请求数据
        Log.d(TAG, "onNewIntent: " +mVideoId);
        mdataBinding.nts.scrollTo(0,0);
        mIntroductionFragment.refreshVideoListFragment();
    }

    private MediaPlayerManager mInstance;

    @Override
    public MediaPlayViewModel getViewModel() {
        return new ViewModelProvider(this).get(MediaPlayViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_media_play;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
        initViewPager();
        initTab();
        initPlayer();

    }

    private void initPlayer() {
        mInstance = MediaPlayerManager.getInstance(MediaPlayActivity.this);
        mInstance.bindPlayerView(mdataBinding.playView);
    }

    /**
     * 初始化指示器让其与ViewPager联动
     */
    private void initTab() {
        mdataBinding.tabLayout.setViewPager(mdataBinding.videoViewPager);
        ArrayList list = new ArrayList();
        list.add("简介");
        list.add("评论");
        mdataBinding.tabLayout.setAdapter(new TabFlowAdapter(list));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        Log.d("TAG", "initViewPager: " + UserManager.getInstance().getUserToken());
        ViewPager2 videoViewPager = mdataBinding.videoViewPager;
        mCommendFragment = (CommendFragment) ARouter.getInstance().
                build(ArouterPath.Video.VIDEO_LIST_FRAGMENT_COMMEND).
                navigation();
        mIntroductionFragment = (IntroductionFragment) ARouter.getInstance().
                build(ArouterPath.Video.VIDEO_LIST_FRAGMENT_INTRODUCTION).
                navigation();
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mIntroductionFragment);
        fragmentArrayList.add(mCommendFragment);
        videoViewPager.setAdapter(new FragmentStateAdapter(this) {
            //匿名内部类隐式持有fragmentArrayList,所以不会被销毁
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentArrayList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentArrayList == null ? 0 : fragmentArrayList.size();
            }
        });
        videoViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    mIntroductionFragment.updateHeight();  //重新计算高
                } else if (position == 1) {
                    mCommendFragment.updateHeight();

                }
            }
        });
    }

    @Override
    public void initData() {
//        Log.d("TAG", "initData: "  + UserManager.getInstance().getUserToken());
        mViewModel.RequestVideoInfo(String.valueOf(mVideoId)); //请求视频相关的数据
        mViewModel.getArchivesInfo().observe(this, new Observer<ArchivesInfo>() {
            @Override
            public void onChanged(ArchivesInfo archivesInfo) {

                mInstance.play(archivesInfo.getVideo_file());
                mViewModel.getCommentList(true);  //点击下一个视频后重新请求评论列表
            }
        });
        //进入这个activity时就加载评论数据
//    mViewModel.getResVideoAllInfo().observe(this, new Observer<ResVideoAllInfo>() {
//        @Override
//        public void onChanged(ResVideoAllInfo resVideoAllInfo) {
//            mIntroductionFragment.LoadData(resVideoAllInfo);
//        }
//    });
    }


    /**
     * 注册EventBus
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mInstance.playWhenReady(true);
    }

    /**
     * 注销EventBus
     */
    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))//判断是否注册了
        {
            EventBus.getDefault().unregister(this);
        }
        mInstance.playWhenReady(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewModel.getArchivesInfo().getValue() != null) {
            mInstance.play(mViewModel.getArchivesInfo().getValue().getVideo_file());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance.destroy();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)  //在主线程中处理,正好收到event时需要更新UI所以在主线程中更新正好
    public void MessageEvent(MessageEvent.LoginEvent loginSuccess) {
        mViewModel.RequestVideoInfo(String.valueOf(mVideoId)); //收到登录状态变更的消息,重新请求一次视频相关的数据
    }
}