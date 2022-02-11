package com.scrat.gogo.framework.common;

import java.io.Serializable;

/**
 * Created by scrat on 2017/5/17.
 */

public class BaseResponse<T> implements Serializable {
    private String msg;
    private int code;
    private T data;

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return code == 200;
    }

}
