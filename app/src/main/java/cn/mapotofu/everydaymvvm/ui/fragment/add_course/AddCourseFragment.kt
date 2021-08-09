package cn.mapotofu.everydaymvvm.ui.fragment.add_course

import android.annotation.SuppressLint
import android.graphics.Color.*
import android.os.Bundle
import android.view.View
import android.widget.*
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.hideSoftKeyboard
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.databinding.FragmentAddCourseBinding
import cn.mapotofu.everydaymvvm.ui.custom.colorpicker.ColorPicker
import cn.mapotofu.everydaymvvm.ui.custom.coursedialog.CourseDialog
import cn.mapotofu.everydaymvvm.ui.custom.timeplandialog.TimePlanDialog
import cn.mapotofu.everydaymvvm.viewmodel.state.AddCourseViewModel
import com.blankj.utilcode.util.SnackbarUtils
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.ext.nav
import java.util.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.add_course
 * @author milk
 * @date 2021/8/6
 */
class AddCourseFragment : BaseFragment<AddCourseViewModel, FragmentAddCourseBinding>() {
    private lateinit var course: Course
    private var courseUid: Int = 0
    private var isNewCourse: Boolean = true
    private val totalWeek: Int = Constants.MAX_WEEK
    private val totalSession: Int = Constants.MAX_SESSION
    //回调函数，用于接收TimePlanDialog中设置的值
    private var timePlanCallBack = object : TimePlanCallBack{
        override fun callBack(result: Course){
            this@AddCourseFragment.course = result
            mViewModel.timePlanText.postValue("*周次：${course.weeks}\n*星期几：${course.day}\n*节次：${course.start}-${course.start + course.length - 1}")
        }
    }

    override fun layoutId() = R.layout.fragment_add_course

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        //初始化Course
        if (arguments != null) {
            //如果有附加数据
            isNewCourse = false
            courseUid = requireArguments().getInt(CourseDialog.COURSE_UID, 0)
            course = mViewModel.getCourse(courseUid)
            initCourseUi(course, courseUid)
        } else {
            //如果没有附加数据
            isNewCourse = true
            course = Course().apply {
                courseId = "@${System.currentTimeMillis()}"
                color = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]
            }
        }
        //初始化部分UI
        mViewModel.courseIdText.postValue(course.courseId)
        mViewModel.colorText.postValue(course.color)
        mDatabind.contentBlockColor.setBackgroundColor(parseColor(course.color))
        //按钮监听部分
        mDatabind.buttonColorSelector.setOnClickListener {
            ColorPicker().colorPicker(
                colors = resources.getIntArray(R.array.colors),
                listener = { color ->
                    val hexColorStr = String.format("#%06X", 0xFFFFFF and color)
                    course.color = hexColorStr
                    mViewModel.colorText.postValue(hexColorStr)
                    mDatabind.contentBlockColor.setBackgroundColor(color)
                })
                .show(requireActivity().supportFragmentManager)
        }
        mDatabind.buttonTimePlan.setOnClickListener {
            TimePlanDialog.showTimePlanDialog(requireContext(),course,totalWeek,totalSession,isNewCourse,timePlanCallBack)
        }
        mDatabind.saveCourse.setOnClickListener {
            hideSoftKeyboard(activity)
            //清除课程名错误提示
            mDatabind.textfieldCourseName.error = null
            //获取组件中的值，给Course赋值
            mViewModel.courseNameText.value?.let { course.courseName = it }
            mViewModel.teachersNameText.value?.let { course.teachersName = it }
            mViewModel.campusNameText.value?.let { course.campus = it }
            mViewModel.positionText.value?.let { course.location = it }
            mViewModel.hoursCompositionText.value?.let { course.hoursComposition = it }
            mViewModel.scoreText.value?.let { course.credit = it }
            mViewModel.colorText.value?.let { course.color = it }
            //检查数据
            if (checkDataAndInsert(course)){
                if (isNewCourse)
                    SnackbarUtils.with(requireActivity().rootView).setMessage("添加成功").show()
                else
                    SnackbarUtils.with(requireActivity().rootView).setMessage("修改成功").show()
                nav().navigateUp()
            }
        }
    }

    override fun createObserver() {

    }

    //有附加数据时，将数据自动填充到UI界面中
    @SuppressLint("SetTextI18n", "Range")
    private fun initCourseUi(course: Course, courseUid: Int) {
        mViewModel.courseNameText.postValue(course.courseName)
        mViewModel.teachersNameText.postValue(course.teachersName)
        mViewModel.campusNameText.postValue(course.campus)
        mViewModel.positionText.postValue(course.location)
        mViewModel.hoursCompositionText.postValue(course.hoursComposition)
        mViewModel.scoreText.postValue(course.credit)
        mDatabind.deleteButton.visibility = View.VISIBLE
        mViewModel.colorText.postValue(course.color)
        mDatabind.contentBlockColor.setBackgroundColor(parseColor(course.color))
        mViewModel.timePlanText.postValue("*周次：${course.weeks}\n*星期几：${course.day}\n*节次：${course.start}-${course.start + course.length - 1}")
        mDatabind.deleteButton.setOnClickListener { deleteCourse(course.courseName, courseUid) }
    }

    private fun checkDataAndInsert(course: Course): Boolean{
        when {
            mViewModel.courseNameText.value.isNullOrEmpty() ->
                mDatabind.textfieldCourseName.error = "必须"
            course.weeks.isEmpty() || course.day == 0 || course.start == 0 || course.length == 0 ->
                SnackbarUtils.with(requireActivity().rootView).setMessage("时间计划不能为空").show()
            else -> {
                mViewModel.apply {
                    if (isNewCourse) insertCourse(course) else updateCourse(course)
                }
                return true
            }
        }
        return false
    }

    private fun deleteCourse(courseName: String, courseUid: Int) {
        showMessage(
            "确定要删除[$courseName]课程吗？\n删了就回不来了咯",
            "二次确认",
            "确定",
            {
                mViewModel.deleteCourse(courseUid)
                SnackbarUtils.with(requireActivity().rootView).setMessage("删除[$courseName]成功~")
                    .show()
                nav().navigateUp()
            },
            "取消",
            {}
        )
    }
}

//回调接口
interface TimePlanCallBack{
    fun callBack(result : Course)
}