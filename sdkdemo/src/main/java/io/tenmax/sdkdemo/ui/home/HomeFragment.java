package io.tenmax.sdkdemo.ui.home;

import static io.tenmax.sdkdemo.SupportedSpaces.bottomBannerId;
import static io.tenmax.sdkdemo.SupportedSpaces.floatingId;
import static io.tenmax.sdkdemo.SupportedSpaces.inlineId;
import static io.tenmax.sdkdemo.SupportedSpaces.interstitialId;
import static io.tenmax.sdkdemo.SupportedSpaces.topBannerId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import io.tenmax.sdkdemo.R;
import io.tenmax.sdkdemo.databinding.FragmentHomeBinding;
import io.tenmax.sdkdemo.ui.dashboard.DashboardFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.binding.showInterstitialAd.setOnClickListener((view) -> showAd("fullscreen", interstitialId(getContext())));
        this.binding.showInlineAd.setOnClickListener((view) -> showAd("inline", inlineId(getContext())));
        this.binding.showTopBannerAd.setOnClickListener((view) -> showAd("topBanner", topBannerId(getContext())));
        this.binding.showBottomBannerAd.setOnClickListener((view) -> showAd("bottomBanner", bottomBannerId(getContext())));
        this.binding.showFloatingAd.setOnClickListener((view) -> showAd("floating", floatingId(getContext())));

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
    }

    private void showAd(String type, String spaceId) {
        Fragment dashboard = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("spaceType", type);
        bundle.putString("spaceId", spaceId);
        dashboard.setArguments(bundle);
        this.pushFragment(dashboard);
    }

    private void pushFragment(Fragment fragment) {
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction()
            .add(R.id.fragmentContainer, fragment)
            .hide(this)
            .addToBackStack("home")
            .commit();
        if (getActivity() instanceof AppCompatActivity) {
            (((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }
}
