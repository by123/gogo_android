package com.scrat.gogo.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/19.
 */

public class UpdateInfo implements Serializable {
    private String title;
    private String content;
    @SerializedName("force_ver")
    private String forceVer;
    private String ver;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getForceVer() {
        return convertVer(forceVer);
    }

    public int getVer() {
        return convertVer(ver);
    }

    private int convertVer(String verStr) {
        if (TextUtils.isEmpty(verStr)) {
            return 0;
        }

        try {
            return Integer.parseInt(verStr);
        } catch (Exception e) {
            return 0;
        }
    }

}
