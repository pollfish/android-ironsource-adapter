package com.ironsource.adapters.custom.pollfish

object PollfishHelper {
    val isPollfishSupported
        get() = android.os.Build.VERSION.SDK_INT >= 21
}
