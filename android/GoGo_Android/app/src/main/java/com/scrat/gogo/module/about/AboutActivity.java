package com.scrat.gogo.module.about;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scrat.gogo.R;
import com.scrat.gogo.databinding.ActivityAboutBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.util.Utils;

/**
 * Created by scrat on 2017/11/15.
 */

public class AboutActivity extends BaseActivity {
    public static void show(Context context) {
        Intent i = new Intent(context, AboutActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "AboutActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_about);
        binding.topBar.subject.setText("关于");
        binding.ver.setText(Utils.getVersionName(getApplicationContext()));
    }
}
