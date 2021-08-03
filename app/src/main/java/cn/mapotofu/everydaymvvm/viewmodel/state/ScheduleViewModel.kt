package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.DataBaseUtil
import cn.mapotofu.everydaymvvm.app.util.DateUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/31
 */
class ScheduleViewModel : BaseViewModel(){
    //数据库相关
    var repository = DataBaseUtil(
        AppDataBase.GetDataBaseInstace().courseDao(),
        AppDataBase.GetDataBaseInstace().timetableDao()
    )
    val clientConf = App.appViewModelInstance.clientConf.value
    //周次数据
    val teachingWeekNum = clientConf?.nowWeek
    var teachingWeekText = MutableLiveData("第${teachingWeekNum}教学周")
    var teachingWeekSelected = MutableLiveData<Int>(teachingWeekNum)

    val termStartDate = clientConf?.termStart

    //当前日期
    val nowDate = "${DateUtil.nowDate()[0]}月${DateUtil.nowDate()[1]}日"

    init {
        //当前周
        teachingWeekSelected.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                when (clientConf?.isVacation) {
                    true -> {
                        teachingWeekText.postValue("第" + it + "教学周[假期模式]")
                    }
                    false -> {
                        if (teachingWeekNum == teachingWeekSelected.value)
                            teachingWeekText.postValue("第" + it + "教学周")
                        else
                            teachingWeekText.postValue("第" + it + "教学周[非本周]")
                    }
                }
            }
        }
    }


    fun getCourseFromRoom(): MutableList<Course> {
        return repository.getAllCourse()
    }

    fun getTimeTableFromRoom(): MutableList<TimeTable> {
        return repository.getTimeTable("jiayu")
    }
}