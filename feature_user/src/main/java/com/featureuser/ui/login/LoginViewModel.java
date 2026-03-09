package com.featureuser.ui.login;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.featureuser.bean.ResLogin;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.eventBus.MessageEvent;
import com.libase.manager.UserManager;
import com.network.bean.ResBase;

public class LoginViewModel extends BaseViewmodel {
    private final LoginModel mModel;
    public MutableLiveData<String> mPhone = new MutableLiveData<>();
    public MutableLiveData<String> mCode = new MutableLiveData<>();
    private MutableLiveData<String> mCodeText = new MutableLiveData<>("获取验证码");
    private MutableLiveData<Boolean> enableSendCode = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> isEnableLogin = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isLogin = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isAccept = new MutableLiveData<>(false);
    private static final String TAG = "LoginViewModel";
    private CountDownTimer mCountDownTimer;

    public LoginViewModel() {
        mModel = new LoginModel();
    }

    public MutableLiveData<String> getmPhone() {
        return mPhone;
    }

    public MutableLiveData<String> getmCode() {
        return mCode;
    }

    public MutableLiveData<Boolean> getIsEnableLogin() {
        return isEnableLogin;
    }

    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }

    public MutableLiveData<Boolean> getIsAccept() {
        return isAccept;
    }

    public MutableLiveData<String> getCodeText() {
        return mCodeText;
    }

    public MutableLiveData<Boolean> getEnableSendCode() {
        return enableSendCode;
    }


    /**
     * 更新登录按钮状态
     */
    public void updateEnableLoginStatus() {
        if (mPhone.getValue() == null || mCode.getValue() == null) {
            return;
        }
        boolean value = mPhone.getValue().length() >= 11 && mCode.getValue().length() >= 4;
        isEnableLogin.setValue(value);
    }


    /*
    登录按钮可以按下后,触发这个方法进行登录
     */
    public void LoginByMobile() {
        if (isAccept.getValue()) {
            showLoading(true);
            mModel.LoginByMobile(mPhone.getValue(), mCode.getValue(), new IRequestCallBack<ResBase<ResLogin>>() {
                @Override
                public void RequestSuccess(ResBase<ResLogin> result) {
//                    showLoading(false);
                    showToastText(result.getMsg());
                    UserManager.getInstance().saveUserToken(result.getData().getToken());//存储token
                    getUserInfo(String.valueOf(result.getData().getId()), "archives");//登录后自动获取用户信息
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showLoading(false);
                    showToastText(errorMsg);
                }
            });
        } else {
            showToastText("请勾选隐私协议");
            Log.d(TAG, "请勾选隐私协议 ");
        }
    }


    /**
     * 登录成功后使用该方法获取用户信息显示在UI上
     * @param user_id
     * @param type
     */
    public void getUserInfo(String user_id, String type) {
        mModel.getUserInfo(user_id, type, new IRequestCallBack<ResBase<ResUserData<ResUserInfo>>>() {
            @Override
            public void RequestSuccess(ResBase<ResUserData<ResUserInfo>> result) {
                showLoading(false);
                UserManager.getInstance().saveUserInfo(result.getData());//存储用户的信息
                MessageEvent.LoginEvent.postSticky(true);//发送登录事件,true表示成功登录
                isLogin.setValue(true);//获取信息成功设置登录状态为true通知LoginActivy退出
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showLoading(false);
                showToastText(errorMsg);
            }
        });
    }

    /**
     * 验证码状态更新
     */
    public void TimeCount() {

        if (mPhone.getValue() == null) {
            showToastText("请输入号码");
            return;
        }
        if (mPhone.getValue().length() >= 11) {
            showLoading(true);  //发送后显示加载
            mModel.sendCode(mPhone.getValue(), new IRequestCallBack<ResBase<Object>>() {
                @Override
                public void RequestSuccess(ResBase<Object> result) {
                    showToastText(result.getMsg());
                    showLoading(false);
                }

                @Override
                public void RequestFailed(int errorCodeValue, String errorMsg) {
                    showLoading(false);
                    Log.d(TAG, "RequestFailed: ");
                    showToastText(errorMsg);
                }
            });//发起请求
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            mCountDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    enableSendCode.setValue(false);
                    mCodeText.setValue(millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    mCodeText.setValue("获取验证码");
                    enableSendCode.setValue(true);
                }
            }.start();
        } else {
            showToastText("号码格式错误");
        }
    }
}
