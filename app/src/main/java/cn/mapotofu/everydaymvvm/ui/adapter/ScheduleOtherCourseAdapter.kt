package cn.mapotofu.everydaymvvm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.data.model.entity.AboutItem
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_other_course_info.view.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.adapter
 * @author milk
 * @date 2021/9/23
 */
class ScheduleOtherCourseAdapter: BaseQuickAdapter<Course, BaseViewHolder>(R.layout.item_other_course_info) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: Course) {
        holder.itemView.textview_title.text = "课程：${item.courseName}"
        holder.itemView.textview_teacher.text = "教师：${item.teachersName}"
        holder.itemView.textview_weeks.text = "周次：${item.weeksText}"
        holder.itemView.textview_credit.text = "学分：${item.credit}"
    }
}