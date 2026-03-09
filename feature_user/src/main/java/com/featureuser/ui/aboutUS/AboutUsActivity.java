package com.featureuser.ui.aboutUS;

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
import com.featureuser.databinding.ActivityAboutUsBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;
@Route(path = ArouterPath.User.ACTIVITY_ABOUT_US)
public class AboutUsActivity extends BaseActivity<AboutUsViewModel, ActivityAboutUsBinding> {


    @Override
    public AboutUsViewModel getViewModel() {
        return new ViewModelProvider(this).get(AboutUsViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
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

    }
}