package com.scrat.gogo.framework.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/4/27.
 */

public abstract class BaseRecyclerViewAdapter<Item>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    protected static final int VIEW_TYPE_CONTENT = 1;
    private static final int VIEW_TYPE_FOOTER = 2;


    protected abstract void onBindItemViewHolder(
            BaseRecyclerViewHolder holder, int position, Item item);

    protected abstract BaseRecyclerViewHolder onCreateRecycleItemView(
            ViewGroup parent, int viewType);

    protected List<Item> list;
    protected boolean hasFooter;
    protected boolean hasHeader;
    private View headerView;
    private View footerView;

    public BaseRecyclerViewAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (!hasHeader && !hasFooter) // 无Header, 无Footer
            return VIEW_TYPE_CONTENT;

        if (hasHeader && !hasFooter) {// only Header
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            }
            return VIEW_TYPE_CONTENT;
        }

        if (!hasHeader && hasFooter) { // only Footer
            if (list.size() == position) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_CONTENT;
        }

        // 有Footer 有Header
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        if (list.size() + 1 == position) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER)
            return new BaseRecyclerViewHolder(footerView);

        if (viewType == VIEW_TYPE_HEADER)
            return new BaseRecyclerViewHolder(headerView);

        return onCreateRecycleItemView(parent, viewType);
    }

    public void setHeader(View view) {
        hasHeader = true;
        headerView = view;
        notifyDataSetChanged();
    }

    public void removeHeader() {
        hasHeader = false;
    }

    public void setFooter(View view) {
        hasFooter = true;
        footerView = view;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (hasHeader && position == 0) {
            return;
        }

        int realPos = getRealPosition(position);
        Item item = getItem(realPos);
        if (item == null) {
            return;
        }

        onBindItemViewHolder((BaseRecyclerViewHolder) holder, realPos, item);
    }

    @Override
    public int getItemCount() {
        int count = list.size();

        if (hasFooter) {
            count++;
        }

        if (hasHeader) {
            count++;
        }

        return count;
    }

    public boolean hasData() {
        return list.size() > 0;
    }

    public void setData(List<Item> list, boolean replace) {
        if (replace) {
            replaceData(list);
        } else {
            addList(list);
        }
    }

    public void replaceData(List<Item> list) {
        this.list.clear();

        if (list != null) {
            this.list.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void clearData() {
        list.clear();

        notifyDataSetChanged();
    }

    public List<Item> getData() {
        return new ArrayList<>(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void addList(List<Item> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        if (item == null) {
            return;
        }

        list.add(item);
        notifyDataSetChanged();
    }

    public void addToHead(List<Item> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    protected Item getItem(int realPos) {
        if (list == null) {
            return null;
        }

        if (realPos + 1 > list.size()) {
            return null;
        }

        return list.get(realPos);
    }

    protected int getRealPosition(int position) {
        int realPos = position;
        if (hasHeader) {
            realPos--;
        }
        return realPos;
    }
}