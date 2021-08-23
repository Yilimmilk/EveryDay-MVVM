package cn.mapotofu.everydaymvvm.app.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.widget
 * @author milk
 * @date 2021/8/14
 */
typealias CourseCall = ((course: MutableList<Course>) -> Unit)
typealias TimeTableCall = ((timetable: BTimeTable) -> Unit)

abstract class DailyRemoteViewService : RemoteViewsService() {
    private lateinit var courseList: MutableList<Course>
    private lateinit var timeTable: BTimeTable

    inner class RemoteViewsFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
        private val mAppWidgetId: Int = intent.getIntExtra(Constants.APP_WIDGET_ID, -1)

        override fun onCreate() {}

        override fun onDataSetChanged() {
            dataSetChanged(
                mAppWidgetId,
                courseCall = { courseList = it},
                bTimeTableDCall = { timeTable = it}
            )
        }

        override fun onDestroy() {
            courseList.clear()
        }

        override fun getCount(): Int {
            return courseList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val course: Course = courseList[position]
            val data: BTimeTable = timeTable
            return onBindView(mContext, course, data)
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }

    /**
     * 更新数据
     */
    protected abstract fun dataSetChanged(
        mAppWidgetId: Int,
        courseCall: CourseCall,
        bTimeTableDCall: TimeTableCall
    )

    /**
     * 绑定视图
     */
    protected abstract fun onBindView(
        mContext: Context,
        course: Course,
        data: BTimeTable
    ): RemoteViews

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return RemoteViewsFactory(this, intent)
    }
}