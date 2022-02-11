package com.scrat.gogo.framework.util;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by scrat on 2017/7/8.
 */

public abstract class BaseFileDownloadCallback implements Callback {
    private static final int DOWNLOAD_CHUNK_SIZE = 2048;

    protected abstract File getTargetFile();

    public abstract void onBegin();

    protected abstract void onPrecess(long totalRead);

    public abstract void onComplete(boolean success, @Nullable String msg);

    @Override
    public void onFailure(Call call, IOException e) {
        final String msg = e.getMessage();
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onComplete(false, msg);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        L.d("[CODE] %s", response.code());

        if (!response.isSuccessful()) {
            onFailure(call, new IOException("Unexpected code " + response.body().string()));
            return;
        }

        BufferedSink sink = null;
        try {
            ResponseBody body = response.body();

            BufferedSource source = body.source();
            sink = Okio.buffer(Okio.sink(getTargetFile()));

            long totalRead = 0L;
            long read;
            while ((read = source.read(sink.buffer(), DOWNLOAD_CHUNK_SIZE)) != -1) {
                totalRead += read;
//                Thread.sleep(300L);
                L.d("read %s totalRead %s", read, totalRead);
                final long finalTotalRead = totalRead;
                MainHandlerUtil.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onPrecess(finalTotalRead);
                    }
                });
            }
            sink.writeAll(source);
            sink.flush();
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onComplete(true, null);
                }
            });
        } catch (Exception e) {
            L.e(e);
            onFailure(call, new IOException("Parse error " + e.getMessage()));
        } finally {
            if (sink != null) {
                try {
                    sink.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }
    }
}
