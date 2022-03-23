package com.ironsource.adapters.custom.pollfish

import android.content.Context
import android.util.Log
import com.ironsource.mediationsdk.adunit.adapter.BaseAdapter
import com.ironsource.mediationsdk.adunit.adapter.listener.NetworkInitializationListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData

class PollfishCustomAdapter : BaseAdapter() {

    companion object {
        val TAG: String = PollfishCustomAdapter::class.java.simpleName
    }

    override fun init(adData: AdData, context: Context, listener: NetworkInitializationListener?) {
        if (!PollfishHelper.isPollfishSupported) {
            Log.w(TAG, PollfishAdapterError.VersionBelowMinimum.message)
            listener?.onInitFailed(
                PollfishAdapterError.VersionBelowMinimum.code,
                PollfishAdapterError.VersionBelowMinimum.message
            )
        } else {
            listener?.onInitSuccess()
        }
    }

    override fun getNetworkSDKVersion(): String =
        PollfishConstants.POLLFISH_SDK_VERSION

    override fun getAdapterVersion(): String =
        PollfishConstants.POLLFISH_ADAPTER_VERSION
}