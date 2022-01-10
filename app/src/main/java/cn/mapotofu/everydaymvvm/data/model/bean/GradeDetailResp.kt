package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2022/1/3
 */

@JsonClass(generateAdapter = true)
data class GradeDetailResp(
    @Json(name = "courseName")
    var courseName: String,

    @Json(name = "courseDetail")
    var courseDetail: MutableList<DetailItem>
) {
    @JsonClass(generateAdapter = true)
    data class DetailItem(
        @Json(name = "name")
        var name: String,

        @Json(name = "percent")
        var percent: String,

        @Json(name = "score")
        var score: String,
    )
}
