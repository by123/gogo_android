package com.scrat.gogo.framework.common;

import android.support.annotation.Nullable;

import com.scrat.gogo.BuildConfig;
import com.scrat.gogo.data.api.ServerException;
import com.scrat.gogo.framework.util.L;
import com.scrat.gogo.framework.util.MainHandlerUtil;
import com.scrat.gogo.framework.util.OkHttpHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by scrat on 2017/5/17.
 */

public abstract class BaseNetCallback<T extends BaseResponse> implements Callback {
    private Class<T> resClass;

    public BaseNetCallback(Class<T> resClass) {
        this.resClass = resClass;
    }

    protected abstract void onRequestSuccess(T res);

    protected abstract void onRequestFailure(Exception e);

    private T parseResponse(Response response) {
        return OkHttpHelper.getInstance().getGson()
                .fromJson(response.body().charStream(), resClass);
    }

    @Override
    public void onFailure(@Nullable Call call, @Nullable final IOException e) {
        notifyFailure(e);
    }

    @Override
    public void onResponse(
            @Nullable final Call call, @Nullable final Response response) throws IOException {
        if (BuildConfig.DEBUG) {
            L.d("[CODE] %s", response != null ? response.code() : -1000);
        }

        if (response == null) {
            onFailure(call, new IOException("response is null"));
            return;
        }

        if (!response.isSuccessful()) {
            onFailure(call, new IOException("Unexpected code " + response.body().string()));
            return;
        }

        T t;
        try {
            t = parseResponse(response);
            if (t.isSuccess()) {
                notifyResponseSuccess(t);
                return;
            }
            L.e("[SERVER CODE] %s", t.getCode());
            notifyFailure(new ServerException(t));
        } catch (Exception e) {
            L.e(e);
            notifyFailure(new IOException("Parse error " + e.getMessage()));
        }
    }

    private void notifyFailure(final Exception e) {
        MainHandlerUtil.runOnUiThread(() -> onRequestFailure(e));
    }

    private void notifyResponseSuccess(final T t) {
        MainHandlerUtil.runOnUiThread(() -> onRequestSuccess(t));
    }
}
