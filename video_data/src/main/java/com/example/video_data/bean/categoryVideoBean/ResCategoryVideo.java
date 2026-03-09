package com.example.video_data.bean.categoryVideoBean;

import java.util.Objects;

public class ResCategoryVideo {

    /**
     * id : 145
     * title : 全民灌肠计划，这支广告玩的有点大
     * image : http://ali-img.kaiyanapp.com/035d50b81f26550ad6b6a984cfbf3cb4.jpeg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
     * video_file : http://t-cdn.kaiyanapp.com/1682609003870_e0a45647.mp4
     * description : Travis Barker 这位全身遍布纹身的帅酷鼓手凭借着自己职业的态度、技术和天分，已成为摇滚舞台上最具影响力的鼓手之一，被誉为世界级朋克鼓王。在这支短片中，他说出了成功的秘诀「灌肠」，「全民灌肠计划」正在进行中，你还在等什么呢。From Liquid Death
     * comments : 7
     * likes : 2
     * collection : 0
     * createtime : 1732528037
     * user_id : 1
     * price : 0.00
     * views : 30
     * author : admin
     * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
     * islike : 0
     * iscollection : 0
     */

    private int id;
    private String title;
    private String image;
    private String video_file;
    private String description;
    private String comments;
    private String likes;
    private String collection;
    private int createtime;
    private int user_id;
    private String price;
    private int views;
    private String author;
    private String avatar;
    private int islike;
    private int iscollection;

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
        if (description == null || description.isEmpty()) {
            description = "这条视频没有简介哦";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public int getIscollection() {
        return iscollection;
    }

    public void setIscollection(int iscollection) {
        this.iscollection = iscollection;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ResCategoryVideo) {
            ResCategoryVideo resCategoryVideo = (ResCategoryVideo) o;
            if (this.id == resCategoryVideo.id && this.author.equals(resCategoryVideo.author)) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author);
    }
}
