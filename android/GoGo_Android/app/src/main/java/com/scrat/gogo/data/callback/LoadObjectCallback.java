package com.scrat.gogo.data.callback;

/**
 * Created by scrat on 16/7/2.
 */
public interface LoadObjectCallback<T> {
    void onSuccess(T t);

    // 业务异常
    void onFail(String msg);

    // 程序异常
    void onError(Exception e);
}
