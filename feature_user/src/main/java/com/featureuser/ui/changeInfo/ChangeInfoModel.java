package com.featureuser.ui.changeInfo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.featureuser.api.UserApiService;
import com.featureuser.api.UserApiServiceProvider;
import com.featureuser.bean.ChangeInfoBody;
import com.featureuser.bean.ResLoadFile;
import com.libase.base.BaseApplication;
import com.libase.base.IRequestCallBack;
import com.libase.manager.UserManager;
import com.network.ApiCall;
import com.network.RetrofitProvider;
import com.network.bean.ResBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ChangeInfoModel {
    private UserManager manager;

    public ChangeInfoModel() {
        this.manager = UserManager.getInstance();
    }

    public String getUserAvatar() {
        return manager.getUserInfo().getUser().getAvatar();
    }

    public String getUserBio() {
        return manager.getUserInfo().getUser().getBio();
    }

    public String getUserNickName() {
        return manager.getUserInfo().getUser().getNickname();
    }


    /**
     * 发起修改用户信息的请求
     *
     * @param avatar
     * @param username
     * @param nickname
     * @param bio
     * @param requestCallBack
     */
    public void changeUserInfo(String avatar, String username, String nickname, String bio, IRequestCallBack<ResBase<Object>> requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();
        Call<ResBase<Object>> call = apiService.changeUserInfo(UserManager.getInstance().getUserToken(),
                new ChangeInfoBody(avatar, username, nickname, bio));
        UserManager instance = UserManager.getInstance();
        instance.ChangeUserInfo(avatar, username, nickname, bio);
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<Object>>() {
            @Override
            public void GetLiseSuccess(ResBase<Object> result) {
                requestCallBack.RequestSuccess(result);
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });
    }


    /**
     * 上传文件
     * @param uri 文件的资源标识符
     */
    public void uploadFile(Uri uri,IRequestCallBack<ResLoadFile> requestCallBack) {
        UserApiService apiService = UserApiServiceProvider.provider();

        Call<ResBase<ResLoadFile>> call = apiService.uploadFile(UserManager.getInstance().getUserToken(),
                createMultipartBody(uri));
        ApiCall.enqueueAllowDataNull(call, new ApiCall.ApiCallback<ResBase<ResLoadFile>>() {
            @Override
            public void GetLiseSuccess(ResBase<ResLoadFile> result) {
                requestCallBack.RequestSuccess(result.getData());
            }

            @Override
            public void GetLiseFailed(int errorCode, String message) {
                requestCallBack.RequestFailed(errorCode, message);
            }
        });

    }


    /**
     * 创建请求体
     * @param uri
     * @return
     */
    public MultipartBody.Part createMultipartBody(Uri uri) {
        ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();

        String tempMimeType = contentResolver.getType(uri);
        if (tempMimeType == null) {
            String fileName = getFileNameFromUri(uri);
            String extension = null;
            int dot = fileName.lastIndexOf('.');
            if (dot >= 0 && dot < fileName.length() - 1) {
                extension = fileName.substring(dot + 1).toLowerCase();
            }

            if ("jpg".equals(extension) || "jpeg".equals(extension)) {
                tempMimeType = "image/jpeg";
            } else if ("png".equals(extension)) {
                tempMimeType = "image/png";
            } else if ("mp4".equals(extension)) {
                tempMimeType = "video/mp4";
            } else {
                tempMimeType = "application/octet-stream";
            }
        }

        final String mimeType = tempMimeType;
        final String fileName = getFileNameFromUri(uri);

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse(mimeType);
            }

            @Override
            public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
                try (InputStream inputStream = contentResolver.openInputStream(uri)) {
                    if (inputStream == null) {
                        throw new IOException("无法打开输入流: " + uri);
                    }

                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bufferedSink.write(buffer, 0, read);
                    }
                }
            }
        };

        return MultipartBody.Part.createFormData("file", fileName, requestBody);
    }


    /**
     * 根据Scheme不同用不同的方式获取文件名
     * @param uri
     * @return
     */
    public String getFileNameFromUri(Uri uri) {
        if (uri == null) {
            return "upload_file";
        }

        String scheme = uri.getScheme();
        String fileName = null;

        // 1. file:// 直接从路径取文件名
        if ("file".equalsIgnoreCase(scheme)) {
            String path = uri.getPath();
            if (path != null) {
                fileName = new File(path).getName();
            }
        }

        // 2. content:// 通过 ContentResolver 查询 DISPLAY_NAME
        if (fileName == null && "content".equalsIgnoreCase(scheme)) {
            ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        // 3. 兜底：再尝试取 path 最后一段
        if (fileName == null) {
            String path = uri.getPath();
            if (path != null) {
                int lastSlash = path.lastIndexOf('/');
                if (lastSlash >= 0 && lastSlash < path.length() - 1) {
                    fileName = path.substring(lastSlash + 1);
                }
            }
        }

        // 4. 最终兜底
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "upload_file";
        }

        return fileName;
    }


}
