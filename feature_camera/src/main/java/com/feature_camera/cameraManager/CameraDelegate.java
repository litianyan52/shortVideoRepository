package com.feature_camera.cameraManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.Surface;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraDelegate {

    public interface Callback {
        void onPermissionDenied();
        void onCameraReady(@NonNull ImageCapture imageCapture);
        void onError(@NonNull Throwable throwable);
    }

    public interface PhotoCallback {
        void onPhotoSaved(@NonNull Uri uri, @NonNull File photoFile);
        void onError(@NonNull Throwable throwable);
    }

    private final LifecycleOwner lifecycleOwner;
    private final PreviewView previewView;
    private final Callback callback;
    private final ActivityResultLauncher<String> permissionLauncher;

    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;

    public CameraDelegate(
            @NonNull ActivityResultCaller caller,
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull PreviewView previewView,
            @NonNull Callback callback
    ) {
        this.lifecycleOwner = lifecycleOwner;
        this.previewView = previewView;
        this.callback = callback;

        this.permissionLauncher = caller.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        callback.onPermissionDenied();
                    }
                }
        );
    }

    public void start() {
        if (hasCameraPermission()) {
            openCamera();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    public boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                previewView.getContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public ImageCapture getImageCapture() {
        return imageCapture;
    }

    public void takePhoto(@NonNull Context context, @NonNull PhotoCallback photoCallback) {
        if (imageCapture == null) {
            photoCallback.onError(new IllegalStateException("ImageCapture 未初始化，请先调用 start()"));
            return;
        }

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            photoCallback.onError(new IllegalStateException("外部存储当前不可写: " + state));
            return;
        }

        File baseDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (baseDir == null) {
            photoCallback.onError(new IllegalStateException("获取外部存储目录失败"));
            return;
        }

        File cameraDir = new File(baseDir, "FeatureCamera");
        if (!cameraDir.exists() && !cameraDir.mkdirs()) {
            photoCallback.onError(new IllegalStateException("创建图片目录失败: " + cameraDir.getAbsolutePath()));
            return;
        }

        File photoFile = new File(cameraDir, "IMG_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputFileOptions,
                ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = outputFileResults.getSavedUri();
                        if (savedUri == null) {
                            savedUri = Uri.fromFile(photoFile);
                        }
                        photoCallback.onPhotoSaved(savedUri, photoFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        photoCallback.onError(exception);
                    }
                }
        );
    }

    public void release() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    private void openCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(previewView.getContext());

        future.addListener(() -> {
            try {
                cameraProvider = future.get();

                int rotation = Surface.ROTATION_0;
                if (previewView.getDisplay() != null) {
                    rotation = previewView.getDisplay().getRotation();
                }

                Preview preview = new Preview.Builder().build();
                imageCapture = new ImageCapture.Builder()
                        .setTargetRotation(rotation)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                );

                callback.onCameraReady(imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                callback.onError(e);
            } catch (Exception e) {
                callback.onError(e);
            }
        }, ContextCompat.getMainExecutor(previewView.getContext()));
    }
}