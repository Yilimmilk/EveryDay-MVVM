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
) {
    @JsonClass(generateAdapter = true)
    data class NoticeBean(
        @Json(name = "id")
        val id: Int,

        @Json(name = "title")
        val title: String,

        @Json(name = "content")
        val content: String,

        @Json(name = "inner_web_url")
        val inner_web_url: String,

        @Json(name = "addition_action_title")
        val addition_action_title: String,

        @Json(name = "addition_action_url")
        val addition_action_url: String,

        @Json(name = "background_color_1")
        val background_color_1: String,

        @Json(name = "background_color_2")
        val background_color_2: String,

        @Json(name = "background_color_3")
        val background_color_3: String,

        @Json(name = "is_important")
        val is_important: Boolean,

        @Json(name = "date")
        val date: String,
    )
}
