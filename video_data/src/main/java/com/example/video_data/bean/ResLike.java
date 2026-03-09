package com.example.video_data.bean;

public class ResLike {

    /**
     * code : 1001
     * msg : 操作成功
     * likes : 8
     * dislikes : 0
     */

    private int code;
    private String msg;
    private int likes;
    private int dislikes;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
