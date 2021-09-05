package cn.mapotofu.everydaymvvm.data.model.bean

import androidx.annotation.Keep
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

    //后续此参数将被废弃
    @Json(name = "nowWeek")
    val nowWeek: Int,

    //以下两个参数用于数据上报
    //为了兼容老版本，且第一次登录时以下两个参数绝对为空，所以必须为可空数据类型
    @Json(name = "studentId")
    var studentId: String? = null,

    @Json(name = "tokenValid")
    var tokenValid: Boolean? = null,
)
