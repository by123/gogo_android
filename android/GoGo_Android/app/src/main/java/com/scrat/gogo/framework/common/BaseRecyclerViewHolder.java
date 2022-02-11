package com.scrat.gogo.framework.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by scrat on 2017/4/27.
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;
    private View convertView;

    public static BaseRecyclerViewHolder newInstance(
            @LayoutRes int resource,
            @NonNull ViewGroup root) {
        return newInstance(resource, root, false);
    }

    public static BaseRecyclerViewHolder newInstance(
            @LayoutRes int resource,
            @NonNull ViewGroup root,
            boolean attachToRoot) {
        View v = LayoutInflater.from(root.getContext())
                .inflate(resource, root, attachToRoot);
        return new BaseRecyclerViewHolder(v);
    }

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        views = new SparseArray<>();
    }

    public static BaseRecyclerViewHolder newInstance(ViewGroup parent, int layoutId) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new BaseRecyclerViewHolder(v);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId) {
        return getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }

    protected Context getContext() {
        return convertView.getContext();
    }

    public View getRootView() {
        return convertView;
    }

    public BaseRecyclerViewHolder setText(int viewId, int resId) {
        TextView view = getView(viewId);
        view.setText(resId);
        return this;
    }

    public BaseRecyclerViewHolder setText(int viewId, CharSequence content) {
        TextView view = getView(viewId);
        view.setText(content);
        return this;
    }

    public BaseRecyclerViewHolder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColor(getContext(), colorId));
        return this;
    }

    public BaseRecyclerViewHolder setText(int viewId, CharSequence content, int maxLength) {
        if (content != null && content.length() > maxLength) {
            content = content.subSequence(0, maxLength) + "...";
        }

        TextView view = getView(viewId);
        view.setText(content);
        return this;
    }

    public BaseRecyclerViewHolder setVisibility(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseRecyclerViewHolder setVisibility(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public BaseRecyclerViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseRecyclerViewHolder setBackground(int viewId, Drawable background) {
        View view = getView(viewId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
        return this;
    }

    public BaseRecyclerViewHolder setImageDrawable(ImageView imageView, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(convertView.getContext(), drawableId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public BaseRecyclerViewHolder setBackground(int viewId, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(convertView.getContext(), drawableId);
        return setBackground(viewId, drawable);
    }

    public BaseRecyclerViewHolder setOnClickListener(View.OnClickListener l) {
        convertView.setOnClickListener(l);
        return this;
    }

    public BaseRecyclerViewHolder setOnLongClickListener(View.OnLongClickListener l) {
        convertView.setOnLongClickListener(l);
        return this;
    }

    public BaseRecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener l) {
        View view = getView(viewId);
        view.setOnClickListener(l);
        return this;
    }

    public BaseRecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener l) {
        View view = getView(viewId);
        view.setOnLongClickListener(l);
        return this;
    }

}
