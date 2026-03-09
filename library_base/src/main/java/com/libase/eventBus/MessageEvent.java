package com.libase.eventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.EventListener;

public class MessageEvent {
    /**
     * 登录事件
     */
    public static class LoginEvent
    {
        private boolean isLogin;

        public LoginEvent(boolean isLogin) {
            this.isLogin = isLogin;
        }

        public boolean isLogin()
        {
            return isLogin;
        }

        /**
         * 将登陆事件的发送封装起来,调用这个方法即可发送登录事件
         * @param isLogin
         */
        public static void postSticky(boolean isLogin)
        {
            LoginEvent loginEvent = new LoginEvent(isLogin);
            EventBus.getDefault().postSticky(loginEvent); //发送粘性事件
        }
    }
}
