package com.featureuser.api;


import com.featureuser.bean.ChangeInfoBody;
import com.featureuser.bean.EntryLogin;
import com.featureuser.bean.EntrySendCode;
import com.featureuser.bean.ResLogin;
import com.featureuser.bean.SetPassword;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;
import com.featureuser.netAddress.NetAddress;
import com.network.bean.ResBase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
}
