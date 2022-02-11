package com.scrat.gogo.module.race.betting.list;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scrat.gogo.R;
import com.scrat.gogo.data.model.BettingItem;
import com.scrat.gogo.databinding.PopupBettingBinding;

import java.util.Locale;

/**
 * Created by scrat on 2017/11/18.
 */

public class BettingPopupWindow extends PopupWindow implements View.OnClickListener {
    private PopupBettingBinding binding;
    private TextView selectedTextview;
    private int coin;
    private BettingItem item;
    private OnBettingClickListener listener;

    public interface OnBettingClickListener {
        void onBettingClick(BettingItem item, int coin);
    }

    public BettingPopupWindow(Context context, OnBettingClickListener listener) {
        super(context);
        this.listener = listener;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = PopupBettingBinding.inflate(inflater, null);
        setContentView(binding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);

        binding.content.setOnClickListener(this);
        binding.coin100.setOnClickListener(this);
        binding.coin500.setOnClickListener(this);
        binding.coin1000.setOnClickListener(this);
        binding.coin5000.setOnClickListener(this);
        binding.bettingBtn.setOnClickListener(this);

        coin = 100;
        selectItem(binding.coin100);

        setFocusable(true);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.content) {
            dismiss();
            return;
        }

        if (view == binding.bettingBtn) {
            listener.onBettingClick(item, coin);
            return;
        }

        if (view == binding.coin100) {
            coin = 100;
            selectItem(binding.coin100);
            return;
        }

        if (view == binding.coin500) {
            coin = 500;
            selectItem(binding.coin500);
            return;
        }

        if (view == binding.coin1000) {
            coin = 1000;
            selectItem(binding.coin1000);
            return;
        }

        if (view == binding.coin5000) {
            coin = 5000;
            selectItem(binding.coin5000);
            return;
        }

    }

    private void selectItem(TextView textView) {
        if (selectedTextview == textView) {
            return;
        }
        if (selectedTextview != null) {
            selectedTextview.setBackgroundResource(R.drawable.bg_border_c01_3);
        }
        textView.setBackgroundResource(R.drawable.bg_c01_3dp);
        selectedTextview = textView;
        refreshTip();
    }

    public void showUserCoinLoading() {
        binding.userCoin.setText("当前可用竞猜币：计算中");
    }

    public void showUserCoin(long coin) {
        String coinStr = String.format("当前可用竞猜币：%s", coin);
        binding.userCoin.setText(coinStr);
    }

    public void showBettingItem(View view, BettingItem item) {
        this.item = item;
        binding.title.setText(item.getTitle());
        refreshTip();
        showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void refreshTip() {
        float odds = 1f;
        if (item != null) {
            odds = item.getOdds();
        }
        String tip = String.format(Locale.getDefault(), "猜中预计可得 %.0f 竞猜币", odds * coin);
        binding.tip.setText(tip);
    }
}
