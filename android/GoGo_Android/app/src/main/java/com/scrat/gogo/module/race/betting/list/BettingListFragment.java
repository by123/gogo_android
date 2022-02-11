package com.scrat.gogo.module.race.betting.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scrat.gogo.R;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.Betting;
import com.scrat.gogo.data.model.BettingItem;
import com.scrat.gogo.databinding.FragmentBettingListBinding;
import com.scrat.gogo.databinding.ListItemBettingItemBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseOnItemClickListener;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.framework.view.LoginDialog;
import com.scrat.gogo.module.coin.CoinPlanActivity;
import com.scrat.gogo.module.me.betting.BettingHistoryActivity;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;
import java.util.Locale;

/**
 * Created by scrat on 2017/11/15.
 */

public class BettingListFragment extends BaseFragment implements BettingListContract.View {
    private FragmentBettingListBinding binding;
    private BettingPopupWindow popupWindow;
    private Adapter adapter;
    private BettingListContract.Presenter presenter;
    private IosDialog loginDialog;
    private IosDialog coinDialog;
    private static final String TP = "tp";
    private static final String ID = "id";

    public static BettingListFragment newInstance(String raceId, String tp) {
        Bundle args = new Bundle();
        args.putString(ID, raceId);
        args.putString(TP, tp);
        BettingListFragment fragment = new BettingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "BettingListFragment";
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentBettingListBinding.inflate(inflater, container, false);
        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.list.setLayoutManager(layoutManager);
        adapter = new Adapter(bettingItem -> {
            presenter.loadCoinInfo();
            popupWindow.showBettingItem(binding.list, bettingItem);
        });
        binding.list.setAdapter(adapter);
        popupWindow = new BettingPopupWindow(
                getContext(), (item, coin) -> {
                    if (!Preferences.getInstance().isLogin()) {
                        loginDialog.show(binding.list);
                        return;
                    }
                    presenter.betting(item.getBettingItemId(), coin);
                });

        new BettingListPresenter(this, getArguments().getString(ID), getArguments().getString(TP));
        presenter.loadBetting();
        loginDialog = LoginDialog.build(getContext());
        coinDialog = new IosDialog(getContext())
                .setTitle("竞猜币不足")
                .setContent("抱歉！您当前的竞猜币不足，请立即充值！")
                .setNegative("不买，没钱")
                .setPositive("立即充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CoinPlanActivity.show(getActivity());
                    }
                });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        if (coinDialog.isShowing()) {
            coinDialog.dismiss();
        }
        if (loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void showBetting(List<Betting> list) {
        adapter.replaceData(list);
    }

    @Override
    public void showBettingLoadError(String message) {

    }

    @Override
    public void showInsufficientCoinError() {
        popupWindow.dismiss();
        coinDialog.show(binding.list);
    }

    @Override
    public void setPresenter(BettingListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showBettingExecuting() {

    }

    @Override
    public void showBettingExecuteSuccess() {
        popupWindow.dismiss();
        showToast("参与成功");
        BettingHistoryActivity.show(getContext());
    }

    @Override
    public void showBettingExecuteError(String e) {
        popupWindow.dismiss();
        showMsg(e);
    }

    @Override
    public void showLoadingCoin() {
        if (isFinish()) {
            return;
        }

        popupWindow.showUserCoinLoading();
    }

    @Override
    public void showLoadingCoinError(String e) {
        showMsg(e);
    }

    @Override
    public void showUserCoin(long coin) {
        if (isFinish()) {
            return;
        }

        popupWindow.showUserCoin(coin);
    }

    @Override
    public void showBettingLoading() {

    }

    private static class Adapter extends BaseRecyclerViewAdapter<Betting> {
        private BaseOnItemClickListener<BettingItem> onItemClickListener;

        private Adapter(BaseOnItemClickListener<BettingItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, Betting betting) {
            holder.setText(R.id.title, betting.getTitle());
            FlowLayout layout = holder.getView(R.id.list);
            layout.removeAllViews();

            LayoutInflater inflater = LayoutInflater.from(layout.getContext());
            for (final BettingItem item : betting.getItems()) {
                ListItemBettingItemBinding binding
                        = ListItemBettingItemBinding.inflate(inflater, layout, false);
                String odds = String.format(Locale.getDefault(), "%.2f", item.getOdds());
                binding.odds.setText(odds);
                binding.title.setText(item.getTitle());
                if ("unknown".equals(item.getStatus())) {
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.getRoot().setOnClickListener(view -> onItemClickListener.onItemClick(item));
                } else if ("win".equals(item.getStatus())) {
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selection_fill_white_12dp, 0, 0, 0);
                    binding.getRoot().setBackgroundResource(R.drawable.bg_c01_3dp);
                }
                layout.addView(binding.getRoot());
            }
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_betting, parent, false);
            return new BaseRecyclerViewHolder(v);
        }
    }
}
