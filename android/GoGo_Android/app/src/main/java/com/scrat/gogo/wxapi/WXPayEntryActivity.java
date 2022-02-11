package com.scrat.gogo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.scrat.gogo.data.model.WxPayInfo;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.util.L;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by scrat on 2017/7/20.
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String DATA = "data";
    private static final String RESULT = "result";

    public static void show(Activity activity, int requestCode, WxPayInfo info) {
        Intent i = new Intent(activity, WXPayEntryActivity.class);
        i.putExtra(DATA, info);
        activity.startActivityForResult(i, requestCode);
    }

    private IWXAPI api;

    @NonNull
    @Override
    protected String getActivityName() {
        return "WXPayEntryActivity";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WxPayInfo payInfo = (WxPayInfo) getIntent().getSerializableExtra(DATA);
        L.e(payInfo.toString());
        api = WXAPIFactory.createWXAPI(this, payInfo.getAppId());
        api.handleIntent(getIntent(), this);
        PayReq request = new PayReq();
        request.appId = payInfo.getAppId();
        request.partnerId = payInfo.getPartnerId();
        request.prepayId = payInfo.getPrepayId();
        request.packageValue = payInfo.getPkg();
        request.nonceStr = payInfo.getNonceStr();
        request.timeStamp = payInfo.getTimestamp();
        request.sign = payInfo.getSign();
        api.sendReq(request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        L.d("code=%s", resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent i = new Intent();
            i.putExtra(RESULT, resp.errCode == 0);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public static boolean paySuccess(Intent data) {
        return data.getBooleanExtra(RESULT, false);
    }
}
