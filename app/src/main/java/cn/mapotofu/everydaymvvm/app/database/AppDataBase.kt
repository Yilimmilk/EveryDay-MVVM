package cn.mapotofu.everydaymvvm.app.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.Grade
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable

@Database(entities = [Course::class, Grade::class, TimeTable::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun timetableDao(): TimeTableDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getDataBaseInstance(): AppDataBase {
            if (instance == null) {
                synchronized(AppDataBase::class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            App.instance,
                            AppDataBase::class.java,
                            "UserData.db"
                        )
                            .allowMainThreadQueries()
                            .addMigrations(migrate_1to2)
                            .build()
                    }
                }
            }
            return instance!!
        }

        /**
         * 添加原始课程周次字段
         */
        private val migrate_1to2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE course_table ADD COLUMN weeksText TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
