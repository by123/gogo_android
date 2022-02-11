package com.scrat.gogo;

import android.app.Application;

import com.scrat.gogo.data.local.Preferences;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by scrat on 2017/11/1.
 */

public class GoGoApp extends Application {

    private static final String WX_APP_ID = "wx3349545f4d083130";
    public static IWXAPI WX_API;

    @Override
    public void onCreate() {
        super.onCreate();

        Preferences.getInstance().init(getApplicationContext());

        WX_API = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        WX_API.registerApp(WX_APP_ID);

        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
        }
    }
}
