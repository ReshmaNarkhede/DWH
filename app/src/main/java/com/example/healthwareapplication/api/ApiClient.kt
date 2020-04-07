package  com.example.healthwareapplication.api

import android.content.Context
import app.frats.android.api.APIRequests
import com.example.healthwareapplication.app_utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

    companion object {

        fun getRetrofitClient(mContext: Context?): Retrofit? {
            var retrofit: Retrofit? = null
            if (retrofit == null) {
                val oktHttpClient = OkHttpClient.Builder()
                    .addInterceptor(NetworkConnectionInterceptor(mContext))

                retrofit = Retrofit.Builder()
                    .baseUrl(APIRequests.kBASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(oktHttpClient.build())
                    .build()
            }
            return retrofit
        }

   /*     fun getRetrofitClient(mContext: Context?): Retrofit? {
            var retrofit: Retrofit? = null
//            val data: String = AppSessions.getToken(mContext!!)
            if (retrofit == null) {
                val client =
                    OkHttpClient.Builder().addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("authorization", data)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Connection", "close")
                            .build()
                        chain.proceed(newRequest)
                    }.build()
                OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(mContext))

                retrofit = Retrofit.Builder()
                    .baseUrl(APIRequests.kBASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit
        }*/
    }
}