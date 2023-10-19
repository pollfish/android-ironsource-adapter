package com.ironsource.adapters.custom.prodege

import android.content.Context
import android.util.Log
import androidx.annotation.IntDef
import com.ironsource.adapters.custom.prodege.config.ProdegeIronSourceAdapterInitializationParameters
import com.ironsource.adapters.custom.prodege.utils.ProdegeAdMobAdapterUtils
import com.ironsource.adapters.custom.prodege.utils.ProdegeConstants
import com.ironsource.mediationsdk.adunit.adapter.BaseAdapter
import com.ironsource.mediationsdk.adunit.adapter.listener.NetworkInitializationListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.prodege.Prodege
import com.prodege.builder.InitOptions
import com.prodege.builder.Platform
import com.prodege.listener.ProdegeException
import com.prodege.listener.ProdegeInitListener

class ProdegeCustomAdapter : BaseAdapter() {

    @Target(AnnotationTarget.TYPE)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        value = [
            ERROR_LOW_TARGET,
            ERROR_MISSING_SERVER_PARAMETERS,
            ERROR_INVALID_CONFIGURATION,
            ERROR_NOT_INITIALIZED,
            ERROR_INTERNAL,
            ERROR_NO_CONNECTION,
            ERROR_NO_FILL,
            ERROR_PLACEMENT_ALREADY_LOADED,
            ERROR_UNSPECIFIED
        ]
    )
    annotation class AdapterError

    companion object {
        const val ERROR_LOW_TARGET = 101
        const val ERROR_MISSING_SERVER_PARAMETERS = 102
        const val ERROR_INVALID_CONFIGURATION = 103
        const val ERROR_NOT_INITIALIZED = 104
        const val ERROR_INTERNAL = 105
        const val ERROR_NO_CONNECTION = 106
        const val ERROR_NO_FILL = 107
        const val ERROR_PLACEMENT_ALREADY_LOADED = 108
        const val ERROR_UNSPECIFIED = 109

        private var userId: String? = null
        private var testMode: Boolean? = null

        @JvmStatic
        fun setUserId(userId: String) {
            this.userId = userId
        }

        @JvmStatic
        fun setTestMode(testMode: Boolean) {
            this.testMode = testMode
        }
    }

    override fun init(adData: AdData, context: Context, listener: NetworkInitializationListener?) {
        if (!ProdegeAdMobAdapterUtils.isSdkSupported()) {
            listener?.onInitFailed(
                ERROR_LOW_TARGET,
                "Prodege SDK will not run on targets lower than 21."
            )
            return
        }

        val adapterInfo =
            ProdegeIronSourceAdapterInitializationParameters.fromAdData(adData, userId, testMode)

        if (adapterInfo == null) {
            listener?.onInitFailed(
                ERROR_INVALID_CONFIGURATION,
                ProdegeException.EmptyApiKey.message
            )
            return
        }

        Log.d(ProdegeConstants.TAG, "Initializing Prodege SDK with $adapterInfo.")

        val initOptions = InitOptions.Builder()
            .platform(Platform.IRON_SOURCE)

        adapterInfo.testMode?.let {
            initOptions.testMode(it)
        }

        adapterInfo.userId?.let {
            initOptions.userId(it)
        }

        Prodege.initialize(context, adapterInfo.apiKey, object : ProdegeInitListener {
            override fun onError(exception: ProdegeException) {
                val (_, adapterError) = ProdegeAdMobAdapterUtils.asAdapterError(exception)
                listener?.onInitFailed(adapterError, exception.message)
            }

            override fun onSuccess() {
                listener?.onInitSuccess()
            }
        }, initOptions.build())
    }

    override fun getNetworkSDKVersion(): String =
        BuildConfig.PRODEGE_SDK_VERSION

    override fun getAdapterVersion(): String =
        BuildConfig.ADAPTER_VERSION

}