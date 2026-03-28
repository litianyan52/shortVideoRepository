package com.featureuser.ui.pushSetting;

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
import com.featureuser.databinding.ActivityPushSettingBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.User.ACTIVITY_PUSH_SETTING)
public class PushSettingActivity extends BaseActivity<PushSettingViewModel, ActivityPushSettingBinding> {


    @Override
    public PushSettingViewModel getViewModel() {
        return new ViewModelProvider(this).get(PushSettingViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_push_setting;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}