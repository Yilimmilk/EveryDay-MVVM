package cn.mapotofu.everydaymvvm.ui.custom.courseinfo

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.util.DateUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.app.util.DrawablesUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.courseinfo
 * @author milk
 * @date 2021/8/3
 */

typealias CourseInfoListener = ((View) -> Unit)?

const val COURSE_UID = "course_uid"

class CourseInfoPanel : BottomSheetDialogFragment() {
    private var viewCorners: Float = 4F
    private lateinit var course: Course
    private var listener: CourseInfoListener = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) dismiss()
        return inflater.inflate(R.layout.view_btm_course_info, container, false)
    }

    @SuppressLint("SetTextI18n", "Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        coursePosition.text = if (TextUtils.isEmpty((course.location).also {
                positionText = it
            })) "无位置" else positionText
        courseClassDay.text = "周${DateUtil.getWeekInChi(course.day)}"
        courseSession.text = "${course.start}-${course.start + course.length - 1}节"
        courseTeacher.text =
            if (TextUtils.isEmpty(course.teachersName)) "未知" else course.teachersName
        courseHours.text =
            if (TextUtils.isEmpty(course.hoursComposition)) "未知" else course.hoursComposition
        courseScore.text =
            if (TextUtils.isEmpty(course.credit)) "未知" else course.credit + "学分"
        courseWeekInfo.text = "${course.weeks}周"
        courseSession.background = DrawablesUtil.getDrawable(Color.parseColor(course.color), 15, 0, 0)
        courseClassDay.background = DrawablesUtil.getDrawable(Color.BLUE, 15, 0, 0)
        val bundle = Bundle()
        bundle.putInt(COURSE_UID, course.uid)
        edit.setOnClickListener {
            listener?.invoke(it)
            dialog?.dismiss()
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog? ?: return
                val behavior = dialog.behavior
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                behavior.setBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        })

        val gradientDrawable = GradientDrawable().apply {
            cornerRadii =
                floatArrayOf(
                    viewCorners,
                    viewCorners,
                    viewCorners,
                    viewCorners,
                    0f,
                    0f,
                    0f,
                    0f
                )
        }
        view.background = gradientDrawable
    }

    fun courseInfo(
        course: Course,
        listener: CourseInfoListener
    ): CourseInfoPanel {
        this.course = course
        this.listener = listener
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, "CourseInfo")
    }
}

