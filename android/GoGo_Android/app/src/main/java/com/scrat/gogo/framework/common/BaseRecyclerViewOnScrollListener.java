package com.scrat.gogo.framework.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by scrat on 2017/7/4.
 */

public class BaseRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean loading;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private LoadMoreListener loadMoreListener;

    public BaseRecyclerViewOnScrollListener(
            @NonNull GridLayoutManager layoutManager,
            @NonNull LoadMoreListener loadMoreListener) {
        this.gridLayoutManager = layoutManager;
        this.loadMoreListener = loadMoreListener;
    }

    public BaseRecyclerViewOnScrollListener(
            @NonNull LinearLayoutManager linearLayoutManager,
            @NonNull LoadMoreListener loadMoreListener) {
        this.linearLayoutManager = linearLayoutManager;
        this.loadMoreListener = loadMoreListener;
    }

    protected void onScrollUp() {

    }

    protected void onScrollDown() {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
            onScrollUp();
        } else if (dy < 0) {
            onScrollDown();
        }

        if (dy <= 0) {
            return;
        }

        if (loading) {
            return;
        }

        int visibleItemCount = getChildCount();
        int totalItemCount = getItemCount();
        int pastVisibleItems = findFirstVisibleItemPosition();

        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 3) {
            loading = true;
            loadMoreListener.onLoadMore();
        }
    }

    private int getChildCount() {
        if (gridLayoutManager != null) {
            return gridLayoutManager.getChildCount();
        }

        return linearLayoutManager.getChildCount();
    }

    private int getItemCount() {
        if (gridLayoutManager != null) {
            return gridLayoutManager.getItemCount();
        }

        return linearLayoutManager.getItemCount();
    }

    private int findFirstVisibleItemPosition() {
        if (gridLayoutManager != null) {
            return gridLayoutManager.findFirstVisibleItemPosition();
        }

        return linearLayoutManager.findFirstVisibleItemPosition();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
