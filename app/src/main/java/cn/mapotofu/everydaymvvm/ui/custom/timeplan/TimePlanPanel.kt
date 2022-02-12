package cn.mapotofu.everydaymvvm.ui.custom.timeplan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import me.hgj.jetpackmvvm.ext.view.notNull

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.timeplan
 * @author milk
 * @date 2021/8/9
 */

typealias TimePlanListener = ((Course) -> Unit)?

class TimePlanPanel : BottomSheetDialogFragment() {
    private var viewCorners: Float = 4F
    private lateinit var course: Course
    private var listener: TimePlanListener = null
    private var totalWeek: Int = 0
    private var totalSession: Int = 0
    private var isNewCourse: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) dismiss()
        val view = inflater.inflate(R.layout.view_btm_base_dialog_ui, container, false)
        view.findViewById<FrameLayout>(R.id.view_base_btm_dialog_ui).apply {
            addView(
                getWeekView(
                    context,
                    this,
                    course,
                    totalWeek,
                    totalSession,
                    isNewCourse,
                    listener
                )
            )
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    @SuppressLint("SetTextI18n")
    private fun getWeekView(
        context: Context,
        frameLayout: FrameLayout,
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        listener: TimePlanListener
    ): View? {
        val weekView = UisUtil.inflate(context, R.layout.view_btm_week_choose)
        val chipGroup: ChipGroup = weekView.findViewById(R.id.chipGroup)
        chipGroup.removeAllViews()
        for (i in 0 until totalWeek) {
            Chip(context).apply {
                text = "${i + 1}"
                isCheckable = true
                isCheckedIconVisible = true
                if (course.weeks.isNotEmpty() && course.weeks.contains(i + 1)) {
                    isChecked = true
                }
                chipGroup.addView(this)
            }
        }
        weekView.findViewById<Button>(R.id.single).setOnClickListener {
            chipGroup.clearCheck()
            for (i in 0 until totalWeek) {
                if (i % 2 == 0) (chipGroup[i] as Chip).isChecked = true
            }
        }
        weekView.findViewById<Button>(R.id.doubleWeek).setOnClickListener {
            chipGroup.clearCheck()
            (0 until totalWeek).forEach {
                if (it % 2 == 1) (chipGroup[it] as Chip).isChecked = true
            }
        }
        weekView.findViewById<Button>(R.id.allReverse).setOnClickListener {
            (0 until totalWeek).forEach {
                (chipGroup[it] as Chip).isChecked = !(chipGroup[it] as Chip).isChecked
            }
        }
        weekView.findViewById<Button>(R.id.button).setOnClickListener {
            var weeks: MutableList<Int> = mutableListOf()
            (0 until totalWeek).forEach {
                if ((chipGroup[it] as Chip).isChecked) weeks.add(it + 1)
            }
            course.weeks = weeks
            frameLayout.removeAllViews()
            frameLayout.addView(
                getTimeView(
                    context,
                    frameLayout,
                    course,
                    totalWeek,
                    totalSession,
                    isNewCourse,
                    listener
                )
            )
        }
        return weekView
    }

    private fun getTimeView(
        context: Context,
        frameLayout: FrameLayout,
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        listener: TimePlanListener
    ): View? {
        val timeView = UisUtil.inflate(context, R.layout.view_btm_time_choose)
        val weeks = arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
        val sessions = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val dayPicker = timeView.findViewById<NumberPicker>(R.id.day_picker).apply {
            displayedValues = weeks
            minValue = 0
            maxValue = weeks.size - 1
            if (course.day!=0) {
                value = course.day - 1
            }
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        }
        val sessionStartPicker =
            timeView.findViewById<NumberPicker>(R.id.session_start_picker).apply {
                displayedValues = sessions
                minValue = 0
                maxValue = sessions.size - 1
                descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
                if (course.start!=0) {
                    value = course.start - 1
                }
            }
        val sessionEndPicker = timeView.findViewById<NumberPicker>(R.id.session_end_picker).apply {
            displayedValues = sessions
            minValue = 0
            maxValue = sessions.size - 1
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            if (course.length!=0) {
                value = course.start + course.length - 1 - 1
            }
        }
        timeView.findViewById<Button>(R.id.button).setOnClickListener {
            course.day = dayPicker.value + 1
            val r1 = sessions[sessionStartPicker.value]
            val r2 = sessions[sessionEndPicker.value]
            if (r1.toInt() <= r2.toInt()) {
                course.start = r1.toInt()
                course.length = r2.toInt() - r1.toInt() + 1
                listener?.invoke(course)
                dialog?.dismiss()
            } else {
                showMessage("[时间段1]不能大于[时间段2]。\n\nso,要不重新检查一下你的设置？")
            }
        }
        return timeView
    }

    fun timePlan(
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        listener: TimePlanListener
    ): TimePlanPanel {
        this.course = course
        this.totalWeek = totalWeek
        this.totalSession = totalSession
        this.isNewCourse = isNewCourse
        this.listener = listener
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, "CourseInfo")
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}