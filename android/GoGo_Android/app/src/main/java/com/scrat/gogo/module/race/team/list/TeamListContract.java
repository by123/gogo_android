package com.scrat.gogo.module.race.team.list;

import com.scrat.gogo.data.model.Team;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/15.
 */

public interface TeamListContract {
    interface Presenter {
        void loadData(boolean refresh);
    }

    interface View extends BaseContract.BaseListView<Presenter, Team> {

    }
}
