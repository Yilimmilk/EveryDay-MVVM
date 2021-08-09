package cn.mapotofu.everydaymvvm.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @description 课表左侧时间列表实体类
 * @package cn.mapotofu.everyday.base.entity
 * @author milk
 * @date 2021/7/25
 */

@Entity(tableName = "timetable")
data class TimeTable(
    val campus: String = "",
    //第几节
    val sessionNum: Int = 0,
    //起始时间
    val startTime: String = "",
    //结束时间
    val endTime: String = "",
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
)