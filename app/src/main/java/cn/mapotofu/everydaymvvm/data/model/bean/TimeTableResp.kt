package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2021/7/28
 */

@JsonClass(generateAdapter = true)
data class TimeTableResp(
    @Json(name = "timetableForJiaYu")
    val timetableForJiaYu: Times,

    @Json(name = "timetableForWuChang")
    val timetableForWuChang: Times,
) {
    @JsonClass(generateAdapter = true)
    data class Times(
        @Json(name = "timesUp")
        val timesUp: MutableList<String>,

        @Json(name = "timesDown")
        val timesDown: MutableList<String>,
    )
}
