package com.scrat.gogo.module.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.scrat.gogo.MainActivity;
import com.scrat.gogo.R;
import com.scrat.gogo.databinding.ActivityLoginBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by scrat on 2017/10/31.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    public static void show(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private ActivityLoginBinding binding;
    private LoginContract.Presenter presenter;

    @NonNull
    @Override
    protected String getActivityName() {
        return "LoginActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        WXEntryActivity.initHandler(new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                hideLoggingIn();
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        SendAuth.Resp resp = ((SendAuth.Resp) baseResp);
                        presenter.wxLogin(resp.code);
                        break;
                    default:
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        break;
                }
            }
        });

        new LoginPresenter(this);
        binding.code.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                    telLogin(binding.tel);
                    break;
            }
            return true;
        });

    }

    @Override
    protected void onDestroy() {
        presenter.release();
        WXEntryActivity.releaseHandler();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                MainActivity.redirect(this);
                finish();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendSmsCode(View view) {
        presenter.sendSmsCode(binding.tel.getText().toString());
    }

    public void telLogin(View view) {
        presenter.telLogin(binding.tel.getText().toString(), binding.code.getText().toString());
    }

    public void wxLogin(View view) {
        showLoggingIn();
        WXEntryActivity.login();
    }

    public void navigateToDisclaimer(View view) {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLogin() {
        showLoggingIn();
    }

    @Override
    public void showLoginSuccess() {
        hideLoggingIn();
        toast("登录成功");
        MainActivity.redirect(this);
        finish();
    }

    @Override
    public void showLoginFail(String msg) {
        showMessage(msg);
        hideLoggingIn();
    }

    @Override
    public void showSendingSms(int second) {
        if (second <= 0) {
            binding.sendSmsBtn.setText("获取验证码");
            binding.sendSmsBtn.setTextColor(ContextCompat.getColor(this, R.color.c08_text));
            return;
        }
        String tip = String.format("稍后重试(%s)", second);
        binding.sendSmsBtn.setText(tip);
        binding.sendSmsBtn.setTextColor(ContextCompat.getColor(this, R.color.c09_tips));
    }

    @Override
    public void showSendSmsSuccess() {
        toast("发送成功！");
        binding.code.requestFocus();
        binding.code.selectAll();
    }

    @Override
    public void showTelError(String msg) {
        binding.tel.requestFocus();
        binding.tel.setError(msg);
        hideLoggingIn();
    }

    @Override
    public void showSmsCodeError(String msg) {
        binding.code.requestFocus();
        binding.code.setError(msg);
        hideLoggingIn();
    }

    private void showLoggingIn() {
        hideSoftInput();
        binding.sr.startShimmerAnimation();
    }

    private void hideLoggingIn() {
        binding.sr.stopShimmerAnimation();
    }

}
