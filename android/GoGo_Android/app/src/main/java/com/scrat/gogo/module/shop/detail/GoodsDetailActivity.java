package com.scrat.gogo.module.shop.detail;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.scrat.gogo.R;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.Goods;
import com.scrat.gogo.data.model.GoodsDetail;
import com.scrat.gogo.databinding.ActivityGoodsDetailBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.framework.view.LoginDialog;
import com.scrat.gogo.module.me.exchange.ExchangeHistoryActivity;

/**
 * Created by scrat on 2017/11/17.
 */

public class GoodsDetailActivity extends BaseActivity implements GoodsDetailContract.View {
    private ActivityGoodsDetailBinding binding;
    private GoodsDetailContract.Presenter presenter;
    private GlideRequests glideRequests;
    private IosDialog loginDialog;
    private static final String DATA = "data";

    public static void show(Activity activity, Goods goods) {
        Intent i = new Intent(activity, GoodsDetailActivity.class);
        i.putExtra(DATA, goods);
        activity.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "GoodsDetailActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_detail);
        binding.topBar.subject.setText("商品详情");
        glideRequests = GlideApp.with(this);
        Goods goods = (Goods) getIntent().getSerializableExtra(DATA);
        showGoods(goods);
        new GoodsDetailPresenter(this, goods.getGoodsId());
        loginDialog = LoginDialog.build(this);
        presenter.loadData();
    }

    @Override
    protected void onDestroy() {
        if (loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
        super.onDestroy();
    }

    public void exchange(View view) {
        if (!Preferences.getInstance().isLogin()) {
            loginDialog.show(view);
            return;
        }
        presenter.exchange();
    }

    @Override
    public void setPresenter(GoodsDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingGoodsDetail() {

    }

    @Override
    public void showLoadGoodsDetailError(String e) {
        showMessage(e);
    }

    @Override
    public void showGoodsDetail(GoodsDetail detail) {
        String countStr = String.format(
                "剩余：%s件", detail.getMaxCount() - detail.getTotalExchange());
        binding.count.setText(countStr);
        binding.description.setText(detail.getDescription());
        showGoods(detail);
    }

    @Override
    public void showExchangeSuccess() {
        toast("兑换成功");
        ExchangeHistoryActivity.show(this);
    }

    @Override
    public void showExchangeError(String e) {
        showMessage(e);
    }

    private void showGoods(Goods goods) {
        glideRequests.load(goods.getCover()).fitCenter().into(binding.cover);
        binding.title.setText(goods.getTitle());
        binding.coin.setText(String.valueOf(goods.getCoin()));
    }
}
