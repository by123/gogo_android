package com.scrat.gogo.module.race.list;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.RaceGroupItem;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/14.
 */

public class RaceListPresenter implements RaceListContract.Presenter {
    private RaceListContract.View view;
    private String index;
    private Call call;

    public RaceListPresenter(RaceListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadData(final boolean refresh) {

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
        call = DataRepository.getInstance().getApi().getRaceList(
                index, new DefaultLoadObjCallback<Res.ListRes<RaceGroupItem>, Res.RaceGroupItemRes>(Res.RaceGroupItemRes.class) {
                    @Override
                    protected void onSuccess(Res.ListRes<RaceGroupItem> raceGroupItemListRes) {
                        index = raceGroupItemListRes.getIndex();
                        if (raceGroupItemListRes.isEmpty()) {
                            if (refresh) {
                                view.showEmptyList();
                                return;
                            }

                            view.showNoMoreListData();
                            return;
                        }
                        view.showListData(raceGroupItemListRes.getItems(), refresh);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadingListError(e.getMessage());
                    }
                });
    }
}
