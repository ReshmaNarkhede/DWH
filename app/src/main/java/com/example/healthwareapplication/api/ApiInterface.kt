package com.example.healthwareapplication.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("getCountryList")
    fun getCountry(): Call<JsonObject>

    @POST("login")
    fun fetchLogin(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("registration")
    @Multipart
    fun registration(@PartMap fieldMap: Map<String, @JvmSuppressWildcards RequestBody?>): Call<JsonObject>

    @POST("getBodyParts")
    fun getBodyPart(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("getSymptomsByBodyPartId")
    fun getSymptomsByBodyPartId(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("getSearchSymptomsByName")
    fun getSearchSymptomsByName(@Body jsonObject: JsonObject?): Call<JsonObject>

//    @POST("/v1/api/getQuestions")
//    @POST("/v1/api/getQsBySymptomsIds")
    @POST("getQuestionsAns")
    fun getQuestions(@Body jsonObject: JsonObject?): Call<JsonObject>
}