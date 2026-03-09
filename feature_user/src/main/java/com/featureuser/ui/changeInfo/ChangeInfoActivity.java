package com.featureuser.ui.changeInfo;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityChangeInfoBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_CHANGE_INFO)
public class ChangeInfoActivity extends BaseActivity<ChangeInfoViewModel, ActivityChangeInfoBinding> {


    @Override
    public ChangeInfoViewModel getViewModel() {
        return new ViewModelProvider(this).get(ChangeInfoViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_info;
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
        mViewModel.setBaseInfo();
    }
}