package cn.mapotofu.everydaymvvm.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import cn.mapotofu.everydaymvvm.ui.custom.courseinfo.COURSE_UID
import cn.mapotofu.everydaymvvm.ui.custom.courseinfo.CourseInfoPanel
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.CourseTableView
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.DataConfig
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.interfaces.OnClickCourseItemListener
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.UIConfig
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_course_table.view.*

/**
 * @description
 * @package cn.mapotofu.everyday.pages.schedule
 * @author milk
 * @date 2021/7/19
 */
class ScheduleViewPagerAdapter(
    //fragment对象
    private val fragment: Fragment,
    //侧边栏时间表
    private val timetable: MutableList<TimeTable>,
    //学期开始日期
    private val termStartDate: String,
    //当前周
    private val currentWeek: Int,
    //是否暗黑模式
    private val isDarkMode: Boolean,
) : BaseQuickAdapter<MutableList<Course>, BaseViewHolder>(R.layout.item_course_table) {
    private val uiConfig = UIConfig()

    init {
        uiConfig.maxClassDay = 7
        uiConfig.itemTextSize = 12
        uiConfig.itemTopMargin = 5
        uiConfig.itemSideMargin = 3
        uiConfig.sectionViewWidth = UisUtil.dip2px(App.context, 40F)
        uiConfig.isShowTimeTable = true
        uiConfig.maxSection = 12
        uiConfig.isShowNotCurWeekCourse = false
        uiConfig.sectionHeight = UisUtil.dip2px(App.context, 60F)
        uiConfig.chooseWeekColor = App.context.resources.getColor(R.color.colorPrimary)
        if (isDarkMode) uiConfig.colorUI = UIConfig.LIGHT else uiConfig.colorUI = UIConfig.DARK
    }

    override fun convert(holder: BaseViewHolder, item: MutableList<Course>) {
        Log.d("CourseItem:", item.toString())
        val dataConfig = DataConfig()
        dataConfig.courseList = DataMapsUtil.dataMappingCourseToBCourse(item)
        dataConfig.timeTable = DataMapsUtil.dataMappingTimeTableToBTimeTable(timetable)
        dataConfig.isCurrentWeek = currentWeek==holder.layoutPosition + 1
        dataConfig.currentMonth = TimeUtil.getWeekInfoString(termStartDate, holder.layoutPosition + 1)
        for (index in 0 until 7){
            dataConfig.weekDay[index] = TimeUtil.getTodayInfoString(termStartDate, holder.layoutPosition + 1, index + 1)
        }
        holder.getView<CourseTableView>(R.id.courseTableView).courseTableView.update(uiConfig, dataConfig)
        holder.getView<CourseTableView>(R.id.courseTableView).setClickCourseItemListener { view: View, list: List<BCourse>, itemPosition: Int, isThisWeek: Boolean ->
            val course: Course = item[itemPosition]
            Log.d("点击课程格子事件", "itemPosition:${itemPosition}, 课程名为:${course.courseName}")
            val bundle = Bundle()
            bundle.putInt(COURSE_UID, course.uid)
            CourseInfoPanel().courseInfo(
                course = course,
                listener = { v ->
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_scheduleFragment_to_addCourseFragment, bundle)
                })
                .show(fragment.parentFragmentManager)
        }
        holder.getView<CourseTableView>(R.id.courseTableView).setLongClickCourseItemListener{ view: View, list: List<BCourse>, itemPosition: Int, isThisWeek: Boolean ->

        }
    }
}