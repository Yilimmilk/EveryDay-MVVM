package cn.mapotofu.everydaymvvm.app.database

import androidx.room.TypeConverter
import cn.mapotofu.everydaymvvm.app.util.MoshiUtil
import cn.mapotofu.everydaymvvm.app.util.TypeToken

/**
 * @description
 * @package cn.mapotofu.everyday.database
 * @author Yili(yili)
 * @date 2021/4/6
 */
class ListCoverter {
    @TypeConverter
    fun stringToObject(value: String): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {

        }.type
        return MoshiUtil.fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Int>): String {
        return MoshiUtil.toJson(list)
    }
}