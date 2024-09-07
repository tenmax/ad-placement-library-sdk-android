# TenMax Mobile SDK for Android

This repository provides the guidelines and examples to demonstrate how to use TenMax Mobile SDK to show AD on your Android app.

## Prerequisites

Before using the SDK, please contact TenMax (app_support@tenmax.io) to

- register your app bundle ID
- obtain your app publisher ID

These two values would be used to initiate the SDK.

## Get Started

Follow the steps to use the TenMax Mobile SDK:

### Install SDK

You can download the AAR file from the release and put the AAR file into `<project-root>/<your-app>/libs` and let Android Studio import it.

However, the SDK needs other libraries (Gson, Retrofit, and Google Play AD identifier) to work, thus, please also add required libraries:

- Gson (`com.google.code.gson:gson:2.11.0`)
- Retrofit (`com.squareup.retrofit2:retrofit:2.11.0`)
- Gson Converter (`com.squareup.retrofit2:converter-gson:2.11.0`)
- Google Play AD Identifier (`com.google.android.gms:play-services-ads-identifier:18.1.0`)

After importing those libraries, you should see something like this in your `build.gradle.kts`:

```gradle
dependencies {
    implementation(files("$projectDir/libs/adkit.aar"))
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.ads.identifier)
}
```

In this way, you need to download the new AAR file if the SDK is updated.

### SDK Configuration

Open your app's manifest file (`AndroidManifest.xml`) and add `tenmax-publisher-id` as the application's metadata. The publisher ID is provided by TenMax. Note that the SDK would get the bundle ID for you, but you must ensure that is the same as the value you registered in the [Prerequisites](#prerequisites) section.

```xml
<manifest>
    <application>
        <meta-data android:name="tenmax-publisher-id" android:value="{tenmax-publisher-id}" />
    </application>
</manifest>
```

### SDK initiation

TenMax Mobile SDK must be initiated before use, thus, in your application class (if you do not have an application class, please create a simple one), then, add the following code to initiate the SDK in `onCreate()` method. Don't worry, the SDK would run the initiation in an independent thread pool so won't increase your application launch time.

```java
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        TenMaxMobileSDK.initiate(this, (spaces, error) -> {
            if (error != null) {
                Log.d(getClass().toString(), "something wrong");
            }
        });
        super.onCreate();
    }
}
```

If you do not initiate the SDK, every call to show ADs, the SDK will give you an error. The SDK initiation would load the AD information from TenMax's server and try to obtain the Android advertising ID (AAID) for you. Now, you are ready to show AD.

## Show ADs

### Interstitial AD

First, let's show an interstitial AD (fullscreen AD) when your first activity started. Assume your application's `MainActivity` has three tabs: `HomeFragment`, `DashboardFragment`, and `NotificationFragment` (a sample project created by Android Studio). In the `HomeFragment`, add the following lines to show an interstitial AD when Home fragment resumed:

```java
public class HomeFragment extends Fragment {

    private TenMaxAd fullscreenAd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // other view setup code
        this.fullscreenAd = interstitialAd("{interstitial-space-id}", this.getActivity());
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

```

The AD presentation is asynchronous, thus, please keep the `TenMaxAd` object returned from the `interstitialAd` method. You can dispose (cancel) the presentation when the user switches to other tabs by calling `cleanAd` method (or calling `ad.dipose()`). The SDK would cancel the presentation (if not presented yet) or remove the presentation and then clean up resources to reduce memory usage.

### Banner AD

You can show a banner AD on top of the screen or bottom of the screen. Even more, you can show both the top and bottom banners on the same screen. However, the relationship between the banner and your app's UI is up to you. For example, you can let the top banner hover on your app's UI. Thus, you need to add a container in your layout file to determine the relationship, like this.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.notifications.NotificationsFragment">

    <!-- other UI elements-->

    <FrameLayout
        android:id="@+id/topBanner1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">
    </FrameLayout>

    <!-- other UI elements-->
</androidx.constraintlayout.widget.ConstraintLayout>
```

Back to your code, call the `bannerAd` method to let SDK create the banner AD in the specified container.

```java
public class NotificationsFragment extends Fragment {

    private TenMaxAd topBannerAd;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // other view setup code
        this.topBannerAd = bannerAd("{banner-space-id}", this.getActivity(), this.binding.topBanner1, top);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.topBannerAd.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.topBannerAd);
    }
}
```

Yes, you decide the container's position in the layout file, but you still need to provide the position information (`TenMaxBannerPosition.top`) when calling `bannerAd`. The SDK would use this information to avoid duplication (see [Duplication Detection](#duplication-detection)).

### Inline AD

To embed an inline AD into your app, you need to add a container to your layout file. For example, in the `fragment_dashboard.xml`, add a `FrameLayout` as the container and setup its layout. As shown, it uses `wrap_content` as `layout_width` and `layout_height` to show a zero-size container, uses `center_horizontal` as `layout_gravity` to centerize the container, and then sets `paddingBottom` to `10dp` to give some space to other UI elements.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/gray_600"
    tools:context=".ui.dashboard.DashboardFragment">

    <!-- other UI elements-->
    <FrameLayout
        android:id="@+id/inlineAd"
        android:paddingBottom="10dp"
        android:layout_width="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_height="wrap_content" />
    <!-- other UI elements-->
</androidx.constraintlayout.widget.ConstraintLayout>
```

