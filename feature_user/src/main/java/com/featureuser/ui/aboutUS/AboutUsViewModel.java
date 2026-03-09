package com.featureuser.ui.aboutUS;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.libase.base.BaseViewmodel;
import com.libase.utils.GetVersionUtils;

public class AboutUsViewModel extends BaseViewmodel {
    private MutableLiveData<String> mVersion = new MutableLiveData<>();

    public AboutUsViewModel() {
        mVersion.setValue("版本 v"+GetVersionUtils.getVersionName() + GetVersionUtils.getVersionCode());
    }

    public MutableLiveData<String> getVersion() {
        return mVersion;
    }
}
