package com.example.healthwareapplication.app_utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class NetworkConnectionInterceptor:Interceptor {

    private var mContext: Context? = null

    constructor(context: Context?) {
        mContext = context
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        if (!isConnected()) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected(): Boolean {
        val connectivityManager =
            mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}