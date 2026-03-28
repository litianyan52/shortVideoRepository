package com.featureuser.ui.setPassword;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityPasswordBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_PASSWORD)
public class PasswordActivity extends BaseActivity<PasswordViewModel, ActivityPasswordBinding> {


    @Override
    public PasswordViewModel getViewModel() {
        return new ViewModelProvider(this).get(PasswordViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
    }

    @Override
    public void initData() {
        mViewModel.getPhone().observe(this, phone ->
        {
            mViewModel.updateEnableLoginStatus();
        });
        mViewModel.getCode().observe(this, code ->
        {
            mViewModel.updateEnableLoginStatus();
        });
    }
}