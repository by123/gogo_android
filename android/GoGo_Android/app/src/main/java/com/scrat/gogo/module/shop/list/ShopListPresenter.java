package com.scrat.gogo.module.shop.list;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.Goods;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/12.
 */

public class ShopListPresenter implements ShopListContract.Presenter {
    private ShopListContract.View view;
    private final String type;
    private Call call;
    private String index;

    public ShopListPresenter(ShopListContract.View view, String type) {
        this.type = type;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadGoods(final boolean refresh) {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        if (refresh) {
            index = "0";
        }

        if (!Res.ListRes.hasMoreData(index)) {
            view.showNoMoreListData();
            return;
        }

        view.showLoadingList();
        call = DataRepository.getInstance().getApi().getGoodsList(
                type, index, new DefaultLoadObjCallback<Res.ListRes<Goods>, Res.GoodsListRes>(Res.GoodsListRes.class) {
                    @Override
                    protected void onSuccess(Res.ListRes<Goods> goodsListRes) {
                        index = goodsListRes.getIndex();
                        if (goodsListRes.isEmpty()) {
                            if (refresh) {
                                view.showEmptyList();
                                return;
                            }

                            view.showNoMoreListData();
                            return;
                        }
                        view.showListData(goodsListRes.getItems(), refresh);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadingListError(e.getMessage());
                    }
                });
    }
}
