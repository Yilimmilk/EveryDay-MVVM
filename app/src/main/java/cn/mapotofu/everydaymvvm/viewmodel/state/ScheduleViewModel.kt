package cn.mapotofu.everydaymvvm.viewmodel.state

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.data.repository.ScheduleRepository
import cn.mapotofu.everydaymvvm.app.util.DateUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/31
 */
class ScheduleViewModel : BaseViewModel() {
    private val strNumOfTeachingWeek = appContext.resources.getString(R.string.num_of_teaching_week)
    private val strNotThisWeek = appContext.resources.getString(R.string.not_this_week)
    private val strHolidayMode = appContext.resources.getString(R.string.holiday_mode)
    private val strMonth = appContext.resources.getString(R.string.month)
    private val strDay = appContext.resources.getString(R.string.day)

    //数据库相关
    var repository = ScheduleRepository(
        AppDataBase.getDataBaseInstance().courseDao(),
        AppDataBase.getDataBaseInstance().timetableDao()
    )
    private val clientConf = appViewModel.clientConf.value

    //课表当前学年学期
    val currentScheduleYear = clientConf?.scheduleSemester?.substring(IntRange(0, 3))
    val currentScheduleTerm = clientConf?.scheduleSemester?.substring(IntRange(4, 4))

    //周次数据
    val teachingWeekNum = (DateUtil.getDateDif(
        DateUtil.getDate("yyyy-MM-dd"),
        clientConf!!.termStart
    ) / 7) + 1
    var teachingWeekText = MutableLiveData(String.format(strNumOfTeachingWeek, teachingWeekNum))
    var teachingWeekSelected = MutableLiveData<Int>(teachingWeekNum)

    val termStartDate = clientConf?.termStart

    //当前日期
    val nowDate = "${DateUtil.nowDate()[0]}${strMonth}${DateUtil.nowDate()[1]}${strDay}"

    init {
        //当前周
        teachingWeekSelected.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                if (clientConf?.isVacation == true) {
                    teachingWeekText.postValue(
                        "${
                            String.format(
                                strNumOfTeachingWeek,
                                it
                            )
                        }[${strHolidayMode}]"
                    )
                } else {
                    if (teachingWeekNum == teachingWeekSelected.value)
                        teachingWeekText.postValue(String.format(strNumOfTeachingWeek, it))
                    else
                        teachingWeekText.postValue("${
                            String.format(
                                strNumOfTeachingWeek,
                                it
                            )
                        }[${strNotThisWeek}]")
                }
            }
        }
    }

    fun getCourseFromRoom(): MutableList<Course> {
        return repository.getAllCourse()
    }

    fun getTimeTableFromRoom(campus: String): MutableList<TimeTable> {
        return repository.getTimeTable(campus)
    }

    data class OtherCourseModel(
        var itemCourseName: String = "",
        var itemTeacherName: String = "",
        var itemWeeks: String = "",
        var itemCredit: String = ""
    )

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}