package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author Yili(yili)
 * @date 2021/4/4
 */
@JsonClass(generateAdapter = true)
data class LoginTokenResp(
    @Json(name = "studentId")
    var studentId: String?,

    @Json(name = "name")
    var name: String?,

    @Json(name = "token")
    var token: String?,
)