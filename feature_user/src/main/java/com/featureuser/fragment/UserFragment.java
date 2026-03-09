package com.featureuser.fragment;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.LayoutUserFragmentBinding;
import com.featureuser.ui.login.LoginActivity;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;
import com.libase.dialog.YesOrNoDialog;
import com.libase.eventBus.MessageEvent;
import com.libase.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = ArouterPath.User.FRAGMENT_USER)
public class UserFragment extends BaseFragment<UserViewModel, LayoutUserFragmentBinding> {
    @Override
    public UserViewModel getViewModel() {
        return new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_user_fragment;
    }

    @Override
    public int getDataBindVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToKidView(mDataBinding.getRoot(),
                mDataBinding.qualification,
                mDataBinding.setting);
        mDataBinding.setting.setOnClickListener(view ->
        {
            ARouter.getInstance().build(ArouterPath.User.ACTIVITY_SETTING).navigation();
        });
    }

    @Override
    public void initData() {
        mViewmodel.getPersonAction().observe(getViewLifecycleOwner(), personAction ->
        {
            switch (personAction) {
                case MY_COLLECT:
                    ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_MY_COLLECT).navigation();
                    break;
                case MY_RECORD:
                    ARouter.getInstance().build(ArouterPath.Video.ACTIVITY_BROWSE_RECORD).navigation();
                    break;
                case QUIT_LOGIN:
                    YesOrNoDialog.showDialog(getActivity(), "退出登录", "是否退出当前账号", new YesOrNoDialog.DialogCallBack() {
                        @Override
                        public void confirm() {
                            mViewmodel.loginOut(); //退出登录
                        }
                    });
                    break;
                case EDIT_USERINFO:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_CHANGE_INFO).navigation();
                    break;
                case NOT_LOGIN:
                    ARouter.getInstance().build(ArouterPath.User.ACTIVITY_LOGIN).navigation();
                    break;
            }
        });
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
        mViewmodel.LoadUserInfo(loginSuccess.isLogin()); //收到登录状态变更的消息
    }

}
