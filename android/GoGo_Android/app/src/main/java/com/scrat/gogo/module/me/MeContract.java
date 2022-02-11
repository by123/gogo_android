package com.scrat.gogo.module.me;

import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/14.
 */

public interface MeContract {
    interface Presenter {
        void loadUserInfo();
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showNotLogin();

        void showUserInfo(UserInfo info);
    }
}
