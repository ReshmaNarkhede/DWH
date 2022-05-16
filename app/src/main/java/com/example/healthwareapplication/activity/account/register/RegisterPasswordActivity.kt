package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityRegisterPasswordBinding
import com.example.healthwareapplication.model.user.UserDetailModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPasswordBinding
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if (userDetailModel.userType == 1) {
            binding.userInfoTxt.text =
                userDetailModel.speciality.plus(", ").plus(userDetailModel.experience.plus(" ").plus(getString(
                                    R.string.experience_suffix)))
        } else if (userDetailModel.userType == 2) {
            binding.userInfoTxt.text = userDetailModel.dob.plus(", ").plus(userDetailModel.email)
        }
        binding.userInfoTxt.setOnClickListener {
            finish()
        }
    }

    private fun defaultConfiguration() {
        binding.pwdEdtTxt.doAfterTextChanged {
            binding.errorText.visibility = View.INVISIBLE
        }
        binding.cnfmPwdEdtTxt.doAfterTextChanged {
            binding.errorText.visibility = View.INVISIBLE
        }
        binding.okayBtn.setOnClickListener {
            checkValidation()
        }
    }

    private fun checkValidation() {
        val newPwd = binding.pwdEdtTxt.text.toString()
        val cnfmPwd = binding.cnfmPwdEdtTxt.text.toString()
        when {
            TextUtils.isEmpty(newPwd) || newPwd.length < 8 -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = getString(R.string.weak_password)
            }
            !isValidPassword(newPwd)->{
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = getString(R.string.password_limit)
            }
            TextUtils.isEmpty(cnfmPwd) || cnfmPwd.length < 8 -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text =  getString(R.string.weak_cnfm_password)
            }
            !isValidPassword(cnfmPwd)->{
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = getString(R.string.cnfm_password_limit)
            }
            newPwd != cnfmPwd ->{
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = getString(R.string.password_not_match)
            }
            else -> {
                binding.errorText.visibility = View.INVISIBLE
                goToNext()
            }
        }
    }

    private fun goToNext() {
        val pwd = binding.pwdEdtTxt.text.toString()
        userDetailModel.password = pwd

        val intent = Intent(this, RegisterTermsActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

    fun isValidPassword(password: String?): Boolean {
        val regex = ("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$")

        val p: Pattern = Pattern.compile(regex)
        if (password == null) {
            return false
        }
        val m: Matcher = p.matcher(password)
        return m.matches()
    }
}
