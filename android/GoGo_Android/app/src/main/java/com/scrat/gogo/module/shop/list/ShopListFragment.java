package com.scrat.gogo.module.shop.list;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scrat.gogo.R;
import com.scrat.gogo.data.model.Goods;
import com.scrat.gogo.databinding.FragmentShopListBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseOnItemClickListener;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.common.BaseRecyclerViewOnScrollListener;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequest;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.util.Utils;
import com.scrat.gogo.framework.view.GridSpacingItemDecoration;
import com.scrat.gogo.module.shop.detail.GoodsDetailActivity;

import java.util.List;

/**
 * Created by scrat on 2017/11/12.
 */

public class ShopListFragment extends BaseFragment implements ShopListContract.View {
    private static final String TYPE = "type";
    private FragmentShopListBinding binding;
    private BaseRecyclerViewOnScrollListener loadMoreListener;
    private ShopListContract.Presenter presenter;
    private Adapter adapter;

    public static ShopListFragment newEquipmentInstance() {
        return newInstance("equipment");
    }

    public static ShopListFragment newLuckMoneyInstance() {
        return newInstance("lucky_money");
    }

    public static ShopListFragment newGameAroundInstance() {
        return newInstance("game_around");
    }

    public static ShopListFragment newVirtualInstance() {
        return newInstance("virtual");
    }

    private static ShopListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        ShopListFragment fragment = new ShopListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "ShopListFragment";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShopListBinding.inflate(inflater, container, false);

        binding.list.setHasFixedSize(true);
        int spacing = (int) Utils.dpToPx(getContext(), 15);
        binding.list.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true, spacing, spacing));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.list.setLayoutManager(layoutManager);
        binding.srl.setOnRefreshListener(() -> presenter.loadGoods(true));
        loadMoreListener = new BaseRecyclerViewOnScrollListener(
                layoutManager, () -> presenter.loadGoods(false));
        binding.list.addOnScrollListener(loadMoreListener);
        GlideRequests requests = GlideApp.with(this);
        adapter = new Adapter(requests, goods -> GoodsDetailActivity.show(getActivity(), goods));
        binding.list.setAdapter(adapter);

        new ShopListPresenter(this, getArguments().getString(TYPE));
        presenter.loadGoods(true);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(ShopListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingList() {
        showLoading();
    }

    @Override
    public void showListData(List<Goods> list, boolean replace) {
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
        showMsg(e);
    }

    private void showLoading() {
        binding.srl.setRefreshing(true);
    }

    private void hideLoading() {
        binding.srl.setRefreshing(false);
        loadMoreListener.setLoading(false);
    }

    private static class Adapter extends BaseRecyclerViewAdapter<Goods> {
        private final GlideRequest<Drawable> request;
        private BaseOnItemClickListener<Goods> onItemClickListener;

        private Adapter(
                GlideRequests requests, BaseOnItemClickListener<Goods> onItemClickListener) {
            request = requests.asDrawable().centerCrop();
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, final Goods goods) {
            request.load(goods.getCover())
                    .into(holder.getImageView(R.id.img));
            holder.setText(R.id.title, goods.getTitle());
            String coinStr = String.format("%s竞猜币", goods.getCoin());
            holder.setText(R.id.sub_title, coinStr);
            holder.setOnClickListener(view -> onItemClickListener.onItemClick(goods));
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_goods, parent, false);
            return new BaseRecyclerViewHolder(v);
        }
    }
}
