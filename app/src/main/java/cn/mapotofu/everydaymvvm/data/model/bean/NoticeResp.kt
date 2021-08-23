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
data class NoticeResp(
    @Json(name = "notice")
    val notice: MutableList<NoticeBean>?,

    @Json(name = "importantNotice")
    val importantNotice: NoticeBean?
) {
    @JsonClass(generateAdapter = true)
    data class NoticeBean(
        @Json(name = "id")
        val id: Int,

        @Json(name = "title")
        val title: String,

        @Json(name = "image_url")
        val image_url: String,

        @Json(name = "content")
        val content: String,

        @Json(name = "is_show")
        val is_show: Boolean,

        @Json(name = "date")
        val date: String,
    )
}
