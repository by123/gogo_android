package com.scrat.gogo.framework.util;

import com.google.gson.Gson;
import com.scrat.gogo.BuildConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by scrat on 2017/5/17.
 * Http protocol
 */

public class OkHttpHelper {
    private static class SingletonHolder {
        private static OkHttpHelper instance = new OkHttpHelper();
    }

    public static OkHttpHelper getInstance() {
        return SingletonHolder.instance;
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String GET_LOG_FORMAT = "[GET] %s?%s";
    private static final String POST_LOG_FORMAT
            = " -H \"Content-type: application/json\" -X POST -d '%s' %s";
    private static final String DELETE_LOG_FORMAT
            = " -H \"Content-type: application/json\" -X POST -d '%s' %s";
    private static final String PUT_LOG_FORMAT
            = " -H \"Content-type: application/json\" -X PUT -d '%s' %s";

    private OkHttpClient client;
    private Gson gson;

    private OkHttpHelper() {
        // Singleton only
        client = new OkHttpClient();
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public Call post(String url, Map<String, String> header, String json, Callback cb) throws Exception {
        if (json == null) {
            json = "{}";
        }
        if (BuildConfig.DEBUG) {
            // log
            String log = String.format(POST_LOG_FORMAT, json, url);
            StringBuilder sb = new StringBuilder().append("[POST] curl -l ");
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    sb.append(" -H '")
                            .append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("'");
                }
            }
            sb.append(log);
            L.d(sb.toString());
        }
        Request.Builder builder = new Request.Builder().url(url);
        RequestBody body = RequestBody.create(JSON, json);
        builder.post(body);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Call call = client.newCall(builder.build());
        if (cb != null) {
            call.enqueue(cb);
        } else {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException ignore) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }
        return call;
    }

    public Call delete(String url, Map<String, String> header, String json, Callback cb) throws Exception {
        if (BuildConfig.DEBUG) {
            // log
            String log = String.format(DELETE_LOG_FORMAT, json, url);
            StringBuilder sb = new StringBuilder().append("[DELETE] curl -l ");
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    sb.append(" -H '")
                            .append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("'");
                }
            }
            sb.append(log);
            L.d(sb.toString());
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (json != null) {
            RequestBody body = RequestBody.create(JSON, json);
            builder.delete(body);
        } else {
            builder.delete();
        }

        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Call call = client.newCall(builder.build());
        call.enqueue(cb);
        return call;
    }

    public Call put(String url, Map<String, String> header, String json, Callback cb) throws Exception {
        if (BuildConfig.DEBUG) {
            // log
            String log = String.format(PUT_LOG_FORMAT, json, url);
            StringBuilder sb = new StringBuilder().append("[PUT] curl -l ");
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    sb.append(" -H '")
                            .append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("'");
                }
            }
            sb.append(log);
            L.d(sb.toString());
        }

        Request.Builder builder = new Request.Builder().url(url)
                .put(RequestBody.create(JSON, json));

        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Call call = client.newCall(builder.build());
        call.enqueue(cb);
        return call;
    }

    public Call get(String url, Callback cb) throws Exception {
        return get(url, null, null, cb);
    }

    public Call get(String url,
                    Map<String, String> headers,
                    Map<String, String> params,
                    Callback cb) throws Exception {
        if (BuildConfig.DEBUG) {
            // log
            StringBuilder sb = new StringBuilder()
                    .append(String.format(GET_LOG_FORMAT, url, params == null ? "" : params));
            if (headers != null) {
                sb.append("\n");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    sb.append(" -H '")
                            .append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("'");
                }
            }
            String log = sb.toString()
                    .replace("{", "")
                    .replace("}", "")
                    .replace(", ", "&");
            if (isEmpty(params)) {
                log = log.replace("?", "");
            }
            L.d(log);
        }

        Request.Builder request = buildGetFormRequest(url, headers, params);
        Call call = client.newCall(request.build());
        call.enqueue(cb);
        return call;
    }

    public Call download(String url, final BaseFileDownloadCallback cb) throws Exception {
        cb.onBegin();
        Call call = client.newCall(new Request.Builder().url(url).get().build());
        call.enqueue(cb);
        return call;
    }

    private boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    private Request.Builder buildGetFormRequest(
            String url,
            Map<String, String> headers,
            Map<String, String> params) throws UnsupportedEncodingException {
        if (!isEmpty(params)) {
            StringBuilder sb = new StringBuilder(url).append('?');
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (!isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }

        return builder;
    }
}
