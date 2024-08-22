package io.tenmax.sdkdemo;

import static io.tenmax.sdkdemo.TenMaxMobileSDKDemoLogger.error;
import static io.tenmax.sdkdemo.TenMaxMobileSDKDemoLogger.info;

import android.app.Application;

import io.tenmax.mobilesdk.TenMaxMobileSDK;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        TenMaxMobileSDK.initiate(this, (spaces, error) -> {
            if (error != null) {
                error("failed to initiate SDK, due to %s", error.getMessage());
                return;
            }
            info("SDK initiated, available spaces %s", spaces.size());
        });
        super.onCreate();
    }
}
