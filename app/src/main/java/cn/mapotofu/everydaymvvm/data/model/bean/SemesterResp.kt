package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2021/7/26
 */

@JsonClass(generateAdapter = true)
data class SemesterResp(
    @Json(name = "semesterList")
    val semesterList: MutableList<SemesterList>,
) {
    @JsonClass(generateAdapter = true)
    data class SemesterList(
        @Json(name = "id")
        val id: Int,

        @Json(name = "year")
        val year: String,

        @Json(name = "term")
        val term: String,
    )
}
