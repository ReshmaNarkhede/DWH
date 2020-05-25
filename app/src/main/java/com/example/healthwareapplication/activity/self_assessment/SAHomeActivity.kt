package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_s_a_home
import com.example.healthwareapplication.adapter.self_assessment.SAListAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_s_a_home.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SAHomeActivity : AppCompatActivity(), View.OnClickListener {
    var pageCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_s_a_home)

        fetchList(pageCount)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {
        addImg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addImg -> {
                val intent = Intent(this, WhatFeelActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun fetchList(pageCount:Int?) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("user_id", AppSessions.getUserId(this))
        param.addProperty("page", pageCount)

        AppHelper.printParam("list param:", param)

        val call: Call<JsonObject> = apiService.getSAList(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("list URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("list REs:", response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val listAry = responseModel.getDataArray()!!
                        bindData(listAry)
                    } else {
                        AppHelper.showToast(
                            this@SAHomeActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                DialogUtility.hideProgressDialog()
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@SAHomeActivity)
                }
            }
        })
    }

    private fun bindData(listAry: JSONArray) {
        list.layoutManager = LinearLayoutManager(this)
        val adapter = SAListAdapter(this,listAry!!,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val str = listAry.getJSONObject(position).toString()
//                val intent = Intent(this,ReportFromHome::class.java)
//                intent.putExtra(IntentConstants.REPORT_DATA,str)
//                startActivity(intent)
            })
        list.adapter = adapter
    }
}
