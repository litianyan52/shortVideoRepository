package com.libase.bean;

public class ResUserData<T> {

    /**
     * user : {"id":2,"nickname":"cydic","bio":"","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","status":"normal","username":"cydic","url":"/u/2"}
     * fans : 126
     * follow : 18
     * medal : 0
     */

    private T user;
    private int fans;
    private int follow;
    private int medal;

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getMedal() {
        return medal;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }


}
