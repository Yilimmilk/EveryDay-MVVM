package cn.mapotofu.everydaymvvm.ui.fragment.schedule

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.*
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import cn.mapotofu.everydaymvvm.databinding.FragmentScheduleBinding
import cn.mapotofu.everydaymvvm.ui.adapter.ScheduleOtherCourseAdapter
import cn.mapotofu.everydaymvvm.ui.adapter.ScheduleViewPagerAdapter
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestScheduleViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.ScheduleViewModel
import com.codeboy.pager2_transformers.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.view_other_course.*
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import androidx.recyclerview.widget.DividerItemDecoration





/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.schedule
 * @author milk
 * @date 2021/7/31
 */
class ScheduleFragment : BaseFragment<ScheduleViewModel, FragmentScheduleBinding>() {
    private val requestScheduleViewModel: RequestScheduleViewModel by viewModels()
    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter
    private lateinit var scheduleOtherCourseAdapter: ScheduleOtherCourseAdapter
    private lateinit var popupWindowSelectWeek: PopupWindow
    private lateinit var popupWindowOtherCourse: PopupWindow

    private lateinit var courseList: MutableList<Course>
    private lateinit var timeTableList: MutableList<TimeTable>
    private lateinit var termStartDate: String
    private var currentWeek: Int = 0
    private var isDarkMode: Int = 0

