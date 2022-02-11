package com.scrat.gogo.module.me.profile;

import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/19.
 */

public interface ProfileContract {
    interface Presenter {
        void loadUserInfo();

        void logout();

        void updateGenderToMale();

        void updateGenderToFemale();

        void updateAvatar(String imgPath);
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showLoadingUserInfo();

        void showLoadUserInfoError(String e);

        void showUserInfo(UserInfo info);

        void showLogoutSuccess();

        void showProfileUpdating();

        void showProfileUpdateError(String e);

        void showProfileUpdateSuccess(UserInfo info);
    }
}
