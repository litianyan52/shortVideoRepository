package com.featureuser.ui.changeInfo;

import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.featureuser.bean.ChangeInfoBody;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.bean.ResBase;

import retrofit2.Call;

public class ChangeInfoModel {
    private UserManager manager;

    public ChangeInfoModel() {
        this.manager = UserManager.getInstance();
    }

    public String getUserAvatar() {
        return manager.getUserInfo().getUser().getAvatar();
    }

    public String getUserBio() {
        return manager.getUserInfo().getUser().getBio();
    }

    public String getUserNickName() {
        return manager.getUserInfo().getUser().getNickname();
    }


    /**
     * 发起修改用户信息的请求
     * @param avatar
     * @param username
     * @param nickname
     * @param bio
     * @param requestCallBack
     */
    public void changeUserInfo(String avatar, String username, String nickname, String bio, IRequestCallBack<ResBase<Object>> requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.changeUserInfo(UserManager.getInstance().getUserToken(),
                new ChangeInfoBody(avatar, username, nickname, bio));
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<Object>>() {
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
}
