package io.tenmax.sdkdemo.ui.dashboard;

import static io.tenmax.mobilesdk.TenMaxBannerPosition.bottom;
import static io.tenmax.mobilesdk.TenMaxBannerPosition.top;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.bannerAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.cleanAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.floatingAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.inlineAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.interstitialAd;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.mobilesdk.TenMaxAdConfigurer;
import io.tenmax.mobilesdk.TenMaxAdOptions;
import io.tenmax.mobilesdk.TenMaxAdSessionListener;
import io.tenmax.mobilesdk.TenMaxAdSpace;
import io.tenmax.mobilesdk.TenMaxInitiationCallback;
import io.tenmax.mobilesdk.TenMaxMobileSDK;
import io.tenmax.sdkdemo.SupportedSpaces;
import io.tenmax.sdkdemo.databinding.FragmentDashboardBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;

public class DashboardFragment extends Fragment {

    interface AdInitializer {

        TenMaxAd init(String spaceId, Fragment fragment, FragmentDashboardBinding binding, TenMaxAdSessionListener listener, TenMaxInitiationCallback<TenMaxAdSpace> callback);
    }

    private TenMaxAd presentingAd;
    private FragmentDashboardBinding binding;
    private final Map<String, AdInitializer> adInitializers = Map.of(
        "inline", (spaceId, fragment, binding, listener, callback) -> inlineAd(spaceId, fragment.getActivity(), binding.inlineAd, options -> {
            options.listenSession(listener).monitorInitiation(callback);
        }),
        "topBanner", (spaceId, fragment, binding1, listener, callback) -> bannerAd(spaceId, fragment.getActivity(), binding.topBanner, top, options -> {
            options.listenSession(listener).monitorInitiation(callback);
        }),
        "bottomBanner", (spaceId, fragment, binding1, listener, callback) -> bannerAd(spaceId, fragment.getActivity(), binding.bottomBanner, bottom, options -> {
            options.listenSession(listener).monitorInitiation(callback);
        }),
        "floating", (spaceId, fragment, binding1, listener, callback) -> floatingAd(spaceId, fragment, options -> {
            options.listenSession(listener).monitorInitiation(callback);
        }),
        "fullscreen", (spaceId, fragment, binding1, listener, callback) -> interstitialAd(spaceId, fragment.getActivity(), options -> {
            options.listenSession(listener).monitorInitiation(callback);
        })
    );

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SimpleAdSessionListener listener = new SimpleAdSessionListener(this.getContext());
        SimpleInitiationCallback callback = new SimpleInitiationCallback(this.getContext());
        if (getArguments() != null) {
            String spaceId = getArguments().getString("spaceId");
            String type = getArguments().getString("spaceType");
            AdInitializer initializer = adInitializers.get(type);
            if (initializer != null) {
                this.presentingAd = initializer.init(spaceId, this, this.binding, listener, callback);
            }
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.presentingAd != null) {
            this.presentingAd.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.presentingAd);
    }
}
