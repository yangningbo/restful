package com.hello1987.restful.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "RESTFul";
    private static final boolean ENABLE_DEBUG = false;

    private LogUtil() {
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void i(String tag, String message) {
        int maxLogSize = 3000;
        if (message == null) {
            return;
        }
        if (ENABLE_DEBUG) {
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.toString().length() ? message.toString()
                        .length() : end;

                Log.i(tag, message.toString().substring(start, end));
            }
        }
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void e(String tag, String message) {
        if (ENABLE_DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (ENABLE_DEBUG) {
            Log.e(tag, message);
            e.printStackTrace();
        }
    }

    public static void e(String message, Exception e) {
        if (ENABLE_DEBUG) {
            Log.e(TAG, message);
            e.printStackTrace();
        }
    }

}
