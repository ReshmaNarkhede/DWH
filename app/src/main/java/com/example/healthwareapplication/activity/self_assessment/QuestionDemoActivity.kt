package com.example.healthwareapplication.activity.self_assessment

import androidx.appcompat.app.AppCompatActivity


class QuestionDemoActivity : AppCompatActivity() {
//    private var ansJsonObj: JSONObject? = JSONObject()
//    private var sortedArray: JSONArray? = JSONArray()
//    private var QArray: JSONArray? = JSONArray()
//    private lateinit var chkRecyclerAdapter: CheckRecyclerViewAdapter
//
//    private lateinit var dataAry: JSONArray
//    var outerIndex: Int? = 0
//    var innerIndex: Int? = 0
//    private lateinit var questionTxt: TextView
//    private lateinit var answerTxt: TextView
//    private lateinit var checkBoxList: RecyclerView
//    private lateinit var radioList: RecyclerView
//    private lateinit var seekbarParent: LinearLayout
//    private lateinit var nextBtn: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_question_demo)
//        initComponents()
//        defaultConfiguration()
//    }
//
//    private fun initComponents() {
//        AppHelper.transparentStatusBar(this)
//        questionTxt = findViewById(R.id.questionTxt)
//        answerTxt = findViewById(R.id.answerTxt)
//        checkBoxList = findViewById(R.id.checkBoxList)
//        radioList = findViewById(R.id.radioList)
//        seekbarParent = findViewById(R.id.seekbarParent)
//        nextBtn = findViewById(R.id.nextBtn)
//    }
//
//    private fun defaultConfiguration() {
//
////        dataAry = AppSessions.getQuestionData(this)!!
////        if (dataAry.length() == 0) {
//            fetchQuestionData("2")
////        } else {
////            setOuterLoop(outerIndex)
////        }
//        answerTxt.setOnClickListener(this)
//    }
//
//    private fun fetchQuestionData(idStr: String) {
//        val apiService: ApiInterface =
//            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)
//
//        val param = JsonObject()
//        param.addProperty("symptoms_id", idStr)
//        AppHelper.printParam("QUESTION PAram:", param)
//
//        val call: Call<JsonObject> = apiService.getQuestions(param)
//        DialogUtility.showProgressDialog(this)
//        call.enqueue(object : Callback<JsonObject?> {
//
//            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
//                AppHelper.printUrl("QUESTION URL:", response)
//
//                if (response.isSuccessful) {
//                    AppHelper.printResponse("QUESTION REs:", response)
//
//                    DialogUtility.hideProgressDialog()
//                    val json = JSONObject(response.body().toString())
//                    val responseModel = ResponseModel(json)
//                    if (responseModel.isCode()) {
//                        dataAry = responseModel.getDataArray()!!
//
//                        AppSettings.setJsonArrayValue(
//                            this@QuestionDemoActivity,
//                            AppConstants.kQUESTION_ARY,
//                            dataAry.toString()
//                        )
//                        Log.e("QArySize: ", " " + dataAry.length())
//                        setOuterLoop(outerIndex)
//                    } else {
//                        AppHelper.showToast(
//                            this@QuestionDemoActivity,
//                            responseModel.getMessage().toString()
//                        )
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
//                if (t is NoConnectivityException) {
//                    DialogUtility.hideProgressDialog()
//                    AppHelper.showNetNotAvailable(this@QuestionDemoActivity)
//                }
//            }
//        })
//    }
//
//    private fun setOuterLoop(outerIndex: Int?) {
//        QArray = QuestionData(dataAry.getJSONObject(outerIndex!!)).getQuestionAns()
////        Log.e("Qdata: ", ": " + QArray!!.length())
//        setDynamicData(innerIndex,ansJsonObj)
//        sortedArray =
//            QuestionData(dataAry.getJSONObject(outerIndex!!)).parseGroupedQuestionArray(QArray)
////        Log.e("grp Qdata: ", ": " +sortedArray!!.length())
//    }
//
//    private fun setDynamicData(index: Int?, ansJObj: JSONObject?) {
//
//        ansJsonObj = ansJObj
//
//        val ansObj = QuestionData.QuestionAnsModel(ansJsonObj!!)
//
//        val qObj = QuestionData.QuestionAnsModel(QArray!!.getJSONObject(index!!))
//        Log.e("Ans: $innerIndex", ": " +  qObj.getGroupID())
//
//        answerTxt.text = ansObj.getSelectedAnswer()
//        questionTxt.text = qObj.getQuestion()
//        if (qObj.getQuestionType() == "CB") // CrossBar
//        {
//            seekbarParent.visibility = View.VISIBLE
//            radioList.visibility = View.GONE
//            checkBoxList.visibility = View.GONE
//
//            showSeekData(qObj)
//        }
//        if (qObj.getQuestionType() == "SS") //radio button
//        {
//            seekbarParent.visibility = View.GONE
//            radioList.visibility = View.VISIBLE
//            checkBoxList.visibility = View.GONE
//
//            showRadioData(qObj)
//        }
//        if (qObj.getQuestionType() == "MS") // checkbox
//        {
//            seekbarParent.visibility = View.GONE
//            radioList.visibility = View.GONE
//            checkBoxList.visibility = View.VISIBLE
//
//            showCheckData(qObj)
//
//        }
//    }
//
//    private fun showSeekData(qObj: QuestionData.QuestionAnsModel) {
////        val qObj = QuestionData.QuestionAnsModel(jsonObj)
//        seekbarParent.removeAllViews()
//        val result: Array<String> = qObj.getAnswerOptions().split("#").toTypedArray()
//        val seekBar: IndicatorSeekBar = IndicatorSeekBar
//            .with(this)
//            .progress(0f)
//            .tickCount(result.size)
//            .showTickMarksType(TickMarkType.OVAL)
//            .tickTextsArray(result)
//            .showTickTexts(true)
//            .tickTextsColorStateList(
//                resources.getColorStateList(R.color.colorPrimary)
//            )
//            .indicatorColor(resources.getColor(R.color.colorAccent))
//            .indicatorTextColor(Color.parseColor("#ffffff"))
//            .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
//            .thumbColor(Color.parseColor("#ff0000"))
//            .thumbSize(14)
//            .trackProgressColor(resources.getColor(R.color.colorAccent))
//            .trackProgressSize(4)
//            .trackBackgroundColor(resources.getColor(R.color.grey))
//            .trackBackgroundSize(2)
//            .build()
//        seekbarParent.addView(seekBar)
//        seekBar.onSeekChangeListener = object : OnSeekChangeListener {
//            override fun onSeeking(seekParams: SeekParams) {
//                val ansObj = seekParams.tickText
//                Log.e("seek ans: ", ": $ansObj")
//                ansJsonObj!!.put("selected_answer", ansObj)
//                if (ansObj.toLowerCase() == qObj.getAnswer()!!.toLowerCase()) {
//                    innerIndex = innerIndex!!.plus(1)
//                } else {
//                    AppHelper.showToast(this@QuestionDemoActivity, "group id: " + qObj.getGroupID()!!.plus(1))
//                    val ary: JSONArray =
//                        sortedArray!!.get(qObj.getGroupID()!!.minus(1)) as JSONArray
//                    Log.e("ArySize: ", ": ${ary.length()}")
//                    innerIndex = innerIndex!!.plus(ary.length())
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
//            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
//        }
//    }
//
//    private fun showRadioData(qObj: QuestionData.QuestionAnsModel) {
//        val recyclerLayoutManager = LinearLayoutManager(this)
//        radioList.layoutManager = recyclerLayoutManager
//        val str = qObj.getAnswerOptions()
//        val result: List<String> = str!!.split("#")
//        val radioRecyclerAdapter = RadioRecyclerViewAdapter(
//            result,
//            RecyclerItemClickListener.OnItemClickListener { view, position ->
//                val ansObj = result[position]
//                ansJsonObj!!.put("selected_answer", ansObj)
//                Log.e("RadioAns: ", ": $ansObj")
//                if (ansObj.trim().toLowerCase() == qObj.getAnswer()!!.trim()
//                        .toLowerCase() || qObj.getAnswer() == "any"
//                ) {
//                    innerIndex = innerIndex!!.plus(1)
//                } else {
//                    AppHelper.showToast(this@QuestionDemoActivity, "group id: " + qObj.getGroupID()!!.plus(1))
//                    val ary: JSONArray = sortedArray!!.get(qObj.getGroupID()!!.minus(1)) as JSONArray
//                    Log.e("ArySize: ", ": ${ary.length()}")
//                    innerIndex = innerIndex!!.plus(ary.length())
//                }
//            })
//
//        radioList.adapter = radioRecyclerAdapter
//    }
//
//    private fun showCheckData(qObj: QuestionData.QuestionAnsModel) {
//        val recyclerLayoutManager = LinearLayoutManager(this)
//        checkBoxList.layoutManager = recyclerLayoutManager
//        val str = qObj.getAnswerOptions()
//        val result: List<String> = str!!.split("#")
//        chkRecyclerAdapter = CheckRecyclerViewAdapter(
//            result,
//
//            RecyclerItemClickListener.OnItemClickListener { view, position ->
//                val selected = chkRecyclerAdapter.getSelectedItems()!!
//                val commaSeperatedString =
//                    selected.joinToString(separator = "-") { it -> "\'${it}\'" }
//                ansJsonObj!!.put("selected_answer", commaSeperatedString)
//                Log.e("CheckAns: ", ": $commaSeperatedString")
//                if (commaSeperatedString.trim().toLowerCase() == qObj.getAnswer()!!.trim()
//                        .toLowerCase() || qObj.getAnswer() == "all"
//                ) {
//                    innerIndex = innerIndex!!.plus(1)
//                } else {
//                    AppHelper.showToast(this@QuestionDemoActivity, "group id: " + qObj.getGroupID()!!.plus(1))
//                    val ary: JSONArray =
//                        sortedArray!!.get(qObj.getGroupID()!!.minus(1)) as JSONArray
//                    innerIndex = innerIndex!!.plus(ary.length()) //Did minus 1
//                    Log.e("ArySize: $innerIndex", ": ${ary.length()}")
//                }
//            })
//
//        checkBoxList.adapter = chkRecyclerAdapter
//
//    }
//
//    fun nextClick(view: View) {
//        val ansObj = QuestionData.QuestionAnsModel(ansJsonObj!!)
//
//        if (ansObj.getSelectedAnswer()!!.isNotEmpty()) {
//            if (innerIndex!! < (QArray!!.length() - 1)) {
//                setDynamicData(innerIndex,ansJsonObj)
//            }else{
//                AppHelper.showToast(this,"GoTo Report generate")
//            }
//        } else {
//            AppHelper.showToast(this, "Please select answer first")
//        }
//    }
//
//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.answerTxt -> {
//                Log.e("back: ",": $innerIndex")
////                if (innerIndex!! < QArray!!.length() && innerIndex!! > 0) {
////                    setDynamicData(innerIndex, ansJsonObj)
////                }
//            }
//        }
//    }

//    private fun getAnsObj(tickText: String?, qObj: QuestionData): JSONObject {
//        var ansObj: JSONObject = JSONObject()
//        for (i in 0 until qObj.getAnswers()!!.length()) {
//            ansObj = qObj.getAnswers()!!.getJSONObject(i)
//            val ansData = QuestionData.AnswerData(ansObj)
//            if (ansData.getAnswerValue() == tickText) {
//                return ansObj
//            }
//        }
//        return ansObj
//    }

//    fun toStringArray(array: JSONArray?): Array<String?>? {
//        if (array == null) return null
//        val arr = arrayOfNulls<String>(array.length() + 1)
//        arr[0] = "0"
//        for (i in 1 until arr.size) {
//            val obj = QuestionData.AnswerData(array.optJSONObject(i - 1)).getAnswerValue()
//            arr[i] = obj
//        }
//        return arr
//    }


}
