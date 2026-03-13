package com.featureuser.api;


import com.featureuser.bean.ChangeInfoBody;
import com.featureuser.bean.EntryLogin;
import com.featureuser.bean.EntrySendCode;
import com.featureuser.bean.ResLoadFile;
import com.featureuser.bean.ResLogin;
import com.featureuser.bean.SetPassword;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.featureuser.netAddress.NetAddress;
import com.network.bean.ResBase;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApiService {

    @POST(NetAddress.User_SEND_CODE_ADDRESS)
    Call<ResBase<Object>> sendCode(@Body EntrySendCode sendCode);

    @POST(NetAddress.User_QUIT_LOGIN)
    Call<ResBase<Object>> quitLogin(@Header("token") String token);

    @POST(NetAddress.User_LOGIN_ADDRESS)
    Call<ResBase<ResLogin>> LoginByMobile(@Body EntryLogin login);

    @GET(NetAddress.User_GET_USERINFO)
    Call<ResBase<ResUserData<ResUserInfo>>> getUserInfo(@Query("user_id") String user_id, @Query("type") String type);

    @POST(NetAddress.User_SET_PASSWORD)
    Call<ResBase<Object>> changePassword(@Body SetPassword params, @Header("token") String token);

    @POST(NetAddress.User_CHANGE_INFO)
    Call<ResBase<Object>> changeUserInfo(@Header("token") String token, @Body ChangeInfoBody body);

    @Multipart//表明要上传文件
    @POST(NetAddress.User_UPLOAD_FILE)                                   //MultipartBody是要上传的文件
    Call<ResBase<ResLoadFile>> uploadFile(@Header("token") String token, @Part MultipartBody.Part file);


}
