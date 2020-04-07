package com.example.healthwareapplication.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("/v1/api/getCountryList")
    fun getCountry(): Call<JsonObject>

    @POST("/v1/api/login")
    fun fetchLogin(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("/v1/api/registration")
    @Multipart
    fun registration(@PartMap fieldMap: Map<String, @JvmSuppressWildcards RequestBody?>):  Call<JsonObject>

    /* @POST(APIRequests.kUSER_LOGIN)
     fun getStudentLogin(@Body jsonObject: JsonObject?): Call<JsonObject>

     @Multipart
     @POST(APIRequests.kUPDATE_PROFILE_IMAGE)
     fun uploadImage(
         @Part("user_id") userID: RequestBody,
         @Part("domain") domain: RequestBody,
         @Part("sub_domain") subDomain: RequestBody,
         @Part("device_id") deviceId: RequestBody,
         @Part file: MultipartBody.Part
     ): Call<JsonObject>*/
}