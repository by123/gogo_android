package com.scrat.gogo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scrat.gogo.GoGoApp;
import com.scrat.gogo.framework.util.L;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by scrat on 2017/11/1.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static IWXAPIEventHandler handler;

    public static boolean login() {
        if (!GoGoApp.WX_API.isWXAppInstalled()) {
            L.e("wechat not install");
            return false;
        }

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        GoGoApp.WX_API.sendReq(req);
        return true;
    }

    public static void initHandler(IWXAPIEventHandler currHandler) {
        handler = currHandler;
    }

    public static void releaseHandler() {
        handler = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHandleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        setHandleIntent();
    }

    private void setHandleIntent() {
        try {
            GoGoApp.WX_API.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (handler != null) {
            handler.onReq(baseReq);
        }
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (handler != null) {
            handler.onResp(baseResp);
        }
        finish();
    }

}
