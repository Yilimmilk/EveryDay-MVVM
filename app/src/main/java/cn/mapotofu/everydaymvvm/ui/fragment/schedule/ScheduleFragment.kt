package cn.mapotofu.everydaymvvm.ui.fragment.schedule

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.databinding.FragmentScheduleBinding
import cn.mapotofu.everydaymvvm.ui.adapter.ScheduleViewPagerAdapter
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.Drawables
import cn.mapotofu.everydaymvvm.ui.custom.helper.ZoomOutPageTransformer
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestScheduleViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.ScheduleViewModel
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.fragment_schedule.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.schedule
 * @author milk
 * @date 2021/7/31
 */
class ScheduleFragment : BaseFragment<ScheduleViewModel, FragmentScheduleBinding>() {
    private val requestScheduleViewModel: RequestScheduleViewModel by viewModels()
    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter
    private var selectedSemesterSpinnerPosition: Int = 0
    private lateinit var popupWindow: PopupWindow

    private lateinit var courseList: MutableList<Course>
    private lateinit var bCourseList: MutableList<BCourse>
    private lateinit var bTimeTable: BTimeTable
    private lateinit var termStartDate: String
    private var currentWeek: Int = 0

    override fun layoutId(): Int = R.layout.fragment_schedule

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestScheduleViewModel)
        mDatabind.viewmodel = mViewModel

        //初始化数据
        courseList = mViewModel.getCourseFromRoom()
        bCourseList = DataMapsUtil.dataMappingCourseToBCourse(courseList)
        bTimeTable = DataMapsUtil.dataMappingTimeTableToBTimeTable(mViewModel.getTimeTableFromRoom())
        termStartDate = mViewModel.termStartDate!!
        currentWeek = mViewModel.teachingWeekNum!!

        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(
            courseList,
            bCourseList,
            this,
            bTimeTable,
            termStartDate,
            currentWeek
        )
        mDatabind.viewpagerSchedule.adapter = scheduleViewPagerAdapter
        mDatabind.viewpagerSchedule.offscreenPageLimit = 1
        mDatabind.viewpagerSchedule.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mDatabind.viewpagerSchedule.setCurrentItem(mViewModel.teachingWeekNum!! - 1, true)
        mDatabind.viewpagerSchedule.setPageTransformer(ZoomOutPageTransformer())

        // 抽屉按钮
        mDatabind.drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.openDrawer(GravityCompat.START)
        }

        //刷新按钮
        mDatabind.refreshButton.setOnClickListener {
            CrashReport.testJavaCrash()
            //mViewModel.updateStatus.postValue(true)
            //scheduleViewPagerAdapter.setDataSetUpdateCall(() -> initData(true))
        }

        //周次文字点击监听
        mDatabind.textviewCurrentWeek.setOnClickListener{
            val view: View = LayoutInflater.from(App.context).inflate(R.layout.view_change_week, null)
            popupWindow = PopupWindow(
                view,
                UisUtil.dip2px(App.context, 200F),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            //设置外面可触
            popupWindow.isOutsideTouchable = true
            //设置可触
            popupWindow.isFocusable = false
            popupWindow.setBackgroundDrawable(
                Drawables.getDrawable(
                    Color.WHITE,
                    30,
                    0,
                    Color.WHITE
                )
            )
            popupWindow.isTouchable = true
            popupWindow.elevation = 8f
            popupWindow.showAsDropDown(textviewCurrentWeek, 20, 30)

            val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
            val weekTv = view.findViewById<TextView>(R.id.weekText)
            seekBar.max = Constants.MAX_WEEK
            seekBar.progress = mViewModel.teachingWeekSelected.value!!
            weekTv.text = mViewModel.teachingWeekSelected.value!!.toString()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    weekTv.text = (progress + 1).toString()
                    mViewModel.teachingWeekSelected.postValue(progress + 1)
                    viewpagerSchedule.setCurrentItem(progress, true)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        mDatabind.viewpagerSchedule.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            override fun onPageSelected(position: Int) {
                mViewModel.teachingWeekSelected.postValue(position + 1)
                scheduleViewPagerAdapter.setWeek(position + 1)
                scheduleViewPagerAdapter.notifyItemChanged(position)
            }
        })
    }

    override fun initData() {
        super.initData()

    }

    override fun createObserver() {

    }
}