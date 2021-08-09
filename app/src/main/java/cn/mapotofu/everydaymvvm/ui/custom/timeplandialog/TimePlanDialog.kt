package cn.mapotofu.everydaymvvm.ui.custom.timeplandialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.NumberPicker
import androidx.core.view.get
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.ui.fragment.add_course.TimePlanCallBack
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.timeplandialog
 * @author milk
 * @date 2021/8/9
 */
object TimePlanDialog {

    fun showTimePlanDialog(
        context: Context,
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        callBack: TimePlanCallBack
    ) {
        BottomSheetDialog(context, R.style.BottomSheetDialogTheme).apply {
            val bt = this
            dismissWithAnimation = true
            val view = UisUtil.inflate(context, R.layout.view_btm_base_dialog_ui)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            show()
            view.animate().translationY(50F)
            view.findViewById<FrameLayout>(R.id.view_base_btm_dialog_ui).apply {
                addView(
                    getWeekView(
                        context,
                        this,
                        bt,
                        course,
                        totalWeek,
                        totalSession,
                        isNewCourse,
                        callBack
                    )
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeekView(
        context: Context,
        frameLayout: FrameLayout,
        bt: BottomSheetDialog,
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        callBack: TimePlanCallBack
    ): View? {
        val weekView = UisUtil.inflate(context, R.layout.view_btm_week_choose)
        val chipGroup: ChipGroup = weekView.findViewById(R.id.chipGroup)
        chipGroup.removeAllViews()
        for (i in 0 until totalWeek) {
            Chip(context).apply {
                text = "${i + 1}"
                isCheckable = true
                isCheckedIconVisible = true
                if (!isNewCourse && course.weeks.isNotEmpty() && course.weeks.contains(i + 1)) {
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
        weekView.findViewById<Button>(R.id.all).setOnClickListener {
            chipGroup.clearCheck()
            (0 until totalWeek).forEach {
                (chipGroup[it] as Chip).isChecked = true
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
                    bt,
                    course,
                    totalWeek,
                    totalSession,
                    isNewCourse,
                    callBack
                )
            )
        }
        return weekView
    }

    private fun getTimeView(
        context: Context,
        frameLayout: FrameLayout,
        bt: BottomSheetDialog,
        course: Course,
        totalWeek: Int,
        totalSession: Int,
        isNewCourse: Boolean,
        callBack: TimePlanCallBack
    ): View? {
        val timeView = UisUtil.inflate(context, R.layout.view_btm_time_choose)
        val weeks = arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
        val session = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val dayPicker = timeView.findViewById<NumberPicker>(R.id.day_picker).apply {
            displayedValues = weeks
            minValue = 0
            maxValue = weeks.size - 1
            if (!isNewCourse) {
                value = course.day - 1
            }
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        }
        val sessionStartPicker =
            timeView.findViewById<NumberPicker>(R.id.session_start_picker).apply {
                displayedValues = session
                minValue = 0
                maxValue = session.size - 1
                descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
                if (!isNewCourse) {
                    value = course.start - 1
                }
            }
        val sessionEndPicker = timeView.findViewById<NumberPicker>(R.id.session_end_picker).apply {
            displayedValues = session
            minValue = 0
            maxValue = session.size - 1
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            if (!isNewCourse) {
                value = course.length - 1
            }
        }
        timeView.findViewById<Button>(R.id.button).setOnClickListener {
            course.day = dayPicker.value + 1
            val r1 = session[sessionStartPicker.value]
            val r2 = session[sessionEndPicker.value]
            if (r1.toInt() + r2.toInt() - 1 <= totalSession) {
                course.start = r1.toInt()
                course.length = r2.toInt()
                callBack.callBack(course)
                bt.dismiss()
            } else {
                Log.d("设置时间计划错误", "超过最大节次限制(${totalSession}节)")
                ToastUtils.showLong("超过最大节次限制(${totalSession}节)")
            }
        }
        return timeView
    }
}