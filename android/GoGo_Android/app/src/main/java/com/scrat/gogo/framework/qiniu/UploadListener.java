package com.scrat.gogo.framework.qiniu;

/**
 * Created by scrat on 2017/6/23.
 */

public interface UploadListener {
    void onBegin(String path);

    void onProcess(String path, String key, double percent);

    void onSuccess(String path, String url);

    void onFail(String path, String msg);

    boolean isCancel();
}
