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

    @POST("/v1/api/getBodyParts")
    fun getBodyPart(@Body jsonObject: JsonObject?): Call<JsonObject>

}