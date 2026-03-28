package com.example.video_data.bean;

public class ResVideo {
    /**
     * id : 128
     * title : 日本经典漫画，给粉丝写了一份告白信
     * image : http://ali-img.kaiyanapp.com/cover/20230420/9fdbffbfbd5b05e42b5e4cff1df8af17.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
     * video_file : http://t-cdn.kaiyanapp.com/1682091366375_be60972b.mp4
     * description : 各种日漫经典人物聚集在一起给粉丝写了一封告白信，致阅读正版漫画的每一位粉丝，并发起抵制盗版漫画的活动。ABJ 是一家综合法人协会，致力于根除盗版漫画并增加正确阅读漫画的人数。From STOP!海賊版
     * duration : 03:12
     * user_id : 1
     * channel_id : 25
     * price : 0.00
     * likes : 6
     * views : 916
     * author : admin
     * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
     * tag : 广告
     */

    private int id;
    private String title;
    private String image;
    private String video_file;
    private String description;
    private String duration;
    private int user_id;
    private int channel_id;
    private String price;
    private int likes;
    private int views;
    private String author;
    private String avatar;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
