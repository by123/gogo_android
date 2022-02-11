package com.scrat.gogo.data.api;

import com.scrat.gogo.framework.common.BaseResponse;

/**
 * Created by scrat on 2017/11/4.
 */

public class ServerException extends Exception {
    private int code;

    public ServerException(BaseResponse response) {
        super(response.getMsg());
        code = response.getCode();
    }

    public boolean isInsufficientCoin() {
        return code == 998;
    }
}
