package com.scrat.gogo.framework.richtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scrat.gogo.framework.glide.GlideApp;

/**
 * Created by scrat on 2017/11/12.
 */

public class GlideImgGetter implements Html.ImageGetter {
    private TextView textView;

    public GlideImgGetter(TextView textView) {
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String s) {
        if (s == null) {
            return null;
        }

        final UrlDrawable urlDrawable = new UrlDrawable();
        BitmapTarget target = new BitmapTarget(urlDrawable, textView);
        try {
            GlideApp.with(textView).asBitmap().load(s).into(target);
        } catch (Exception ignore) {
        }

        return urlDrawable;
    }

    private class BitmapTarget extends SimpleTarget<Bitmap> {
        private final UrlDrawable urlDrawable;
        private final TextView textView;
        private BitmapTarget(UrlDrawable urlDrawable, TextView textView) {
            this.urlDrawable = urlDrawable;
            this.textView = textView;
        }

        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            Drawable drawable = new BitmapDrawable(textView.getContext().getResources(), resource);
            int screenWidth = getScreenWidth(textView.getContext())
                    - textView.getPaddingRight()
                    - textView.getPaddingLeft();
            int drawableHeight = drawable.getIntrinsicHeight();
            int drawableWidth = drawable.getIntrinsicWidth();
            int targetHeight = drawableHeight * screenWidth / drawableWidth;
            Rect rect = new Rect(0, 0, screenWidth, targetHeight);
            drawable.setBounds(rect);
            urlDrawable.setBounds(rect);
            urlDrawable.setDrawable(drawable);
            textView.setText(textView.getText());
            textView.invalidate();
        }

        private int getScreenWidth(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        }

    }

    private static class UrlDrawable extends BitmapDrawable implements Drawable.Callback {
        private Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null)
                drawable.draw(canvas);
        }

        public Drawable getDrawable() {
            return drawable;
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            unscheduleSelf(what);
        }

        @Override
        public void invalidateDrawable(Drawable who) {
            invalidateSelf();
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}
