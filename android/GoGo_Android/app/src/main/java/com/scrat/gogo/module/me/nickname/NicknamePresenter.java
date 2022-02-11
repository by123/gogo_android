package com.scrat.gogo.module.me.nickname;

import android.text.TextUtils;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.UserInfo;

/**
 * Created by scrat on 2017/11/20.
 */

public class NicknamePresenter implements NicknameContract.Presenter {
    private NicknameContract.View view;

    public NicknamePresenter(NicknameContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void updateNickname(final String nickname) {
        if (TextUtils.isEmpty(nickname)) {
//            view.showNicknameUpdateFail("请输入昵称");
            return;
        }
        view.showNicknameUpdating();
        DataRepository.getInstance().getApi().getUserInfo(
                new DefaultLoadObjCallback<UserInfo, Res.UserInfoRes>(Res.UserInfoRes.class) {
                    @Override
                    protected void onSuccess(UserInfo info) {
                        DataRepository.getInstance().getApi().updateUserInfo(
                                nickname,
                                info.getAvatar(),
                                info.getGender(),
                                new DefaultLoadObjCallback<String, Res.DefaultStrRes>(Res.DefaultStrRes.class) {
                                    @Override
                                    protected void onSuccess(String s) {
                                        view.showNicknameUpdateSuccess();
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        view.showNicknameUpdateFail(e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showNicknameUpdateFail(e.getMessage());
                    }
                });
    }

    @Override
    public void loadNickname() {
        DataRepository.getInstance().getApi().getUserInfo(
                new DefaultLoadObjCallback<UserInfo, Res.UserInfoRes>(Res.UserInfoRes.class) {
                    @Override
                    protected void onSuccess(UserInfo info) {
                        view.showNickname(info.getUsername());
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}
