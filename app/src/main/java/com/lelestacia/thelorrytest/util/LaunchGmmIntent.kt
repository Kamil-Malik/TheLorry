package com.lelestacia.thelorrytest.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lelestacia.thelorrytest.R

fun Context.launchGmmIntent(lat: String, lng: String) {
    val gmmIntentUri = Uri.parse(getString(R.string.gmm_uri, lat, lng))
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
}