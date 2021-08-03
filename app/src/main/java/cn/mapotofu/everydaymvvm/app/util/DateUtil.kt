package cn.mapotofu.everydaymvvm.app.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.util
 * @author milk
 * @date 2021/7/31
 */
object DateUtil {
    /**
     * 获取当前日期数组
     */
    fun nowDate(): Array<Int> {
        val calendar = GregorianCalendar()
        calendar.firstDayOfWeek = Calendar.MONDAY
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayOfWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
        return arrayOf(month, dayOfMonth, dayOfWeek)
    }

    /**
     * 获取星期的汉语
     */
    fun getWeekInChi(day: Int): String? {
        if (day < 1 && day > 7) {
            return ""
        }
        val weeks = arrayOf("一", "二", "三", "四", "五", "六", "日")
        return weeks[day - 1]
    }

}