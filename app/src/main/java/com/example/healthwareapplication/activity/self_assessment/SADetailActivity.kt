package com.example.healthwareapplication.activity.self_assessment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.SelectedSymptomAdapter
import com.example.healthwareapplication.adapter.self_assessment.SymptomAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SADetailActivity : AppCompatActivity() {
    var adapter: SymptomAdapter? = null
    private lateinit var gson: Gson
    private lateinit var symptomList: RecyclerView
    private lateinit var nextBtn: Button
    val symptmJsonAry: JSONArray = JSONArray()

    private lateinit var assessmentDate: TextView
    private lateinit var assessmentTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_a_detail)

        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        symptomList = findViewById(R.id.symptomList)
        nextBtn = findViewById(R.id.nextBtn)

        assessmentDate = findViewById(R.id.assessmentDate)
        assessmentTime = findViewById(R.id.assessmentTime)


        gson = Gson()
        dataBind()

    }

    fun addSymptom(view: View) {
        val intent = Intent(this, AddSymptomActivity::class.java)
        startActivityForResult(intent, 2)
    }

    fun searchClick(view: View) {
        val intent = Intent(this, SearchSymptomActivity::class.java)
        startActivityForResult(intent, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (data != null) {
                val modelObj = data!!.getStringExtra(IntentConstants.kSYMPTOM_SELECTED)
                if (symptmJsonAry.length() > 3) {
                    AppHelper.showToast(this, "You are not able to add more symptom")
                } else {
                    symptmJsonAry.put(JSONObject(modelObj!!))
                    symptomList.adapter!!.notifyDataSetChanged()
                    showBottom()
                }
            }
        }
        if (requestCode == 3) {
            if (data != null) {
                val modelObj = data!!.getStringExtra(IntentConstants.kSYMPTOM_SELECTED)
                if (symptmJsonAry.length() > 3) {
                    AppHelper.showToast(this, "You are not able to add more symptom")
                } else {
                    symptmJsonAry.put(JSONObject(modelObj!!))
                    symptomList.adapter!!.notifyDataSetChanged()
                    showBottom()
                }
            }
        }
    }

    private fun dataBind() {
        val showDeleted: ShowDeleted = object : ShowDeleted {
            override fun showDeleted(size: Int) {
                if (size == 0) {
                    nextBtn.visibility = View.GONE
                }
            }
        }
        symptomList.layoutManager = LinearLayoutManager(this)
        val addAdapter = SelectedSymptomAdapter(symptmJsonAry!!, showDeleted)
        symptomList.adapter = addAdapter

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        assessmentDate.text = "$day/${month + 1}/$year"
        val sdf = SimpleDateFormat("hh:mm aa")
        val result = sdf.format(Calendar.getInstance().time)
        assessmentTime.text = result
    }

    private fun showBottom() {
        Log.e("Show bottom: ", " " + symptmJsonAry.length())
        nextBtn.visibility = View.VISIBLE
    }


    fun clickNext(view: View) {
        val intent = Intent(this, WhenStartActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptmJsonAry.toString())
        startActivity(intent)
//        finish()
    }

    interface ShowDeleted {
        fun showDeleted(size: Int)
    }

    fun backClick(view: View) {
        finish()
    }
}
