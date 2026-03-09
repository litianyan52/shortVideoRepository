package com.featureuser.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;

public class AccountViewModel extends BaseViewmodel {
    private final AccountModel mModel;
    private MutableLiveData<String> mMobile = new MutableLiveData<>();

    public MutableLiveData<String> getMobile() {
        return mMobile;
    }

    public AccountViewModel() {
        mModel  = new AccountModel();
        mMobile.setValue( mModel.getUserMobile());//页面初始化时就设置,保证每次进页面在登录状态下都能看到手机号
    }
}