In your code, use the `inlineAd` method to embed the AD into the container. Then, the SDK would resize your container when the AD is loaded and ready to show.

```java
public class DashboardFragment extends Fragment {

    private TenMaxAd inlineAd;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // other view setup code
        this.inlineAd = inlineAd("{inline-space-id}", this.getActivity(), this.binding.inlineAd);
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
```

## Advanced topics

### Timing and Naming Convention

AD on different pages would have different prices. To collect needed information, TenMax Mobile SDK would monitor your application and fragment lifecycle. Thus, to avoid disturbing information collection, please follow these conventions:

- Always show ADs when the activity or fragment resumed (`onResume`) to ensure the size layout is already finished
- Give a unique name to the fragment that plays the role of a page, e.g., `HomeFragment`, `DashboardFragment`, or `NotificationsFragment` in the previous samples.

If you do not follow the convention and SDK can not collect the correct information, the SDK will refuse to show AD in the unexpected case.

### Callback and Listener
Each method that shows AD has two optional parameters: listener and callback. The callback would be called immediately when the specified space ID is found or something wrong happened. You can provide a callback to know what happened during the setup.

You can provide the listener to listen all the events of the entire presentation lifecycle. Here is a simple listener to listen to three important events:
- `adViewableEventSent` - the user saw the AD for 1 second, and SDK would send viewable event to the TenMax server.
- `adLoadingTimeout` - the AD loading timeout (maybe network is too slow) so the presentation is cancelled.
- `adNotFound` - can not find an AD for the specified space so the presentation is cancelled.


```java
public class SimpleAdSessionListener implements TenMaxAdSessionListener {

    private final Context context;

    public SimpleAdSessionListener(Context context) {
        this.context = context;
    }

    @Override
    public void adViewableEventSent(TenMaxAdSession session) {
        Toast.makeText(this.context, "viewable event sent", 1).show();
    }

    @Override
    public void adLoadingTimeout(TenMaxAdSession session) {
        Toast.makeText(this.context, "AD loading timeout", 1).show();
    }

    @Override
    public void adNotFound(TenMaxAdSession session) {
        Toast.makeText(this.context, "received adNoFill event", 1).show();
    }
}
```

### Duplication Detection

For most ADs, the presentation must be unique on the page. Thus, TenMax Mobile SDK would track the presentation requests. If SDK found the duplication, it would show a warning message for the app developer to fix the case. Also, TenMax would review your app to ensure you follow TenMax's rules.

## Issues and Contact

If you have any issue when using TenMax Mobile SDK, please contact app_support@tenmax.io. We will help you as soon as possible.

## License

TenMax
