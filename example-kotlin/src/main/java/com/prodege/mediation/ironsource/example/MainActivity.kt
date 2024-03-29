package com.prodege.mediation.ironsource.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ironsource.adapters.custom.prodege.ProdegeCustomAdapter
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
import com.prodege.mediation.ironsource.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), RewardedVideoListener {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initUIElements()
        initIronSource()
        updateButtonsState()
    }

    private fun initIronSource() {
        IntegrationHelper.validateIntegration(this)
        IronSource.setRewardedVideoListener(this)

        // Optional
        ProdegeCustomAdapter.setTestMode(true)
        ProdegeCustomAdapter.setUserId("USER_ID")

        IronSource.init(this, getString(R.string.iron_source_app_id))
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
        updateButtonsState()
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
        updateButtonsState()
    }

    private fun updateButtonsState() {
        handleVideoButtonState(IronSource.isRewardedVideoAvailable())
    }

    private fun handleVideoButtonState(available: Boolean) {
        binding.showRewardedAd.isEnabled = available
    }

    private fun initUIElements() {
        binding.showRewardedAd.setOnClickListener {
            if (IronSource.isRewardedVideoAvailable())
                IronSource.showRewardedVideo()
        }
    }

    override fun onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened")
    }

    override fun onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed")
        initIronSource()
    }

    override fun onRewardedVideoAvailabilityChanged(available: Boolean) {
        handleVideoButtonState(available)
        Log.d(TAG, "onRewardedVideoAvailabilityChanged $available")
    }

    override fun onRewardedVideoAdStarted() {
        Log.d(TAG, "onRewardedVideoAdStarted")
    }

    override fun onRewardedVideoAdEnded() {
        Log.d(TAG, "onRewardedVideoAdEnded")
    }

    override fun onRewardedVideoAdRewarded(placement: Placement) {
        val message = "onRewardedVideoAdRewarded: ${placement.rewardAmount} ${placement.rewardName}"
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
        Log.d(TAG, message)
    }

    override fun onRewardedVideoAdShowFailed(ironSourceError: IronSourceError?) {
        Log.d(TAG, "onRewardedVideoAdShowFailed $ironSourceError")
    }

    override fun onRewardedVideoAdClicked(placement: Placement) {
        Log.d(TAG, "onRewardedVideoAdClicked")
    }

}