package com.example.video_data.bean;

import java.util.List;

public class ArchivesInfo {
        /**
         * id : 129
         * user_id : 1
         * channel_id : 25
         * channel_ids :
         * model_id : 1
         * special_ids :
         * title : 三得利插画感广告，满屏都是治愈色
         * flag :
         * style :
         * image : http://ali-img.kaiyanapp.com/cover/20230417/20cbaf4f63cdaf864e132771de812c24.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
         * images :
         * video_file : http://t-cdn.kaiyanapp.com/1681829377291_8fd4cbcd.mp4
         * seotitle :
         * keywords :
         * description : 日本三得利创意广告，插画感十足，满屏的治愈色彩都要溢出来了。这支短片将真人与动画相结合，清新治愈的日系色彩，温暖而又美好，简直太有创意了。From サントリー公式チャンネル「SUNTORY」
         * tags :
         * price : 0.00
         * outlink :
         * views : 819
         * comments : 49
         * likes : 7
         * dislikes : 0
         * collection : 2
         * diyname :
         * isguest : 1
         * iscomment : 1
         * createtime : 1732527854
         * updatetime : 1746108310
         * publishtime : 1746021900
         * memo :
         * duration : 01:50
         * content :
         * author :
         * islike : 0
         * iscollection : 0
         * user : {"id":1,"nickname":"admin","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"","url":"/u/1"}
         * channel : {"id":25,"parent_id":24,"name":"广告","image":"https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient","diyname":"guanggao","items":3,"url":"/cms/guanggao.html","fullurl":"https://titok.fzqq.fun/cms/guanggao.html"}
         * url : /cms/guanggao/129.html
         * fullurl : https://titok.fzqq.fun/cms/guanggao/129.html
         * likeratio : 100
         * taglist : []
         * create_date : 11月前
         * ispaid : true
         */

        private int id;
        private int user_id;
        private int channel_id;
        private String channel_ids;
        private int model_id;
        private String special_ids;
        private String title;
        private String flag;
        private String style;
        private String image;
        private String images;
        private String video_file;
        private String seotitle;
        private String keywords;
        private String description;
        private String tags;
        private String price;
        private String outlink;
        private int views;
        private int comments;
        private int likes;
        private int dislikes;
        private int collection;
        private String diyname;
        private int isguest;
        private int iscomment;
        private int createtime;
        private int updatetime;
        private int publishtime;
        private String memo;
        private String duration;
        private String content;
        private String author;
        private int islike;
        private int iscollection;
        private ResVideoUserInfo user;
        private ResChannel channel;
        private String url;
        private String fullurl;
        private String likeratio;
        private String create_date;
        private boolean ispaid;
        private List<?> taglist;

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

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public String getChannel_ids() {
            return channel_ids;
        }

        public void setChannel_ids(String channel_ids) {
            this.channel_ids = channel_ids;
        }

        public int getModel_id() {
            return model_id;
        }

        public void setModel_id(int model_id) {
            this.model_id = model_id;
        }

        public String getSpecial_ids() {
            return special_ids;
        }

        public void setSpecial_ids(String special_ids) {
            this.special_ids = special_ids;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getVideo_file() {
            return video_file;
        }

        public void setVideo_file(String video_file) {
            this.video_file = video_file;
        }

        public String getSeotitle() {
            return seotitle;
        }

        public void setSeotitle(String seotitle) {
            this.seotitle = seotitle;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOutlink() {
            return outlink;
        }

        public void setOutlink(String outlink) {
            this.outlink = outlink;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
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

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public String getDiyname() {
            return diyname;
        }

        public void setDiyname(String diyname) {
            this.diyname = diyname;
        }

        public int getIsguest() {
            return isguest;
        }

        public void setIsguest(int isguest) {
            this.isguest = isguest;
        }

        public int getIscomment() {
            return iscomment;
        }

        public void setIscomment(int iscomment) {
            this.iscomment = iscomment;
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

        public int getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(int publishtime) {
            this.publishtime = publishtime;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
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

        public ResVideoUserInfo getUser() {
            return user;
        }

        public void setUser(ResVideoUserInfo user) {
            this.user = user;
        }

        public ResChannel getChannel() {
            return channel;
        }

        public void setChannel(ResChannel channel) {
            this.channel = channel;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFullurl() {
            return fullurl;
        }

        public void setFullurl(String fullurl) {
            this.fullurl = fullurl;
        }

        public String getLikeratio() {
            return likeratio;
        }

        public void setLikeratio(String likeratio) {
            this.likeratio = likeratio;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public boolean isIspaid() {
            return ispaid;
        }

        public void setIspaid(boolean ispaid) {
            this.ispaid = ispaid;
        }

        public List<?> getTaglist() {
            return taglist;
        }

        public void setTaglist(List<?> taglist) {
            this.taglist = taglist;
        }





}
