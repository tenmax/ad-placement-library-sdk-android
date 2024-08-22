package io.tenmax.sdkdemo;

import android.util.Log;

public interface TenMaxMobileSDKDemoLogger {

    String tag = "TenMaxMobileSDKDemo";

    static void info(String message, Object... args) {
        Log.i(tag, String.format(message, args));
    }

    static void error(String message, Object... args) {
        Log.e(tag, String.format(message, args));
    }

    static void debug(String message, Object... args) {
        Log.d(tag, String.format(message, args));
    }

    static void info(String message, Throwable e, Object... args) {
        Log.i(tag, String.format(message, args), e);
    }

    static void error(String message, Throwable e, Object... args) {
        Log.e(tag, String.format(message, args), e);
    }

    static void debug(String message, Throwable e, Object... args) {
        Log.d(tag, String.format(message, args), e);
    }
}
