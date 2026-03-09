package com.featureuser.bean;

public class ChangeInfoBody {
    private String avatar;  //头像Uri
    private String username;  //用户真实姓名
    private String nickname;  //用户昵称
    private String bio;   //用户简介

    public ChangeInfoBody(String avatar, String username, String nickname, String bio) {
        this.avatar = avatar;
        this.username = username;
        this.nickname = nickname;
        this.bio = bio;
    }
}
