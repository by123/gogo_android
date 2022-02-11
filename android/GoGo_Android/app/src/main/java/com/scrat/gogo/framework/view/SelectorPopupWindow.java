package com.scrat.gogo.framework.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scrat.gogo.R;
import com.scrat.gogo.databinding.PopupSelectorBinding;
import com.scrat.gogo.framework.common.BaseOnItemClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yixuanxuan on 2017/7/10.
 */

public class SelectorPopupWindow extends PopupWindow {

    private PopupSelectorBinding binding;
    private Map<String, TextView> map;

    public SelectorPopupWindow(Context context,
                               @NonNull final BaseOnItemClickListener<String> listener,
                               @NonNull String... items) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = PopupSelectorBinding.inflate(inflater, null);
        setContentView(binding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);

        binding.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        map = new HashMap<>(items.length);
        binding.contentList.removeAllViews();
        for (final String item : items) {
            View v = inflater.inflate(
                    R.layout.list_item_popup_selector, binding.contentList, false);
            TextView textView = (TextView) v.findViewById(R.id.title);
            textView.setText(item);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    listener.onItemClick(item);
                }
            });
            map.put(item, textView);
            binding.contentList.addView(v);
        }

        setFocusable(true);
    }

    public void setVisible(boolean visible, String... items) {
        for (String item : items) {
            TextView textView = map.get(item);
            if (textView != null) {
                textView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    public void setSelected(boolean selected, String... items) {
        for (String item : items) {
            TextView textView = map.get(item);
            if (textView != null) {
                int color = selected ? R.color.c01_blue : R.color.c07_bar;
                textView.setTextColor(ContextCompat.getColor(binding.list.getContext(), color));
            }
        }
    }

    public void show(View view) {
        showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
