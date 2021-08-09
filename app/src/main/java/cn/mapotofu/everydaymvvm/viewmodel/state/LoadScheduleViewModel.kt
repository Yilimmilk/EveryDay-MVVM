package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.data.repository.ScheduleRepository
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.data.model.bean.ScheduleResp
import cn.mapotofu.everydaymvvm.data.model.bean.TimeTableResp
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/8/1
 */
class LoadScheduleViewModel : BaseViewModel() {
    //数据库相关
    var repository = ScheduleRepository(
        AppDataBase.GetDataBaseInstace().courseDao(),
        AppDataBase.GetDataBaseInstace().timetableDao()
    )

    fun initCourse(course:ScheduleResp) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.isCourseEmpty()) {
                repository.insertCourse(DataMapsUtil.dataMappingScheduleRespToCourse(course))
            }else {
                repository.insertCourseAfterDeleted(DataMapsUtil.dataMappingScheduleRespToCourse(course))
            }
        }
    }

    fun initTimeTable(timetable:TimeTableResp) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.isTimeTableEmpty()) {
                repository.insertTimeTable(DataMapsUtil.dataMappingTimetableRespToTimeTable(timetable))
            }else {
                repository.insertTimeTableAfterDeleted(DataMapsUtil.dataMappingTimetableRespToTimeTable(timetable))
            }
        }
    }

    fun getCourseFromRoom(): MutableList<Course> {
        return repository.getAllCourse()
    }

    fun getTimeTableFromRoom(): MutableList<TimeTable> {
        return repository.getTimeTable("wuchang")
    }
}