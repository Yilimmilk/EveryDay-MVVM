package cn.mapotofu.everydaymvvm.app.util

import android.util.Log
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.data.model.bean.*
import cn.mapotofu.everydaymvvm.data.model.entity.*
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable
import cn.mapotofu.everydaymvvm.viewmodel.state.GradeViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.ScheduleViewModel
import java.util.*

/**
 * @description 数据映射
 * @package cn.mapotofu.everyday.base.entity
 * @author milk
 * @date 2021/7/25
 */

object DataMapsUtil {
    //Response配置文件映射
    fun dataMappingConfRespToClientConf(confResp: ConfResp): ClientConf {
        return ClientConf(
            confResp.version,
            confResp.chooseSemester,
            confResp.gradeSemester,
            confResp.scheduleSemester,
            confResp.isMaintenance,
            confResp.isVacation,
            confResp.canCourseChoose,
            confResp.termStart
        )
    }

    //Response课表映射
    fun dataMappingScheduleRespToCourse(scheduleResp: ScheduleResp): MutableList<Course> {
        val normalCourseListBeforeParse = scheduleResp.normalCourseList
        val otherCourseListBeforeParse = scheduleResp.otherCourseList
        //普通课程
        val courseList = mutableListOf<Course>()
        //其他课程随机ID
        val randomIdStart = (400..500).random()
        //颜色队列
        val colorQueue: Queue<String> = LinkedList()
        //打乱颜色数组后依次入队
        for (item in Constants.COLOR_PALETTE.shuffled()) colorQueue.offer(item)
        //已经出现过的课程Map
        val alreadyAppearedCourseId = mutableMapOf<String, String>()
        //遍历
        normalCourseListBeforeParse.forEach { it ->
            //出队第一个值
            val color = colorQueue.poll()
            //随即将出队的值从末尾入列
            colorQueue.offer(color)
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
                (if (alreadyAppearedCourseId.containsKey(it.courseId)) alreadyAppearedCourseId[it.courseId] else color)!!,
            )
            //如果当前map不包含key，则将当前循环课程id加入到数组中
            if (!alreadyAppearedCourseId.containsKey(it.courseId)) alreadyAppearedCourseId[it.courseId] =
                color!!
            //向最终结果添加当前item
            courseList.add(course)
        }
        otherCourseListBeforeParse.forEachIndexed { index, it ->
            val course = Course().apply {
                courseId = "#${randomIdStart + index}"
                courseName = it.courseTitle
                teachersName = it.teacher
                weeksText = it.courseWeek
                credit = it.credit
                color = Constants.COLOR_PALETTE[Random().nextInt(Constants.COLOR_PALETTE.size)]
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
        semesterListBeforeParse!!.forEach { it ->
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

    fun dataMappingGradeRespToGradeModel(gradeList: GradeResp): MutableList<GradeViewModel.GradeModel> {
        val gradeModelList = mutableListOf<GradeViewModel.GradeModel>()
        gradeList.course.forEach { it ->
            val gradeModel = GradeViewModel.GradeModel(
                it.courseTitle,
                it.classId,
                it.courseNature,
                "${it.credit}学分",
                "${it.grade}分",
                if(it.gradePoint == " ") "无" else "${it.gradePoint}绩点",
                it.gradeNature
            )
            gradeModelList.add(gradeModel)
        }
        return gradeModelList
    }

    fun dataMappingGradeDetailRespToGradeDetailModel(gradeDetailList: GradeDetailResp): MutableList<GradeViewModel.GradeDetailModel> {
        val gradeDetailModelList = mutableListOf<GradeViewModel.GradeDetailModel>()
        gradeDetailList.courseDetail.forEach { it ->
            val gradeDetailModel = GradeViewModel.GradeDetailModel(
                it.name,
                it.percent,
                "${it.score}分"
            )
            gradeDetailModelList.add(gradeDetailModel)
        }
        return gradeDetailModelList
    }

    fun dataMappingOtherCourseListToOtherCourseModel(otherCourseList: MutableList<Course>): MutableList<ScheduleViewModel.OtherCourseModel> {
        val otherCourseModelList = mutableListOf<ScheduleViewModel.OtherCourseModel>()
        otherCourseList.forEach { it ->
            val otherCourseModel = ScheduleViewModel.OtherCourseModel(
                it.courseName,
                it.teachersName,
                it.weeksText,
                "${it.credit}学分"
            )
            otherCourseModelList.add(otherCourseModel)
        }
        return otherCourseModelList
    }
}