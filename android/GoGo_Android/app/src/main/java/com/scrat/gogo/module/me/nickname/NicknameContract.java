package com.scrat.gogo.module.me.nickname;

import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/20.
 */

public interface NicknameContract {
    interface Presenter {
        void updateNickname(String nickname);

        void loadNickname();
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showNickname(String nickname);

        void showNicknameUpdating();

        void showNicknameUpdateSuccess();

        void showNicknameUpdateFail(String e);
    }
}
