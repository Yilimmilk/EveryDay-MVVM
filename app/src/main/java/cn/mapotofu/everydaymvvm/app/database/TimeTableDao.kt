package cn.mapotofu.everydaymvvm.app.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable

/**
 * @description
 * @package cn.mapotofu.everyday.database
 * @author milk
 * @date 2021/7/28
 */
@Dao
abstract class TimeTableDao {
    @Query("SELECT * from timetable where campus = :campus")
    abstract fun getTimeTableLiveData(campus: String): LiveData<MutableList<TimeTable>>

    @Query("SELECT * from timetable  where campus = :campus")
    abstract fun getTimeTable(campus: String): MutableList<TimeTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(timetable: TimeTable)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(timetable: MutableList<TimeTable>)

    @Query("DELETE FROM timetable")
    abstract suspend fun deleteAll()

    @Query("select count(*) from timetable")
    abstract suspend fun timetableCount(): Int

    @Transaction
    open suspend fun insertAfterDeleted(timetable: MutableList<TimeTable>) {
        deleteAll()
        insert(timetable)
    }
}