package cn.mapotofu.everydaymvvm.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.entity
 * @author milk
 * @date 2021/7/29
 */

@SuppressLint("ParcelCreator")
@Parcelize
@JsonClass(generateAdapter = true)
data class UserInfo(
    var studentId: String="",
    var name: String="",
    var password: String="",
    var token: String="",
    var type: Int =0
) : Parcelable
