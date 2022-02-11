package com.scrat.gogo.framework.common;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.scrat.gogo.framework.util.L;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by scrat on 2017/4/27.
 */

public abstract class BaseFragment extends Fragment {
    @NonNull
    protected abstract String getFragmentName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getFragmentName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getFragmentName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(getFragmentName());
    }

    protected void showMsg(CharSequence message) {
        showSnackbar(message);
    }

    protected void showSnackbar(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            L.w("message is empty");
            return;
        }
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            showToast(message);
        }
    }

    protected void showSnackbar(int resStr) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, resStr, Snackbar.LENGTH_LONG).show();
        } else {
            showToast(resStr);
        }
    }

    protected void showToast(int resStr) {
        if (getContext() == null) {
            L.e("getContext() is null");
            return;
        }

        Toast.makeText(getContext().getApplicationContext(), resStr, Toast.LENGTH_LONG).show();
    }

    protected void showToast(CharSequence msg) {
        if (getContext() == null) {
            L.e("getContext() is null");
            return;
        }

        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected boolean isFinish() {
        if (getView() == null) {
            return true;
        }

        if (getActivity() == null) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity().isDestroyed()) {
                return true;
            }
        }

        if (getActivity().isFinishing()) {
            return true;
        }

        return false;
    }

}
