package com.featureuser.ui.setPassword;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.network.bean.ResBase;

public class PasswordViewModel extends BaseViewmodel {
    public PasswordViewModel() {
        mModel = new PasswordModel();
    }

    private final PasswordModel mModel;
    public MutableLiveData<String> mPhone = new MutableLiveData<>();
    public MutableLiveData<String> mCode = new MutableLiveData<>();
    private MutableLiveData<String> mCodeText = new MutableLiveData<>("获取验证码");
    private MutableLiveData<String> mPassword1 = new MutableLiveData<>();
    private MutableLiveData<String> mPassword2 = new MutableLiveData<>();
    private MutableLiveData<Boolean> enableSendCode = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> isEnableLogin = new MutableLiveData<>(false);
    private CountDownTimer mCountDownTimer;

    public MutableLiveData<Boolean> getIsEnableLogin() {
        return isEnableLogin;
    }

    public MutableLiveData<String> getPhone() {
        return mPhone;
    }

    public MutableLiveData<String> getCode() {
        return mCode;
    }

    public MutableLiveData<String> getCodeText() {
        return mCodeText;
    }

    public MutableLiveData<Boolean> getEnableSendCode() {
        return enableSendCode;
    }

    public MutableLiveData<String> getPassword1() {
        return mPassword1;
    }

    public MutableLiveData<String> getPassword2() {
        return mPassword2;
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

    /**
     * 提交按钮的关联方法
     */
    public void submit() {
        if (mPassword1.getValue() != null && mPassword2.getValue() != null && !mPassword1.getValue().isEmpty()
                && !mPassword1.getValue().isEmpty()) {
            if (mPassword1.getValue().equals(mPassword2.getValue()))  //密码相同允许设置
            {
                showLoading(true);
                changePassword(); //发起修改请求
            } else {
                showToastText("请确认两次输入的密码相同");
            }
        } else {
            showToastText("密码不能为空");
        }
    }


    /**
     * 发起修改密码请求的方法
     */
    public void changePassword() {
        mModel.changePassword(mPassword1.getValue(), mPhone.getValue(), mCode.getValue(), new IRequestCallBack<ResBase<Object>>() {
            @Override
            public void RequestSuccess(ResBase<Object> result) {
                showLoading(false);
                showToastText(result.getMsg());
                finishPage();  //修改成功后关闭页面
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
                showLoading(false);
                showToastText(errorMsg);
            }
        });
    }
}
