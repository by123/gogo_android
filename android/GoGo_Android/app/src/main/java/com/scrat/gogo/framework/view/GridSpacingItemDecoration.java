package com.scrat.gogo.framework.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by scrat on 16/8/23.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private int bottomSpacing;
    private int topSpacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(
            int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    public GridSpacingItemDecoration(
            int spanCount, int spacing, boolean includeEdge, int topSpacing, int bottomSpacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.topSpacing = topSpacing;
        this.bottomSpacing = bottomSpacing;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
        if (position < spanCount) {
            outRect.top = topSpacing;
        }
        int totalCount = parent.getAdapter().getItemCount();
        int tmp = totalCount % spanCount;
        if (tmp == 0) {
            tmp = spanCount;
        }
        int pos = totalCount - tmp - 1;
        if (position > pos) {
            outRect.bottom = bottomSpacing;
        }
    }
}
