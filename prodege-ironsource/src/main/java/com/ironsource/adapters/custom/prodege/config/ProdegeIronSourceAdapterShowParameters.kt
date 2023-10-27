package com.ironsource.adapters.custom.prodege.config

import com.ironsource.adapters.custom.prodege.utils.ProdegeConstants
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData

data class ProdegeIronSourceAdapterShowParameters(
    val placementId: String,
    val muted: Boolean?
) {

    override fun toString(): String {
        return "ProdegeIronSourceAdapterShowParameters(placementId=$placementId, muted=$muted)"
    }

    companion object {
        fun fromAdData(adData: AdData): ProdegeIronSourceAdapterShowParameters? {
            val remotePlacementKey =
                adData.getString(ProdegeConstants.REMOTE_PLACEMENT_ID_AD_DATA_KEY)
                    ?.let { if (it == "null") null else it }
                    ?.ifBlank { null }

            val remoteMuted =
                adData.getString(ProdegeConstants.REMOTE_MUTED_AD_DATA_KEY)?.let {
                    if (it == "null") null else it
                }.toBoolean()

            return remotePlacementKey?.let { if (it == "null") null else it }?.let {
                ProdegeIronSourceAdapterShowParameters(
                    it,
                    remoteMuted
                )
            }
        }
    }
}