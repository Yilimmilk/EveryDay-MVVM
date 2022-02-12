package cn.mapotofu.everydaymvvm.data.model.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.hgj.jetpackmvvm.network.BaseResponse

/**
 * @description 网络基类，要使用Keep注解
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author Yili(yili)
 * @date 2021/4/19
 */
@Keep
@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    @field:Json(name = "status")
    val status: Int,
    @field:Json(name = "statusMsg")
    val statusMsg: String,
    @field:Json(name = "data")
    val data: T
) : BaseResponse<T>() {
    override fun isSucces() = status == 200
    override fun getResponseCode() = status
    override fun getResponseMsg() = statusMsg
    override fun getResponseData() = data
}