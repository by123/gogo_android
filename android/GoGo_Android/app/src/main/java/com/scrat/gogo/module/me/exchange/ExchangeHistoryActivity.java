package com.scrat.gogo.module.me.exchange;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scrat.gogo.R;
import com.scrat.gogo.data.model.ExchangeHistory;
import com.scrat.gogo.databinding.ActivityExchangeHistoryBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.common.BaseRecyclerViewOnScrollListener;
import com.scrat.gogo.framework.util.Utils;

import java.util.List;

/**
 * Created by scrat on 2017/11/17.
 */

public class ExchangeHistoryActivity extends BaseActivity implements ExchangeHistoryContract.View {
    private ActivityExchangeHistoryBinding binding;
    private ExchangeHistoryContract.Presenter presenter;
    private Adapter adapter;
    private BaseRecyclerViewOnScrollListener loadMoreListener;

    public static void show(Context context) {
        Intent i = new Intent(context, ExchangeHistoryActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "ExchangeActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_history);
        binding.topBar.subject.setText("兑换记录");

        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.list.setLayoutManager(layoutManager);
        adapter = new Adapter();
        binding.list.setAdapter(adapter);
        loadMoreListener = new BaseRecyclerViewOnScrollListener(
                layoutManager, () -> presenter.loadData(false));
        binding.list.addOnScrollListener(loadMoreListener);

        new ExchangeHistoryPresenter(this);
        presenter.loadData(true);
    }

    @Override
    public void setPresenter(ExchangeHistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingList() {
        showLoading();
    }

    @Override
    public void showListData(List<ExchangeHistory> list, boolean replace) {
        hideLoading();
        adapter.setData(list, replace);
    }

    @Override
    public void showNoMoreListData() {
        hideLoading();
    }

    @Override
    public void showEmptyList() {
        hideLoading();
    }

    @Override
    public void showLoadingListError(String e) {
        hideLoading();
        showMessage(e);
    }

    private void showLoading() {

    }

    private void hideLoading() {

    }

    private static class Adapter extends BaseRecyclerViewAdapter<ExchangeHistory> {

        private void setStatus(TextView textView, String status) {
            if ("done".equals(status)) {
                textView.setText("已完成");
                int color = ContextCompat.getColor(textView.getContext(), R.color.c09_tips);
                textView.setTextColor(color);
            }
//            apply
            textView.setText("处理中");
            int color = ContextCompat.getColor(textView.getContext(), R.color.c01_blue);
            textView.setTextColor(color);
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, ExchangeHistory history) {
            holder.setText(R.id.title, history.getGoods().getTitle())
                    .setText(R.id.coin, String.format("%s竞猜币", history.getGoods().getCoin()))
                    .setText(R.id.date, Utils.formatDate(history.getOrder().getCreateTs()));
            setStatus(holder.getTextView(R.id.status), history.getOrder().getStatus());
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            return BaseRecyclerViewHolder.newInstance(R.layout.list_item_exchange_history, parent);
        }
    }
}
