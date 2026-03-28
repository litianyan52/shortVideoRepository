package com.featureuser.fragment;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.featureuser.api.ILoadUserInfoCallback;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.eventBus.MessageEvent;
import com.libase.manager.UserManager;
import com.network.bean.ResBase;

public class UserViewModel extends BaseViewmodel {
    private static final String TAG = "UserViewModel";
    private final UserModel mModel;
    private MutableLiveData<String> fans = new MutableLiveData<>();
    private MutableLiveData<String> medals = new MutableLiveData<>();
    private MutableLiveData<String> follows = new MutableLiveData<>();
    private MutableLiveData<String> avatar = new MutableLiveData<>();
    private MutableLiveData<String> bio = new MutableLiveData<>();
    private MutableLiveData<String> nickname = new MutableLiveData<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private MutableLiveData<Integer> isVisible = new MutableLiveData<>();
    private MutableLiveData<PersonAction> mPersonAction = new MutableLiveData<>();

    public UserViewModel() {
        mModel = new UserModel();
        LoadUserInfo(mModel.isLogin()); //Model创建时用UserManager判断了是否登录可以拿来用
        isVisible.setValue(mModel.isLogin() ? View.VISIBLE : View.GONE);  //在创建时先决定显不显示
    }

    public MutableLiveData<String> getFans() {
        return fans;
    }

    public MutableLiveData<String> getMedals() {
        return medals;
    }

    public MutableLiveData<String> getFollows() {
        return follows;
    }

    public MutableLiveData<String> getAvatar() {
        return avatar;
    }

    public MutableLiveData<String> getBio() {
        return bio;
    }

    public MutableLiveData<String> getNickname() {
        return nickname;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public MutableLiveData<Integer> getIsVisible() {
        return isVisible;
    }

    public MutableLiveData<PersonAction> getPersonAction() {
        return mPersonAction;
    }

    /**
     * 在这之前数据
     * 从UserModel中拿用户数据回来
     * @param login
     */
    public void LoadUserInfo(boolean login) {
        if (login) {
            showLoading(true);  //拿数据时显示加载
            mModel.LoadUserInfo(new ILoadUserInfoCallback() {
                @Override
                public void getSuccess(ResUserData<ResUserInfo> result) {
                    Log.d(TAG, "getSuccess: " + result.getUser().getUsername());
                    showLoading(false);
                    upDateToUI(result);
                    isVisible.setValue(mModel.isLogin() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void getFailed(int errorCode, String errorMsg) {
                    showLoading(false);
                    isVisible.setValue(mModel.isLogin() ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            unLoginUpDateToUI();  //调用未登录时返回空的数据回去
        }
    }

    /**
     * 未登录时返回空的数据回去的方法,这样未登录时刷新数据拿到的就是空数据,不用再另写一个方法
     */
    private void unLoginUpDateToUI() {
        ResUserData<ResUserInfo> userData = new ResUserData<>();
        ResUserInfo info = new ResUserInfo();
        userData.setUser(info);
        upDateToUI(userData);  //把空的数据更新到UI上

    }

    /**
     * 更新UserFragment页面数据
     * @param result
     */
    private void upDateToUI(ResUserData<ResUserInfo> result) {
        fans.setValue(result.getFans() + " 粉丝");
        medals.setValue(result.getMedal() + " 勋章");
        follows.setValue(result.getFollow() + " 关注");
        String Avatar = result.getUser().getAvatar();
        if (Avatar != null && !Avatar.isEmpty()) {
            avatar.setValue(Avatar);
        } else {
            avatar.setValue(null);
        }
        String NickName = result.getUser().getNickname();
        if (NickName != null && !NickName.isEmpty()) {
            nickname.setValue(NickName);
        } else {
            nickname.setValue("还未有昵称");
        }

        String Bio = result.getUser().getBio();
        if (Bio != null && !Bio.isEmpty()) {
            bio.setValue(Bio);
        } else {
            bio.setValue("这个人很懒什么都没留下");
        }

    }

    /**
     * 编辑个人信息关联方法
     */

    public void onEditUserInfo() {
        if (mModel.isLogin()) {
            mPersonAction.setValue(PersonAction.EDIT_USERINFO);
        } else {
            mPersonAction.setValue(PersonAction.NOT_LOGIN);
        }
    }

    /**
     * 我的收藏关联方法
     */
    public void onMyCollect() {
        if (mModel.isLogin()) {
            mPersonAction.setValue(PersonAction.MY_COLLECT);
        } else {
            mPersonAction.setValue(PersonAction.NOT_LOGIN);
        }
    }

    /**
     * 观看记录关联方法
     */
    public void onBrowseRecord() {
        if (mModel.isLogin()) {
            mPersonAction.setValue(PersonAction.MY_RECORD);
        } else {
            mPersonAction.setValue(PersonAction.NOT_LOGIN);
        }
    }


    /**
     * 退出登录关联方法，用于通知UI显示弹窗，退出登录的发起不在这个方法里
     */
    public void onQuitLogin() {
        if (mModel.isLogin()) {
            mPersonAction.setValue(PersonAction.QUIT_LOGIN);
        } else {
            mPersonAction.setValue(PersonAction.NOT_LOGIN);
        }
    }



    /**
     * 真正发起退出登录请求
     * 清除用户信息
     * 更新UI
     * 向服务端发起退出登录请求
     */
    public void loginOut() {
        showLoading(true);
        mModel.loginOut(new IRequestCallBack<ResBase<Object>>() {
            @Override
            public void RequestSuccess(ResBase<Object> result) {
                UserManager.getInstance().ExitLogin();//清除用户数据
                MessageEvent.LoginEvent.postSticky(false); //发送退出登录消息 ,这里要先清除用户数据
                showToastText(result.getMsg());
                showLoading(false);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showLoading(false);
                showToastText(errorMsg);  //显示错误信息
            }
        });
    }

    public enum PersonAction {
        EDIT_USERINFO, //编辑个人信息
        MY_COLLECT, //我的收藏
        MY_RECORD,  //浏览记录
        QUIT_LOGIN, //退出登录
        NOT_LOGIN  //未登录
    }


}
