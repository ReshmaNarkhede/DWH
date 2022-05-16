package com.example.healthwareapplication.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("api/search_city")
    fun getCity(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/search_country")
    fun getCountry(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/search_city_by_country")
    fun getCityByCountry(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/login")
    fun fetchLogin(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/user_ac_register")
    fun fetchRegister(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/verify_register_ac")
    fun verifyUser(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/getSpeciality")
    fun getSpeciality(@Body jsonObject: JsonObject): Call<JsonObject>

    @POST("api/forgotPassword")
    fun forgotPassword(@Body jsonObject: JsonObject?): Call<JsonObject>

    @POST("api/resetPassword")
    fun resetPassword(@Body jsonObject: JsonObject?): Call<JsonObject>

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