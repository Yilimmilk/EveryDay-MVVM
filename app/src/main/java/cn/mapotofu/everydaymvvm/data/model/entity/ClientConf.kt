package cn.mapotofu.everydaymvvm.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.entity
 * @author milk
 * @date 2021/7/31
 */
@SuppressLint("ParcelCreator")
@Parcelize
@JsonClass(generateAdapter = true)
data class ClientConf(
    val version: Int = 0,
    val chooseSemester: String = "",
    val gradeSemester: String = "",
    val scheduleSemester: String = "",
    val isMaintenance: Boolean = false,
    val isVacation: Boolean = false,
    val canCourseChoose: Boolean = false,
    val termStart: String = "",
    val nowWeek: Int = 0,
) : Parcelable