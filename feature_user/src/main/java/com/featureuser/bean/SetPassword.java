package com.featureuser.bean;

public class SetPassword {
    private String newpassword;
    private String mobile;
    private String captcha;
    private String type;

    public SetPassword(String newpassword, String mobile, String captcha) {
        this.newpassword = newpassword;
        this.mobile = mobile;
        this.captcha = captcha;
        this.type = "mobile"; //固定值
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
