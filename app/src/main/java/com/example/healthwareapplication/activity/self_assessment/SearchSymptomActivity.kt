package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.model.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.SearchSymptomAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivitySearchSymptomBinding
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchSymptomActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivitySearchSymptomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSymptomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {

        binding.searchTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.isNotEmpty()) {
                    binding.cncleImg.visibility = View.VISIBLE
                    binding.cncleImg.setOnClickListener(this@SearchSymptomActivity)
                } else {
                    binding.cncleImg.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.symptom.visibility = View.VISIBLE
                fetchSymptomBySearch(s.toString())
            }
        })
    }

    private fun fetchSymptomBySearch(search: String?) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("search", search)
        param.addProperty( "gender", AppSessions.getUserSex(this))
        AppHelper.printParam("SEARCH PAram:", param)

        val call: Call<JsonObject> = apiService.getSearchSymptomsByName(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("SEARCH URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("SEARCH REs:", response)

                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val symptomListAry = responseModel.getDataArray()!!
                        bindSymptomSearchName(symptomListAry,search)
                    } else {
                        AppHelper.showToast(
                            this@SearchSymptomActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@SearchSymptomActivity)
                }
            }
        })
    }

    private fun bindSymptomSearchName(symptomListAry: JSONArray, searchStr: String?) {
        binding.symptom.layoutManager = LinearLayoutManager(this)
        val adapter = SearchSymptomAdapter(this,symptomListAry!!,searchStr,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val modelObj = JSONObject(symptomListAry.getJSONObject(position).toString())
                val intent = Intent(this, WhenStartActivity::class.java)
                intent.putExtra(IntentConstants.kSYMPTOM_DATA, modelObj.toString())
                startActivity(intent)
                binding.searchTxt.text.clear()
//                val resultIntent = Intent()
//                resultIntent.putExtra(IntentConstants.kSYMPTOM_SELECTED,modelObj.toString())
//                setResult(Activity.RESULT_OK, resultIntent)
//                searchTxt.text.clear()
                finish()
            })
        binding.symptom.adapter = adapter
    }

    fun backClick(view: View) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        DialogUtility.hideProgressDialog()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cncleImg -> {
                binding.searchTxt.text.clear()
                binding.symptom.visibility = View.GONE
            }
        }
    }
}
