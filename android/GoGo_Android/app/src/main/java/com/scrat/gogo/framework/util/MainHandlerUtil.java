package com.scrat.gogo.framework.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by scrat on 2017/5/17.
 */

public class MainHandlerUtil {
    private MainHandlerUtil() {
//        Singleton only
    }

    private static class SingletonHolder {
        private static Handler instance = new Handler(Looper.getMainLooper());
    }

    public static Handler getMainHandler() {
        return SingletonHolder.instance;
    }

    public static void runOnUiThread(Runnable runnable) {
        getMainHandler().post(runnable);
    }
}
