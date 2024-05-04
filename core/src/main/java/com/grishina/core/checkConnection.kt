package com.grishina.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.firebase.FirebaseApp

fun isNetworkAvailable(applicationContext: Context): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

fun isFirebaseAvailable(applicationContext: Context): Boolean = FirebaseApp.getApps(applicationContext).isNotEmpty()

fun toastCheckConnection(applicationContext: Context): Boolean {
    if (!toastValidateAnyFiled(
        context = applicationContext,
        value = applicationContext,
        reflectFunction = ::isNetworkAvailable,
        badReflectString = applicationContext.getString(R.string.networkError)
    )) return false
    if (!toastValidateAnyFiled(
        context = applicationContext,
        value = applicationContext,
        reflectFunction = ::isFirebaseAvailable,
        badReflectString = applicationContext.getString(R.string.FBError)
    )) return false
    return true
}

fun alertCheckConnection(applicationContext: Context): Boolean {
    if (!alertValidateAnyFiled(
            context = applicationContext,
            value = applicationContext,
            reflectFunction = ::isNetworkAvailable,
            badReflectString = applicationContext.getString(R.string.networkError)
        )) return false
    if (!alertValidateAnyFiled(
            context = applicationContext,
            value = applicationContext,
            reflectFunction = ::isFirebaseAvailable,
            badReflectString = applicationContext.getString(R.string.FBError)
        )) return false
    return true
}