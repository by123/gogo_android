package com.scrat.gogo.framework.common;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.scrat.gogo.framework.util.L;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/4/27.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private List<PopupWindow> popupWindowList;

    @NonNull
    protected abstract String getActivityName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(getActivityName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getActivityName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivityName());
        MobclickAgent.onPause(this);
    }

    protected void showMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        View view = getCurrentFocus();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            toast(message);
        }
    }

    protected void showMessage(int resStr) {
        View view = getCurrentFocus();
        if (view != null) {
            Snackbar.make(view, resStr, Snackbar.LENGTH_LONG).show();
        } else {
            toast(resStr);
        }
    }

    protected void toast(int resStr) {
        Toast.makeText(getApplication(), resStr, Toast.LENGTH_LONG).show();
    }

    protected void toast(CharSequence msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    public void back(View v) {
        onBackPressed();
    }

    protected void requestFocus(EditText editText) {
        editText.requestFocus();
        showSoftInput();
        editText.selectAll();
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                // ignore
            }
        }
        return result;
    }

    private boolean isFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return true;
            }
        }
        if (isFinishing()) {
            return true;
        }
        return false;
    }

    protected synchronized void registerPopupWindow(PopupWindow window) {
        if (window == null) {
            return;
        }
        if (popupWindowList == null) {
            popupWindowList = new ArrayList<>();
        }
        for (PopupWindow curr : popupWindowList) {
            if (curr == window) {
                return;
            }
        }
        popupWindowList.add(window);
    }

    @Override
    protected void onDestroy() {
        if (popupWindowList != null) {
            for (PopupWindow curr : popupWindowList) {
                if (curr != null && curr.isShowing()) {
                    curr.dismiss();
                }
            }
        }
        super.onDestroy();
    }

    protected void hideSoftInput() {
        if (getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    protected void showSoftInput() {
        if (getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(getCurrentFocus(), 0);
    }
}
