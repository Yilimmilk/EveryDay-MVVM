package cn.mapotofu.everydaymvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import cn.mapotofu.everydaymvvm.app.database.CourseDao
import cn.mapotofu.everydaymvvm.app.database.TimeTableDao
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import java.lang.Exception

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.repository
 * @author milk
 * @date 2021/8/1
 */
class ScheduleRepository(
    private val courseDao: CourseDao,
    private val timeTableDao: TimeTableDao
) {
    //获取课表
    fun getAllCourseLiveData(): LiveData<MutableList<Course>> {
        return courseDao.getCourseLiveData()
    }

    fun getAllCourse(): MutableList<Course> {
        return courseDao.getCourse()
    }

    //day取值范围为1-7，requireWeek取值范围为1-最大周
    fun getCourseByDay(day: Int, requireWeek: Int): MutableList<Course>{
        //Log.d("日期","当前日${day},所需周${requireWeek}")
        val courseList: MutableList<Course> = courseDao.getTodayCourse(day)
        val handleList: MutableList<Course> = mutableListOf()
        try {
            courseList.forEach { course ->
                if (course.courseName.first() != '#' && course.weeks.contains(requireWeek)) {
                    handleList.add(course)
                }
            }
        } catch (e: Exception) {
            handleList.clear()
        }
        return handleList
    }

    suspend fun insertCourse(course: Course) {
        Log.i("log", "插入单项$course")
        courseDao.insert(course)
    }

    suspend fun insertCourse(courseList: MutableList<Course>) {
        Log.i("log", "插入列表$courseList")
        courseDao.insert(courseList)
    }

    suspend fun insertCourseAfterDeleted(courseList: MutableList<Course>) {
        courseDao.insertAfterDeleted(courseList)
    }

    suspend fun updateCourseAll(courseList: MutableList<Course>) {
        Log.i("log", "插入列表$courseList")
        courseDao.insert(courseList)
    }

    suspend fun deleteCourse(course: Course) {
        Log.i("log", "删除$course")
        courseDao.delete(course)
    }

    suspend fun deleteCourseAll() {
        Log.i("log", "删除全部")
        courseDao.deleteAll()
    }

    suspend fun isCourseEmpty(): Boolean {
        return courseDao.courseCount() == 0
    }

    //获取时间表,jiayu为嘉鱼校区，wuchang为武昌校区
    fun getTimeTableLiveData(campus: String): LiveData<MutableList<TimeTable>> {
        return timeTableDao.getTimeTableLiveData(campus)
    }

    fun getTimeTable(campus: String): MutableList<TimeTable> {
        return timeTableDao.getTimeTable(campus)
    }

    suspend fun insertTimeTable(timetable: TimeTable) {
        Log.i("log", "插入单项$timetable")
        timeTableDao.insert(timetable)
    }

    suspend fun insertTimeTable(timetable: MutableList<TimeTable>) {
        Log.i("log", "插入列表$timetable")
        timeTableDao.insert(timetable)
    }

    suspend fun insertTimeTableAfterDeleted(timetableList: MutableList<TimeTable>) {
        timeTableDao.insertAfterDeleted(timetableList)
    }

    suspend fun deleteTimeTableAll() {
        timeTableDao.deleteAll()
    }

    suspend fun isTimeTableEmpty(): Boolean {
        return timeTableDao.timetableCount() == 0
    }
}