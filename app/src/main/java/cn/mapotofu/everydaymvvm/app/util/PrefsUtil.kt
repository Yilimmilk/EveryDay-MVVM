package cn.mapotofu.everydaymvvm.app.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

fun Context.getPrefer(name: String = "config"): SharedPreferences = getSharedPreferences(name, MODE_PRIVATE)

object Const {
    const val KEY_CAMPUS = "campus"
    const val KEY_USER_AGREEMENT = "user-agreement"
    const val KEY_PRIVACY_POLICY = "privacy-policy"
}