package com.scrat.gogo.framework.qiniu;

import android.support.annotation.NonNull;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.scrat.gogo.framework.util.BitmapUtil;
import com.scrat.gogo.framework.util.L;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scrat on 2017/6/20.
 */

public class QiniuUploadManager {
    private static class SingletonHolder {
        private static QiniuUploadManager instance = new QiniuUploadManager();
    }

    public static QiniuUploadManager getInstance() {
        return SingletonHolder.instance;
    }

    private UploadManager manager;

    private QiniuUploadManager() {
        // singleton only
        manager = new UploadManager();

    }

    public void uploadImg(
            @NonNull final String domain,
            @NonNull String token,
            @NonNull final String path,
            @NonNull final DefaultUploadListener listener) {

        listener.onBegin(path);
        byte[] bytes = BitmapUtil.createImageThumbnailByte(path);
        manager.put(bytes, null, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                L.d("key=%s\nresponse=%s\ninfo=%s", key, response, info);
                if (info.isOK()) {
                    String url;
                    try {
                        url = domain + response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFail(path, "解析出错");
                        return;
                    }
                    listener.onSuccess(path, url);
                } else {
                    listener.onFail(path, info.error);
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {

            @Override
            public void progress(String key, double percent) {
                listener.onProcess(path, key, percent);
            }
        }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return listener.isCancel();
            }
        }));
    }

    public void upload(@NonNull final String domain,
                       @NonNull String token,
                       byte[] bytes,
                       @NonNull final DefaultUploadListener listener) {
        final String path = "";
        listener.onBegin(path);
        manager.put(bytes, null, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                L.d("key=%s\nresponse=%s\ninfo=%s", key, response, info);
                if (info.isOK()) {
                    String url;
                    try {
                        url = domain + response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFail(path, "解析出错");
                        return;
                    }
                    listener.onSuccess(path, url);
                } else {
                    listener.onFail(path, info.error);
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {

            @Override
            public void progress(String key, double percent) {
                listener.onProcess(path, key, percent);
            }
        }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return listener.isCancel();
            }
        }));
    }

    public void uploadFile(@NonNull final String domain,
                           @NonNull String token,
                           @NonNull final String path,
                           @NonNull final DefaultUploadListener listener) {
        listener.onBegin(path);
        manager.put(path, null, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                L.d("key=%s\nresponse=%s\ninfo=%s", key, response, info);
                if (info.isOK()) {
                    String url;
                    try {
                        url = domain + response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFail(path, "解析出错");
                        return;
                    }
                    listener.onSuccess(path, url);
                } else {
                    listener.onFail(path, info.error);
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {

            @Override
            public void progress(String key, double percent) {
                listener.onProcess(path, key, percent);
            }
        }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return listener.isCancel();
            }
        }));
    }
}