    override fun layoutId(): Int = R.layout.fragment_schedule

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestScheduleViewModel)
        mDatabind.viewmodel = mViewModel

        //获取当前UI模式
        isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        //初始化数据
        courseList = mViewModel.getCourseFromRoom()
        timeTableList = mViewModel.getTimeTableFromRoom(
            App.context.getPrefer().getString(Const.KEY_CAMPUS, "jiayu")!!
        )
        termStartDate = mViewModel.termStartDate!!
        currentWeek = mViewModel.teachingWeekNum

        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(
            this,
            timeTableList,
            termStartDate,
            currentWeek,
            when (isDarkMode) {
                Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        )
        scheduleOtherCourseAdapter = ScheduleOtherCourseAdapter()
        viewpagerSchedule.adapter = scheduleViewPagerAdapter
        viewpagerSchedule.offscreenPageLimit = 1
        viewpagerSchedule.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpagerSchedule.setCurrentItem(currentWeek - 1, true)
        //注意此处，部分动画可能导致不同页面PageItem重叠
        viewpagerSchedule.setPageTransformer(Pager2_ZoomOutSlideTransformer())

        val afterSplitWeekCourseList: MutableList<MutableList<Course>> = mutableListOf()
        val otherCourseList: MutableList<Course> = mutableListOf()
        //外层循环周次
        for (indexWeek in 1..Constants.MAX_WEEK) {
            //当前周次课程list
            val currentWeekCourseList: MutableList<Course> = mutableListOf()
            //内层循环遍历所有课程，寻找包含indexWeek的课程并加入list
            courseList.forEach { it ->
                if (it.courseName.first() != '#' && it.weeks.contains(indexWeek)){
                    currentWeekCourseList.add(it)
                }
            }
            //将当前周次列表加入最终结果
            afterSplitWeekCourseList.add(currentWeekCourseList)
        }
        courseList.forEach { it ->
            if (it.courseId.first() == '#'){
                otherCourseList.add(it)
            }
        }

        if (otherCourseList.isEmpty()) {
            fabOtherCourse.hide()
        } else {
            fabOtherCourse.show()
            fabOtherCourse.setOnClickListener {
                fabOtherCourse.hide()
                val view: View =
                    LayoutInflater.from(App.context).inflate(R.layout.view_other_course, null)
                popupWindowOtherCourse = PopupWindow(
                    view,
                    UisUtil.dip2px(App.context, 200F),
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                //设置外面可触
                popupWindowOtherCourse.isOutsideTouchable = true
                //设置可触
                popupWindowOtherCourse.isFocusable = false
                popupWindowOtherCourse.setBackgroundDrawable(
                    DrawablesUtil.getDrawable(
                        Color.WHITE,
                        30,
                        0,
                        Color.WHITE
                    )
                )
                popupWindowOtherCourse.isTouchable = true
                popupWindowOtherCourse.elevation = 8f
                popupWindowOtherCourse.showAtLocation(activity?.window?.decorView, Gravity.CENTER, 0, 0);

                val recyclerOtherCourse = view.findViewById<RecyclerView>(R.id.recyclerOtherCourse)
                recyclerOtherCourse.adapter = scheduleOtherCourseAdapter
                recyclerOtherCourse.layoutManager = LinearLayoutManager(App.context)
                scheduleOtherCourseAdapter.setNewInstance(otherCourseList)

                popupWindowOtherCourse.setOnDismissListener {
                    fabOtherCourse.show()
                }
            }
        }

        scheduleViewPagerAdapter.setNewInstance(afterSplitWeekCourseList)

        // 抽屉按钮
        drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.openDrawer(GravityCompat.START)
        }

        //添加课程按钮
        addCourseButton.setOnClickListener {
            nav().navigateAction(R.id.action_scheduleFragment_to_addCourseFragment)
        }

        //刷新按钮
        refreshButton.setOnClickListener {
            showMessage(
                "确定要重新从服务器获取课程表吗？\n\n\n仅能刷新最新学期课程表:\n${mViewModel.currentScheduleYear}学年第${mViewModel.currentScheduleTerm}学期",
                "提醒",
                "确定",
                { nav().navigateAction(R.id.action_scheduleFragment_to_loadScheduleFragment) },
                "取消",
                {})

        }

        //选择学期按钮
        semesterButton.setOnClickListener {
            CacheUtil.getStuInfo()?.let { it1 -> showSemesterMenu(it1.studentId) }
        }

        //周次文字点击监听
        textviewCurrentWeek.setOnClickListener {
            val view: View =
                LayoutInflater.from(App.context).inflate(R.layout.view_change_week, null)
            popupWindowSelectWeek = PopupWindow(
                view,
                UisUtil.dip2px(App.context, 200F),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            //设置外面可触
            popupWindowSelectWeek.isOutsideTouchable = true
            //设置可触
            popupWindowSelectWeek.isFocusable = false
            popupWindowSelectWeek.setBackgroundDrawable(
                DrawablesUtil.getDrawable(
                    Color.WHITE,
                    30,
                    0,
                    Color.WHITE
                )
            )
            popupWindowSelectWeek.isTouchable = true
            popupWindowSelectWeek.elevation = 8f
            popupWindowSelectWeek.showAsDropDown(textviewCurrentWeek, 20, 30)

            val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
            val weekTv = view.findViewById<TextView>(R.id.weekText)
            seekBar.max = Constants.MAX_WEEK
            seekBar.progress = mViewModel.teachingWeekSelected.value!!
            weekTv.text = mViewModel.teachingWeekSelected.value!!.toString()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    weekTv.text = (progress + 1).toString()
                    mViewModel.teachingWeekSelected.postValue(progress + 1)
                    viewpagerSchedule.setCurrentItem(progress, true)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        viewpagerSchedule.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            override fun onPageSelected(position: Int) {
                mViewModel.teachingWeekSelected.postValue(position + 1)
                scheduleViewPagerAdapter.notifyItemChanged(position)
            }
        })

    }

    override fun initData() {
        super.initData()

    }

    override fun createObserver() {
        requestScheduleViewModel.semesterResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                val listPopupSemesterSelector =
                    PopupMenu(requireContext(), mDatabind.semesterButton)
                it.semesterList.forEach { value ->
                    listPopupSemesterSelector.menu.add(
                        Menu.NONE,
                        value.id,
                        Menu.NONE,
                        "${value.year}年第${value.term}学期"
                    )
                }
                listPopupSemesterSelector.setOnMenuItemClickListener { item ->
                    val bundle = Bundle()
                    bundle.putString("req-year", it.semesterList[item.itemId].year)
                    bundle.putString("req-term", it.semesterList[item.itemId].term)
                    bundle.putBoolean("use-cache", true)
                    nav().navigateAction(
                        R.id.action_scheduleFragment_to_loadScheduleFragment,
                        bundle
                    )
                    true
                }
                listPopupSemesterSelector.show()
            }, {
                showMessage(
                    "请求失败，你要不再点一次试试？\n状态码:${it.errCode}\n错误消息:${it.errorMsg}\n错误日志:${it.errorLog}",
                    "啊哈出错了"
                )
            })
        })
    }

    private fun showSemesterMenu(stuId: String) {
        requestScheduleViewModel.semesterReq(stuId)
        showLoading("正在请求学期表，等一下咯...")
    }

//    @ColorInt
//    fun getColorFromAttr(
//        @AttrRes attrColor: Int,
//        typedValue: TypedValue = TypedValue(),
//        resolveRefs: Boolean = true
//    ): Int {
//        requireContext().theme.resolveAttribute(attrColor, typedValue, resolveRefs)
//        return typedValue.data
//    }

    override fun onResume() {
        super.onResume()
        //回到当前周，有个小bug可以临时通过这个解决一下
        viewpagerSchedule.setCurrentItem(mViewModel.teachingWeekSelected.value!! - 1, false)
    }
}