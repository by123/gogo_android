package com.scrat.gogo.module.me.exchange;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.ExchangeHistory;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/17.
 */

public class ExchangeHistoryPresenter implements ExchangeHistoryContract.Presenter {
    private ExchangeHistoryContract.View view;
    private String index;
    private Call call;

    public ExchangeHistoryPresenter(ExchangeHistoryContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadData(final boolean refresh) {

        if (refresh) {
            index = "0";
        }

        if (Res.ListRes.hasMoreData(index)) {
            view.showNoMoreListData();
        }

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        call = DataRepository.getInstance().getApi().getExchangeHistory(
                index,
                new DefaultLoadObjCallback<Res.ListRes<ExchangeHistory>, Res.ExchangeHistoryListRes>(Res.ExchangeHistoryListRes.class) {
                    @Override
                    protected void onSuccess(Res.ListRes<ExchangeHistory> exchangeHistoryListRes) {
                        index = exchangeHistoryListRes.getIndex();
                        if (exchangeHistoryListRes.isEmpty()) {
                            if (refresh) {
                                view.showEmptyList();
                            } else {
                                view.showNoMoreListData();
                            }
                            return;
                        }

                        view.showListData(exchangeHistoryListRes.getItems(), refresh);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadingListError(e.getMessage());
                    }
                });
    }
}
