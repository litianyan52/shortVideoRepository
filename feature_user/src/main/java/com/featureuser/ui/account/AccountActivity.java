package com.featureuser.ui.account;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityAcountBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_ACCOUNT)
public class AccountActivity extends BaseActivity<AccountViewModel, ActivityAcountBinding> {


    @Override
    public AccountViewModel getViewModel() {
        return new ViewModelProvider(this).get(AccountViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
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