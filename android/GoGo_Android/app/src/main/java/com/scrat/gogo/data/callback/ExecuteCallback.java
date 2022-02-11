package com.scrat.gogo.data.callback;

/**
 * Created by scrat on 16/7/2.
 */
public interface ExecuteCallback {
    void onSuccess();

    void onFail(Exception e);
}
