# TenMax Mobile SDK for Android

This repository provides the guideline and examples to demostrate how to use TenMax Mobile SDK to show AD on your Android app.

## Prerequisites

Before using the SDK, please contact TenMax (contact@tenmax.io) to

- register you app bundle ID
- obtain you app publisher ID
- a user name and an read-only access token to import the SDK from GitHub Packages

The first two values would be used to initiate the SDK.

## Get Started

Follow the steps to use the TenMax Mobile SDK:

### Install SDK

There are two ways to install the SDK, you can choose one to meet your case.

#### Import from GitHub Packages

Add the following lines into the `repositories` section in your project gradle setting  (`<project-root>/<your-app>/build.gradle.kts`).

```gradle
maven {
    url = uri("https://maven.pkg.github.com/dbi1463/mobile-sdk")
    credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
        password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
    }
}
```

Add the `gpr.user` and `gpr.token` (obtained from [Prerequisites](#prerequisites)) into `local.propeerties` and make sure this file not committed to your repository. Please pass these two values with the secret environment variables (`GITHUB_ACTOR` and `GITHUB_TOKEN`) on the CI server.

```
gpr.user=xxx
gpr.token=xxx
```

Then, add the library to your project dependencies:

```gradle
dependencies {
    implementation("io.tenmax:mobile-sdk:1.0.0")
}
```

#### Import AAR

You can download the AAR file from the release and put the AAR file into `<project-root>/<your-app>/libs`. However, the SDK needs other libraries (GSON, Retrofit, and AD identifier) to work, thus, please add the SDK and required libraries together:

```gradle
dependencies {
    implementation(files("$projectDir/libs/adkit.aar"))
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.ads.identifier)
}
```

In this way, you do not need the user name and token, but you need to download the new AAR file if the SDK is updated. You can see the setting in the [feat/build-with-aar](https://github.com/dbi1463/mobile-sdk/tree/feat/build-with-aar) branch.

### SDK Configuration

Open your app's manifest file (`AndroidManifest.xml`) and add `tenmax-publisher-id` and `tenmax-bundle-id` as the application's metadata. The publisher ID is provided by TenMax and the bundle ID must be the same as the value you registered in the [Prerequisites](#prerequisites) section.

```xml
<manifest>
    <application>
        <meta-data android:name="tenmax-publisher-id" android:value="{tenmax-publisher-id}" />
        <meta-data android:name="tenmax-bundle-id" android:value="{app-bundle-id}" />
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

If you do not initiate the SDK, every call to show ADs, the SDK would give you an error. The SDK initiation would load the AD information from the TenMax's server and try to obtain the Android advertising ID (AAID) for you. Now, you are ready to show AD.

## Show ADs

### Interstitial AD

First, let show an interstitial AD (fullscreen AD) when your first activity started. Assume your application's `MainActivity` has three tabs: `HomeFragment`, `DashboardFragment`, and `NotificationFragment` (a sample project created by Android Studio). In the `HomeFragment`, add following lines to show an interstitial AD when Home fragment resumed:

```java
public class HomeFragment extends Fragment {

    private TenMaxAdDisposable fullscreenAd;

    @Override
    public void onResume() {
        super.onResume();
        this.fullscreenAd = presentAd("{interstitial-space-id}", this.getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.fullscreenAd);
    }
}

```

The AD presentation is asynchronous, thus, please keep the `TenMaxAdDisposable` object returned from the `presentAd` method. You can dispose (cancel) the presentation when the user switch to other tabs by calling `cleanAd` method (or calling `disposable.dipose()`). The SDK would cancel the presentation (if not presented yet) or remove the presentation and then clean up resources to reduce the memory usage.

### Banner AD

You can show a banner AD on top of the screen or bottom of the screen. Even more, you can show both top and bottom banner on the same screen. However, the relationship between the banner and your app's UI is up to you. For example, you can let the top banner is hover on your app's UI. Thus, you need to add a container in your layout file to determine the relationship, like this.

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

Back to your code, call the `showBannerAd` method to let SDK show the banner AD in the specified container.

```java
public class NotificationsFragment extends Fragment {

    private TenMaxAdDisposable topBannerAd;
    private FragmentNotificationsBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        this.topBannerAd = showBannerAd("{banner-space-id}", this.getActivity(), this.binding.topBanner1, TenMaxBannerPosition.top);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cleanAd(this.topBannerAd);
    }
}
```

Yes, you decide the container's position in the layout file, but you still need to provide the position information (`TenMaxBannerPosition.top`) when calling `showBannerAd`. The SDK would use this information to avoid duplication (see [Duplication Detection](#duplication-detection)).

### Inline AD

To embed an inline AD into your app, you need to add a container into your layout file. For example, in the `fragment_dashboard.xml`, add a `FrameLayout` as the container and setup its layout. As shown, it uses `wrap_content` as `layout_width` and `layout_height` to show a zero-size container, uses `center_horizontal` as `layout_gravity` to centerize the container, and then sets `paddingBottom` to `10dp` to give some space to other UI elements.

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

In your code, use the `embedAd` method to embed the AD into the container. Then, the SDK would resize your container when the AD is loaded and ready to show.

```java
public class DashboardFragment extends Fragment {

    private TenMaxAdDisposable inlineAd;
    private FragmentDashboardBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        this.inlineAd = embedAd("{inline-space-id}", this.getActivity(), this.binding.inlineAd);
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

AD on different pages would have different prices. To collect needed information, TenMax Mobile SDK would monitor your application and fragment lifecycle. Thus, to aovid disturbing infomration collection, please follow these convention:

- Always show ADs when the activity or fragment resumed (`onResume`) to ensure the size layout is already finished
- Give unique name to the fragment that plays the role like a page, e.g., `HomeFragment`, `DashboardFragment`, or `NotificationsFragment` in the previous samples.

If you do not follow the convention and SDK can not collect the correct information, the SDK would refuse to show AD in the unexpected case.

### Callback and Listener
Each method that show AD has two optional parameters: listener and callback. The callback would be called immediately when the specified space ID is found or something wrong happened. You can provide the callback to know what happened during the setup.

You can provide the listener to listen all the events of the entire presentation lifecycle. Here is a simple listener to listen three important events:
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

For most of ADs, the presentation must be unique on page. Thus, TenMax Mobile SDK would track the presentation requests. If SDK found the duplication, it would show the warning message for the app developer to fix the case. Also, TenMax would review your app to ensure you follow TenMax's rules.

## Issues and Contact

If you have any issue when using TenMax Mobile SDK, please contact contact@tenmax.io. We would help you as soon as possible.

## License

??
