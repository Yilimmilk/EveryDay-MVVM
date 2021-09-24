package cn.mapotofu.everydaymvvm.app.util

import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.data.model.bean.AboutResp
import cn.mapotofu.everydaymvvm.data.model.bean.ScheduleResp
import cn.mapotofu.everydaymvvm.data.model.bean.SemesterResp
import cn.mapotofu.everydaymvvm.data.model.bean.TimeTableResp
import cn.mapotofu.everydaymvvm.data.model.entity.AboutItem
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.Semester
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable
import java.util.*

/**
 * @description 数据映射
 * @package cn.mapotofu.everyday.base.entity
 * @author milk
 * @date 2021/7/25
 */

object DataMapsUtil {

    //Response课表映射
    fun dataMappingScheduleRespToCourse(scheduleResp: ScheduleResp): MutableList<Course> {
        val normalCourseListBeforeParse = scheduleResp.normalCourseList
        val otherCourseListBeforeParse = scheduleResp.otherCourseList
        val courseList = mutableListOf<Course>()
        val randomIdStart = (400..500).random()
        normalCourseListBeforeParse.forEach { it ->
            val course = Course(
                it.courseId,
                it.courseTitle,
                it.teacher,
                it.campus,
                it.courseRoom,
                it.courseWeek,
                it.includeWeeks,
                it.courseWeekday,
                it.includeSection.first(),
                it.includeSection.last() - it.includeSection.first() + 1,
                it.hoursComposition,
                it.credit,
                Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)],
            )
            courseList.add(course)
        }
        otherCourseListBeforeParse.forEachIndexed { index, it ->
            val course = Course().apply {
                courseId = "#${randomIdStart + index}"
                courseName = it.courseTitle
                teachersName = it.teacher
                weeksText = it.courseWeek
                credit = it.credit
                color = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]
            }
            courseList.add(course)
        }
        return courseList
    }

    //Response时间表映射
    fun dataMappingTimetableRespToTimeTable(
        timeTableResp: TimeTableResp
    ): MutableList<TimeTable> {
        val timetableJiaYuListTimesUp: MutableList<String> =
            timeTableResp.timetableForJiaYu.timesUp
        val timetableJiaYuListTimesDown: MutableList<String> =
            timeTableResp.timetableForJiaYu.timesDown
        val timetableWuChangListTimesUp: MutableList<String> =
            timeTableResp.timetableForWuChang.timesUp
        val timetableWuChangListTimesDown: MutableList<String> =
            timeTableResp.timetableForWuChang.timesDown
        val timetableResultList = mutableListOf<TimeTable>()
        timetableJiaYuListTimesUp.forEachIndexed() { index, it ->
            timetableResultList.add(
                TimeTable(
                    "jiayu",
                    index,
                    timetableJiaYuListTimesUp[index],
                    timetableJiaYuListTimesDown[index]
                )
            )
        }
        timetableWuChangListTimesUp.forEachIndexed() { index, it ->
            timetableResultList.add(
                TimeTable(
                    "wuchang",
                    index,
                    timetableWuChangListTimesUp[index],
                    timetableWuChangListTimesDown[index]
                )
            )
        }
        return timetableResultList
    }

    //课表映射
    fun dataMappingCourseToBCourse(courseList: MutableList<Course>): MutableList<BCourse> {
        val bCourseList = mutableListOf<BCourse>()
        courseList.forEach { it ->
            val bCourse = BCourse()
            bCourse.id = it.uid
            bCourse.name = it.courseName
            bCourse.teacher = it.teachersName
            bCourse.position = it.location
            bCourse.day = it.day
            bCourse.sectionStart = it.start
            bCourse.sectionContinue = it.length
            bCourse.color = it.color
            bCourse.week = it.weeks
            bCourseList.add(bCourse)
        }
        return bCourseList
    }

    //课表映射
    fun dataMappingCourseToBCourse(course: Course): BCourse {
        val bCourse = BCourse()
        bCourse.id = course.uid
        bCourse.name = course.courseName
        bCourse.teacher = course.teachersName
        bCourse.position = course.location
        bCourse.day = course.day
        bCourse.sectionStart = course.start
        bCourse.sectionContinue = course.length
        bCourse.color = course.color
        bCourse.week = course.weeks
        return bCourse
    }

    //时间表映射
    fun dataMappingTimeTableToBTimeTable(timeTableList: MutableList<TimeTable>): MutableList<BTimeTable> {
        val bTimetableList = mutableListOf<BTimeTable>()
        timeTableList.forEach() { it ->
            val bTimeTable = BTimeTable()
            bTimeTable.sessionNo = it.sessionNum
            bTimeTable.startTime = it.startTime
            bTimeTable.endTime = it.endTime
            bTimetableList.add(bTimeTable)
        }
        return bTimetableList
    }

    //时间表映射
    fun dataMappingTimeTableToBTimeTable(timeTable: TimeTable): BTimeTable {
        val bTimetable = BTimeTable()
        bTimetable.sessionNo = timeTable.sessionNum
        bTimetable.startTime = timeTable.startTime
        bTimetable.endTime = timeTable.endTime
        return bTimetable
    }

    //学期表映射
    fun dataMappingSemesterRespToSemester(semesterResp: SemesterResp): MutableList<Semester> {
        val semesterListBeforeParse = semesterResp.semesterList
        val semesterList = mutableListOf<Semester>()
        semesterListBeforeParse.forEach { it ->
            val semester = Semester(it.year, it.term, "${it.year}年第${it.term}学期")
            semesterList.add(semester)
        }
        return semesterList
    }

    fun dataMappingAboutRespAboutMeToAboutItem(aboutmeList: MutableList<AboutResp.AboutMeList>): MutableList<AboutItem> {
        val aboutmeItemList = mutableListOf<AboutItem>()
        aboutmeList.forEach { it ->
            val aboutItem = AboutItem(
                it.title,
                it.content,
                it.url
            )
            aboutmeItemList.add(aboutItem)
        }
        return aboutmeItemList
    }

    fun dataMappingAboutRespQAToAboutItem(qaList: MutableList<AboutResp.QAList>): MutableList<AboutItem> {
        val qaItemList = mutableListOf<AboutItem>()
        qaList.forEach { it ->
            val qaItem = AboutItem(
                it.question,
                it.answer,
                it.url
            )
            qaItemList.add(qaItem)
        }
        return qaItemList
    }
}