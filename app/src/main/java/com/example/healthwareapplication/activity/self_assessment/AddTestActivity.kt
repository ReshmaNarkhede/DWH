package com.example.healthwareapplication.activity.self_assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthwareapplication.R
import com.example.healthwareapplication.databinding.ActivityAddMedicineBinding
import com.example.healthwareapplication.databinding.ActivityAddTestBinding

class AddTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
