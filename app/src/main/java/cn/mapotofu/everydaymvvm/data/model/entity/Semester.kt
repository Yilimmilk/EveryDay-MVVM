package cn.mapotofu.everydaymvvm.data.model.entity

import androidx.room.PrimaryKey

/**
 * @description
 * @package cn.mapotofu.everyday.base.entity
 * @author milk
 * @date 2021/7/25
 */
data class Semester(
    //学年起始(2020)
    var year: String,
    //学期(1/2)
    var term: String,
    //显示字符串
    var str: String,
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
)
