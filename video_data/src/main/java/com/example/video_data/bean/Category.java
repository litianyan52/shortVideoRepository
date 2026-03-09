package com.example.video_data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {
        /**
         * id : 25
         * name : 广告
         * image : https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient
         * icon : http://ali-img.kaiyanapp.com/c13345e2c2e812ef4e179a4eac2b81f1.png
         * description : 为广告人的精彩创意点赞
         * url : /cms/25.html
         * fullurl : https://titok.fzqq.fun/cms/25.html
         */

        private int id;
        private String name;
        private String image;
        private String icon;
        private String description;
        private String url;
        private String fullurl;

    public Category() {
    }

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        icon = in.readString();
        description = in.readString();
        url = in.readString();
        fullurl = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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
    }
}
