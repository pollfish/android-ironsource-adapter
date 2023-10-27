package com.ironsource.adapters.custom.prodege.utils

import com.ironsource.adapters.custom.prodege.ProdegeCustomAdapter
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.prodege.listener.ProdegeException

internal object ProdegeAdMobAdapterUtils {

    fun isSdkSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= 21
    }

    fun asAdapterError(exception: ProdegeException): Pair<AdapterErrorType, @ProdegeCustomAdapter.AdapterError Int> {
        return when (exception) {
            is ProdegeException.EmptyApiKey -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION
            )

            is ProdegeException.EmptyPlacementId -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION
            )

            is ProdegeException.WrongApiKey -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION
            )

            is ProdegeException.WrongPlacementId -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INVALID_CONFIGURATION
            )

            is ProdegeException.ConnectionError -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_NO_CONNECTION
            )

            is ProdegeException.NoFill -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_NO_FILL,
                ProdegeCustomAdapter.ERROR_NO_FILL
            )

            is ProdegeException.NotInitialized -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_NOT_INITIALIZED
            )

            is ProdegeException.InternalError -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_INTERNAL
            )

            else -> Pair(
                AdapterErrorType.ADAPTER_ERROR_TYPE_INTERNAL,
                ProdegeCustomAdapter.ERROR_UNSPECIFIED
            )
        }
    }
}
