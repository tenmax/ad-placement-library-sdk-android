package io.tenmax.sdkdemo.ui;

import android.content.Context;
import android.widget.Toast;

import io.tenmax.mobilesdk.TenMaxAdSpace;
import io.tenmax.mobilesdk.TenMaxInitiationCallback;

public class SimpleInitiationCallback implements TenMaxInitiationCallback<TenMaxAdSpace> {

    private final Context context;

    public SimpleInitiationCallback(Context context) {
        this.context = context;
    }

    @Override
    public void initiated(TenMaxAdSpace space, Throwable error) {
        if (error != null) {
            Toast.makeText(context, String.format("failed to initiate, due to %s", error.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }
}
