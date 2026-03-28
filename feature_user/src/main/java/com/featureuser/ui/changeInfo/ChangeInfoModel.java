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
        String type  = contentResolver.getType(uri); //从uri中获取类型
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse(type);
            }

            @Override
            public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
                try (InputStream inputStream = contentResolver.openInputStream(uri)) {
                    byte buffer[] = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bufferedSink.write(buffer, 0, read);
                    }
                }
            }
        };

        MultipartBody.Part file = MultipartBody.Part.createFormData("file",
                getFileNameFromUri(uri), requestBody);

        return file;
    }


    /**
     * 根据Scheme不同用不同的方式获取文件名
     * @param uri
     * @return
     */
    public String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();

            try (Cursor cursor = contentResolver.query(uri, null, null, null, null);) {
                if (cursor != null && cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (uri.getScheme().equals("file") ) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName != null ? fileName : "unname_file";
    }


}
