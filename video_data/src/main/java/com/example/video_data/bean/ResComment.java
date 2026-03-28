package com.example.video_data.bean;

import androidx.annotation.Nullable;

public class ResComment {

    /**
     * id : 299
     * user_id : 69
     * type : archives
     * aid : 128
     * pid : 0
     * content : 厉害呀！！我居然是1楼沙发，哈哈哈~
     * comments : 0
     * ip : 117.69.22.65
     * useragent : PostmanRuntime-ApipostRuntime/1.1.0
     * subscribe : 0
     * createtime : 1761377594
     * updatetime : 1761377594
     * deletetime : null
     * status : normal
     * user : {"id":69,"nickname":"173****6169","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","url":"/u/69"}
     * create_date : 0秒前
     */

    private int id;
    private int user_id;
    private String type;
    private int aid;
    private int pid;
    private String content;
    private int comments;
    private String ip;
    private String useragent;
    private int subscribe;
    private int createtime;
    private int updatetime;
    private Object deletetime;
    private String status;
    private UserBean user;
    private String create_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public Object getDeletetime() {
        return deletetime;
    }

    public void setDeletetime(Object deletetime) {
        this.deletetime = deletetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public static class UserBean {
        /**
         * id : 69
         * nickname : 173****6169
         * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
         * url : /u/69
         */

        private int id;
        private String nickname;
        private String avatar;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ResComment) {
            ResComment resComment = (ResComment) obj;
            if (resComment.getUser().getNickname()==this.getUser().getNickname())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
}
