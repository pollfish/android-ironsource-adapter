package com.ironsource.adapters.custom.prodege.config

import com.ironsource.adapters.custom.prodege.utils.ProdegeConstants
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData

internal data class ProdegeIronSourceAdapterInitializationParameters(
    val apiKey: String,
    val userId: String?,
    val testMode: Boolean?
) {

    override fun toString(): String =
        "ProdegeIronSourceAdapterInitializationParameters(apiKey=$apiKey, userId=$userId, testMode=$testMode)"

    companion object {
        fun fromAdData(
            adData: AdData,
            localUserId: String?,
            localTestMode: Boolean?
        ): ProdegeIronSourceAdapterInitializationParameters? {
            return adData.getString(ProdegeConstants.REMOTE_API_KEY_AD_DATA_KEY)
                ?.let { if (it == "null") null else it }
                ?.ifBlank { null }?.let { apiKey ->
                    val testMode = localTestMode
                        ?: adData.getString(ProdegeConstants.REMOTE_TEST_MODE_AD_DATA_KEY)
                            ?.let { if (it == "null") null else it }
                            ?.toBoolean()

                    ProdegeIronSourceAdapterInitializationParameters(
                        apiKey,
                        localUserId,
                        testMode
                    )
                }
        }
    }

}
