package com.ironsource.adapters.custom.pollfish

import android.util.Log
import com.ironsource.adapters.custom.pollfish.BuildConfig.DEBUG
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData

data class AdapterInfo(
    val apiKey: String,
    val releaseMode: Boolean?,
    val offerwallMode: Boolean?,
    val requestUUID: String?
) {

    override fun toString(): String =
        "{api_key: $apiKey, release_mode: ${releaseMode ?: false}, offerwall_mode: ${offerwallMode ?: false}, request_mode: $requestUUID}"

    companion object {
        fun fromAdData(adData: AdData): AdapterInfo? {
            val apiKey = adData.getString(PollfishConstants.POLLFISH_API_KEY_AD_DATA_KEY)
            val releaseMode =
                adData.getString(PollfishConstants.POLLFISH_RELEASE_MODE_AD_DATA_KEY).toBoolean()
            val requestUUID = adData.getString(PollfishConstants.POLLFISH_REQUEST_UUID_AD_DATA_KEY)
            val offerwallMode =
                adData.getString(PollfishConstants.POLLFISH_OFFERWALL_MODE_AD_DATA_KEY).toBoolean()

            return apiKey?.let {
                AdapterInfo(
                    it,
                    releaseMode,
                    offerwallMode,
                    if (requestUUID.isNullOrEmpty() || requestUUID == "null") null else requestUUID
                )
            }.apply {
                if (DEBUG)
                    Log.d(
                        "PollfishAdapterInfo",
                        "Initializing Pollfish with the following params: $this"
                    )
            }
        }
    }

}
