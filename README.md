# Prodege Android IronSource Mediation Adapter

IronSource Mediation Adapter for Android apps looking to load and show Rewarded ads from Prodege in the same waterfall with other Rewarded Ads.

> **Note:** A detailed step by step guide is provided on how to integrate can be found [here](https://www.pollfish.com/docs/android-ironsource-adapter)

<br/>

## Add Prodege IronSource Adapter to your project

Retrieve Prodege IronSource Adapter through **maven()** with gradle by adding the following line in your app's module **build.gradle** file:

```groovy
dependencies {
    implementation 'com.prodege.mediation:prodege-ironsource:7.0.0-beta01.0'
}
```

<br/>

## Request for a RewardedAd

Import the following packages.

<span style="text-decoration:underline">Kotlin</span>

```kotlin
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
```

<span style="text-decoration:underline">Java</span>

```java
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
```

<br/>Initialize IronSource SDK by calling the `init` method, passing the `Activity` and your App Key as provided by the IronSource dashboard. Do this as soon as possible after your app starts, for example in the `onCreate()` method of your launch Activity.

<span style="text-decoration:underline">Kotlin</span>

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ...

    IntegrationHelper.validateIntegration(this)
    IronSource.setRewardedVideoListener(this)
    IronSource.init(this, "APP_KEY")
}
```

<span style="text-decoration:underline">Java</span>

```java
protected void onCreate(Bundle savedInstanceState) {
    ...

    IntegrationHelper.validateIntegration(this);
    IronSource.setRewardedVideoListener(this);
    IronSource.init(this, "APP_KEY");
}
```

<br/>

Implement `RewardedVideoListener` so that you are notified when your ad is ready and of other ad-related events.

<span style="text-decoration:underline">Kotlin</span>

```kotlin
class MainActivity : AppCompatActivity(), RewardedVideoListener
```

<span style="text-decoration:underline">Java</span>

```java
class MainActivity extends AppCompatActivity implements RewardedVideoListener
```

<br/>

When the Rewarded Ad is ready, present the ad by invoking `IronSource.showRewardedVideo`. Just to be sure, you can combine show with a check to see if the Ad you are about to show is actualy ready.

<span style="text-decoration:underline">Kotlin</span>

```kotlin
if (IronSource.isRewardedVideoAvailable())
    IronSource.showRewardedVideo()
```

<span style="text-decoration:underline">Java</span>

```java
if (IronSource.isRewardedVideoAvailable())
    IronSource.showRewardedVideo();
```

<br/>

You can view a short example on how to intergate rewarded ads below.

<span style="text-decoration:underline">Kotlin</span>

```kotlin
class MainActivity : AppCompatActivity(), RewardedVideoListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        // ...

        IntegrationHelper.validateIntegration(this)
        IronSource.setRewardedVideoListener(this)
        IronSource.init(this, "APP_KEY")
    }

    override fun onResume() {
        // ...

        IronSource.onResume(this)
    }

    override fun onPause() {
        // ...

        IronSource.onPause(this)
    }

    fun onShowRewardedAdClick(view: View) {
        if (IronSource.isRewardedVideoAvailable())
            IronSource.showRewardedVideo()
    }

    override fun onRewardedVideoAdOpened() {}

    override fun onRewardedVideoAdClosed() {}

    override fun onRewardedVideoAvailabilityChanged(available: Boolean) {}

    override fun onRewardedVideoAdStarted() {}

    override fun onRewardedVideoAdEnded() {}

    override fun onRewardedVideoAdRewarded(placement: Placement) {}

    override fun onRewardedVideoAdShowFailed(ironSourceError: IronSourceError?) {}

    override fun onRewardedVideoAdClicked(placement: Placement) {}

}
```

<span style="text-decoration:underline">Java</span>

```java
class MainActivity extends AppCompatActivity implements RewardedVideoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...

        IntegrationHelper.validateIntegration(this);
        IronSource.setRewardedVideoListener(this);
        IronSource.init(this, "APP_KEY");
    }

    @Override
    protected void onResume() {
        // ...
        
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        // ...

        IronSource.onPause(this);
    }

    void onShowRewardedAdClick(View view) {
        if (IronSource.isRewardedVideoAvailable())
            IronSource.showRewardedVideo();
    }
    
    @Override
    public void onRewardedVideoAdOpened() {}

    @Override
    public void onRewardedVideoAdClosed() {}

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean available) {}

    @Override
    public void onRewardedVideoAdStarted() {}

    @Override
    public void onRewardedVideoAdEnded() {}

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {}

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {}

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {}

}
```

<br/>

## Configure the Prodege SDK programmatically

Prodege IronSource Adapter provided a couple of options you can use to control the behaviour of Prodege SDK. Any configuration, if applied, will override the corresponding configuration done in IronSource's dashboard.

### **6.1. `.setUserId(String)`**

An optional id used to identify a user.

Setting the Prodege's `userId` will override the default behaviour and use that instead of the Advertising Id in order to identify a user.

> **Note:** <span style="color: red">You can pass the id of a user as identified on your system. Prodege will use this id to identify the user across sessions instead of an ad id/idfa as advised by the stores. You are solely responsible for aligning with store regulations by providing this id and getting relevant consent by the user when necessary. Prodege takes no responsibility for the usage of this id. In any request from your users on resetting/deleting this id and/or profile created, you should be solely liable for those requests.</span>

<span style="text-decoration:underline">Kotlin</span>

```kotlin
ProdegeCustomAdapter.setUserId("USER_ID")
```

<span style="text-decoration:underline">Java</span>

```java
ProdegeCustomAdapter.setUserId("USER_ID");
```

<br/>

### **6.2. `.setTestMode(Boolean)`**

- **`true`** is used to show to the developer how Prodege ads will be shown through an app (useful during development and testing).
- **`false`** is the mode to be used for a released app in any app store (start receiving paid surveys).

If you have already specified the test mode on IronSource's UI, this will override the one defined on Web UI.

Prodege IronSource Adapter works by default in live mode. If you would like to test with test ads:

<span style="text-decoration:underline">Kotlin</span>

```kotlin
ProdegeCustomAdapter.setTestMode(true)
```

<span style="text-decoration:underline">Java</span>

```java
ProdegeCustomAdapter.setTestMode(true);
```

<br/>

## Proguard

If you use proguard with your app, please insert the following lines in your proguard configuration file:

```java
-dontwarn com.prodege.**
-keep class com.prodege.** { *; }
```

<br/>

## Publish

If everything worked fine during the previous steps, you are ready to proceed with publishing your app.

> **Note:** After you take your app live, you should request your account to get verified through the [Publisher Dashboard](https://www.pollfish.com/publisher/) in the App Settings area.

> **Note:** There is an option to show **Standalone Demographic Questions** needed for Prodege to target users with surveys even when no actually surveys are available. Those surveys do not deliver any revenue to the publisher (but they can increase fill rate) and therefore if you do not want to show such surveys in the Waterfall you should visit your **App Settings** are and disable that option. You can read more [here](https://www.pollfish.com/docs/demographic-surveys).

# More info

You can read more info on how the Pollfish SDKs work or how to get started with IronSource at the following links:

[Prodege iOS SDK](https://pollfish.com/docs/android)

[IronSource iOS SDK](https://developers.is.com/ironsource-mobile/android/android-sdk)