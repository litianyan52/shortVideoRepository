package com.featureuser.userConfig;

public class UserConfig {


    /**
     * 决定在同一个WebAgreement页面显示那个协议
     */
    public static class AgreementType {
        public static final String KEY_AGREEMENT_TYPE = "key_agreement_type";
        public static final int USER_AGREEMENT = 0; //用户协议
        public static final int SUM_PRIVATE = 1;//隐私概要
        public static final int PRIVATE_POLICY = 2;//隐私政策
        public static final int USER_INFO_COLLECT = 3;//个人信息收集清单

    }
}
