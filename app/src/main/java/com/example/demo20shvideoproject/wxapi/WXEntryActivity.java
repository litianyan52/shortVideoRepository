//package com.example.demo20shvideoproject.wxapi;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//
//import com.example.WxApplication;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
//
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        IWXAPI api = WxApplication.getApi();
//        api.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//
//    }
//}
