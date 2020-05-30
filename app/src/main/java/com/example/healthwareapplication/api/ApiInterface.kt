package com.example.healthwareapplication.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("api/getCountryList")
    fun getCountry(): Call<JsonObject>

    @POST("api/login")
    fun fetchLogin(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/registration")
    @Multipart
    fun registration(@PartMap fieldMap: Map<String, @JvmSuppressWildcards RequestBody?>): Call<JsonObject>

    @POST("api/getBodyParts")
    fun getBodyPart(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/getSymptomsByBodyPartId")
    fun getSymptomsByBodyPartId(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/getSearchSymptomsByName")
    fun getSearchSymptomsByName(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/getQuestionsAns")
    fun getQuestions(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("selfassessment/tests")
    fun submitSelfData(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("selfassessment/report_details")
    fun getReport(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("selfassessment/get_assessments")
    fun getSAList(@Body jsonObject: JsonObject?): Call<JsonObject>
}