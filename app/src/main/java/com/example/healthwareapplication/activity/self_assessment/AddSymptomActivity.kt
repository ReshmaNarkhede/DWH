package com.example.healthwareapplication.activity.self_assessment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.SymptomAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.BodyParts
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.richpath.RichPath
import com.richpath.RichPathView
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class AddSymptomActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var gson: Gson
    private var user: UserDetailModel? = null
    private lateinit var bodyImg: RichPathView
    private lateinit var rotate: TextView
    var image_front_back_flag = true
    var bodyPartLists = arrayListOf<BodyParts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_symptom)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        user = AppSessions.getLoginModel(this)
        gson = Gson()
        fetchBodyParts(user!!.sex)

        bodyImg = findViewById(R.id.bodyImg)
        rotate = findViewById(R.id.rotate)
    }

    private fun defaultConfiguration() {
        val array = bodyImg.findAllRichPaths()
        rotate.setOnClickListener(this)

        setDefaultImage(user)

        bodyImg.setOnPathClickListener(RichPath.OnPathClickListener { richPath ->
            for (i in array.indices) {
                val obj: RichPath = bodyImg.findRichPathByIndex(i)!!
                if (richPath.name == obj.name) {
                    getIDFromBodyPart(obj)
                    obj.fillColor = ContextCompat.getColor(this, R.color.body_fill)
                } else {
                    obj.fillColor = ContextCompat.getColor(this, R.color.white)
                    obj.strokeColor = ContextCompat.getColor(this, R.color.body_outline)
                }
            }
        })
    }
    private fun getIDFromBodyPart(obj: RichPath) {
        for (i in 0 until bodyPartLists.size) {
            val bodyParts = bodyPartLists[i]
            if (bodyParts.name!!.equals(obj.name, true)) {
                if (image_front_back_flag && (bodyParts.side=="front")) {
                    fetchSymptom(bodyParts)
                    break
                } else if(!image_front_back_flag && bodyParts.side.equals("back",true)){
                    fetchSymptom(bodyParts)
                    break
                }
            }
        }
    }

    private fun setDefaultImage(user: UserDetailModel?) {
        if (user!!.sex == "female") {
            bodyImg.setVectorDrawable(R.drawable.female_body_front)
        } else {
            bodyImg.setVectorDrawable(R.drawable.male_body_front)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rotate -> {
                rotateImageClick()
            }
        }
    }

    private fun rotateImageClick() {
        if (image_front_back_flag) {
            if (user!!.sex == "female") {
                bodyImg.setVectorDrawable(R.drawable.female_body_back)
            } else {
                bodyImg.setVectorDrawable(R.drawable.male_body_back)
            }
            image_front_back_flag = false
        } else {
            if (user!!.sex == "female") {
                bodyImg.setVectorDrawable(R.drawable.female_body_front)
            } else {
                bodyImg.setVectorDrawable(R.drawable.male_body_front)
            }
            image_front_back_flag = true
        }
    }

    private fun fetchBodyParts(sex: String?) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("gender", sex)

        AppHelper.printParam("BODY PARTS Param:",param)

        val call: Call<JsonObject> = apiService.getBodyPart(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                AppHelper.printUrl("BODY PARTS:",response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("BODY PARTS REs:",response)
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val bodyAry = responseModel.getDataArray()
                        val listType: Type = object : TypeToken<List<BodyParts?>?>() {}.type
                        bodyPartLists = gson.fromJson(bodyAry.toString(), listType)
                    } else {
                        AppHelper.showToast(this@AddSymptomActivity, responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@AddSymptomActivity)
                }
            }
        })
    }

    private fun fetchSymptom(bodyParts: BodyParts) {
        val apiService: ApiInterface = ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("body_part_id", bodyParts.id)
        AppHelper.printParam("SYMPTOMS PAram:",param)


        val call: Call<JsonObject> = apiService.getSymptomsByBodyPartId(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("SYMPTOMS URL:",response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("SYMPTOMS REs:",response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val symptomAry = responseModel.getDataArray()
                        openSymptomDialog(bodyParts,symptomAry!!)
                    } else {
                        AppHelper.showToast(this@AddSymptomActivity, responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@AddSymptomActivity)
                }
            }
        })
    }
    private fun openSymptomDialog(bodyParts: BodyParts, symptomAry: JSONArray) {

        val view = layoutInflater.inflate(R.layout.symptom_list, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        val bodyPartName: TextView = view.findViewById(R.id.bodyPartName)
        val symptmList: RecyclerView = view.findViewById(R.id.symptmList)
        val cncleImg: ImageView = view.findViewById(R.id.cncleImg)

        cncleImg.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        symptmList.layoutManager = LinearLayoutManager(this)
        val adapter = SymptomAdapter(symptomAry!!,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val modelObj = symptomAry.getJSONObject(position)
                val resultIntent = Intent()
                resultIntent.putExtra(IntentConstants.kSYMPTOM_SELECTED,modelObj.toString())
                setResult(Activity.RESULT_OK, resultIntent)
                dialog.dismiss()
                finish()

            })
        symptmList.adapter = adapter
        bodyPartName.text = bodyParts.name

        dialog.show()
    }
}
