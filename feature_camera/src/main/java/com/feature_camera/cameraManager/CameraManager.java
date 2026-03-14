package com.feature_camera.cameraManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraManager {
    private static volatile CameraManager manager;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private int PermissionRequestCode;
    private ImageCapture imageCapture;

    public static CameraManager getInstance() {
        if (manager == null) {
            synchronized (CameraManager.class) {
                if (manager == null) {
                    manager = new CameraManager();
                }
            }
        }

        return manager;
    }


    public void startCamera(Context context, Activity activity, int RequestPermissionCode, PreviewView previewView, LifecycleOwner lifecycleOwner) {
        if (checkCameraPermissions(context)) {


            ListenableFuture<ProcessCameraProvider> instance = ProcessCameraProvider.getInstance(context);
            instance.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = instance.get();
                        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                        imageCapture = new ImageCapture.Builder().build();
                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(previewView.getSurfaceProvider());
                        cameraProvider.unbindAll();
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, ContextCompat.getMainExecutor(context));


        } else {
            PermissionRequestCode = RequestPermissionCode;
            requestPermissions(context, activity);
        }
    }


    public boolean checkCameraPermissions(Context context) {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }


    public void requestPermissions(Context context, Activity activity) {
        ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PermissionRequestCode);
    }

}
