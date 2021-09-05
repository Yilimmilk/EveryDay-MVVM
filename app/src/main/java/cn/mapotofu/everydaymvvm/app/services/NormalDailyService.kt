package cn.mapotofu.everydaymvvm.app.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.app.util.*
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.repository.ScheduleRepository
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.widget
 * @author milk
 * @date 2021/8/14
 */
class NormalDailyService : DailyRemoteViewService() {
    private val repository = ScheduleRepository(
        AppDataBase.GetDataBaseInstace().courseDao(),
        AppDataBase.GetDataBaseInstace().timetableDao()
    )

    //这里回调数据
    override fun dataSetChanged(
        mAppWidgetId: Int,
        courseCall: CourseCall,
        bTimeTableDCall: TimeTableCall
    ) {
        //是否下一日
        val isNextDay = getPrefer().getBoolean(Const.NEXT_DAY_STATUS + mAppWidgetId, false)
        //今日数字,1234567
        val today = if (DateUtil.getWeekDay() == 0) 7 else DateUtil.getWeekDay()
        //明日数字
        val nextDay = if (today + 1 > 7) 1 else today + 1
        //所需星期几数字
        val requireDay = if (isNextDay) nextDay else today
        //当前周
        //val currentWeek = CacheUtil.getClientConf()!!.nowWeek
        val currentWeek = (DateUtil.getDateDif(
            DateUtil.getDate("yyyy-MM-dd"),
            CacheUtil.getClientConf()!!.termStart
        ) / 7) + 1
        //是否下一周(因为学校教学周开始为周日，所以为7。且必须所需日和是否下一天同时满足，才能设置为true)
        val isNextWeek = requireDay == 7 && isNextDay
        //所需周
        val requireWeek = if (isNextWeek) currentWeek + 1 else currentWeek
        Log.d(
            "小组件调试",
            "是否下一日${isNextDay},今日数字${today},明日数字${nextDay},所需星期几数字${requireDay},当前周${currentWeek},是否下一周${isNextWeek},所需周${requireWeek}"
        )
        try {
            val courseList = repository.getCourseByDay(requireDay, requireWeek)
            val timetable = DataMapsUtil.dataMappingTimeTableToBTimeTable(
                repository.getTimeTable(getPrefer().getString(Const.KEY_CAMPUS, "jiayu")!!)
            )
            courseCall.invoke(courseList)
            bTimeTableDCall.invoke(timetable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //这里绑定视图
    @SuppressLint("Range")
    override fun onBindView(
        mContext: Context,
        course: Course,
        data: MutableList<BTimeTable>
    ): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.item_widget_day_class)
        if (course != null) {
            //remoteViews.setInt(R.id.widgetContainer, "setBackgroundColor", Color.parseColor(course.color))
            remoteViews.setTextViewText(R.id.day_class_title, course.courseName)
            remoteViews.setTextViewText(R.id.day_class_subtitle, "@${course.location}")
            remoteViews.setTextViewText(R.id.day_class_start, "${course.start}")
            remoteViews.setTextViewText(R.id.day_class_end, "${course.start + course.length - 1}")
            remoteViews.setTextViewText(
                R.id.day_time_start,
                data[course.start - 1].startTime
            )
            remoteViews.setTextViewText(
                R.id.day_time_end,
                data[course.start + course.length - 1 - 1].endTime
            )
        } else {
            val str = ""
            remoteViews.setTextViewText(R.id.day_class_title, str)
            remoteViews.setTextViewText(R.id.day_class_subtitle, str)
            remoteViews.setTextViewText(R.id.day_class_start, str)
            remoteViews.setTextViewText(R.id.day_class_end, str)
            remoteViews.setTextViewText(R.id.day_time_start, str)
            remoteViews.setTextViewText(R.id.day_time_end, str)
        }
        return remoteViews
    }
}