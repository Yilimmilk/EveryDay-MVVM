package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author milk
 * @date 2022/1/3
 */

@JsonClass(generateAdapter = true)
data class GradeResp(
    @Json(name = "name")
    var name: String,

    @Json(name = "studentId")
    var studentId: String,

    @Json(name = "schoolYear")
    var schoolYear: String,

    @Json(name = "schoolTerm")
    var schoolTerm: String,

    //废弃字段
    @Json(name = "gpa")
    var gpa: Float = 0.0F,

    @Json(name = "course")
    var course: MutableList<Course>
) {
    @JsonClass(generateAdapter = true)
    data class Course(
        @Json(name = "courseTitle")
        var courseTitle: String,

        @Json(name = "teacher")
        var teacher: String,

        @Json(name = "courseId")
        var courseId: String,

        @Json(name = "classId")
        var classId: String,

        @Json(name = "courseNature")
        var courseNature: String,

        @Json(name = "credit")
        var credit: String,

        @Json(name = "grade")
        var grade: String,

        @Json(name = "gradePoint")
        var gradePoint: String,

        @Json(name = "gradeNature")
        var gradeNature: String,

        @Json(name = "startCollege")
        var startCollege: String,

        @Json(name = "courseMark")
        var courseMark: String,

        @Json(name = "courseCategory")
        var courseCategory: String,

        @Json(name = "courseAttribution")
        var courseAttribution: String
    )
}
