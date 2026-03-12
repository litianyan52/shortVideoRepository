package com.feature_camera.ui.camera;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.feature_camera.R;
import com.feature_camera.cameraManager.CameraManager;
import com.feature_camera.databinding.ActivityCameraBinding;
import com.libase.base.BaseActivity;
import com.libase.base.BaseViewmodel;
import com.libase.config.ArouterPath;


@Route(path = ArouterPath.Camera.ACTIVITY_CAMERA)
public class CameraActivity extends BaseActivity<BaseViewmodel, ActivityCameraBinding> {

    private int RequestPermissionCode = 200;
    private CameraManager manager;
    private PreviewView previewView;

    @Override
    public BaseViewmodel getViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public int getViewModelId() {
        return 0;
    }

    @Override
    public void initView() {
        manager = CameraManager.getInstance();
        previewView = mdataBinding.cameraPreviewView;
        manager.startCamera(CameraActivity.this,RequestPermissionCode, previewView);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        if (requestCode != RequestPermissionCode) {
            return;
        }

        if (manager.checkCameraPermissions(CameraActivity.this)){
            manager.startCamera(CameraActivity.this,RequestPermissionCode,previewView); //获得权限重新启动相机
        }
        else {
            Toast.makeText(CameraActivity.this,"未获得相机权限",Toast.LENGTH_SHORT);
            finish(); //关闭相机页面
        }

    }
}