package com.scrat.gogo.module.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scrat.gogo.MainActivity;
import com.scrat.gogo.framework.common.BaseActivity;

/**
 * Created by scrat on 2017/11/22.
 */

public class SplashActivity extends BaseActivity {
    @NonNull
    @Override
    protected String getActivityName() {
        return "SplashActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.redirect(this);
        finish();
    }
}
