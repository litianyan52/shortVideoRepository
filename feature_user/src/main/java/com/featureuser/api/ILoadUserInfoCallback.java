package com.featureuser.api;

import android.service.autofill.UserData;

import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;


/**
 * 获取到用户信息后的回调接口
 */
public interface ILoadUserInfoCallback {
    void getSuccess(ResUserData<ResUserInfo> result);
    void getFailed(int errorCode,String errorMsg);
}
