package com.featureuser.ui.setting;

import android.util.Log;

import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;
import com.network.config.NetworkStatusConfig;

import retrofit2.Call;

/**
 * 判断是否登录
 */
public class SettingModel {
    public boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    /**
     * 获取电话号码
     *
     * @return
     */
    public String getUserMobile() {
        if (isLogin()) {
            String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
            StringBuilder builder = new StringBuilder();
            builder.append(mobile.substring(0, 3));
            builder.append("****");//给号码做个隐藏处理
            builder.append(mobile.substring(7));
            String newMobile = builder.toString();
            return newMobile;
        } else {
            return null;
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
