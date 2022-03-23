package com.ironsource.mediation.ironsource.example;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

public class MainActivity extends AppCompatActivity implements RewardedVideoListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // TODO: Place your App Key here
    private static final String APP_KEY = "APP_KEY";

    private Button mRewardedAdButton;
    private Placement mPlacement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIElements();
        initIronSource();
        updateButtonsState();
    }

    private void initIronSource() {
        IntegrationHelper.validateIntegration(this);
        IronSource.setRewardedVideoListener(this);
        IronSource.init(this, APP_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
        updateButtonsState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
        updateButtonsState();
    }

    private void updateButtonsState() {
        handleVideoButtonState(IronSource.isRewardedVideoAvailable());
    }

    private void handleVideoButtonState(boolean available) {
        mRewardedAdButton.setEnabled(available);
    }

    private void initUIElements() {
        mRewardedAdButton = findViewById(R.id.showRewardedAd);
        mRewardedAdButton.setOnClickListener(v -> onShowRewardedAdClick());
    }

    private void onShowRewardedAdClick() {
        if (IronSource.isRewardedVideoAvailable())
            IronSource.showRewardedVideo();
    }

    private void showToastMessage(Placement placement) {
        Toast.makeText(
                this,
                "You've received " + placement.getRewardAmount() + " " + placement.getRewardName(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed");

        if (mPlacement != null) {
            showToastMessage(mPlacement);
            mPlacement = null;
        }
    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean available) {
        handleVideoButtonState(available);
        Log.d(TAG, "onRewardedVideoAvailabilityChanged $available");
    }

    @Override
    public void onRewardedVideoAdStarted() {
        Log.d(TAG, "onRewardedVideoAdStarted");
    }

    @Override
    public void onRewardedVideoAdEnded() {
        Log.d(TAG, "onRewardedVideoAdEnded");
    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {
        Log.d(TAG, "onRewardedVideoAdRewarded $placement");
        mPlacement = placement;
    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
        Log.d(TAG, "onRewardedVideoAdShowFailed $ironSourceError");
    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {}

}