package com.scrat.gogo.module.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.scrat.gogo.R;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.UpdateInfo;
import com.scrat.gogo.databinding.ActivitySettingBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.module.about.AboutActivity;
import com.scrat.gogo.module.me.feedback.FeedbackActivity;
import com.scrat.gogo.module.update.UpdateHelper;

/**
 * Created by scrat on 2017/11/22.
 */

public class SettingActivity extends BaseActivity {
    private ActivitySettingBinding binding;
    private IosDialog updateDialog;

    public static void show(Context context) {
        Intent i = new Intent(context, SettingActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "SettingActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.topBar.subject.setText("设置");
        updateDialog = new IosDialog(this);
    }

    @Override
    protected void onDestroy() {
        if (updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        super.onDestroy();
    }

    public void feedback(View view) {
        FeedbackActivity.show(this);
    }

    public void about(View view) {
        AboutActivity.show(this);
    }

    public void update(final View view) {
        UpdateHelper.checkUpdate(this, true, new UpdateHelper.UpdateListener() {
            @Override
            public void update(boolean force, final UpdateInfo info) {
                updateDialog.setTitle(info.getTitle())
                        .setContent(info.getContent())
                        .setPositive("立即更新", view1 -> {
                            Preferences.getInstance().setLastCheckVerCode(info.getVer());
                            UpdateHelper.downloadApk(SettingActivity.this, info.getUrl());
                        });
                if (!force) {
                    updateDialog.setNegative("稍后再说", view2 -> Preferences.getInstance().setLastCheckVerCode(info.getVer()));
                }
                updateDialog.show(view);
            }

            @Override
            public void showNoNeedToUpdate() {
                toast("已经是最新版本");
            }
        });
    }
}
