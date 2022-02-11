package com.scrat.gogo.framework.view;

import android.content.Context;
import android.view.View;

import com.scrat.gogo.module.login.LoginActivity;

/**
 * Created by scrat on 2017/11/21.
 */

public class LoginDialog {
    public static IosDialog build(Context context) {
        return new IosDialog(context)
                .setTitle("登录提示")
                .setContent("请先登录")
                .setNegative("看看其他的")
                .setPositive("马上登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginActivity.show(view.getContext());
                    }
                });
    }
}
