package com.scrat.gogo.data;

import com.scrat.gogo.data.api.Api;

/**
 * Created by scrat on 2017/11/2.
 */

public class DataRepository {

    private static class SingletonHolder {
        private static DataRepository instance = new DataRepository();
    }

    public static DataRepository getInstance() {
        return DataRepository.SingletonHolder.instance;
    }

    private Api api;

    private DataRepository() {
        api = new Api();
        // singleton only
    }

    public Api getApi() {
        return api;
    }
}
