package com.scrat.gogo.module.login;

import android.text.TextUtils;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.TokenInfo;
import com.scrat.gogo.framework.util.L;
import com.scrat.gogo.framework.util.MainHandlerUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

/**
 * Created by scrat on 2017/11/3.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private long lastSendSmsTs;
    private static final long SMS_INTERVAL = 60 * 1000L;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void wxLogin(String code) {
        view.showLogin();
        DataRepository.getInstance().getApi().wxLogin(
                code, new DefaultLoadObjCallback<TokenInfo, Res.TokenRes>(Res.TokenRes.class) {
                    @Override
                    protected void onSuccess(TokenInfo tokenInfo) {
                        Preferences.getInstance().setUid(tokenInfo.getUid());
                        Preferences.getInstance().setAccessToken(tokenInfo.getAccessToken());
                        Preferences.getInstance().setRefreshToken(tokenInfo.getRefreshToken());
                        MobclickAgent.onProfileSignIn("wx", tokenInfo.getUid());
                        view.showLoginSuccess();
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoginFail(e.getMessage());
                    }

                });
    }

    @Override
    public void sendSmsCode(String tel) {
        long nowTs = new Date().getTime();
        if (nowTs - lastSendSmsTs <= SMS_INTERVAL) {
            return;
        }
        if (TextUtils.isEmpty(tel) || tel.length() < 11) {
            view.showTelError("请输入有效的手机号码");
            return;
        }
        if (smsInterval) {
            return;
        }
        smsInterval = true;
        lastSendSmsTs = nowTs;
        startInterval(true);
        DataRepository.getInstance().getApi().sendSms(
                tel, new DefaultLoadObjCallback<String, Res.DefaultStrRes>(Res.DefaultStrRes.class) {
                    @Override
                    protected void onSuccess(String s) {
                        view.showSendSmsSuccess();
                    }

                    @Override
                    public void onError(Exception e) {
                        L.e(e);
                        stopInterval();
                        view.showTelError(e.getMessage());
                    }
                });
    }

    private volatile int second;
    private boolean smsInterval;

    private synchronized void startInterval(boolean refresh) {
        if (!smsInterval) {
            return;
        }
        if (refresh) {
            second = 60;
        }
        if (second <= 0) {
            stopInterval();
            return;
        }
        view.showSendingSms(second);
        MainHandlerUtil.getMainHandler().postDelayed(() -> {
            second--;
            startInterval(false);
        }, 1000L);
    }

    private synchronized void stopInterval() {
        smsInterval = false;
        view.showSendingSms(0);
    }

    @Override
    public void telLogin(String tel, String code) {
        if (TextUtils.isEmpty(tel) || tel.length() < 11) {
            view.showTelError("请输入有效的手机号码");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            view.showSmsCodeError("请输入有效的验证码");
            return;
        }

        view.showLogin();
        DataRepository.getInstance().getApi().smsLogin(
                tel, code, new DefaultLoadObjCallback<TokenInfo, Res.TokenRes>(Res.TokenRes.class) {
                    @Override
                    protected void onSuccess(TokenInfo tokenInfo) {
                        Preferences.getInstance().setUid(tokenInfo.getUid());
                        Preferences.getInstance().setRefreshToken(tokenInfo.getRefreshToken());
                        Preferences.getInstance().setAccessToken(tokenInfo.getAccessToken());
                        MobclickAgent.onProfileSignIn("tel", tokenInfo.getUid());
                        view.showLoginSuccess();
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoginFail(e.getMessage());
                    }
                });
    }

    @Override
    public void release() {
        stopInterval();
    }
}
