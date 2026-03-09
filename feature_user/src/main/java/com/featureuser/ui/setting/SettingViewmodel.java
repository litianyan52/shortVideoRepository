package com.featureuser.ui.setting;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.eventBus.MessageEvent;
import com.libase.manager.UserManager;
import com.network.bean.ResBase;

public class SettingViewmodel extends BaseViewmodel {
    private final SettingModel mModel;
    private MutableLiveData<String> mobile = new MutableLiveData<>();
    private MutableLiveData<Integer> isVisibility = new MutableLiveData<>();
    private MutableLiveData<String> cacheSize = new MutableLiveData<>();
    private MutableLiveData<SettingAction> settingAction = new MutableLiveData<>();

    public SettingViewmodel() {
        mModel = new SettingModel();
        refreshSetting();

    }

    public void refreshSetting() {
        mobile.setValue(mModel.getUserMobile());//页面初始化时就设置,保证每次进页面在登录状态下都能看到手机号
        isVisibility.setValue(mModel.isLogin() ? View.VISIBLE : View.GONE);
    }

    public MutableLiveData<String> getMobile() {
        return mobile;
    }

    public MutableLiveData<Integer> getIsVisibility() {
        return isVisibility;
    }

    public MutableLiveData<String> getCacheSize() {
        return cacheSize;
    }

    public MutableLiveData<SettingAction> getSettingAction() {
        return settingAction;
    }

    /**
     * 账号与绑定
     */
    public void onAccountAndBind() {
        if (!mModel.isLogin()) {
            settingAction.setValue(SettingAction.NOT_LOGIN);
            return;
        }
        settingAction.setValue(SettingAction.ACCOUNT);
    }

    /**
     * 设置密码
     */
    public void onSetPassword() {
        if (!mModel.isLogin()) {
            settingAction.setValue(SettingAction.NOT_LOGIN);
            return;
        }
        settingAction.setValue(SettingAction.PASSWORD);
    }

    /**
     * 推送设置
     */
    public void onPushSetting() {

        settingAction.setValue(SettingAction.PUSH_SETTING);
    }

    /**
     * 播放设置
     */
    public void onPlaySetting() {
        settingAction.setValue(SettingAction.PLAY_SETTING);
    }

    /**
     * 清理缓存
     */
    public void onClearCache() {
        settingAction.setValue(SettingAction.CLEAR_CACHE);
    }

    /**
     * 用户协议
     */
    public void onUserAgreement() {
        settingAction.setValue(SettingAction.USER_AGREEMENT);
    }

    /**
     * 隐私概要
     */
    public void onSumOfPrivatePolicy() {
        settingAction.setValue(SettingAction.SUM_OF_PRIVATE);
    }

    /**
     * 隐私政策
     */
    public void onPrivatePolicy() {
        settingAction.setValue(SettingAction.PRIVATE_POLICY);
    }

    /**
     * 隐私权限设置
     */
    public void onSettingOfPrivate() {
        settingAction.setValue(SettingAction.PRIVATE_SETTING);
    }

    /**
     * 个人信息搜集清单
     */
    public void onCollectOfPersonInfo() {
        settingAction.setValue(SettingAction.PERSON_INFO_COLLECT);
    }

    /**
     * 关于我们
     */
    public void onAboutUs() {
        settingAction.setValue(SettingAction.ABOUT_US);
    }

    /**
     * 退出登录按钮关联方法
     */
    public void onQuitLogin() {
        settingAction.setValue(SettingAction.QUIT_LOGIN);
    }


    /**
     * 发起退出登录
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

    public static enum SettingAction {
        ACCOUNT,     //账号绑定
        PASSWORD,    //设置密码
        PUSH_SETTING, //推送设置
        PLAY_SETTING, //播放设置
        CLEAR_CACHE,  //清除缓存
        USER_AGREEMENT, //用户协议
        SUM_OF_PRIVATE, //隐私政策概要
        PRIVATE_POLICY, //隐私政策
        PRIVATE_SETTING,  //隐私权限设置
        PERSON_INFO_COLLECT, //个人信息收集清单
        ABOUT_US,  //关于我们
        NOT_LOGIN, //未登录的情况
        QUIT_LOGIN  //退出登录
    }

}
