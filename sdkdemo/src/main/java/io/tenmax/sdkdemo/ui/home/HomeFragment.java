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
import androidx.lifecycle.ViewModelProvider;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.sdkdemo.databinding.FragmentHomeBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TenMaxAd fullscreenAd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        SimpleAdSessionListener listener = new SimpleAdSessionListener(this.getContext());
        SimpleInitiationCallback callback = new SimpleInitiationCallback(this.getContext());
        this.fullscreenAd = interstitialAd("d1061fb7cebc43d3", this.getActivity(), listener, callback);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.fullscreenAd.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.fullscreenAd);
    }
}
