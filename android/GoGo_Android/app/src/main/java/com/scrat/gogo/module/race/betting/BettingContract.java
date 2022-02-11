package com.scrat.gogo.module.race.betting;

import com.scrat.gogo.data.model.RaceInfo;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/15.
 */

public interface BettingContract {
    interface Presenter {
        void loadData();
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showLoadingBetting();

        void showLoadBettingError(String e);

        void showBetting(RaceInfo info);
    }
}
