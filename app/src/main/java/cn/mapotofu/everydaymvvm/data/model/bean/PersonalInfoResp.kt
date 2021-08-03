package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2021/7/17
 */
@JsonClass(generateAdapter = true)
data class PersonalInfoResp (
    @Json(name = "name")
    var name: String,

    @Json(name = "studentId")
    var studentId: String,

    @Json(name = "birthDay")
    var birthDay: String,

    @Json(name = "idNumber")
    var idNumber: String,

    @Json(name = "candidateNumber")
    var candidateNumber: String,

    @Json(name = "status")
    var status: String,

    @Json(name = "collegeName")
    var collegeName: String,

    @Json(name = "majorName")
    var majorName: String,

    @Json(name = "className")
    var className: String,

    @Json(name = "entryDate")
    var entryDate: String,

    @Json(name = "graduationSchool")
    var graduationSchool: String,

    @Json(name = "domicile")
    var domicile: String,

    @Json(name = "phoneNumber")
    var phoneNumber: String,

    @Json(name = "parentsNumber")
    var parentsNumber: String,

    @Json(name = "email")
    var email: String,

    @Json(name = "politicalStatus")
    var politicalStatus: String,

    @Json(name = "national")
    var national: String,

    @Json(name = "education")
    var education: String,

    @Json(name = "postalCode")
    var postalCode: String,

    @Json(name = "grade")
    var grade: Int,
)