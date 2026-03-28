package com.featureuser.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityLoginBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_LOGIN)
public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public LoginViewModel getViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public int getViewModelId() {
        return BR.viewmodel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToKidView(mdataBinding.getRoot(), mdataBinding.loginBack,
                mdataBinding.qualificationInLogin,
                mdataBinding.settingInLogin);
        SpannableString spannableString = new SpannableString("请阅读并同意《用户协议》和《隐私政策》");
        /**
         * 为隐私政策字符串添加可点击的效果
         */
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT).navigation();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                Log.d(TAG, "updateDrawState: ");
                super.updateDrawState(ds);
                //   ds.setUnderlineText(false);
                ds.setColor(Color.GRAY);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT).navigation();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //   ds.setUnderlineText(false);
                ds.setColor(Color.GRAY);
            }
        };

        //隐私政策字符串的效果实现
        spannableString.setSpan(clickableSpan1, 6, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 13, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mdataBinding.privateText.setText(spannableString);
        mdataBinding.privateText.setMovementMethod(LinkMovementMethod.getInstance());// 这行代码是使ClickableSpan生效的关键，没有它文本将无法被点击
        mdataBinding.privateText.setHighlightColor(Color.TRANSPARENT); //设置为透明色，表示点击时没有背景色变化
        mdataBinding.qualificationInLogin.setOnClickListener(view ->
        {
            ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT).navigation();
        });
//        mdataBinding.loginBack.setOnClickListener(view -> //返回上个页面
//        {
//            finish();
//        });
    }

    @Override
    public void initData() {
        //手机号和验证码观察,满足条件就更新登录按钮状态
        mViewModel.getmPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phone) {
                mViewModel.updateEnableLoginStatus();
            }
        });
        mViewModel.getmCode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String code) {
                mViewModel.updateEnableLoginStatus();
            }
        });

        //用户实现登录成功后的自动退出
        mViewModel.getIsLogin().observe(this, isLogin ->
        {
            if (isLogin) {
                finish();
            }
        });
    }


    /**
     * 清理ViewModel中的CountDownTimer匿名内部类
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clearInnerClass();
    }
}