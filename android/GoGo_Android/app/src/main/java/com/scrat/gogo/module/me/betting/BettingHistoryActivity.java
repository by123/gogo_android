package com.scrat.gogo.module.me.betting;

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
import com.scrat.gogo.data.model.Betting;
import com.scrat.gogo.data.model.BettingInfo;
import com.scrat.gogo.data.model.BettingItem;
import com.scrat.gogo.databinding.ActivityBetttingHistoryBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.common.BaseRecyclerViewOnScrollListener;
import com.scrat.gogo.framework.util.Utils;

import java.util.List;
import java.util.Locale;

/**
 * Created by scrat on 2017/11/18.
 */

public class BettingHistoryActivity extends BaseActivity implements BettingHistoryContract.View {
    private ActivityBetttingHistoryBinding binding;
    private Adapter adapter;
    private BettingHistoryContract.Presenter presenter;
    private BaseRecyclerViewOnScrollListener loadMoreListener;

    public static void show(Context context) {
        Intent i = new Intent(context, BettingHistoryActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "BettingHistoryActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bettting_history);
        binding.topBar.subject.setText("竞猜历史");

        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.list.setLayoutManager(layoutManager);
        adapter = new Adapter();
        binding.list.setAdapter(adapter);
        loadMoreListener = new BaseRecyclerViewOnScrollListener(
                layoutManager, () -> presenter.loadData(false));
        binding.list.addOnScrollListener(loadMoreListener);
        new BettingHistoryPresenter(this);
        presenter.loadData(true);
    }

    @Override
    public void setPresenter(BettingHistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingList() {

    }

    @Override
    public void showListData(List<BettingInfo> list, boolean replace) {
        adapter.setData(list, replace);
    }

    @Override
    public void showNoMoreListData() {

    }

    @Override
    public void showEmptyList() {

    }

    @Override
    public void showLoadingListError(String e) {
        showMessage(e);
    }

    private static class Adapter extends BaseRecyclerViewAdapter<BettingInfo> {

        private void setResultTitle(TextView title, TextView state, BettingInfo info) {
            Context ctx = title.getContext();
            if ("apply".equals(info.getStatus())) {
                title.setText("比赛未完成");
                title.setTextColor(ContextCompat.getColor(ctx, R.color.c08_text));
                state.setText("-");
                state.setTextColor(ContextCompat.getColor(ctx, R.color.c09_tips));
                return;
            }

            if ("win".equals(info.getStatus())) {
                String titleStr = String.format(
                        Locale.getDefault(),
                        "+ %.0f",
                        (float) info.getCoin() * (info.getOdds() - 1f));
                title.setText(titleStr);
                title.setTextColor(ContextCompat.getColor(ctx, R.color.c01_blue));
                state.setText("WIN");
                state.setTextColor(ContextCompat.getColor(ctx, R.color.c01_blue));
                return;
            }

            if ("lose".equals(info.getStatus())) {
                String titleStr = String.format("- %s", info.getCoin());
                title.setText(titleStr);
                title.setTextColor(ContextCompat.getColor(ctx, R.color.c02_red));
                state.setText("LOSE");
                state.setTextColor(ContextCompat.getColor(ctx, R.color.c02_red));
                return;
            }

            title.setText(String.valueOf(info.getCoin()));
            title.setTextColor(ContextCompat.getColor(ctx, R.color.c09_tips));
            state.setText("-");
            state.setTextColor(ContextCompat.getColor(ctx, R.color.c09_tips));
            return;
        }

        private void setBetting(TextView textView, BettingInfo info) {
            List<Betting> bettingList = info.getBetting();
            if (bettingList == null || bettingList.isEmpty()) {
                textView.setText("");
                return;
            }

            Betting betting = bettingList.get(0);
            List<BettingItem> items = betting.getItems();
            if (items == null || items.isEmpty()) {
                textView.setText(betting.getTitle());
                return;
            }

            BettingItem item = items.get(0);

            textView.setText(betting.getTitle() + " / " + item.getTitle());
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, BettingInfo info) {
            setResultTitle(
                    holder.getTextView(R.id.result_title),
                    holder.getTextView(R.id.result_status),
                    info);

            setBetting(holder.getTextView(R.id.betting), info);

            holder.setText(R.id.date, Utils.formatDate(info.getCreateTs()))
                    .setText(R.id.game, info.getRace().getRaceName());

            String coinStr = String.format(
                    Locale.getDefault(),
                    "%s竞猜币 / 赔率 %.2f",
                    info.getCoin(), info.getOdds());
            holder.setText(R.id.coin, coinStr);
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            return BaseRecyclerViewHolder.newInstance(R.layout.list_item_betting_history, parent);
        }
    }

}
