package com.scrat.gogo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.scrat.gogo.framework.util.L;
import com.scrat.gogo.module.login.LoginActivity;

/**
 * Created by scrat on 2017/6/26.
 */

public class LoginReceiver extends BroadcastReceiver {
    private static final String TYPE = "type";
    private static final int TYPE_NAVIGATE = 1;
    private static final String LOGIN_RECEIVER = "com.scrat.gogo.receiver.login";

    public static void sendLoginReceiver(Context context) {
        Intent i = new Intent(LOGIN_RECEIVER);
        i.putExtra(TYPE, TYPE_NAVIGATE);
        context.sendBroadcast(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        L.d("login %s", intent.getAction());
        int type = intent.getIntExtra(TYPE, TYPE_NAVIGATE);
        switch (type) {
            case TYPE_NAVIGATE:
                LoginActivity.show(context);
                break;
        }
    }
}
