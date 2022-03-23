package com.pollfish.mediation.ironsource

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
import com.pollfish.mediation.ironsource.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), RewardedVideoListener {

    companion object {
        // TODO: Place your App Key here
        const val APP_KEY = "APP_KEY"
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private var placement: Placement? = null

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
        IronSource.init(this, APP_KEY)
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

    private fun showToastMessage(placement: Placement) {
        Toast.makeText(
            this,
            "You've received ${placement.rewardAmount} ${placement.rewardName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened")
    }

    override fun onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed")

        placement?.let {
            showToastMessage(it)
            placement = null
        }
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
        Log.d(TAG, "onRewardedVideoAdRewarded $placement")
        this.placement = placement
    }

    override fun onRewardedVideoAdShowFailed(ironSourceError: IronSourceError?) {
        Log.d(TAG, "onRewardedVideoAdShowFailed $ironSourceError")
    }

    override fun onRewardedVideoAdClicked(placement: Placement) {}

}