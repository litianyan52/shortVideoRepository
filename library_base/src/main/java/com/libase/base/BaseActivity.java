package com.libase.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.libase.utils.StatusBarUtils;

public abstract class BaseActivity<VM extends BaseViewmodel, V extends ViewDataBinding> extends AppCompatActivity {
    protected VM mViewModel;
    protected V mdataBinding;
    private ProgressBar mProgressBar;
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initViewmodel();
        initDataBinding();
        ARouter.getInstance().inject(this);  //自动注入,注意时机
        initView();
        initData();
        initProgressBar();
        StatusBarUtils.setImmersiveLayout(this);  //将页面顶进系统栏实现沉浸式
    }

    /**
     * 添加ProgressBar控件
     */
    private void initProgressBar() {
        mProgressBar = new ProgressBar(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setLayoutParams(params);  //这是新创建的控件,所以直接new布局参数
        ConstraintLayout view = (ConstraintLayout) mdataBinding.getRoot();
        view.addView(mProgressBar);
    }

    /**
     * 初始化viewmodel,并设置通用观察者
     */
    private void initViewmodel() {
        if ((getViewModel() != null)) {
            mViewModel = getViewModel();
            mViewModel.getToastText().observe(this, text ->
            {
                if (text != null && !text.isEmpty()) {
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();  //通用弹窗功能
                }
            });
            mViewModel.getIsLoading().observe(this, status ->
            {
                mProgressBar.setVisibility(status == true ? View.VISIBLE : View.GONE);
            });
        }
        /**
         * 观察是否需要关闭页面
         */
        mViewModel.getIsFinish().observe(this, isFinish ->
        {
            if (isFinish) {
                finish();
            }
        });

    }

    private void initDataBinding() {
        mdataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (getLayoutId() != 0 && getViewModelId() != 0) {
            mdataBinding.setVariable(getViewModelId(), mViewModel);
            mdataBinding.setLifecycleOwner(this);
            mdataBinding.executePendingBindings(); //实时更新数据
        }

    }
    // EdgeToEdge.enable(this);


    //setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//        return insets;
//    });

    public abstract VM getViewModel();

    public abstract int getLayoutId();

    public abstract int getViewModelId();

    public abstract void initView();

    public abstract void initData();
}
