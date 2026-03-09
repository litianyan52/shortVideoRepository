package com.featurepiazza.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class ResPiazza {
    private String type;
    private List<ResPiazzaDetail> lists;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ResPiazzaDetail> getLists() {
        return lists;
    }

    public void setLists(List<ResPiazzaDetail> lists) {
        this.lists = lists;
    }



    public static class ResPiazzaDetail implements Parcelable
    {
        
        /**
         * id : 26
         * name : 运动
         * image : http://ali-img.kaiyanapp.com/481837cc0b1c52a3a87d6156d52cfdfa.jpeg?image_process=image/auto-orient
         * icon : http://ali-img.kaiyanapp.com/be368b691fec8cffd21112e460621f33.png
         * description : 冲浪、滑板、健身、跑酷，我过着停不下来的生活
         * url : /cms/26.html
         * fullurl : https://titok.fzqq.fun/cms/26.html
         */

        private int id;
        private String name;
        private String image;
        private String icon;
        private String description;
        private String url;
        private String fullurl;
        /**
         * 这里一个类两用了,上方字段放轮播图的数据,下方字段放图片浏览的数据,这么做的原因是lists字段名字不能重复,无法定义两个类型不同的lists字段
         * title : 和老孙一起去看青山流水
         * images : ["https://pic.rmb.bdstatic.com/bjh/240305/654445218841bf7a9cbd6b80a856cf1d2196.jpeg","http://ali-img.kaiyanapp.com/302209431/9c40f4d0392212e3cecb34b9e84aa595.png?image_process=image/auto-orient,1/resize,w_480/format,webp/interlace,1/quality,q_80","http://ali-img.kaiyanapp.com/302209431/9c40f4d0392212e3cecb34b9e84aa595.png?image_process=image/auto-orient,1/resize,w_480/format,webp/interlace,1/quality,q_80","http://ali-img.kaiyanapp.com/304398712/0-6c1f1e2417b947f8fed0e5b69e5cff52.jpeg?image_process=image/auto-orient,1/resize,w_480/format,webp/interlace,1/quality,q_80"]
         * author : 李白
         * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
         * cover : http://ali-img.kaiyanapp.com/302209431/9c40f4d0392212e3cecb34b9e84aa595.png?image_process=image/auto-orient,1/resize,w_480/format,webp/interlace,1/quality,q_80
         */

        private String title;
        private String author;
        private String avatar;
        private String cover;
        private List<String> images;



        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {

            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(image);
            dest.writeString(icon);
            dest.writeString(description);
            dest.writeString(url);
            dest.writeString(fullurl);
            dest.writeString(title);
            dest.writeString(author);
            dest.writeString(avatar);
            dest.writeString(cover);
            dest.writeStringList(images);
        }

        protected ResPiazzaDetail(Parcel in) {
            id = in.readInt();
            name = in.readString();
            image = in.readString();
            icon = in.readString();
            description = in.readString();
            url = in.readString();
            fullurl = in.readString();
            title = in.readString();
            author = in.readString();
            avatar = in.readString();
            cover = in.readString();
            images = in.createStringArrayList();
        }

        public static final Creator<ResPiazzaDetail> CREATOR = new Creator<ResPiazzaDetail>() {
            @Override
            public ResPiazzaDetail createFromParcel(Parcel in) {
                return new ResPiazzaDetail(in);
            }

            @Override
            public ResPiazzaDetail[] newArray(int size) {
                return new ResPiazzaDetail[size];
            }
        };
    }


}
