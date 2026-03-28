package com.featureuser.ui.login;

import android.util.Log;

import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.featureuser.bean.EntryLogin;
import com.featureuser.bean.EntrySendCode;
import com.featureuser.bean.ResLogin;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.base.IRequestCallBack;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.config.NetworkStatusConfig;

import retrofit2.Call;

public class LoginModel {
    private static final String TAG = "LoginModel";

    /**
     * @param mobile          号码用于发送验证码
     * @param requestCallBack LoginViewModel的回调
     */
    public void sendCode(String mobile, IRequestCallBack requestCallBack) {
        UserApiService service = UserApiServiceProvider.provider();
        EntrySendCode code = new EntrySendCode(mobile, "mobilelogin");
        Call<ResBase<Object>> call = service.sendCode(code);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                Log.d(TAG, "GetLiseFailed: ");
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    public void LoginByMobile(String mobile, String captcha, IRequestCallBack requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        EntryLogin login = new EntryLogin(mobile, captcha);
        Call<ResBase<ResLogin>> call = apiService.LoginByMobile(login);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResLogin>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResLogin> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }

    public void getUserInfo(String user_id, String type, IRequestCallBack<ResBase<ResUserData<ResUserInfo>>> requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        Call<ResBase<ResUserData<ResUserInfo>>> call = apiService.getUserInfo(user_id, type);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResUserData<ResUserInfo>>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResUserData<ResUserInfo>> result) {
                if (result.getData() == null) {
                    requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, "数据为空");
                } else {
                    requestCallBack.RequestSuccess(result);
                }
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }
}
