package com.example.video_data.bean;

import java.util.List;

public class ResVideoAllInfo {

        /**
         * archivesInfo : {"id":129,"user_id":1,"channel_id":25,"channel_ids":"","model_id":1,"special_ids":"","title":"三得利插画感广告，满屏都是治愈色","flag":"","style":"","image":"http://ali-img.kaiyanapp.com/cover/20230417/20cbaf4f63cdaf864e132771de812c24.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","images":"","video_file":"http://t-cdn.kaiyanapp.com/1681829377291_8fd4cbcd.mp4","seotitle":"","keywords":"","description":"日本三得利创意广告，插画感十足，满屏的治愈色彩都要溢出来了。这支短片将真人与动画相结合，清新治愈的日系色彩，温暖而又美好，简直太有创意了。From サントリー公式チャンネル「SUNTORY」","tags":"","price":"0.00","outlink":"","views":819,"comments":49,"likes":7,"dislikes":0,"collection":2,"diyname":"","isguest":1,"iscomment":1,"createtime":1732527854,"updatetime":1746108310,"publishtime":1746021900,"memo":"","duration":"01:50","content":"","author":"","islike":0,"iscollection":0,"user":{"id":1,"nickname":"admin","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"","url":"/u/1"},"channel":{"id":25,"parent_id":24,"name":"广告","image":"https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient","diyname":"guanggao","items":3,"url":"/cms/guanggao.html","fullurl":"https://titok.fzqq.fun/cms/guanggao.html"},"url":"/cms/guanggao/129.html","fullurl":"https://titok.fzqq.fun/cms/guanggao/129.html","likeratio":"100","taglist":[],"create_date":"11月前","ispaid":true}
         * commentList : [{"id":289,"user_id":64,"pid":0,"content":"666","comments":0,"createtime":1758907346,"user":{"id":64,"nickname":"李斌","avatar":"https://titok.fzqq.fun傻子","bio":"傻子","email":"","url":"/u/64"},"create_date":"3周前"},{"id":221,"user_id":7,"pid":0,"content":"ddfghh","comments":0,"createtime":1753002792,"user":{"id":7,"nickname":"11111uuuuu","avatar":"https://titok.fzqq.fun/uploads/20250907/085ae5ed305b42cff5eae3826667ca42.jpg","bio":"hhhhh","email":"","url":"/u/7"},"create_date":"3月前"},{"id":176,"user_id":17,"pid":0,"content":"hello world","comments":0,"createtime":1750521230,"user":{"id":17,"nickname":"182****7508","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"","email":"","url":"/u/17"},"create_date":"4月前"},{"id":150,"user_id":37,"pid":0,"content":"又骗你的，其实我真的是！！","comments":0,"createtime":1749114893,"user":{"id":37,"nickname":"哈哈哈哈哈","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"天天开心","email":"","url":"/u/37"},"create_date":"4月前"},{"id":149,"user_id":37,"pid":0,"content":"骗你的，其实我不是恋爱脑，哈哈哈哈","comments":0,"createtime":1749114802,"user":{"id":37,"nickname":"哈哈哈哈哈","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"天天开心","email":"","url":"/u/37"},"create_date":"4月前"},{"id":148,"user_id":37,"pid":0,"content":"恋爱脑有救吗，我是人机在刷评论","comments":0,"createtime":1749114716,"user":{"id":37,"nickname":"哈哈哈哈哈","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"天天开心","email":"","url":"/u/37"},"create_date":"4月前"},{"id":128,"user_id":16,"pid":0,"content":"111111","comments":0,"createtime":1748345942,"user":{"id":16,"nickname":"32","avatar":"https://titok.fzqq.fun/uploads/20250528/c430acdbf9ba6be0f48a8888f384b012.jpg","bio":"999999","email":"","url":"/u/16"},"create_date":"4月前"},{"id":100,"user_id":6,"pid":0,"content":"点点滴滴","comments":0,"createtime":1747148554,"user":{"id":6,"nickname":"186i肉肉肉肉","avatar":"https://titok.fzqq.fun/assets/img/qrcode.png","bio":"弹弹堂发广告trr家家户户86有","email":"123@qq.com","url":"/u/6"},"create_date":"5月前"},{"id":89,"user_id":30,"pid":0,"content":"11111","comments":0,"createtime":1746095111,"user":{"id":30,"nickname":"199****0687","avatar":"https://titok.fzqq.fun/uploads/20250511/8deda77ddc6c1ff4bd1d78433563aa80.jpeg","bio":"哈哈哈哈","email":"","url":"/u/30"},"create_date":"5月前"},{"id":85,"user_id":30,"pid":0,"content":"111","comments":0,"createtime":1746094956,"user":{"id":30,"nickname":"199****0687","avatar":"https://titok.fzqq.fun/uploads/20250511/8deda77ddc6c1ff4bd1d78433563aa80.jpeg","bio":"哈哈哈哈","email":"","url":"/u/30"},"create_date":"5月前"}]
         * __token__ : e7559102241fe2bf8f16988e16946fd7
         */

        private ArchivesInfo archivesInfo;
        private String __token__;
        private List<CommentListBean> commentList;

        public ArchivesInfo getArchivesInfo() {
            return archivesInfo;
        }

        public void setArchivesInfo(ArchivesInfo archivesInfo) {
            this.archivesInfo = archivesInfo;
        }

        public String get__token__() {
            return __token__;
        }

        public void set__token__(String __token__) {
            this.__token__ = __token__;
        }

        public List<CommentListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListBean> commentList) {
            this.commentList = commentList;
        }



        public static class CommentListBean {
            /**
             * id : 289
             * user_id : 64
             * pid : 0
             * content : 666
             * comments : 0
             * createtime : 1758907346
             * user : {"id":64,"nickname":"李斌","avatar":"https://titok.fzqq.fun傻子","bio":"傻子","email":"","url":"/u/64"}
             * create_date : 3周前
             */

            private int id;
            private int user_id;
            private int pid;
            private String content;
            private int comments;
            private int createtime;
            private UserBeanX user;
            private String create_date;

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

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getComments() {
                return comments;
            }

            public void setComments(int comments) {
                this.comments = comments;
            }

            public int getCreatetime() {
                return createtime;
            }

            public void setCreatetime(int createtime) {
                this.createtime = createtime;
            }

            public UserBeanX getUser() {
                return user;
            }

            public void setUser(UserBeanX user) {
                this.user = user;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public static class UserBeanX {
                /**
                 * id : 64
                 * nickname : 李斌
                 * avatar : https://titok.fzqq.fun傻子
                 * bio : 傻子
                 * email :
                 * url : /u/64
                 */

                private int id;
                private String nickname;
                private String avatar;
                private String bio;
                private String email;
                private String url;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getBio() {
                    return bio;
                }

                public void setBio(String bio) {
                    this.bio = bio;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

}
