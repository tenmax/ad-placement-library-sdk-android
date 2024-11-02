package io.tenmax.sdkdemo;

import android.content.Context;

public interface SupportedSpaces {

    static String interstitialId(Context context) {
        return context.getResources().getString(R.string.interstitialId);
    }

    static String topBannerId(Context context) {
        return context.getResources().getString(R.string.topBannerId);
    }

    static String bottomBannerId(Context context) {
        return context.getResources().getString(R.string.bottomBannerId);
    }

    static String inlineId(Context context) {
        return context.getResources().getString(R.string.inlineId);
    }

    static String floatingId(Context context) {
        return context.getResources().getString(R.string.floatingId);
    }
}
