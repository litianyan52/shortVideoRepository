package com.featureuser.ui.changeInfo;

import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityChangeInfoBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.dialog.TwoChoiceDialog;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_CHANGE_INFO)
public class ChangeInfoActivity extends BaseActivity<ChangeInfoViewModel, ActivityChangeInfoBinding> {


    @Override
    public ChangeInfoViewModel getViewModel() {
        return new ViewModelProvider(this).get(ChangeInfoViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_info;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot());
    }

    @Override
    public void initData() {
        mViewModel.setBaseInfo();
        /**
         * 保存后关闭页面
         */
        mViewModel.getIsChanged().observe(ChangeInfoActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isChanged) {
                if (isChanged){
                    finish();
                }
            }
        });

        mViewModel.getIsShowChangeAvatarDialog().observe(ChangeInfoActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                if (isShow){
                    TwoChoiceDialog.showDialog(ChangeInfoActivity.this, "更换头像", "拍照", "相册", new TwoChoiceDialog.TwoChoiceDialogCallBack() {
                        @Override
                        public void chooseFirst() {

                            ARouter.getInstance().build(ArouterPath.Camera.ACTIVITY_CAMERA).navigation(ChangeInfoActivity.this, new NavCallback() {
                                @Override
                                public void onArrival(Postcard postcard) {
                                    Log.d("nav", "onArrival: ");
                                }

                                @Override
                                public void onFound(Postcard postcard) {
                                    super.onFound(postcard);
                                    Log.d("nav", "onFound: ");
                                }

                                @Override
                                public void onLost(Postcard postcard) {
                                    super.onLost(postcard);
                                    Log.d("nav", "onLost: ");
                                }

                                @Override
                                public void onInterrupt(Postcard postcard) {
                                    super.onInterrupt(postcard);
                                    Log.d("nav", "onInterrupt: ");
                                }
                            });
                        }

                        @Override
                        public void chooseSecond() {

                        }
                    });
                }
            }
        });
    }
}