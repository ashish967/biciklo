package com.bycycle.android.utils;

import android.util.Log;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public class Logger {

    public static void log(Object message) {
            String methodName = "";
            String TAG = "";
            try {
                StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                StackTraceElement e = stacktrace[3];
                methodName = e.getMethodName();
                TAG = e.getClassName();
            } catch (Exception e) {
            }
            Log.e(TAG , methodName + ": " + message);
    }
}
