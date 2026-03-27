package com.featuremediaplay.ui.introdution;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.example.video_data.bean.ArchivesInfo;
import com.featuremediaplay.databinding.LayoutIntroductionBinding;
import com.featuremediaplay.ui.commend.report.CommendPopWindow;
import com.featuremediaplay.ui.mediaplay.MediaPlayViewModel;
import com.featuremediaplay.ui.videoList.VideoListFragment;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.Video.VIDEO_LIST_FRAGMENT_INTRODUCTION)
public class IntroductionFragment extends BaseFragment<MediaPlayViewModel, LayoutIntroductionBinding> {


    private ArchivesInfo mArchivesInfo;
    private CommendPopWindow mPopWindow;
    private VideoListFragment mVideoListFragment;
    private Handler mHandler;

    public void updateHeight() {
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataBinding.getRoot().requestLayout(); //等fragment渲染完成后重新计算和当前fragment相关布局的大小和位置
            }
        }, 100);

    }

    @Override
    public MediaPlayViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(MediaPlayViewModel.class); //获取宿主Activity的viewModel
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_introduction;
    }

    @Override
    public int getDataBindVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        getVideoListFragment(); //获取视频列表Fragment

        mDataBinding.commend.setOnClickListener(v -> {
            if (mPopWindow == null) {
                mPopWindow = new CommendPopWindow((AppCompatActivity) getActivity());
            }
            mPopWindow.setCallback(new CommendPopWindow.IEditTextCallback() {
                @Override
                public void sendMsg(String msg) {
                   mViewmodel.sendComment(msg); //发送评论
                }
            });
            mPopWindow.showPopupWindow(mDataBinding.getRoot());//关联到当前的根布局上
        });
    }

    private void getVideoListFragment() {
        //获取视频播放下方的视频列表
        mVideoListFragment = (VideoListFragment) ARouter.getInstance().build(ArouterPath.Video.FRAGMENT_VideoList)
                .withInt(ArouterPath.Video.VIDEO_LIST_FRAGMENT_TYPE_KEY,
                        ArouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .withBoolean(ArouterPath.Video.VIDEO_STYLE_KEY, ArouterPath.Video.VIDEO_STYLE_WHITE)
                .navigation();
        getChildFragmentManager().beginTransaction().add(mDataBinding.mediaPlayFcv.getId(), mVideoListFragment).commit();
    }

    /**
     * 每播放一个新的视频下方的视频列表进行一次刷新
     */
    public void refreshVideoListFragment(){
       mVideoListFragment.refreshForExternalUse();
    }

    @Override
    public void initData() {
        //更新点赞按钮图片状态
        mViewmodel.getIsLike().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    mDataBinding.like.setImageResource(R.mipmap.icon_video_unlike);
                }
                if (integer == 1) {
                    mDataBinding.like.setImageResource(R.mipmap.icon_solid_like);
                }
            }
        });

        //更新收藏按钮图片状态
        mViewmodel.getIsCollection().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    mDataBinding.collect.setImageResource(R.mipmap.icon_video_collect);
                }
                if (integer == 1) {
                    mDataBinding.collect.setImageResource(R.mipmap.icon_solid_collect);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
