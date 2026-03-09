package com.featurefind.bean;

public class ResCategoryDataOne {

        /**
         * info : {"id":25,"image":"https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient","name":"广告","description":"为广告人的精彩创意点赞","people":18,"browse":3369,"collection":24,"likes":44}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 25
             * image : https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient
             * name : 广告
             * description : 为广告人的精彩创意点赞
             * people : 18
             * browse : 3369
             * collection : 24
             * likes : 44
             */

            private int id;
            private String image;
            private String name;
            private String description;
            private int people;
            private int browse;
            private int collection;
            private int likes;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getPeople() {
                return people;
            }

            public void setPeople(int people) {
                this.people = people;
            }

            public int getBrowse() {
                return browse;
            }

            public void setBrowse(int browse) {
                this.browse = browse;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }
        }

}
