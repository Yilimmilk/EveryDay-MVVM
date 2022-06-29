package cn.mapotofu.everydaymvvm.data.model.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.model.bean
 * @author Yili(yili)
 * @date 2021/4/4
 */
@JsonClass(generateAdapter = true)
data class ScheduleResp(
    @Json(name = "name")
    var name: String,

    @Json(name = "studentId")
    var studentId: String,

    @Json(name = "schoolYear")
    var schoolYear: String,

    @Json(name = "schoolTerm")
    var schoolTerm: String,

    @Json(name = "normalCourse")
    var normalCourseList: MutableList<NormalCourseList>,

    @Json(name = "otherCourse")
    var otherCourseList: MutableList<OtherCourseList>
) {
    @JsonClass(generateAdapter = true)
    data class NormalCourseList(
        @Json(name = "courseTitle")
        var courseTitle: String,

        @Json(name = "courseTitleShort")
        var courseTitleShort: String,

        @Json(name = "teacher")
        var teacher: String,

        @Json(name = "courseId")
        var courseId: String,

        @Json(name = "courseWeekday")
        var courseWeekday: Int,

        @Json(name = "courseSection")
        var courseSection: String,

        @Json(name = "includeSection")
        var includeSection: MutableList<Int>,

        @Json(name = "courseWeek")
        var courseWeek: String,

        @Json(name = "includeWeeks")
        var includeWeeks: MutableList<Int>,

        @Json(name = "exam")
        var exam: String,

        @Json(name = "campus")
        var campus: String,

        @Json(name = "courseRoom")
        var courseRoom: String,

        @Json(name = "className")
        var className: String,

        @Json(name = "classComposition")
        var classComposition: String,

        @Json(name = "hoursComposition")
        var hoursComposition: String,

        @Json(name = "weeklyHours")
        var weeklyHours: String,

        @Json(name = "totalHours")
        var totalHours: String,

        @Json(name = "credit")
        var credit: String
    )
    @JsonClass(generateAdapter = true)
    data class OtherCourseList(
        @Json(name = "courseTitle")
        var courseTitle: String,

        @Json(name = "teacher")
        var teacher: String,

        @Json(name = "courseWeek")
        var courseWeek: String,

        @Json(name = "courseText")
        var courseText: String,

        @Json(name = "credit")
        var credit: String
    )
}
