package com.example.healthwareapplication.fragment.self_assessment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.model.BodyParts
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.richpath.RichPath
import com.richpath.RichPathView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class BodyFragment : Fragment(), View.OnClickListener {
    private lateinit var gson: Gson
    private var user: UserDetailModel? = null
    private lateinit var bodyImg: RichPathView
    private lateinit var rotate: TextView
    var image_front_back_flag = true
    var bodyPartLists = arrayListOf<BodyParts>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val view: View? = inflater.inflate(R.layout.fragment_body, container, false)

        initComponents(view)
        defaultConfiguration()
        return view
    }

    private fun initComponents(view: View?) {
        user = AppSessions.getLoginModel(context!!)
        gson = Gson()
        fetchBodyParts(user!!.sex)

        bodyImg = view!!.findViewById(R.id.bodyImg)
        rotate = view!!.findViewById(R.id.rotate)
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
                    obj.fillColor = ContextCompat.getColor(activity!!, R.color.body_fill)
                } else {
                    obj.fillColor = ContextCompat.getColor(activity!!, R.color.white)
                    obj.strokeColor = ContextCompat.getColor(activity!!, R.color.body_outline)
                }
            }
        })
    }

    private fun getIDFromBodyPart(obj: RichPath) {
        for (i in 0 until bodyPartLists.size) {
            if (bodyPartLists[i].name!!.equals(obj.name, true)) {
                AppHelper.showToast(context!!, bodyPartLists[i].name.toString())
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
            ApiClient.getRetrofitClient(context)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("gender", sex)

        val call: Call<JsonObject> = apiService.getBodyPart(param)
        DialogUtility.showProgressDialog(context!!)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                Log.d("BODY PARTS: $param", ": " + response.raw().request().url())
                if (response.isSuccessful) {
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val bodyAry = responseModel.getDataArray()
                        val listType: Type = object : TypeToken<List<BodyParts?>?>() {}.type
                        bodyPartLists = gson.fromJson(bodyAry.toString(), listType)
                    } else {
                        AppHelper.showToast(context!!, responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(context!!)
                }
            }
        })
    }
}
