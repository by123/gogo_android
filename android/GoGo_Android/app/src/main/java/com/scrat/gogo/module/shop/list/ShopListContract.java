package com.scrat.gogo.module.shop.list;

import com.scrat.gogo.data.model.Goods;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/12.
 */

public interface ShopListContract {
    interface Presenter {
        void loadGoods(boolean refresh);
    }

    interface View extends BaseContract.BaseListView<Presenter, Goods> {

    }
}
