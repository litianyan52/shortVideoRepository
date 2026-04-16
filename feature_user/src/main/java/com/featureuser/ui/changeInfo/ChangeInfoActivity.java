package com.featureuser.ui.changeInfo;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.feature_camera.ui.camera.CameraActivity;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityChangeInfoBinding;
import com.libase.adapter.commonBindingAdapter;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.dialog.TwoChoiceDialog;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_CHANGE_INFO)
public class ChangeInfoActivity extends BaseActivity<ChangeInfoViewModel, ActivityChangeInfoBinding> {

    private static final String TAG = "ChangeInfoActivity";
    /**
     * 表明取回来的是图片,在这里处理拿回来的图片Uri
     */
    private final ActivityResultLauncher<String>  imagePickLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override    //跳转相册
        public void onActivityResult(Uri uri) {
            if (uri == null){
                return;
            }
            //改变当前头像,但是还未保存头像
            Log.d(TAG, "onActivityResult: " + uri);
            commonBindingAdapter.LoadCircleImage(mdataBinding.changeInfoAvatar,uri.toString());
            mViewModel.upLoadFile(uri);
        }
    });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            Log.d(TAG, ": " + uri);
                            String uriStr = data.getStringExtra("photo_uri");
                            String path = data.getStringExtra("photo_path");
                            mViewModel.upLoadFile(uri);
                            // 这里处理照片
                        }
                    }
            ); //跳转相机


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

//        /**
//         * 上传成功后使用uri更新婴一下头像,不然等Glide通过URL从网络获取时间过长
//         */
//        mViewModel.getAvatar().observe(ChangeInfoActivity.this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if (mAvatarUri == null)
//                {
//                    return;
//                }
//                commonBindingAdapter.LoadCircleImage(mdataBinding.changeInfoAvatar,mAvatarUri.toString());
//                Log.d("tag", "onChanged: ");
//            }
//        });
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
                        //跳转到拍照页面
                            Intent intent = new Intent(ChangeInfoActivity.this, CameraActivity.class);
                            cameraLauncher.launch(intent);
                        }

                        @Override
                        public void chooseSecond() {
                            imagePickLauncher.launch("image/*");//打开相册
                        }
                    });
                }
            }
        });
    }
}