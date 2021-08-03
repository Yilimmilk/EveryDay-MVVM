package cn.mapotofu.everydaymvvm.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.ui.custom.coursedialog.CourseDialog
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.interfaces.OnClickCourseItemListener
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.view.CourseTableView
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.view.DataConfig
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.view.UIConfig
import me.hgj.jetpackmvvm.base.appContext

/**
 * @description
 * @package cn.mapotofu.everyday.pages.schedule
 * @author milk
 * @date 2021/7/19
 */
class ScheduleViewPagerAdapter(
    //原始Course数据，用于课程详情
    private val courseList: MutableList<Course>,
    //映射后的BCourse数据，用于课表显示
    private val bCourseList: MutableList<BCourse>,
    //fragment对象
    private val fragment: Fragment,
    //侧边时间表
    private val timeTable: BTimeTable,
    //学期起始日期
    private val termStartDate: String,
    //当前周
    week: Int,
) : RecyclerView.Adapter<ViewPagerViewHolder>() {
    private var currentWeek = week
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
        uiConfig.colorUI = UIConfig.DARK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_table, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val dataConfig = DataConfig()
        dataConfig.setCurTermStartDate(termStartDate)
            .setCourseList(bCourseList)
            .setTimeTable(timeTable)
            .setCurrentWeek(currentWeek)
            .setSelectedPosition(position)
        holder.courseTableView.update(uiConfig, dataConfig)
        holder.courseTableView.setmClickCourseItemListener { view: View?, list: List<BCourse?>?, itemPosition: Int, isThisWeek: Boolean ->
            val course: Course = courseList[itemPosition]
            CourseDialog.showCourseInfoDialog(
                fragment,
                course,
                10
            )
        }
        holder.courseTableView.longClickCourseItemListener = OnClickCourseItemListener { view: View, list: List<BCourse>, itemPosition: Int, isThisWeek: Boolean ->

        }
    }

    override fun getItemCount(): Int {
        return bCourseList.size
    }

    fun setWeek(week: Int) {
        currentWeek = week
    }

}

class ViewPagerViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var courseTableView: CourseTableView

    init {
        courseTableView = itemView.findViewById(R.id.courseTableView)
    }
}