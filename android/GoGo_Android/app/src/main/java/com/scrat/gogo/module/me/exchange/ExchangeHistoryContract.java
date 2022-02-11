package com.scrat.gogo.module.me.exchange;

import com.scrat.gogo.data.model.ExchangeHistory;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/17.
 */

public interface ExchangeHistoryContract {
    interface Presenter {
        void loadData(boolean refresh);
    }

    interface View extends BaseContract.BaseListView<Presenter, ExchangeHistory> {

    }
}
