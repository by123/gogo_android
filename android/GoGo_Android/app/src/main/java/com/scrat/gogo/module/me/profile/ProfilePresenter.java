package com.scrat.gogo.module.me.profile;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Api;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.Uptoken;
import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.framework.qiniu.DefaultUploadListener;
import com.scrat.gogo.framework.qiniu.QiniuUploadManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by scrat on 2017/11/19.
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadUserInfo() {
        view.showLoadingUserInfo();
        DataRepository.getInstance().getApi().getUserInfo(
                new DefaultLoadObjCallback<UserInfo, Res.UserInfoRes>(Res.UserInfoRes.class) {
                    @Override
                    protected void onSuccess(UserInfo info) {
                        view.showUserInfo(info);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadUserInfoError(e.getMessage());
                    }
                });
    }

    @Override
    public void logout() {
        String refreshToken = Preferences.getInstance().getRefreshToken();
        DataRepository.getInstance().getApi().logout(refreshToken);
        Preferences.getInstance().clearAuth();
        MobclickAgent.onProfileSignOff();
        view.showLogoutSuccess();
    }

    @Override
    public void updateGenderToMale() {
        updateUserInfo("male");
    }

    @Override
    public void updateGenderToFemale() {
        updateUserInfo("female");
    }

    @Override
    public void updateAvatar(final String imgPath) {
        view.showProfileUpdating();
        final Api api = DataRepository.getInstance().getApi();
        api.getQiniuUptoken(new DefaultLoadObjCallback<Uptoken, Res.UptokenRes>(Res.UptokenRes.class) {
            @Override
            protected void onSuccess(Uptoken uptoken) {
                QiniuUploadManager.getInstance().uploadImg(uptoken.getDomain(), uptoken.getToken(), imgPath, new DefaultUploadListener() {
                    @Override
                    public void onSuccess(String path, final String url) {
                        api.getUserInfo(new DefaultLoadObjCallback<UserInfo, Res.UserInfoRes>(Res.UserInfoRes.class) {
                            @Override
                            protected void onSuccess(final UserInfo info) {
                                api.updateUserInfo(info.getUsername(), url, info.getGender(), new DefaultLoadObjCallback<String, Res.DefaultStrRes>(Res.DefaultStrRes.class) {
                                    @Override
                                    protected void onSuccess(String s) {
                                        info.setAvatar(url);
                                        view.showUserInfo(info);
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        view.showProfileUpdateError(e.getMessage());
                                    }

                                });
                            }

                            @Override
                            public void onError(Exception e) {
                                view.showProfileUpdateError(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onFail(String path, String msg) {
                        view.showProfileUpdateError(msg);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                view.showProfileUpdateError(e.getMessage());
            }
        });
    }

    private void updateUserInfo(final String gender) {
        view.showProfileUpdating();
        DataRepository.getInstance().getApi().getUserInfo(
                new DefaultLoadObjCallback<UserInfo, Res.UserInfoRes>(Res.UserInfoRes.class) {
                    @Override
                    protected void onSuccess(final UserInfo info) {
                        DataRepository.getInstance().getApi()
                                .updateUserInfo(info.getUsername(),
                                        info.getAvatar(),
                                        gender,
                                        new DefaultLoadObjCallback<String, Res.DefaultStrRes>(Res.DefaultStrRes.class) {
                                            @Override
                                            protected void onSuccess(String s) {
                                                info.setGender(gender);
                                                view.showProfileUpdateSuccess(info);
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                view.showProfileUpdateError(e.getMessage());
                                            }
                                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showProfileUpdateError(e.getMessage());
                    }
                });
    }
}
