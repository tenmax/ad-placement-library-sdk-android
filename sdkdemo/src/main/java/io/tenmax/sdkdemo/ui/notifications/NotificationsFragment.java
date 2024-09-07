package io.tenmax.sdkdemo.ui.notifications;

import static io.tenmax.mobilesdk.TenMaxBannerPosition.bottom;
import static io.tenmax.mobilesdk.TenMaxBannerPosition.top;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.cleanAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.bannerAd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.sdkdemo.SupportedSpaces;
import io.tenmax.sdkdemo.databinding.FragmentNotificationsBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private TenMaxAd topBannerAd;
    private TenMaxAd bottomBannerAd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SimpleAdSessionListener listener = new SimpleAdSessionListener(this.getContext());
        SimpleInitiationCallback callback = new SimpleInitiationCallback(this.getContext());
        this.topBannerAd = bannerAd(SupportedSpaces.topBannerId, this.getActivity(), this.binding.topBanner1, top, listener, callback);
        this.bottomBannerAd = bannerAd(SupportedSpaces.bottomBannerId, this.getActivity(), this.binding.bottomBanner1, bottom, listener, callback);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.topBannerAd.show();
        this.bottomBannerAd.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.topBannerAd);
        cleanAd(this.bottomBannerAd);
    }
}
