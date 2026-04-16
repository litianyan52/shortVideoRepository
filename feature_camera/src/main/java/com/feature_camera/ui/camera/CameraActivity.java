package com.feature_camera.ui.camera;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.feature_camera.R;
import com.feature_camera.cameraManager.CameraDelegate;
import com.feature_camera.databinding.ActivityCameraBinding;
import com.libase.base.BaseActivity;
import com.libase.base.BaseViewmodel;
import com.libase.config.ArouterPath;

import java.io.File;

@Route(path = ArouterPath.Camera.ACTIVITY_CAMERA)
public class CameraActivity extends BaseActivity<BaseViewmodel, ActivityCameraBinding> {

    private CameraDelegate cameraDelegate;

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
        cameraDelegate = new CameraDelegate(
                this,
                this,
                mdataBinding.cameraPreviewView,
                new CameraDelegate.Callback() {
                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(CameraActivity.this, "未获得相机权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCameraReady(@NonNull ImageCapture imageCapture) {
                        // 相机已经准备好
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Toast.makeText(CameraActivity.this, "相机启动失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraDelegate.start();

        // 假设你的布局里有一个拍照按钮 btnTakePhoto
        mdataBinding.btnTakePhoto.setOnClickListener(v -> {
            cameraDelegate.takePhoto(CameraActivity.this, new CameraDelegate.PhotoCallback() {
                @Override
                public void onPhotoSaved(@NonNull Uri uri, @NonNull File photoFile) {
                    Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.setData(uri);
                    resultIntent.putExtra("photo_uri", uri.toString());
                    resultIntent.putExtra("photo_path", photoFile.getAbsolutePath());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onError(@NonNull Throwable throwable) {
                    Toast.makeText(CameraActivity.this, "拍照失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        if (cameraDelegate != null) {
            cameraDelegate.release();
        }
        super.onDestroy();
    }
}