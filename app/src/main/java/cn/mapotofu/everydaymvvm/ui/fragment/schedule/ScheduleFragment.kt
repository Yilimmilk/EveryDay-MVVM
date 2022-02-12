package cn.mapotofu.everydaymvvm.ui.fragment.schedule

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.mapotofu.everydaymvvm.BR
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.*
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.model.entity.TimeTable
import cn.mapotofu.everydaymvvm.databinding.FragmentScheduleBinding
import cn.mapotofu.everydaymvvm.ui.adapter.ScheduleViewPagerAdapter
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestScheduleViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.ScheduleViewModel
import kotlinx.android.synthetic.main.fragment_schedule.*
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import cn.mapotofu.everydaymvvm.ui.activity.MainActivity
import com.drake.brv.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.widget.LinearLayout
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.ui.custom.helper.Pager2_ZoomOutSlideTransformer
import com.google.android.material.slider.Slider
import me.hgj.jetpackmvvm.base.appContext


/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.schedule
 * @author milk
 * @date 2021/7/31
 */
class ScheduleFragment : BaseFragment<ScheduleViewModel, FragmentScheduleBinding>() {
    private val requestScheduleViewModel: RequestScheduleViewModel by viewModels()
    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter
    private lateinit var popupSelectWeek: PopupWindowUtil

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
        setHasOptionsMenu(true)
        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.model

        //默认菜单和fab图标
        requireActivity().top_app_bar.title = mViewModel.nowDate
        mViewModel.teachingWeekText.observe(this) {
            requireActivity().top_app_bar.subtitle = it
        }
        requireActivity().fab.setImageResource(R.drawable.ic_practice_24dp)
        requireActivity().bottom_app_bar.setFabAlignmentModeAndReplaceMenu(
            BottomAppBar.FAB_ALIGNMENT_MODE_CENTER,
            R.menu.menu_schedule
        )

        popupSelectWeek = PopupWindowUtil.Builder(requireContext())
            .setContentView(R.layout.view_select_week)
            .setWidth(UisUtil.screenWidth / 2)
            .setHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
            .setElevation(8)
            .setAnimationStyle(R.anim.pop_enter_anim)
            .build()

        //获取当前UI模式
        isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        //初始化数据
        courseList = mViewModel.getCourseFromRoom()
        timeTableList = mViewModel.getTimeTableFromRoom(
            appContext.getPrefer().getString(Const.KEY_CAMPUS, "jiayu")!!
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
                if (it.courseName.first() != '#' && it.weeks.contains(indexWeek)) {
                    currentWeekCourseList.add(it)
                }
            }
            //将当前周次列表加入最终结果
            afterSplitWeekCourseList.add(currentWeekCourseList)
        }
        courseList.forEach { it ->
            if (it.courseId.first() == '#') {
                otherCourseList.add(it)
            }
        }

        requireActivity().fab.setOnClickListener {
            if (otherCourseList.isEmpty()) {
                (activity as MainActivity).makeSnackBar("您没有实践课或其他课程~")
            }else {
                val view = View.inflate(requireContext(),R.layout.view_other_course,null)
                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerOtherCourse)
                recyclerView.staggered(2).setup {
                    addType<ScheduleViewModel.OtherCourseModel>(R.layout.item_other_course_info)
                }.models = DataMapsUtil.dataMappingOtherCourseListToOtherCourseModel(otherCourseList)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.other_course))
                    .setView(view)
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                    }
                    .show()
            }
        }
        scheduleViewPagerAdapter.setNewInstance(afterSplitWeekCourseList)

        viewpagerSchedule.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            override fun onPageSelected(position: Int) {
                mViewModel.teachingWeekSelected.postValue(position + 1)
                scheduleViewPagerAdapter.notifyItemChanged(position)
            }
        })
    }

    override fun createObserver() {
        requestScheduleViewModel.semesterResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                val listPopupSemesterSelector =
                    PopupMenu(requireContext(), requireActivity().bottom_app_bar)
                it.semesterList.forEach { value ->
                    listPopupSemesterSelector.menu.add(
                        Menu.NONE,
                        value.id,
                        Menu.NONE,
                        value.text
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
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage(
                    "${resources.getString(R.string.get_response_fail_try_again)}\n\n${String.format(errorLog, it.errCode, it.errorMsg, it.errorLog)}",
                    resources.getString(R.string.aha_get_error)
                )
            })
        }
    }

    private fun showSemesterMenu(stuId: String) {
        requestScheduleViewModel.semesterReq(stuId)
        showLoading("正在请求学期表，等一下咯...")
    }

    override fun onResume() {
        super.onResume()
        //回到当前周，有个小bug可以临时通过这个解决一下
        viewpagerSchedule.setCurrentItem(mViewModel.teachingWeekSelected.value!! - 1, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_schedule, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.select_week -> {
                val slider = popupSelectWeek.getItemView(R.id.slider_select_week) as Slider
                slider.valueFrom = 1F
                slider.valueTo = Constants.MAX_WEEK.toFloat()
                slider.value = mViewModel.teachingWeekSelected.value!!.toFloat()
                slider.addOnChangeListener { slider, value, fromUser ->
                    mViewModel.teachingWeekSelected.postValue(value.toInt())
                    viewpagerSchedule.setCurrentItem(value.toInt() - 1, true)
                }
                val xOff = 0
                val yOff = - requireActivity().bottom_app_bar.height - requireActivity().fab.height
                popupSelectWeek.showAsDropDown(requireActivity().fab, xOff, yOff)
            }
            R.id.more_sections -> {
                requireActivity().bottom_app_bar.setFabAlignmentModeAndReplaceMenu(
                    BottomAppBar.FAB_ALIGNMENT_MODE_END,
                    R.menu.menu_schedule_alternate
                )
            }
            R.id.refresh_course -> {
                showMessage(
                    "确定要重新从服务器获取课程表吗？\n\n\n仅能刷新当前学期课程表:\n${mViewModel.currentScheduleYear}学年第${mViewModel.currentScheduleTerm}学期",
                    "提醒",
                    "确定",
                    { nav().navigateAction(R.id.action_scheduleFragment_to_loadScheduleFragment) },
                    "取消",
                    {})
            }
            R.id.add_course -> {
                nav().navigateAction(R.id.action_scheduleFragment_to_addCourseFragment)
            }
            R.id.switch_semester -> {
                appViewModel.studentInfo.value?.let { it1 -> showSemesterMenu(it1.studentId) }
            }
            R.id.back -> {
                requireActivity().bottom_app_bar.setFabAlignmentModeAndReplaceMenu(
                    BottomAppBar.FAB_ALIGNMENT_MODE_CENTER,
                    R.menu.menu_schedule
                )
            }
        }
        return true
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}