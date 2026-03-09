package com.libase.manager;

import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.libase.base.BaseApplication;
import com.libase.bean.ResUserData;
import com.libase.bean.ResUserInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserManager {
    private static UserManager instance;
    private SharedPreferences mSP;
    private static final String SP_NAME = "userSP";
    private static final String KEY_TOKEN = "user_token_key";
    private static final String KEY_USER_ID = "user_id_key";
    private static final String KEY_AVATAR = "user_avatar_key";
    private static final String KEY_BIO = "user_bio_key";
    private static final String KEY_NICKNAME = "user_nickname_key";
    private static final String KEY_STATUS = "user_status_key";
    private static final String KEY_USERNAME = "user_username_key";


    private static final String KEY_FANS = "user_fans_key";
    private static final String KEY_FOLLOW = "user_follow_key";
    private static final String KEY_MEDAL = "user_medal_key";


    private UserManager() {
        try {
            //设置加密算法提高安全性
            String masterAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            mSP = EncryptedSharedPreferences.create(SP_NAME, masterAlias, BaseApplication.getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取示例
     * 单例模式
     *
     * @return
     */
    public static UserManager getInstance() {
        synchronized (UserManager.class)  //防止在new的时候,另一个线程进来,然后又进入if中
        {
            if (instance == null) {
                instance = new UserManager();
                return instance;
            }
        }
        return instance;
    }

    /**
     * 存储用户token
     *
     * @param token
     */
    public void saveUserToken(String token) {
        mSP.edit().putString(KEY_TOKEN, token).apply(); //
    }

    /**
     * 获取用户token
     *
     * @return
     */

    public String getUserToken() {
        String userToken = mSP.getString(KEY_TOKEN, null);
        return userToken;
    }


    /**
     * 存储用户信息
     *
     * @param userData
     */
    public void saveUserInfo(ResUserData<ResUserInfo> userData) {
        ResUserInfo userInfo = userData.getUser();
        mSP.edit().putInt(KEY_FANS, userData.getFans())
                .putInt(KEY_FOLLOW, userData.getFollow())
                .putInt(KEY_MEDAL, userData.getMedal())
                .putString(KEY_AVATAR, userInfo.getAvatar())
                .putString(KEY_BIO, userInfo.getBio())
                .putString(KEY_USER_ID, userInfo.getId())
                .putString(KEY_NICKNAME, userInfo.getNickname())
                .putString(KEY_STATUS, userInfo.getStatus())
                .putString(KEY_USERNAME, userInfo.getUsername())
                .apply();
        //   userInfo.getUrl();
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public ResUserData<ResUserInfo> getUserInfo() {
        ResUserData<ResUserInfo> resUserData = new ResUserData<>();
        resUserData.setFans(mSP.getInt(KEY_FANS, 0));
        resUserData.setFollow(mSP.getInt(KEY_FOLLOW, 0));
        resUserData.setMedal(mSP.getInt(KEY_MEDAL, 0));
        ResUserInfo info = new ResUserInfo();
        info.setAvatar(mSP.getString(KEY_AVATAR, null));
        info.setBio(mSP.getString(KEY_BIO, null));
        info.setId(mSP.getString(KEY_USER_ID, null));
        info.setNickname(mSP.getString(KEY_NICKNAME, null));
        info.setStatus(mSP.getString(KEY_STATUS, null));
        info.setUsername(mSP.getString(KEY_USERNAME, null));
        resUserData.setUser(info);
        return resUserData;
    }


    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        String token = getUserToken();
        boolean isLogin = token != null && !token.isEmpty();
        return isLogin;
    }

    /**
     * 退出登录,因为用的KEY是写死的,所以为了其他用户登录,目前用户退出登录后要把数据清除
     */
    public void ExitLogin() {
        mSP.edit().remove(KEY_FANS)
                .remove(KEY_FOLLOW)
                .remove(KEY_MEDAL)
                .remove(KEY_AVATAR)
                .remove(KEY_BIO)
                .remove(KEY_USER_ID)
                .remove(KEY_NICKNAME)
                .remove(KEY_STATUS)
                .remove(KEY_USERNAME)
                .remove(KEY_TOKEN)  //记得token也要移除
                .apply();
    }
}
