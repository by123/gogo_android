package com.scrat.gogo.module.login;

import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/3.
 */

public interface LoginContract {
    interface Presenter {
        void wxLogin(String code);

        void sendSmsCode(String tel);

        void telLogin(String tel, String code);

        void release();
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showLogin();

        void showLoginSuccess();

        void showLoginFail(String msg);

        void showSendingSms(int second);

        void showSendSmsSuccess();

        void showTelError(String msg);

        void showSmsCodeError(String msg);
    }
}
