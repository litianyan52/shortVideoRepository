package com.libase.config;

public class ArouterPath {

    public static class Home {
        private static final String HOME = "/home";
        public static final String FRAGMENT_HOME = HOME + "/homeFragment";

    }

    public static class User {
        private static final String USER = "/user";
        public static final String FRAGMENT_USER = USER + "/userFragment";
        public static final String ACTIVITY_LOGIN = USER + "/loginActivity";
        public static final String ACTIVITY_WB_AGREEMENT = USER + "/agreementWebActivity";
        public static final String ACTIVITY_SETTING = USER + "/settingActivity";
        public static final String ACTIVITY_ACCOUNT = USER + "/accountActivity";
        public static final String ACTIVITY_PASSWORD = USER + "/passwordActivity";
        public static final String ACTIVITY_PUSH_SETTING = USER + "/pushSettingActivity";
        public static final String ACTIVITY_PLAY_SETTING = USER + "/playSettingActivity";

        public static final String ACTIVITY_PRI_SETTING = USER + "/priSettingActivity";
        public static final String ACTIVITY_ABOUT_US = USER + "/aboutUsActivity";

        public static final String ACTIVITY_CHANGE_INFO = USER + "/changeInfoActivity";


    }

    public static class Find {
        private static final String FIND = "/find";
        public static final String FRAGMENT_FIND = FIND + "/findFragment";
        public static final String ACTIVITY_CATEGORY_DETAIL = FIND + "/categoryDetailActivity";
        public static final String KEY_CATEGORY_DETAIL_DATA = FIND + "key_category_detail_data";
    }

    public static class Piazza {
        private static final String PIAZZA = "/piazza";
        public static final String FRAGMENT_PIAZZA = PIAZZA + "/piazzaFragment";
        public static final String ACTIVITY_IMAGES_SHOW = PIAZZA + "/imagesShowActivity";
        public static final String KEY_ACTIVITY_IMAGES_SHOW_DATA = PIAZZA + "/keyImagesShow";
        public static final String KEY_IMAGE_CONTENT = PIAZZA + "/keyImageContent";
        public static final String FRAGMENT_VIEW_PAGER_ITEM = PIAZZA + "/itemViewpagerFragment";
    }


    public static class Video {
        private static final String VIDEO = "/video";
        public static final int VIDEO_LIST_FRAGMENT_RECOMMEND = 0;
        public static final int VIDEO_LIST_FRAGMENT_DAILY = 1;
        public static final String VIDEO_LIST_FRAGMENT_TYPE_KEY = "video_list_key";
        public static final String KEY_VIDEO_ID = "video_id_key";
        public static final String VIDEO_STYLE_KEY = "video_id_key";
        public static final boolean VIDEO_STYLE_WHITE = true;
        public static final String FRAGMENT_VideoList = VIDEO + "/videoListFragment";
        public static final String VIDEO_LIST_FRAGMENT_COMMEND = VIDEO + "/commendFragment";
        public static final String VIDEO_LIST_FRAGMENT_INTRODUCTION = VIDEO + "/introductionFragment";
        public static final String ACTIVITY_MEDIA_PLAY = VIDEO + "/mediaPlayActivity";
        public static final String ACTIVITY_SEARCH_FUNCTION = VIDEO + "/searchFunctionActivity";
        public static final String FRAGMENT_CATEGORY_VIDEO_LIST = VIDEO + "/categoryVideoListFragment";
        public static final String KEY_CHANNEL_ID = VIDEO + "/key_channel_id";
        public static final String KEY_TYPE = VIDEO + "/key_type_id";
        public static final String ACTIVITY_MY_COLLECT = VIDEO + "/myCollectActivity";
        public static final String ACTIVITY_BROWSE_RECORD = VIDEO + "/recordActivity";
        public static final String ACTIVITY_THEME_LIST_PLAY = VIDEO + "/themeListPlayActivity";
    }

}
