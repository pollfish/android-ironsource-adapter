package com.ironsource.adapters.custom.prodege

import android.app.Activity
import android.util.Log
import com.ironsource.adapters.custom.prodege.config.ProdegeIronSourceAdapterRequestParameters
import com.ironsource.adapters.custom.prodege.config.ProdegeIronSourceAdapterShowParameters
import com.ironsource.adapters.custom.prodege.utils.ProdegeAdMobAdapterUtils
import com.ironsource.adapters.custom.prodege.utils.ProdegeConstants
import com.ironsource.mediationsdk.adunit.adapter.BaseRewardedVideo
import com.ironsource.mediationsdk.adunit.adapter.listener.RewardedVideoAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.ironsource.mediationsdk.model.NetworkSettings
import com.prodege.Prodege
import com.prodege.builder.AdOptions
import com.prodege.builder.AdRequest
import com.prodege.listener.ProdegeEventListener
import com.prodege.listener.ProdegeException
import com.prodege.listener.ProdegeReward
import com.prodege.listener.ProdegeRewardListener
import com.prodege.listener.ProdegeRewardedInfo
import com.prodege.listener.ProdegeRewardedLoadListener
import com.prodege.listener.ProdegeShowListener

class ProdegeCustomRewardedVideo(networkSettings: NetworkSettings) :
    BaseRewardedVideo<ProdegeCustomAdapter>(networkSettings), ProdegeEventListener,
    ProdegeRewardListener {

    private var adapterListener: RewardedVideoAdListener? = null

    override fun loadAd(adData: AdData, activity: Activity, listener: RewardedVideoAdListener) {
        if (!ProdegeAdMobAdapterUtils.isSdkSupported()) {
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_LOW_TARGET,
                "Prodege SDK will not run on targets lower than 21."
            )
            return
        }

        val adRequestConfiguration = ProdegeIronSourceAdapterRequestParameters.fromAdData(adData)

        if (adRequestConfiguration == null) {
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION,
                ProdegeException.EmptyPlacementId.message
            )
            return
        }

        if (Prodege.isPlacementVisible(adRequestConfiguration.placementId)) {
            listener.onAdLoadFailed(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_UNSPECIFIED,
                null
            )
            return
        }

        val adRequest = AdRequest.Builder()

        adRequestConfiguration.requestUuid?.let {
            adRequest.requestUuid(it)
        }

        adRequestConfiguration.surveyFormat?.let {
            adRequest.surveyFormat(it)
        }

        Log.d(ProdegeConstants.TAG, "Loading Prodege Ads with $adRequestConfiguration.")

        Prodege.loadRewardedAd(
            adRequestConfiguration.placementId,
            object : ProdegeRewardedLoadListener {
                override fun onRewardedLoadFailed(placementId: String, exception: ProdegeException) {
                    val (errorType, errorCode) = ProdegeAdMobAdapterUtils.asAdapterError(exception)
                    listener.onAdLoadFailed(errorType, errorCode, exception.message)
                }

                override fun onRewardedLoaded(placementId: String, info: ProdegeRewardedInfo) {
                    adapterListener = listener
                    Prodege.setRewardListener(this@ProdegeCustomRewardedVideo)
                    Prodege.setEventListener(this@ProdegeCustomRewardedVideo)
                    listener.onAdLoadSuccess()
                }
            },
            adRequest.build()
        )
    }

    override fun showAd(adData: AdData, listener: RewardedVideoAdListener) {
        if (!ProdegeAdMobAdapterUtils.isSdkSupported()) {
            listener.onAdShowFailed(
                ProdegeCustomAdapter.ERROR_LOW_TARGET,
                "Prodege SDK will not run on targets lower than 21."
            )
            return
        }

        val adShowConfiguration = ProdegeIronSourceAdapterShowParameters.fromAdData(adData)

        if (adShowConfiguration == null) {
            listener.onAdShowFailed(
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION,
                ProdegeException.EmptyPlacementId.message
            )
            return
        }

        val adOptions = AdOptions.Builder()

        adShowConfiguration.muted?.let {
            adOptions.muted(it)
        }

        Prodege.showPlacement(adShowConfiguration.placementId, object : ProdegeShowListener {
            override fun onClosed(placementId: String) {
                listener.onAdClosed()
            }

            override fun onOpened(placementId: String) {
                listener.onAdOpened()
            }

            override fun onShowFailed(placementId: String, exception: ProdegeException) {
                val (_, errorCode) = ProdegeAdMobAdapterUtils.asAdapterError(exception)
                listener.onAdShowFailed(errorCode, exception.message)
            }
        }, adOptions.build())
    }

    override fun isAdAvailable(adData: AdData): Boolean {
        if (!ProdegeAdMobAdapterUtils.isSdkSupported()) {
            Log.w(ProdegeConstants.TAG, "Prodege SDK will not run on targets lower than 21.")
            return false
        }

        return ProdegeIronSourceAdapterRequestParameters.fromAdData(adData)?.let {
            Prodege.isPlacementLoaded(it.placementId)
        } ?: false
    }

    override fun onClick(placementId: String) {
        adapterListener?.onAdClicked()
    }

    override fun onStart(placementId: String) {
        adapterListener?.onAdStarted()
    }

    override fun onComplete(placementId: String) {
        adapterListener?.onAdEnded()
    }

    override fun onUserNotEligible(placementId: String) {
        adapterListener?.onAdEnded()
    }

    override fun onUserReject(placementId: String) {
        adapterListener?.onAdEnded()
    }

    override fun onRewardReceived(reward: ProdegeReward) {
        adapterListener?.onAdRewarded()
    }

}