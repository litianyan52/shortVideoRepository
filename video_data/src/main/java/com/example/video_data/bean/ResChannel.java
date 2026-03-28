package com.example.video_data.bean;

public class ResChannel {

        /**
         * id : 25
         * parent_id : 24
         * name : 广告
         * image : https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient
         * diyname : guanggao
         * items : 3
         * url : /cms/guanggao.html
         * fullurl : https://titok.fzqq.fun/cms/guanggao.html
         */

        private int id;
        private int parent_id;
        private String name;
        private String image;
        private String diyname;
        private int items;
        private String url;
        private String fullurl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
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

        public String getDiyname() {
            return diyname;
        }

        public void setDiyname(String diyname) {
            this.diyname = diyname;
        }

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
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

}
