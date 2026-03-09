package com.featureuser.ui.setting;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivitySettingBinding;
import com.featureuser.userConfig.UserConfig;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.dialog.YesOrNoDialog;
import com.libase.eventBus.MessageEvent;
import com.libase.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = ArouterPath.User.ACTIVITY_SETTING)
public class SettingActivity extends BaseActivity<SettingViewmodel, ActivitySettingBinding> {
    private static final String TAG = "SettingActivity";

    @Override
    public SettingViewmodel getViewModel() {
        return new ViewModelProvider(this).get(SettingViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
        mViewModel.getSettingAction().observe(this, action ->
        {
            switch (action) {
                case ACCOUNT:
                    Log.d(TAG, "account: ");
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_ACCOUNT).navigation();
                    break;
                case PASSWORD:
                    Log.d(TAG, "password: ");
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_PASSWORD).navigation();
                    break;
                case PUSH_SETTING:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_PUSH_SETTING).navigation();
                    break;
                case PLAY_SETTING:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_PLAY_SETTING).navigation();
                    break;
                case CLEAR_CACHE:
                    YesOrNoDialog.showDialog(this, "清除缓存", "是否要清除缓存", new YesOrNoDialog.DialogCallBack() {
                        @Override
                        public void confirm() {
                            mViewModel.showToastText("正在清除缓存...");
                        }
                    });
                    break;
                case USER_AGREEMENT:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.USER_AGREEMENT)
                            .navigation();
                    break;
                case SUM_OF_PRIVATE:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.SUM_PRIVATE)
                            .navigation();
                    break;
                case PRIVATE_POLICY:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.PRIVATE_POLICY)
                            .navigation();
                    break;
                case PRIVATE_SETTING:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_PRI_SETTING).navigation();
                    break;
                case PERSON_INFO_COLLECT:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_WB_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.USER_INFO_COLLECT)
                            .navigation();
                    break;
                case ABOUT_US:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_ABOUT_US)
                            .navigation();
                    break;
                case NOT_LOGIN:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_LOGIN).navigation();
                    break;
                case QUIT_LOGIN:
                    YesOrNoDialog.showDialog(this, "退出登录", "是否要退出登录", new YesOrNoDialog.DialogCallBack() {
                        @Override
                        public void confirm() {
                            mViewModel.loginOut();
                        }
                    });
                default:
                    break;
            }
        });


    }

    @Override
    public void initData() {

    }


    /**
     * 注册EventBus
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * 注销EventBus
     */
    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))//判断是否注册了
        {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)  //在主线程中处理,正好收到event时需要更新UI所以在主线程中更新正好
    public void MessageEvent(MessageEvent.LoginEvent loginSuccess) {
        Log.d("TAG", "MessageEvent: " + loginSuccess.isLogin());
        if (mViewModel != null) {
            mViewModel.refreshSetting();//刷新设置页面
        }

    }
}