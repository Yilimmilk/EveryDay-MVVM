package cn.mapotofu.everydaymvvm.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.mapotofu.everydaymvvm.app.database.ListCoverter
import com.squareup.moshi.Json

@Entity(tableName = "course_table")
@TypeConverters(ListCoverter::class)
data class Course(
    //课程ID
    var courseId: String,
    //课程名
    var courseName: String,
    //教师名
    var teachersName: String,
    //校区
    var campus: String,
    //教室位置
    var location: String,
    //包含周次列表
    var weeks: List<Int>,
    //星期几
    var day: Int,
    //课程开始节次，1为起始位
    var start: Int,
    //课程持续节次
    var length: Int,
    //学时组成
    var hoursComposition: String,
    //学分
    var credit: String,
    //课程颜色
    var color: String,
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
)