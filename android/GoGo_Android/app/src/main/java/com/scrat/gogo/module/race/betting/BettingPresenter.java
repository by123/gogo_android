package com.scrat.gogo.module.race.betting;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.RaceInfo;

/**
 * Created by scrat on 2017/11/15.
 */

public class BettingPresenter implements BettingContract.Presenter {
    private BettingContract.View view;
    private String raceId;

    public BettingPresenter(BettingContract.View view, String raceId) {
        this.raceId = raceId;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadData() {
        view.showLoadingBetting();
        DataRepository.getInstance().getApi().getRaceDetail(
                raceId,
                new DefaultLoadObjCallback<RaceInfo, Res.RaceInfoRes>(Res.RaceInfoRes.class) {
                    @Override
                    protected void onSuccess(RaceInfo raceInfo) {
                        view.showBetting(raceInfo);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showLoadBettingError(e.getMessage());
                    }
                }
        );
    }
}
