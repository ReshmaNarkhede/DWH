package com.example.healthwareapplication.activity.self_assessment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.SearchSymptomAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityAddSymptomBinding
import com.example.healthwareapplication.model.self_assessment.BodyParts
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.richpath.RichPath
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class BodySymptomActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddSymptomBinding
    private lateinit var gson: Gson
    private var user: UserDetailModel? = null
    var image_front_back_flag = true
    var bodyPartLists = arrayListOf<BodyParts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSymptomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        user = AppSessions.getLoginModel(this)
        gson = Gson()
        fetchBodyParts()
    }

    private fun defaultConfiguration() {
        binding.rotate.setOnClickListener(this)
        setDefaultImage(user)
    }

    private fun getIDFromBodyPart(obj: RichPath) {
        for (i in 0 until bodyPartLists.size) {
            val bodyParts = bodyPartLists[i]
            if (bodyParts.name!!.equals(obj.name, true)) {
                if (image_front_back_flag && (bodyParts.side == "front")) {
                    fetchSymptom(bodyParts,obj)
                    break
                } else if (!image_front_back_flag && bodyParts.side.equals("back", true)) {
                    fetchSymptom(bodyParts, obj)
                    break
                }
            }
        }
    }

    private fun setDefaultImage(user: UserDetailModel?) {
        if (user!!.sex == "female") {
            binding.bodyImg.setVectorDrawable(R.drawable.female_body_front_test)
        } else {
            binding.bodyImg.setVectorDrawable(R.drawable.female_body_front_test)
//            bodyImg.setVectorDrawable(R.drawable.male_body_front)
        }
        fetchImageClick()
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
                binding.bodyImg.setVectorDrawable(R.drawable.female_body_back)
            } else {
                binding.bodyImg.setVectorDrawable(R.drawable.male_body_back)
            }
            image_front_back_flag = false
        } else {
            if (user!!.sex == "female") {
                binding.bodyImg.setVectorDrawable(R.drawable.female_body_front_test)
            } else {
                binding.bodyImg.setVectorDrawable(R.drawable.female_body_front_test)
//                bodyImg.setVectorDrawable(R.drawable.male_body_front)
            }
            image_front_back_flag = true
        }
        fetchImageClick()
    }

    private fun fetchImageClick() {
        val array = binding.bodyImg.findAllRichPaths()
        binding.bodyImg.setOnPathClickListener(RichPath.OnPathClickListener { richPath ->
            for (i in array.indices) {
                val obj: RichPath = binding.bodyImg.findRichPathByIndex(i)!!
                if (richPath.name == obj.name) {
                    Log.e("check: ", obj.name.trim())
                    getIDFromBodyPart(obj)
                    obj.fillColor = ContextCompat.getColor(this, R.color.pink)
                } else {
//                    obj.fillColor = ContextCompat.getColor(this, R.color.white)
                    obj.strokeColor = ContextCompat.getColor(this, R.color.white)
                }
            }
        })
    }

    private fun fetchBodyParts() {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("gender", AppSessions.getUserSex(this))

        AppHelper.printParam("BODY PARTS Param:", param)

        val call: Call<JsonObject> = apiService.getBodyPart(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                AppHelper.printUrl("BODY PARTS:", response)

                if (response.isSuccessful) {

                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val bodyAry = responseModel.getDataArray()
                        Log.e("Size: ", "" + bodyAry!!.length())
                        val listType: Type = object : TypeToken<List<BodyParts?>?>() {}.type
                        bodyPartLists = gson.fromJson(bodyAry.toString(), listType)
                    } else {
                        AppHelper.showToast(
                            this@BodySymptomActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@BodySymptomActivity)
                }
            }
        })
    }

    private fun fetchSymptom(bodyParts: BodyParts, obj: RichPath) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("body_part_id", bodyParts.id)
        AppHelper.printParam("SYMPTOMS PAram:", param)

        val call: Call<JsonObject> = apiService.getSymptomsByBodyPartId(param)
