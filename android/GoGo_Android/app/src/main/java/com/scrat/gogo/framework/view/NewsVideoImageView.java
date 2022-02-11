package com.scrat.gogo.framework.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by scrat on 16/9/9.
 */
public class NewsVideoImageView extends AppCompatImageView {
    public NewsVideoImageView(Context context) {
        super(context);
    }

    public NewsVideoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsVideoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width * 9 / 16;
        setMeasuredDimension(width, height);
    }
}
