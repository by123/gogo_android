package com.scrat.gogo.module.me.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.scrat.gogo.R;
import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.databinding.ActivityProfileBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideCircleTransform;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.view.CropPhotoActivity;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.framework.view.SelectorPopupWindow;
import com.scrat.gogo.framework.view.SingleImgSelectorActivity;
import com.scrat.gogo.module.me.address.AddressActivity;
import com.scrat.gogo.module.me.nickname.NicknameActivity;

/**
 * Created by scrat on 2017/11/19.
 */

public class ProfileActivity extends BaseActivity implements ProfileContract.View {
    private ActivityProfileBinding binding;
    private ProfileContract.Presenter presenter;
    private GlideRequests glideRequests;
    private RequestOptions options;
    private IosDialog logoutDialog;
    private SelectorPopupWindow sexDialog;
    private static final int UPDATE_NICKNAME = 1;
    private static final int SELECT_IMG = 2;
    private static final int CROP_IMG = 3;

    public static void show(Context context) {
        Intent i = new Intent(context, ProfileActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "ProfileActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.topBar.subject.setText("个人信息");
        glideRequests = GlideApp.with(this);
        options = new RequestOptions()
                .centerCrop()
                .transform(new GlideCircleTransform());

        new ProfilePresenter(this);
        presenter.loadUserInfo();

        logoutDialog = new IosDialog(this)
                .setTitle("退出提示")
                .setContent("是否现在退出账号？")
                .setNegative("取消")
                .setPositive("退出", view -> presenter.logout());

        sexDialog = new SelectorPopupWindow(this, s -> {
            if ("男".equals(s)) {
                presenter.updateGenderToMale();
                return;
            }

            if ("女".equals(s)) {
                presenter.updateGenderToFemale();
            }
        }, "男", "女");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case UPDATE_NICKNAME:
                presenter.loadUserInfo();
                break;
            case SELECT_IMG:
                Uri uri = SingleImgSelectorActivity.parseUri(data);
                if (uri != null) {
                    CropPhotoActivity.show(this, CROP_IMG, uri, 1, 1);
                }
                break;
            case CROP_IMG:
                String cropPath = CropPhotoActivity.parsePath(data);
                if (!TextUtils.isEmpty(cropPath)) {
                    presenter.updateAvatar(cropPath);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (sexDialog.isShowing()) {
            sexDialog.dismiss();
        }
        if (logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingUserInfo() {

    }

    @Override
    public void showLoadUserInfoError(String e) {
        showMessage(e);
    }

    @Override
    public void showUserInfo(UserInfo info) {
        binding.nickname.setText(info.getUsername());
        binding.gender.setText(getGenderStr(info.getGender()));
        glideRequests.load(info.getAvatar()).apply(options).into(binding.avatar);
    }

    @Override
    public void showLogoutSuccess() {
        toast("退出成功");
        finish();
    }

    @Override
    public void showProfileUpdating() {

    }

    @Override
    public void showProfileUpdateError(String e) {

    }

    @Override
    public void showProfileUpdateSuccess(UserInfo info) {
        toast("更新成功");
        showUserInfo(info);
    }

    private String getGenderStr(String gender) {
        if ("male".equals(gender)) {
            return "男";
        }

        if ("female".equals(gender)) {
            return "女";
        }

        return "未知";
    }

    public void logout(View view) {
        logoutDialog.show(view);
    }

    public void selectGender(View view) {
        sexDialog.show(view);
    }

    public void updateNickname(View view) {
        NicknameActivity.show(this, UPDATE_NICKNAME);
    }

    public void selectAvatar(View view) {
        SingleImgSelectorActivity.show(this, SELECT_IMG);
    }

    public void address(View view) {
        AddressActivity.show(this);
    }
}
