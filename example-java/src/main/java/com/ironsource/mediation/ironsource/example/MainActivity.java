package com.ironsource.mediation.ironsource.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.adapters.custom.prodege.ProdegeCustomAdapter;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.prodege.mediation.ironsource.example.R;

public class MainActivity extends AppCompatActivity implements RewardedVideoListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mRewardedAdButton;

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

        // Optional
        ProdegeCustomAdapter.setTestMode(true);
        ProdegeCustomAdapter.setUserId("USER_ID");

        IronSource.init(this, getString(R.string.iron_source_app_id));
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

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed");
        initIronSource();
    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean available) {
        handleVideoButtonState(available);
        Log.d(TAG, String.format("onRewardedVideoAvailabilityChanged: %s", available ? "Available" : "Unavailable"));
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
        @SuppressLint("DefaultLocale") String message =
                String.format("onRewardedVideoAdRewarded: %d %s", placement.getRewardAmount(), placement.getRewardName());
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
        Log.d(TAG, message);
    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
        Log.d(TAG, "onRewardedVideoAdShowFailed $ironSourceError");
    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {
        Log.d(TAG, "onRewardedVideoAdClicked");
    }

}