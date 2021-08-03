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
data class CountDownResp(
    @Json(name = "countdown")
    val countdown: MutableList<CountdownList>,
) {
    @JsonClass(generateAdapter = true)
    data class CountdownList(
        @Json(name = "id")
        val id: Int,

        @Json(name = "name")
        val name: String,

        @Json(name = "title")
        val title: String,

        @Json(name = "date")
        val date: String,

        @Json(name = "remainingDays")
        val remainingDays: String,
    )
}
