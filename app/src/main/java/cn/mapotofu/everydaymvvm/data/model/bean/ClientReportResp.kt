package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2021/8/11
 */

@JsonClass(generateAdapter = true)
data class ClientReportResp(
    @Json(name = "studentId")
    val studentId: String,

    @Json(name = "tokenValid")
    val tokenValid: Boolean,
)
