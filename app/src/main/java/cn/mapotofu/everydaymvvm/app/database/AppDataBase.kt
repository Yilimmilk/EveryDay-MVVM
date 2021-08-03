package cn.mapotofu.everydaymvvm.app.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.Grade
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable

@Database(entities = [Course::class, Grade::class, TimeTable::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun timetableDao(): TimeTableDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun GetDataBaseInstace(): AppDataBase {
            if (instance == null) {
                synchronized(AppDataBase::class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            App.instance,
                            AppDataBase::class.java,
                            "UserData.db"
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}
