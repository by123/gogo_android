package com.scrat.gogo.module.me.betting;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.BettingInfo;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/18.
 */

public class BettingHistoryPresenter implements BettingHistoryContract.Presenter {
    private BettingHistoryContract.View view;
    private String index;
    private Call call;

    public BettingHistoryPresenter(BettingHistoryContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void loadData(final boolean refresh) {
        if (refresh) {
            index = "0";
        }

        if (!Res.ListRes.hasMoreData(index)) {
            view.showNoMoreListData();
            return;
        }

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        view.showLoadingList();
        call = DataRepository.getInstance().getApi().getBettingHistory(
                index,
                new DefaultLoadObjCallback<Res.ListRes<BettingInfo>, Res.BettingInfoListRes>(Res.BettingInfoListRes.class) {
                    @Override
                    protected void onSuccess(Res.ListRes<BettingInfo> info) {
                        index = info.getIndex();
                        if (info.isEmpty()) {
                            if (refresh) {
                                view.showEmptyList();
                            } else {
                                view.showNoMoreListData();
                            }
                            return;
                        }

                        view.showListData(info.getItems(), refresh);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadingListError(e.getMessage());
                    }
                });
    }
}
