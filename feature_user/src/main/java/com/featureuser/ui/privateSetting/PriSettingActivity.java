package com.featureuser.ui.privateSetting;

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
import com.featureuser.databinding.ActivityPriSettingBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.User.ACTIVITY_PRI_SETTING)
public class PriSettingActivity extends BaseActivity<PriSettingViewmodel, ActivityPriSettingBinding> {


    @Override
    public PriSettingViewmodel getViewModel() {
        return new ViewModelProvider(this).get(PriSettingViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pri_setting;
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