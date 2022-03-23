package com.ironsource.adapters.custom.pollfish

import android.app.Activity
import android.util.Log
import com.ironsource.mediationsdk.adunit.adapter.BaseRewardedVideo
import com.ironsource.mediationsdk.adunit.adapter.listener.RewardedVideoAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrors
import com.ironsource.mediationsdk.model.NetworkSettings
import com.pollfish.Pollfish
import com.pollfish.builder.Params
import com.pollfish.builder.Platform
import com.pollfish.callback.*

class PollfishCustomRewardedVideo(networkSettings: NetworkSettings) :
    BaseRewardedVideo<PollfishCustomAdapter>(networkSettings),
    PollfishClosedListener,
    PollfishOpenedListener,
    PollfishSurveyCompletedListener,
    PollfishSurveyNotAvailableListener,
    PollfishSurveyReceivedListener,
    PollfishUserNotEligibleListener,
    PollfishUserRejectedSurveyListener {

    companion object {
        val TAG: String = PollfishCustomRewardedVideo::class.java.simpleName
    }

    private var adapterListener: RewardedVideoAdListener? = null

    override fun loadAd(adData: AdData, activity: Activity, listener: RewardedVideoAdListener) {

        if (!PollfishHelper.isPollfishSupported) {
            Log.w(TAG, PollfishAdapterError.VersionBelowMinimum.message)
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                PollfishAdapterError.VersionBelowMinimum.code,
                PollfishAdapterError.VersionBelowMinimum.message
            )
            return
        }

        if (Pollfish.isPollfishPanelOpen()) {
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                PollfishAdapterError.PanelAlreadyOpen.code,
                PollfishAdapterError.PanelAlreadyOpen.message
            )
            return
        }

        val adapterInfo = AdapterInfo.fromAdData(adData)

        if (adapterInfo == null) {
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                PollfishAdapterError.MissingApiKey.code,
                PollfishAdapterError.MissingApiKey.message
            )
            return
        }

        val params = Params.Builder(adapterInfo.apiKey)
            .apply {
                (adapterInfo.requestUUID?.let {
                    this.requestUUID(it)
                })
                (adapterInfo.offerwallMode?.let {
                    this.offerwallMode(it)
                })
                (adapterInfo.releaseMode?.let {
                    this.releaseMode(it)
                })
            }
            .rewardMode(true)
            .pollfishClosedListener(this)
            .pollfishOpenedListener(this)
            .pollfishSurveyCompletedListener(this)
            .pollfishSurveyNotAvailableListener(this)
            .pollfishUserNotEligibleListener(this)
            .pollfishUserRejectedSurveyListener(this)
            .pollfishSurveyReceivedListener(this)
            .platform(Platform.MAX)
            .build()

        Pollfish.initWith(activity, params)

        this.adapterListener = listener
    }

    override fun showAd(adData: AdData, listener: RewardedVideoAdListener) {
        if (!PollfishHelper.isPollfishSupported) {
            Log.w(TAG, PollfishAdapterError.VersionBelowMinimum.message)
            return
        }

        when {
            Pollfish.isPollfishPanelOpen() -> listener.onAdShowFailed(
                AdapterErrors.ADAPTER_ERROR_INTERNAL,
                PollfishAdapterError.PanelAlreadyOpen.message
            )
            Pollfish.isPollfishPresent() -> Pollfish.show()
            else -> listener.onAdShowFailed(
                AdapterErrors.ADAPTER_ERROR_INTERNAL,
                PollfishAdapterError.NotLoaded.message
            )
        }
    }

    override fun isAdAvailable(adData: AdData): Boolean {
        if (!PollfishHelper.isPollfishSupported) {
            Log.w(TAG, PollfishAdapterError.VersionBelowMinimum.message)
            return false
        }

        return Pollfish.isPollfishPresent()
    }

    override fun onPollfishClosed() {
        adapterListener?.onAdClosed()
    }

    override fun onPollfishOpened() {
        adapterListener?.onAdOpened()
    }

    override fun onPollfishSurveyCompleted(surveyInfo: SurveyInfo) {
        adapterListener?.onAdEnded()
        adapterListener?.onAdRewarded()
    }

    override fun onPollfishSurveyNotAvailable() {
        adapterListener?.onAdLoadFailed(
            AdapterErrorType.ADAPTER_ERROR_TYPE_NO_FILL,
            PollfishAdapterError.NotAvailable.code,
            PollfishAdapterError.NotAvailable.message
        )
    }

    override fun onPollfishSurveyReceived(surveyInfo: SurveyInfo?) {
        adapterListener?.onAdLoadSuccess()
    }

    override fun onUserNotEligible() {
        if (Pollfish.isPollfishPanelOpen()) {
            adapterListener?.onAdEnded()
        } else {
            adapterListener?.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_NO_FILL,
                PollfishAdapterError.NotEligible.code,
                PollfishAdapterError.NotEligible.message
            )
        }
    }

    override fun onUserRejectedSurvey() {
        adapterListener?.onAdEnded()
    }
}