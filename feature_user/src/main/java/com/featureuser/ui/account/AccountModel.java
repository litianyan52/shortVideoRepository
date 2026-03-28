package com.featureuser.ui.account;

import com.libase.manager.UserManager;

public class AccountModel {

    public boolean isLogin()
    {
        return UserManager.getInstance().isLogin();
    }

    /**
     * 获取电话号码
     * @return
     */
    public String getUserMobile() {
        if (isLogin())
        {
            String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
            StringBuilder builder =new StringBuilder();
            builder.append(mobile.substring(0,3));
            builder.append("****");//给号码做个隐藏处理
            builder.append(mobile.substring(7));
            String newMobile = builder.toString();
            return  newMobile;
        }
        else
        {
            return null;
        }
    }
}
