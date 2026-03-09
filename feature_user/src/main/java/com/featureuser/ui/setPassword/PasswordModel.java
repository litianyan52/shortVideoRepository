package com.featureuser.ui.setPassword;

import android.util.Log;

import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.featureuser.bean.EntrySendCode;
import com.featureuser.bean.SetPassword;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.RetrofitProvider;
import com.network.bean.ResBase;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PasswordModel {
    /**
     * @param mobile          号码用于发送验证码
     * @param requestCallBack LoginViewModel的回调
     */
    public void sendCode(String mobile, IRequestCallBack requestCallBack) {
        UserApiService service = UserApiServiceProvider.provider();
        EntrySendCode code = new EntrySendCode(mobile, "resetpwd");
        Call<ResBase<Object>> call = service.sendCode(code);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);

            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    /**
     * 发送修改密码的请求
     *
     * @param newpassword 新密码
     * @param mobile      手机号
     * @param captcha     验证码
     * @param callBack    回调接口
     */
    public void changePassword(String newpassword, String mobile, String captcha, IRequestCallBack<ResBase<Object>> callBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        SetPassword setPassword = new SetPassword(newpassword, mobile, captcha);

        Call<ResBase<Object>> call = apiService.changePassword(setPassword, UserManager.getInstance().getUserToken());
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                callBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                callBack.RequestFailed(errorCode, message);
            }
        });
    }
}
