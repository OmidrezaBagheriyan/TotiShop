package com.omidrezabagherian.totishop.core

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkManager(private val context: Context) : LiveData<Boolean>() {
    var connectionManger: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    lateinit var networkCallback: ConnectivityManager.NetworkCallback

    fun checkConnection() {
        val activeNetwork: NetworkInfo? = connectionManger.activeNetworkInfo
        postValue((activeNetwork?.isConnected == true))
    }

    private fun networkReceiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkConnection()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onActive() {
        super.onActive()
        checkConnection()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectionManger.registerDefaultNetworkCallback(networkCallback())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val requestBuilder = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            connectionManger.registerNetworkCallback(
                requestBuilder.build(),
                networkCallback()
            )
        } else {
            context.registerReceiver(
                networkReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun networkCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }
            }
            return networkCallback
        } else {
            throw IllegalAccessError("Error!")
        }

    }
}