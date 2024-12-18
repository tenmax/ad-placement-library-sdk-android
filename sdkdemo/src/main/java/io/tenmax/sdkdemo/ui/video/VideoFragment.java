package io.tenmax.sdkdemo.ui.video;

import static io.tenmax.mobilesdk.TenMaxBannerPosition.bottom;
import static io.tenmax.mobilesdk.TenMaxBannerPosition.top;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.bannerAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.cleanAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.floatingAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.inlineAd;
import static io.tenmax.mobilesdk.TenMaxMobileSDK.interstitialAd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;

import java.util.Map;

import io.tenmax.mobilesdk.TenMaxAd;
import io.tenmax.mobilesdk.TenMaxAdSessionListener;
import io.tenmax.mobilesdk.TenMaxAdSpace;
import io.tenmax.mobilesdk.TenMaxInitiationCallback;
import io.tenmax.sdkdemo.databinding.FragmentVideoBinding;
import io.tenmax.sdkdemo.ui.SimpleAdSessionListener;
import io.tenmax.sdkdemo.ui.SimpleInitiationCallback;

public class VideoFragment extends Fragment {

    interface AdInitializer {
        TenMaxAd init(String spaceId, Fragment fragment, FragmentVideoBinding binding, TenMaxAdSessionListener listener, TenMaxInitiationCallback<TenMaxAdSpace> callback);
    }

    private TenMaxAd presentingAd;
    private FragmentVideoBinding binding;
    private Player player;
    private final Map<String, AdInitializer> adInitializers = Map.of(
            "inline", (spaceId, fragment, binding, listener, callback) -> inlineAd(spaceId, fragment.getActivity(), binding.inlineAd, options -> {
                options.listenSession(listener).monitorInitiation(callback);
            })
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.adContainer.setVisibility(View.GONE);
        binding.closeAd.setVisibility(View.GONE);
        binding.adContainer.setOnTouchListener((v, event) -> true);
        player = new ExoPlayer.Builder(requireContext()).build();
        binding.playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri("https://media.w3.org/2010/05/sintel/trailer.mp4");
        player.setMediaItem(mediaItem);
        player.prepare();

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

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    showAd();
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (!isPlaying) {
                    showAd();
                } else {
                    hideAd();
                }
            }
        });

        binding.playerView.setOnClickListener(view -> {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.play();
            }
        });

        binding.closeAd.setOnClickListener(view -> {
            hideAd();
            player.play();
        });

        return root;
    }

    private void showAd() {
        binding.adContainer.setVisibility(View.VISIBLE);
        binding.closeAd.setVisibility(View.VISIBLE);
        if (presentingAd != null) {
            presentingAd.show();
        }
    }

    private void hideAd() {
        binding.adContainer.setVisibility(View.GONE);
        binding.closeAd.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.play();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
            player = null;
        }
        binding = null;
        cleanAd(this.presentingAd);
    }
}