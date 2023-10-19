package com.ironsource.adapters.custom.prodege.config

import com.ironsource.adapters.custom.prodege.utils.ProdegeConstants
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData
import com.prodege.builder.SurveyFormat

data class ProdegeIronSourceAdapterRequestParameters(
    val placementId: String,
    val requestUuid: String?,
    val surveyFormat: SurveyFormat?
) {
    override fun toString(): String {
        return "ProdegeIronSourceAdapterRequestParameters(placementId=$placementId, requestUuid=$requestUuid)"
    }

    companion object {
        fun fromAdData(adData: AdData): ProdegeIronSourceAdapterRequestParameters? {
            val remotePlacementId =
                adData.getString(ProdegeConstants.REMOTE_PLACEMENT_ID_AD_DATA_KEY)
                    ?.ifBlank { null }

            val remoteRequestUuid =
                adData.getString(ProdegeConstants.REMOTE_REQUEST_UUID_AD_DATA_KEY)?.ifBlank { null }
                    ?.let { if (it == "null") null else it }

            val remoteSurveyFormat =
                (adData.getString(ProdegeConstants.REMOTE_SURVEY_FORMAT_KEY)
                    ?.let { if (it == "null") null else it }?.toIntOrNull())?.let {
                    SurveyFormat.values().getOrNull(it)
                }

            if (remotePlacementId == null) {
                return null
            }

            return ProdegeIronSourceAdapterRequestParameters(
                remotePlacementId,
                remoteRequestUuid,
                remoteSurveyFormat
            )
        }
    }
}
