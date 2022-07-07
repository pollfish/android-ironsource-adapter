# Pollfish Android IronSource Mediation Adapter

IronSource Mediation Adapter for Android apps looking to load and show Rewarded Surveys from Pollfish in the same waterfall with other Rewarded Ads.

> **Note:** A detailed step by step guide is provided on how to integrate can be found [here](https://www.pollfish.com/docs/android-ironsource-adapter)

<br/>

## Step 1: Add Pollfish IronSource Adapter to your project

Download the following libraries

* [Pollfish SDK](https://www.pollfish.com/docs/android/google-play)
* [IronSource SDK](https://developers.is.com/ironsource-mobile/android/android-sdk/)
* [PollfishIronSourceAdapter](https://pollfish.com/docs/android-ironsource-adapter)

Import Pollfish IronSource adapter **.aar** file as it can be found in the **pollfish-ironsource-aar** folder, into your project libraries. Also import the **pollfish-googleplay-xxx.aar** which can be found [here](https://pollfish.com/docs/android/google-play)

If you are using Android Studio, right click on your project and select New Module. Then select Import .jar or .aar Package option and from the file browser locate Pollfish IronSource Adapter aar file. Right click again on your project and in the Module Dependencies tab choose to add Pollfish module that you recently added, as a dependency.

**OR**

#### **Retrieve Pollfish IronSource Adapter through mavenCentral()**

Retrieve Pollfish IronSource Adapter through **mavenCentral()** with gradle by adding the following line in your project **build.gradle** (not the top level one, the one under 'app') in dependencies section:

```groovy
dependencies {
    implementation 'com.pollfish.mediation:pollfish-ironsource:6.2.4.1'
}
```

<br/>

## Step 2: Request for a RewardedAd

Import the following packages

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

<br/>

Initialize IronSource SDK by calling the `init` method, passing the `Activity` and your App Key as provided by the IronSource dashboard. Do this as soon as possible after your app starts, for example in the `onCreate()` method of your launch Activity.

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

You can view a short example on how to intergate rewarded ads below

<span style="text-decoration:underline">Kotlin</span>

```kotlin
class MainActivity : AppCompatActivity(), RewardedVideoListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        IntegrationHelper.validateIntegration(this)
        IronSource.setRewardedVideoListener(this)
        IronSource.init(this, "APP_KEY")
    }

    override fun onResume() {
        ...

        IronSource.onResume(this)
    }

    override fun onPause() {
        ...

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
        ...

        IntegrationHelper.validateIntegration(this);
        IronSource.setRewardedVideoListener(this);
        IronSource.init(this, "APP_KEY");
    }

    @Override
    protected void onResume() {
        ...
        
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        ...

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

## Step 3: Publish

If you everything worked fine during the previous steps, you should turn Pollfish to release mode and publish your app.

> **Note:** After you take your app live, you should request your account to get verified through Pollfish Dashboard in the App Settings area.

> **Note:** There is an option to show **Standalone Demographic Questions** needed for Pollfish to target users with surveys even when no actually surveys are available. Those surveys do not deliver any revenue to the publisher (but they can increase fill rate) and therefore if you do not want to show such surveys in the Waterfall you should visit your **App Settings** are and disable that option.

# More info

You can read more info on how the Pollfish SDKs work or how to get started with IronSource at the following links:

[Pollfish iOS SDK](https://pollfish.com/docs/android/google-play)

[IronSource iOS SDK](https://developers.is.com/ironsource-mobile/android/android-sdk)