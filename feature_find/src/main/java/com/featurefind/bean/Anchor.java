package com.featurefind.bean;

public class Anchor {
        /**
         * id : 1
         * title : 「日系广告」拿捏东亚人的 8 大潜规则
         * type : image
         * image : http://ali-img.kaiyanapp.com/404fcf7d65dc4172aa3167607cfca2a4.jpeg?image_process=image/auto-orient,1/resize,w_600/format,webp/interlace,1/quality,q_80
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
