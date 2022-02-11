package com.scrat.gogo.module.race.list;

import com.scrat.gogo.data.model.RaceGroupItem;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/14.
 */

public interface RaceListContract {
    interface Presenter {
        void loadData(boolean refresh);
    }

    interface View extends BaseContract.BaseListView<Presenter, RaceGroupItem> {

    }
}
