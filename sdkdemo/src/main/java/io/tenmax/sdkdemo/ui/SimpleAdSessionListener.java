package io.tenmax.sdkdemo.ui;

import static io.tenmax.sdkdemo.TenMaxMobileSDKDemoLogger.info;

import android.content.Context;
import android.widget.Toast;

import io.tenmax.mobilesdk.TenMaxAdSession;
import io.tenmax.mobilesdk.TenMaxAdSessionListener;

public class SimpleAdSessionListener implements TenMaxAdSessionListener {

    private final Context context;

    public SimpleAdSessionListener(Context context) {
        this.context = context;
    }

    @Override
    public void adViewableEventSent(TenMaxAdSession session) {
        Toast.makeText(this.context, "viewable event sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void adLoadingTimeout(TenMaxAdSession session) {
        Toast.makeText(this.context, "AD loading timeout", Toast.LENGTH_SHORT).show();
        info("AD (spaceId: %s) loading timeout (sessionId: %s)", session.getSpace().getSpaceId(), session.getId());
    }

    @Override
    public void adNotFound(TenMaxAdSession session) {
        Toast.makeText(this.context, "received adNoFill event", Toast.LENGTH_SHORT).show();
    }
}