//        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("SYMPTOMS URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("SYMPTOMS REs:", response)

//                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val symptomAry = responseModel.getDataArray()
                        openDialog(bodyParts, symptomAry!!,obj)
//                        openSymptomDialog(bodyParts, symptomAry!!)
                    } else {
                        AppHelper.showToast(
                            this@BodySymptomActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
//                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@BodySymptomActivity)
                }
            }
        })
    }

    private fun openDialog(bodyParts: BodyParts, symptomAry: JSONArray, obj: RichPath) {
        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        val dialogWindowHeight = (displayHeight * 0.5f).toInt()
        layoutParams.height = dialogWindowHeight
        dialog.window!!.attributes = layoutParams

        val window: Window? = dialog.window
        window!!.setGravity(Gravity.BOTTOM)

        dialog.setTitle(null)
        dialog.setContentView(R.layout.symptom_list)
        dialog.setCancelable(true)
        val cncleImg: ImageView = dialog.findViewById(R.id.cncleImg)
        val bodyPartName: TextView = dialog.findViewById(R.id.bodyPartName)
        val symptmList: RecyclerView = dialog.findViewById(R.id.symptmList)

        symptmList.layoutManager = LinearLayoutManager(this)
        val adapter = SearchSymptomAdapter(this, symptomAry, "",
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val modelObj = symptomAry.getJSONObject(position)
                val intent = Intent(this, WhenStartActivity::class.java)
                intent.putExtra(IntentConstants.kSYMPTOM_DATA, modelObj.toString())
                startActivity(intent)
//                val resultIntent = Intent()
//                resultIntent.putExtra(IntentConstants.kSYMPTOM_SELECTED, modelObj.toString())
//                setResult(Activity.RESULT_OK, resultIntent)
                dialog.dismiss()
                finish()
            })
        symptmList.adapter = adapter
        bodyPartName.text = bodyParts.name
        cncleImg.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            obj.fillColor = ContextCompat.getColor(this, R.color.pink)
        })

        dialog.show()
    }
    fun backImgClick(view: View) {
        finish()
    }
    /* private fun openSymptomDialog(bodyParts: BodyParts, symptomAry: JSONArray) {
         val view = layoutInflater.inflate(R.layout.symptom_list, null)
         val dialog = BottomSheetDialog(this)
         dialog.setContentView(view)
         val behavior: BottomSheetBehavior<*> =
             BottomSheetBehavior.from(view.parent as View)
         behavior.isHideable = false
         behavior.setBottomSheetCallback(object : BottomSheetCallback() {
             override fun onStateChanged(
                 @NonNull bottomSheet: View,
                 newState: Int
             ) {
                 if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                     behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                 }
             }

             override fun onSlide(
                 @NonNull bottomSheet: View,
                 slideOffset: Float
             ) {
                 behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
             }
         })
         val bodyPartName: TextView = view.findViewById(R.id.bodyPartName)
         val symptmList: RecyclerView = view.findViewById(R.id.symptmList)
         val cncleImg: ImageView = view.findViewById(R.id.cncleImg)

         symptmList.setHasFixedSize(true)
         cncleImg.setOnClickListener(View.OnClickListener {
             dialog.dismiss()
         })

         symptmList.layoutManager = LinearLayoutManager(this)
         val adapter = SearchSymptomAdapter(this, symptomAry!!, "",
             RecyclerItemClickListener.OnItemClickListener { view, position ->
                 val modelObj = symptomAry.getJSONObject(position)
                 val resultIntent = Intent()
                 resultIntent.putExtra(IntentConstants.kSYMPTOM_SELECTED, modelObj.toString())
                 setResult(Activity.RESULT_OK, resultIntent)
                 dialog.dismiss()
                 finish()
             })
         symptmList.adapter = adapter
         bodyPartName.text = bodyParts.name

         dialog.show()
     }*/


}
