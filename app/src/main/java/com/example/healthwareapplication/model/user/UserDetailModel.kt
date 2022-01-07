package com.example.healthwareapplication.model.user

import com.example.healthwareapplication.model.country.CityData
import java.io.Serializable

class UserDetailModel:Serializable {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var dob: String = ""
    var tob: String = ""
    var mobile: String = ""
    var mobileLength: String? = ""
    var mobilePrefix: String? = ""
    var cityId: Int? = 0
    var cityName: String? = ""
    var countryId: String? = ""
    var countryName: String? = ""
    var password: String = ""
    var userType: Int = 0
    var parentId: String = ""
    var sex: String = ""
    var height: String = ""
    var resume: String = ""
    var resumeName: String = ""
    var mciNo: String = ""
    var speciality: String = ""
    var experience: String = ""
}

