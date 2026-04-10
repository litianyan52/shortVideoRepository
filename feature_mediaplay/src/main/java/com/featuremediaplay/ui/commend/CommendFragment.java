package com.featuremediaplay.ui.commend;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.video_data.bean.ResComment;
import com.featuremediaplay.BR;
import com.featuremediaplay.R;
import com.featuremediaplay.databinding.LayoutCommendBinding;
import com.featuremediaplay.dialog.DeleteCommentDialog;
import com.featuremediaplay.ui.commend.adapter.CommentAdapter;
import com.featuremediaplay.ui.mediaplay.MediaPlayViewModel;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

@Route(path = ArouterPath.Video.VIDEO_LIST_FRAGMENT_COMMEND)
public class CommendFragment extends BaseFragment<MediaPlayViewModel, LayoutCommendBinding> {

    private static final String TAG = "CommendFragment";

    private CommentAdapter mAdapter;
    private ViewTreeObserver.OnGlobalLayoutListener mKeyboardLayoutListener;

    @Override
    public MediaPlayViewModel getViewModel() {
        //设置共享ViewModel,使各个地方的数据能够及时同步
        return new ViewModelProvider(requireActivity()).get(MediaPlayViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_commend;
    }

    @Override
    public int getDataBindVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        initInputBar();
        observeKeyboard();
    }

    private void initInputBar() {
        mDataBinding.seCmd.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String content = mDataBinding.seCmd.getText().toString().trim();
                if (!content.isEmpty()) {
                    mViewmodel.sendComment(content);
                    mDataBinding.seCmd.getText().clear();
                }
                return true;
            }
            return false;
        });
    }

    private void observeKeyboard() {
        View rootView = mDataBinding.getRoot();
        mKeyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = rootView.getRootView().getHeight();
                int keyboardHeight = screenHeight - rect.bottom;
                int bottomMargin = 0;
                if (keyboardHeight > screenHeight * 0.15f) {
                    bottomMargin = keyboardHeight - getNavigationBarHeight();
                    if (bottomMargin < 0) {
                        bottomMargin = 0;
                    }
                }
                mDataBinding.fctPanel.setTranslationY(-bottomMargin);
                mDataBinding.seCmd.setTranslationY(-bottomMargin);
                mDataBinding.like.setTranslationY(-bottomMargin);
                mDataBinding.numLike.setTranslationY(-bottomMargin);
                mDataBinding.collect.setTranslationY(-bottomMargin);
                mDataBinding.numCollect.setTranslationY(-bottomMargin);
                mDataBinding.commend.setTranslationY(-bottomMargin);
                mDataBinding.numCommend.setTranslationY(-bottomMargin);
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mKeyboardLayoutListener);
    }

    @Override
    public void initData() {
        mViewmodel.getCommentList(true);  //由于是共享viewModel,所以可以在fragment创建时再加载数据,创建时加载是第一次加载
        initRefreshLayout();
        initObservers();
    }

    /**
     * 设置livaData数据观察者
     */
    private void initObservers() {
        //获取到评论列表后设置到adapter中
        mViewmodel.getCommentsList().observe(getViewLifecycleOwner(), new Observer<List<ResComment>>() {
            @Override
            public void onChanged(List<ResComment> resComments) {
                //数据请求回来把加载或者刷新动画关闭
                if (mDataBinding.refreshLayout.isRefreshing()) {
                    mDataBinding.refreshLayout.finishRefresh();
                }
                if (mDataBinding.refreshLayout.isLoading()) {
                    mDataBinding.refreshLayout.finishLoadMore();
                }
                Log.d(TAG, "onChanged: " + resComments);
                mAdapter = new CommentAdapter();
                mAdapter.setCallBack(new CommentAdapter.DeleteCallBack() {
                    @Override
                    public void onCommentLongClick(ResComment comment) {
                        DeleteCommentDialog.showDeleteDialog(getActivity(), new DeleteCommentDialog.IDeleteCommentDialog() {
                            @Override  //确认删除评论
                            public void confirmDelete() {
                                mViewmodel.deleteComment(comment);
                            }
                        });
                    }
                });
                mAdapter.setDatas(resComments);
                mDataBinding.cmdRecycler.setAdapter(mAdapter);
                mDataBinding.cmdRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        mViewmodel.getIsEnableLoadMore().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEnableLoadMore) {
                mDataBinding.refreshLayout.setEnableLoadMore(isEnableLoadMore);  //设置是否允许下拉加载
            }
        });

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

    /**
     * 设置刷新和加载要执行的动作
     */
    private void initRefreshLayout() {
        mDataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mDataBinding.refreshLayout.isLoading()) {
                    mDataBinding.refreshLayout.finishLoadMore();
                }
                mViewmodel.getCommentList(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mDataBinding.refreshLayout.isRefreshing()) {
                    mDataBinding.refreshLayout.finishRefresh();
                }
                mViewmodel.getCommentList(true);
                mViewmodel.getIsEnableLoadMore().setValue(true);  //刷新后允许加载
            }
        });
    }

//    /**
//     * 重新计算整个布局的高
//     */
//    public void updateHeight() {
//        mHandler = new Handler(Looper.getMainLooper());
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDataBinding.getRoot().requestLayout(); //等fragment渲染完成后重新计算和当前fragment相关布局的大小和位置
//            }
//        }, 300);
//    }

    private int getNavigationBarHeight() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        if (mKeyboardLayoutListener != null) {
            mDataBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(mKeyboardLayoutListener);
            mKeyboardLayoutListener = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler = null;
//        }
    }
}
