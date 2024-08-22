package io.tenmax.sdkdemo.ui.dashboard;

import static io.tenmax.mobilesdk.TenMaxMobileSDK.cleanAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.inlineAd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.mobilesdk.TenMaxMobileSDK;
import io.tenmax.sdkdemo.databinding.FragmentDashboardBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private TenMaxAd inlineAd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SimpleAdSessionListener listener = new SimpleAdSessionListener(this.getContext());
        SimpleInitiationCallback callback = new SimpleInitiationCallback(this.getContext());
        this.inlineAd = inlineAd("f95fc92d4a824a41", this.getActivity(), this.binding.inlineAd, listener, callback);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.inlineAd.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.inlineAd);
    }
}
