package io.tenmax.sdkdemo.ui.home;

import static io.tenmax.mobilesdk.TenMaxMobileSDK.cleanAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.interstitialAd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.sdkdemo.R;
import io.tenmax.sdkdemo.SupportedSpaces;
import io.tenmax.sdkdemo.databinding.FragmentHomeBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;
import io.tenmax.sdkdemo.ui.dashboard.DashboardFragment;
import io.tenmax.sdkdemo.ui.notifications.NotificationsFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TenMaxAd fullscreenAd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SimpleAdSessionListener listener = new SimpleAdSessionListener(this.getContext());
        SimpleInitiationCallback callback = new SimpleInitiationCallback(this.getContext());
        this.fullscreenAd = interstitialAd(SupportedSpaces.interstitialId, this.getActivity(), listener, callback);
        this.binding.showInterstitialAd.setOnClickListener((view) -> {
            this.fullscreenAd.show();
        });

        this.binding.showInlineAd.setOnClickListener((view) -> {
            Fragment dashboard = new DashboardFragment();
            this.pushFragment(dashboard);
        });
        this.binding.showBannerAd.setOnClickListener((view) -> {
            Fragment notifications = new NotificationsFragment();
            this.pushFragment(notifications);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.fullscreenAd);
    }

    private void pushFragment(Fragment fragment) {
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction()
            .add(R.id.fragmentContainer, fragment)
            .hide(this)
            .addToBackStack("home")
            .commit();
    }
}
