package com.example.video_data.bean.themPlayListBean;

import java.util.List;

public class ResThemePlayList {

    /**
     * title : 「日系广告」拿捏东亚人的 8 大潜规则
     * type : text
     * desc : 日系广告往往像一个短篇电影，整体基调偏向治愈与温馨，注重与观众的情感共鸣，用细腻的情感表达打动人心。开眼为您精选了 8 支日系广告，在温柔的广告里，观看动人的故事。
     * lists : [{"type":"video","id":129,"title":"三得利插画感广告，满屏都是治愈色","play_url":"http://t-cdn.kaiyanapp.com/1681829377291_8fd4cbcd.mp4","duration":"01:50","cover":"http://ali-img.kaiyanapp.com/cover/20230417/20cbaf4f63cdaf864e132771de812c24.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","tag":"日系"},{"type":"video","id":132,"title":"日本分屏温情广告：60 秒的亲子温暖","play_url":"http://t-cdn.kaiyanapp.com/1681392720741_b840cc78.mp4","duration":"01:00","cover":"http://ali-img.kaiyanapp.com/cover/20230412/8e4bdd503ec5655de179cc8e5c7d7e22.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","tag":"日系"}]
     */

    private String title;
    private String type;
    private String desc;
    private List<ListsBean> lists;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * type : video
         * id : 129
         * title : 三得利插画感广告，满屏都是治愈色
         * play_url : http://t-cdn.kaiyanapp.com/1681829377291_8fd4cbcd.mp4
         * duration : 01:50
         * cover : http://ali-img.kaiyanapp.com/cover/20230417/20cbaf4f63cdaf864e132771de812c24.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
         * tag : 日系
         */

        private String type;
        private int id;
        private String title;
        private String play_url;
        private String duration;
        private String cover;
        private String tag;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
