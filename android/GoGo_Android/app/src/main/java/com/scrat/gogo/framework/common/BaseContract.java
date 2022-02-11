package com.scrat.gogo.framework.common;

import java.util.List;

/**
 * Created by scrat on 2017/11/3.
 */

public interface BaseContract {
    interface BaseView<T> {
        void setPresenter(T t);
    }

    interface BaseListView<P, I> {
        void setPresenter(P p);

        void showLoadingList();

        void showListData(List<I> list, boolean replace);

        void showNoMoreListData();

        void showEmptyList();

        void showLoadingListError(String e);
    }
}
