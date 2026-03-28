package com.example;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WxShareManager {

    private volatile static WxShareManager mInstance;
    private WxShareManager(){

    }

    public static WxShareManager getInstance() {
        if (mInstance==null){
            synchronized (WxShareManager.class){
                if (mInstance == null){
                    mInstance = new WxShareManager();
                }
            }
        }

        return mInstance;
    }


    public void WxShareTextToFriend(String text){
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

//用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction =String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession ;
        IWXAPI api = WxApplication.getApi();
//调用api接口，发送数据到微信
        api.sendReq(req);
    }
}
