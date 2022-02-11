package com.scrat.gogo.module.race.team.list;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.Team;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/15.
 */

public class TeamListPresenter implements TeamListContract.Presenter {
    private TeamListContract.View view;
    private Call call;
    private String index;

    public TeamListPresenter(TeamListContract.View view) {
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

        call = DataRepository.getInstance().getApi().getTeams(
                index, new DefaultLoadObjCallback<Res.ListRes<Team>, Res.TeamListRes>(Res.TeamListRes.class) {
                    @Override
                    protected void onSuccess(Res.ListRes<Team> teamListRes) {
                        index = teamListRes.getIndex();
                        if (teamListRes.isEmpty()) {
                            if (refresh) {
                                view.showEmptyList();
                                return;
                            }

                            view.showNoMoreListData();
                            return;
                        }
                        view.showListData(teamListRes.getItems(), refresh);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadingListError(e.getMessage());
                    }
                });
    }
}
