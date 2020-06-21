package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_register_terms
import com.example.healthwareapplication.activity.account.login.OldLoginActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_register_info.*
import kotlinx.android.synthetic.main.activity_register_terms.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterTermsActivity : AppCompatActivity() {

    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register_terms)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        pwdTxt.text = userDetailModel.password
        pwdTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun defaultConfiguration() {
    }

    fun signupwaaClick(view: View) {
        if (termAndConditionSwitch.isChecked) {
            val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
            DialogUtility.showProgressDialog(this)

            callRegisteration(userDetailModel)
        } else {
            Toast.makeText(this, "Check Term and condition !", Toast.LENGTH_LONG).show()
        }
    }


    fun termAndConditionClick(view: View) {
        termAndConditionDetailLayout.visibility = View.VISIBLE
        termAndConditionLayout.visibility = View.GONE
    }

    fun termAndConditionReadClick(view: View) {
        termAndConditionDetailLayout.visibility = View.GONE
        termAndConditionLayout.visibility = View.VISIBLE
        termAndConditionSwitch.isChecked = true
    }


    private fun callRegisteration(userDetailModel: UserDetailModel) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val fieldsmap = HashMap<String, RequestBody>()
        fieldsmap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.mobile))
        fieldsmap.put("firstName", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.firstName))
        fieldsmap.put("lastName", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.lastName))
        fieldsmap.put("dob", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.dob))
        fieldsmap.put("tob", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.tob))
        fieldsmap.put("sex", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.gender))
        fieldsmap.put("countryId", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.countryId))
        fieldsmap.put("password", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.password))
        fieldsmap.put("userType", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.userType.toString()))
        fieldsmap.put("parentId", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.parentId))
        if (userDetailModel.resume != "") {
            val file = File(userDetailModel.resume)
            fieldsmap["resume\"; filename=" + userDetailModel.resumeName + "." + file.extension] = RequestBody.create(
                MediaType.parse("image/*"), file)
        }
        fieldsmap["mciNo"] = RequestBody.create(MediaType.parse("text/plain"), userDetailModel.mciNo)

        Log.d("map: ",": $fieldsmap")
        val call: Call<JsonObject> = apiService.registration(fieldsmap)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                Log.e("REGISTER: ",": "+response.raw().request().url())
                if (response.isSuccessful) {
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        AppHelper.showToast(this@RegisterTermsActivity,responseModel.getMessage().toString())
                        val intent = Intent(this@RegisterTermsActivity, OldLoginActivity::class.java)
                        startActivity(intent)
                     }
                    else{
                        AppHelper.showToast(this@RegisterTermsActivity,responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@RegisterTermsActivity)
                }
            }
        })
    }
}
