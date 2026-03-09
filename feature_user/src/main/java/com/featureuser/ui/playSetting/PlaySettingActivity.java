package com.featureuser.ui.playSetting;

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
import com.featureuser.databinding.ActivityPlaySettingBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.User.ACTIVITY_PLAY_SETTING)
public class PlaySettingActivity extends BaseActivity<PlaySettingViewModel, ActivityPlaySettingBinding> {


    @Override
    public PlaySettingViewModel getViewModel() {
        return new ViewModelProvider(this).get(PlaySettingViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_play_setting;
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