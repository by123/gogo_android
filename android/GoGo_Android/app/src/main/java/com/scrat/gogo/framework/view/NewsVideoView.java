package com.scrat.gogo.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by scrat on 2017/11/28.
 */

public class NewsVideoView extends VideoView {
    public NewsVideoView(Context context) {
        super(context);
    }

    public NewsVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
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
