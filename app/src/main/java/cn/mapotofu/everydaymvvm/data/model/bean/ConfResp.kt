package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author Yili(yili)
 * @date 2021/4/24
 */

@JsonClass(generateAdapter = true)
data class ConfResp(
    @Json(name = "version")
    val version: Int,

    @Json(name = "chooseSemester")
    val chooseSemester: String,

    @Json(name = "gradeSemester")
    val gradeSemester: String,

    @Json(name = "scheduleSemester")
    val scheduleSemester: String,

    @Json(name = "isMaintenance")
    val isMaintenance: Boolean,

    @Json(name = "isVacation")
    val isVacation: Boolean,

    @Json(name = "canCourseChoose")
    val canCourseChoose: Boolean,

    @Json(name = "termStart")
    val termStart: String,

    @Json(name = "nowWeek")
    val nowWeek: Int,
)
