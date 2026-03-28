package com.featurefind.bean;

public class Topic {
        /**
         * id : 1
         * title : 冬日旅行收藏夹
         * type : image
         * image : https://static.thefair.net.cn/thefair_project/20241203/26acd56528959dc49cbae3b6332aa35c.jpg?x-image-process=image/auto-orient,1/resize,w_390/format,webp/interlace,1/quality,q_80
         */

        private int id;
        private String title;
        private String type;
        private String image;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
}
