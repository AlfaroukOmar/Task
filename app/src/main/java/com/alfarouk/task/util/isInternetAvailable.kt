package com.alfarouk.task.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


public fun ConnectivityManager.isInternetAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = this.activeNetwork ?: return false
        val actNw = this.getNetworkCapabilities(networkCapabilities) ?: return false
        when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        return activeNetworkInfo != null && activeNetworkInfo!!.isConnected;
    }
}