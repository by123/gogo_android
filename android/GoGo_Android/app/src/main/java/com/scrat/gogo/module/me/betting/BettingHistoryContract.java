package com.scrat.gogo.module.me.betting;

import com.scrat.gogo.data.model.BettingInfo;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/18.
 */

public interface BettingHistoryContract {
    interface Presenter {
        void loadData(boolean refresh);
    }

    interface View extends BaseContract.BaseListView<Presenter, BettingInfo> {

    }
}
