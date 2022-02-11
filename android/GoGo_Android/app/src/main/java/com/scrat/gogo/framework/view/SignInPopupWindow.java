package com.scrat.gogo.framework.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.PopupWindow;

import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.databinding.PopupSignInDialogBinding;

/**
 * Created by scrat on 2017/12/17.
 */

public class SignInPopupWindow extends PopupWindow {
    private PopupSignInDialogBinding binding;
//    private boolean hasSign;
//    private long oldCoin;
    private OnConfirmListener listener;

    public SignInPopupWindow(Context context, OnConfirmListener listener) {
        super(context);
        binding = PopupSignInDialogBinding.inflate(LayoutInflater.from(context), null);
        setContentView(binding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        setFocusable(true);

        binding.container.setOnClickListener(v -> dismiss());
        binding.dialog.setOnClickListener(v -> {});
        this.listener = listener;
        binding.positive.setOnClickListener(v -> {
            listener.onConfirm();
        });
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public SignInPopupWindow show(View view) {
        showAtLocation(view, Gravity.CENTER, 0, 0);
        return this;
    }

    public SignInPopupWindow refresh(Res.SignInInfo signInInfo) {
//        oldCoin = signInInfo.getCoin();
        binding.coin.setText(String.valueOf(signInInfo.getCoin()));
        String msg = String.format("已连续领取 %s 天", signInInfo.getSignIn().getDay());
        binding.days.setText(msg);
//        hasSign = signInInfo.getSignIn().isHasSign();
        if (signInInfo.getSignIn().isHasSign()) {
            binding.positive.setText("关 闭");
            binding.positive.setOnClickListener(v -> {dismiss();});
        } else {
            binding.positive.setText("领 取");
            binding.positive.setOnClickListener(v -> {listener.onConfirm();});
        }
        return this;
    }

    public void showSignInSuccess(String coin) {
        Animation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(1000L);
        binding.currAddCoin.setText(coin);
        binding.currAddCoin.startAnimation(alphaAnimation);
    }
}
