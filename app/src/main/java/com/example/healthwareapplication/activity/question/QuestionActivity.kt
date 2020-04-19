package com.example.healthwareapplication.activity.question

import android.drm.DrmRights
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.question.PagerAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.fragment.self_assessment.question.QFragment
import com.google.gson.JsonObject
import com.stepstone.stepper.internal.widget.RightNavigationButton
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionActivity : AppCompatActivity(), QFragment.OnButtonClickListener {

    private lateinit var viewpager: ViewPager
    private lateinit var leftNavigation: ImageView
    private lateinit var rightNavigation: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        initComponents()
        defaultConfiguration()
    }


    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        viewpager = findViewById(R.id.pager)
//        leftNavigation = findViewById(R.id.leftNavigation)
//        rightNavigation = findViewById(R.id.rightNavigation)

//        leftNavigation.(this)
//        rightNavigation.setOnClickListener(this)
    }

    private fun defaultConfiguration() {
        fetchQuestionData("4")
//        val symptomJsonAry = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
//        try {
//
//            val ids: MutableList<String> = ArrayList()
//            val array = JSONArray(symptomJsonAry)
//            for (i in 0 until array.length()) {
//                val model = SymptomJsonModel(array.getJSONObject(i))
//                ids.add(model.getId()!!)
//            }
//            val idStr = android.text.TextUtils.join(",", ids)
//            fetchQuestionData(idStr)
//            Log.e("next: ", " $idStr")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
    }

    private fun fetchQuestionData(idStr: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("symptoms_id", idStr)
        AppHelper.printParam("QUESTION PAram:", param)

        val call: Call<JsonObject> = apiService.getQuestions(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("QUESTION URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("QUESTION REs:", response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val questionAry = responseModel.getDataArray()!!
                        Log.e("QArySize: ", " " + questionAry.length())
                        setDynamicData(questionAry)
                    } else {
                        AppHelper.showToast(
                            this@QuestionActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@QuestionActivity)
                }
            }
        })
    }

    private fun setDynamicData(questionAry: JSONArray) {
        setupViewPager(questionAry)
//        val questionData = QuestionData(questionAry.getJSONObject(i))
//            val view: View = getQuestionView(questionData)
//            questionView.addView(view)
    }

    private fun setupViewPager(questionAry: JSONArray) {
        val adapter = PagerAdapter(
            supportFragmentManager
        )
        for (i in 0 until questionAry.length()) {
            val firstFragment: QFragment = QFragment.newInstance(questionAry,i)
            adapter.addFragment(firstFragment)
        }

        viewpager.adapter = adapter
        viewpager.addOnPageChangeListener(viewPagerPageChangeListener)

    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

    override fun onButtonClicked(view: View?) {
        val currPos: Int = viewpager.currentItem

        when (view!!.id) {
//            R.id.nextBtn ->             //handle currPos is zero
//                viewpager.currentItem = currPos - 1
            R.id.nextBtn ->             //handle currPos is reached last item
                viewpager.currentItem = currPos + 1
        }
    }


}
