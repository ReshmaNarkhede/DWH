package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.SAListAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivitySAHomeBinding
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SAHomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySAHomeBinding
    var pageCount = 1
    var isLoading = false
    var allowRefresh: Boolean = false
    private var assessmentAry: JSONArray? = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySAHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
        fetchList(pageCount)
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {
        binding.addImg.setOnClickListener(this)
//        assessmentAry = AppSessions.getAssessmentData(this)!!
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addImg -> {
                val intent = Intent(this, WhatFeelActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun fetchList(pageCount: Int?) {
        Log.e("Count: ",": $pageCount")
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("user_id", AppSessions.getUserId(this))
        param.addProperty("page", pageCount)

        AppHelper.printParam("list param:", param)

        val call: Call<JsonObject> = apiService.getSAList(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("list URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("list REs:", response)

                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val listAry = responseModel.getDataArray()!!
                        for (i in 0 until listAry.length()) {
                            val obj = listAry.get(i)
                            assessmentAry!!.put(obj)
                        }
                        isLoading = false
                        bindData(assessmentAry!!)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@SAHomeActivity)
                }
            }
        })
    }

    private fun bindData(listAry: JSONArray) {
        binding.list.layoutManager = LinearLayoutManager(this)
        val adapter = SAListAdapter(this, listAry!!,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val str = listAry.getJSONObject(position).toString()
                val intent = Intent(this, ReportFromHome::class.java)
                intent.putExtra(IntentConstants.REPORT_DATA, str)
                startActivity(intent)
            })
        binding.list.adapter = adapter
        binding.list.addOnScrollListener(recyclerViewOnScrollListener)
    }
    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (layoutManager!!.findLastCompletelyVisibleItemPosition() == assessmentAry!!.length() - 1) {
                        pageCount++
                        fetchList(pageCount)
                        isLoading = true
                    }
                }
            }
        }
    override fun onResume() {
        super.onResume()
        if (allowRefresh) {
            allowRefresh = false
            fetchList(pageCount)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!allowRefresh) allowRefresh = true
    }
}
