package com.scrat.gogo.module.shop.detail;

import com.scrat.gogo.data.model.GoodsDetail;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/17.
 */

public interface GoodsDetailContract {
    interface Presenter {
        void loadData();

        void exchange();
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showLoadingGoodsDetail();

        void showLoadGoodsDetailError(String e);

        void showGoodsDetail(GoodsDetail detail);

        void showExchangeSuccess();

        void showExchangeError(String e);
    }
}
