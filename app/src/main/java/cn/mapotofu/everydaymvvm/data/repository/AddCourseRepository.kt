package cn.mapotofu.everydaymvvm.data.repository

import cn.mapotofu.everydaymvvm.app.database.CourseDao
import cn.mapotofu.everydaymvvm.data.model.entity.Course

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.data.repository
 * @author milk
 * @date 2021/8/8
 */
class AddCourseRepository(private val courseDao: CourseDao) {
    suspend fun insertCourse(course: Course) {
        courseDao.insert(course)
    }

    fun deleteCourse(uid: Int) {
        courseDao.delete(uid)
    }

    fun updateCourse(course: Course) {
        courseDao.update(course)
    }

    fun getCourse(uid: Int): Course {
        return courseDao.getCourse(uid)
    }
}