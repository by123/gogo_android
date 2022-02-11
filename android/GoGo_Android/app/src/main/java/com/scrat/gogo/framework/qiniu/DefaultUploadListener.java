package com.scrat.gogo.framework.qiniu;

/**
 * Created by scrat on 2017/6/23.
 */

public abstract class DefaultUploadListener implements UploadListener {
    private boolean cancel = false;

    @Override
    public void onBegin(String path) {

    }

    @Override
    public void onProcess(String path, String key, double percent) {

    }

    @Override
    public boolean isCancel() {
        return cancel;
    }

    public DefaultUploadListener setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }
}
