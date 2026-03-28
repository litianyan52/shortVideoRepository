package com.featureuser.fragment;

import com.featureuser.api.ILoadUserInfoCallback;
import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.libase.base.IRequestCallBack;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.config.NetworkStatusConfig;

import retrofit2.Call;

public class UserModel {
    private boolean isLogin;

    public UserModel() {
        this.isLogin = UserManager.getInstance().isLogin();
    }

    public boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    /**
     * 获取用户数据
     *
     * @param callback
     */
    public void LoadUserInfo( ILoadUserInfoCallback callback) {
        isLogin = isLogin();//发起请求时要更新一下isLogin
        if (isLogin) {
            ResUserData<ResUserInfo> result = UserManager.getInstance().getUserInfo();
            if (result == null) {
                //数据为空也判定为错误
                callback.getFailed(NetworkStatusConfig.ERROR_NO_LOGIN, "未登录");
            }
            else
            {
                callback.getSuccess(result);
            }

        } else {
            callback.getFailed(NetworkStatusConfig.ERROR_NO_LOGIN, "未登录");
        }
    }


    /**
     * 像服务端发起退出登录请求
     * @param requestCallBack 回调接口
     */
    public void loginOut(IRequestCallBack<ResBase<Object>> requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.quitLogin(UserManager.getInstance().getUserToken());
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                if (result.getCode() == 1) {
                    requestCallBack.RequestSuccess(result);
                } else {
                    requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, result.getMsg());
                }
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, message);
            }
        });
    }
}
