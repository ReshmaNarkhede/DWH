package com.example.healthwareapplication.model.user

import com.example.healthwareapplication.model.country.CountryData
import java.io.Serializable

class UserDetailModel:Serializable {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var dob: String = ""
    var tob: String = ""
    var mobile: String = ""
    var countryId: String? = ""
    var password: String = ""
    var userType: Int = 1
    var parentId: String = ""
    var relation: String = ""
    var sex: String = ""
    var image: String = ""
    var birthPlaceLatLong: String = ""
    var paediaticName: String = ""
    var operationType: String = ""
    var height: String = ""
    var heightType: String = ""
    var weight: String = ""
    var weightType: String = ""
    var bodyTemp: String = ""
    var bodyTempType: String = ""
    var birthPlaceName: String = ""
    var placeTemp: String = ""
    var placeTempType: String = ""
    var breastfeeding: String = ""
    var country: CountryData? = null
    var doctorSpecificationList: ArrayList<String>? = ArrayList()
    var resume: String = ""
    var resumeName: String = ""
    var mciNo: String = ""
}

