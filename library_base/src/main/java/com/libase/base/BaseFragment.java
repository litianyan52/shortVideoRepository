package com.libase.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.libase.R;
import com.libase.databinding.LayoutStatusViewBinding;
import com.network.config.NetworkStatusConfig;

public abstract class BaseFragment<VM extends BaseViewmodel, V extends ViewDataBinding> extends Fragment {
    public VM mViewmodel;
    public V mDataBinding;
    private static final String TAG = "BaseFragment";
    protected LayoutStatusViewBinding mStatusViewBinding;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewModel();
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View root = mDataBinding.getRoot();
        mDataBinding.setLifecycleOwner(this);

        if (getDataBindVariableId() != 0 && getViewModel() != null) {
            mDataBinding.setVariable(getDataBindVariableId(), mViewmodel);
            mDataBinding.executePendingBindings();  //绑定后要调用这个实时刷新
        }
        ARouter.getInstance().inject(this);  //自动注入,调用时机要注意
        initView();
        initStatusView();
        initData();
        initProgressBar();
        return root;
    }

    private void initViewModel() {
        if (getViewModel() != null) {
            mViewmodel = getViewModel();
            mViewmodel.getToastText().observe(getViewLifecycleOwner(), text ->
            {
                if (text != null && !text.isEmpty()) {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();  //通用弹窗功能
                }

            });
            mViewmodel.getIsLoading().observe(getViewLifecycleOwner(), status -> {
                mProgressBar.setVisibility(status == true ? View.VISIBLE : View.GONE);//设置是否可见
                Log.d(TAG, "initViewModel: " + status);
            });
        }
    }

    private void initProgressBar() {
        mProgressBar = new ProgressBar(getContext());

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setLayoutParams(params);  //这是新创建的控件,所以直接new布局参数
        View view = mDataBinding.getRoot();
        if (view instanceof ConstraintLayout) {
            ConstraintLayout layout = (ConstraintLayout) view;
            layout.addView(mProgressBar); //因为有的是其他布局形式
        }

    }

    private void initStatusView() {
        ViewGroup root = (ViewGroup) mDataBinding.getRoot();
        mStatusViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_status_view, null, false);
        View statusViewRoot = mStatusViewBinding.getRoot();
        statusViewRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(statusViewRoot);  //添加错误页面进去
        if (mViewmodel != null) {
            mViewmodel.getErrorCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer errorCode) {
                    String content;
                    switch (errorCode) {
                        case NetworkStatusConfig.ERROR_NO_LOGIN:
                            content = "请登录后再操作!";
                            break;
                        case NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL:
                            content = "网络有问题!";
                            break;
                        case NetworkStatusConfig.ERROR_STATUS_EMPTY:
                            content = "没有更多数据了!";
                            break;
                        case NetworkStatusConfig.STATUS_SUCCESS:
                        default:
                            content = "";
                    }
                    mStatusViewBinding.netErrorView    //错误状态的页面的DataBing在BaseFragment中已经拿到
                            .setVisibility(errorCode == NetworkStatusConfig.STATUS_SUCCESS ?
                                    View.GONE : View.VISIBLE);
                    mStatusViewBinding.errorText.setText(content);
                }
            });
        }

    }


    public abstract VM getViewModel();

    public abstract int getLayoutId();

    public abstract int getDataBindVariableId();

    public abstract void initView();

    public abstract void initData();

}
