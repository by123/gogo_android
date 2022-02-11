package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/13.
 */

public class WxPayInfo implements Serializable {
    @SerializedName("app_id")
    private String appId;
    @SerializedName("partner_id")
    private String partnerId;
    @SerializedName("prepay_id")
    private String prepayId;
    @SerializedName("package")
    private String pkg;
    @SerializedName("nonce_str")
    private String nonceStr;
    private String timestamp;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getPkg() {
        return pkg;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }
}
