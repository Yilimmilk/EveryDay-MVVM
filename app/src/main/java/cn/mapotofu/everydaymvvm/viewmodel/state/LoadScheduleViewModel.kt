package cn.mapotofu.everydaymvvm.viewmodel.state

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.repository.ScheduleRepository
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.data.model.bean.ScheduleResp
import cn.mapotofu.everydaymvvm.data.model.bean.TimeTableResp
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
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
        AppDataBase.getDataBaseInstance().courseDao(),
        AppDataBase.getDataBaseInstance().timetableDao()
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

    //是否已登陆
    val isLogin = MutableLiveData<Boolean>(CacheUtil.getIsLogin())

    init {
        isLogin.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(LoginViewModel.TAG,"isLogin数据变化")
                CacheUtil.setIsLogin(it)
            }
        }
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}