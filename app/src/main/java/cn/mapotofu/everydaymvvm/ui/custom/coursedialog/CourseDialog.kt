package cn.mapotofu.everydaymvvm.ui.custom.coursedialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.util.DateUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.Drawables
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.coursedialog
 * @author milk
 * @date 2021/8/3
 */
object CourseDialog {
    const val COURSE_ID = "course_id"

    /**
     * 获取一个底部弹窗的基础UI
     *
     * @return
     */
    fun getBaseConfig(context: Context, view: View, block: (view: View, bt: BottomSheetDialog) -> Unit) = BottomSheetDialog(context, R.style.CourseDialogTheme).apply {
        setContentView(view)
        window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dismissWithAnimation = true
        view.animate().translationY(50f)
        block(view, this)
        show()
    }


    /**
     * 显示课详情
     *
     * @param baseFragment       上下文
     * @param course             课程
     * @param alphaForCourseItem
     */
    @SuppressLint("SetTextI18n")
    fun showCourseInfoDialog(fragment: Fragment, course: Course, alphaForCourseItem: Int) {
        val bt = BottomSheetDialog(fragment.requireActivity(), R.style.CourseDialogTheme)
        var view: View
        bt.setContentView(UisUtil.inflate(fragment.requireActivity(), R.layout.view_course_info).also { view = it })
        bt.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bt.dismissWithAnimation = true
        bt.show()
        view.animate().translationY(50f)
        val courseName = view.findViewById<TextView>(R.id.courseName)
        val courseCampus = view.findViewById<TextView>(R.id.courseCampus)
        val coursePosition = view.findViewById<TextView>(R.id.coursePosition)
        val courseClassDay = view.findViewById<TextView>(R.id.courseClassDay)
        val courseSession = view.findViewById<TextView>(R.id.courseSession)
        val courseTeacher = view.findViewById<TextView>(R.id.courseTeacher)
        val courseHours = view.findViewById<TextView>(R.id.courseHours)
        val courseScore = view.findViewById<TextView>(R.id.courseScore)
        val courseWeekInfo = view.findViewById<TextView>(R.id.courseWeeks)
        val edit = view.findViewById<ImageView>(R.id.courseEdit)
        courseName.text = course.courseName
        courseCampus.text = course.campus
        var positionText: String?
        coursePosition.text = if (TextUtils.isEmpty((course.location).also { positionText = it })) "无位置" else positionText
        courseClassDay.text = "周${DateUtil.getWeekInChi(course.day)}"
        courseSession.text = "${course.start}-${course.start+course.length-1}节"
        courseTeacher.text = if (TextUtils.isEmpty(course.teachersName)) "未知" else course.teachersName
        courseHours.text = course.hoursComposition
        courseScore.text = if (TextUtils.isEmpty(course.credit)) "未知" else course.credit + "学分"
        courseWeekInfo.text = "${course.weeks}周"
        if (alphaForCourseItem > 0) {
            courseSession.background = Drawables.getDrawable(Color.parseColor(course.color), 15, 0, 0)
        } else {
            courseSession.background = Drawables.getDrawable(Color.TRANSPARENT, 15, 4, Color.BLACK)
            courseSession.setTextColor(Color.BLACK)
        }
        courseClassDay.background = Drawables.getDrawable(Color.BLUE, 15, 0, 0)
//        val bundle = Bundle()
//        bundle.putString(COURSE_ID, course.id)
//        edit.setOnClickListener {
//            open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, bundle)
//            bt.dismiss()
//        }
    }


}