package cn.mapotofu.everydaymvvm.app.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.mapotofu.everydaymvvm.data.model.entity.Course

@Dao
abstract class CourseDao {
    @Query("SELECT * from course_table")
    abstract fun getCourseLiveData(): LiveData<MutableList<Course>>

    @Query("SELECT * from course_table")
    abstract fun getCourse(): MutableList<Course>

    @Query("SELECT * from course_table where uid = :uid")
    abstract fun getCourse(uid: Int): Course

    @Query("select * from course_table where day = :day order by start")
    abstract fun getTodayCourse(day: Int): MutableList<Course>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(course: Course)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(courseList: MutableList<Course>)

    @Update
    abstract fun update(courses: Course)

    @Query("DELETE FROM course_table")
    abstract suspend fun deleteAll()

    @Delete
    abstract suspend fun delete(course: Course)

    @Query("delete from course_table where uid = :uid")
    abstract fun delete(uid: Int)

    @Query("select count(*) from course_table")
    abstract suspend fun courseCount(): Int

    @Transaction
    open suspend fun insertAfterDeleted(courseList: MutableList<Course>) {
        deleteAll()
        insert(courseList)
    }
}