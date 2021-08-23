package cn.mapotofu.everydaymvvm.data.model.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2021/7/17
 */

@JsonClass(generateAdapter = true)
data class AboutResp(
    @Json(name = "qa")
    val qa: MutableList<QAList>,

    @Json(name = "license")
    val license: MutableList<LicenseList>,

    @Json(name = "donate")
    val donate: MutableList<String>,

    @Json(name = "aboutMe")
    val aboutMe: MutableList<AboutMeList>,

    @Json(name = "contact")
    val contact: ContactBean,
) {
    @JsonClass(generateAdapter = true)
    data class QAList(
        @Json(name = "question")
        val question: String,

        @Json(name = "answer")
        val answer: String,

        @Json(name = "url")
        val url: String,
    )

    @JsonClass(generateAdapter = true)
    data class LicenseList(
        @Json(name = "title")
        val title: String,

        @Json(name = "content")
        val content: String,

        @Json(name = "url")
        val url: String,
    )

    @JsonClass(generateAdapter = true)
    data class AboutMeList(
        @Json(name = "title")
        val title: String,

        @Json(name = "content")
        val content: String,

        @Json(name = "url")
        val url: String,
    )

    @JsonClass(generateAdapter = true)
    data class ContactBean(
        @Json(name = "wechat")
        val wechat: String,

        @Json(name = "email")
        val email: String,

        @Json(name = "qq")
        val qq: String,
    )
}
