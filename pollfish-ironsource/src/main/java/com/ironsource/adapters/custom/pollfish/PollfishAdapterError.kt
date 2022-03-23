package com.ironsource.adapters.custom.pollfish

sealed class PollfishAdapterError(val message: String, val code: Int) {
    object VersionBelowMinimum :
        PollfishAdapterError("Pollfish surveys will not run on targets lower than 21", 1)

    object PanelAlreadyOpen : PollfishAdapterError("Pollfish survey panel is already visible", 2)
    object MissingApiKey :
        PollfishAdapterError("api_key parameter is missing from your Pollfish Network configuration", 3)

    object NotLoaded : PollfishAdapterError("Pollfish has not loaded", 4)
    object NotAvailable : PollfishAdapterError("Pollfish is not available", 5)
    object NotEligible : PollfishAdapterError("User is not eligible for Pollfish surveys", 6)
}
