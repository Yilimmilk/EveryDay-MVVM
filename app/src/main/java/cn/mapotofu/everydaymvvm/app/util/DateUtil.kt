package cn.mapotofu.everydaymvvm.app.util

import android.annotation.SuppressLint
import java.text.ParseException
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
     * 根据字符串获取日期
     *
     * @param dateStr 字符串
     * @param format  格式
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(dateStr: String, format: String): Date? {
        val simpleDateFormat = SimpleDateFormat(format)
        try {
            return simpleDateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
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

    /**
     * 获取日期
     *
     * @param format 格式
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(format: String): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    /**
     * 获取日期
     *
     * @param date   字符串
     * @param format 格式
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateFormat(date: Date, format: String): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    /**
     * 获取某个日期的前后几天
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateBeforeOfAfter(time: String, num: Int): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        lateinit var date: Date
        try {
            date = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        calendar.time = date
        val day = calendar[Calendar.DATE]
        // 后一天为 +1   前一天 为-1
        calendar[Calendar.DATE] = day + num
        return calendar.time
    }

    /**
     * 获取今天星期几的数字
     * 周日0 周一1 周二2  ……
     */
    fun getWeekDay(): Int {
        val cal = Calendar.getInstance()
        return cal[Calendar.DAY_OF_WEEK] - 1
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date1
     * @param date2
     */
    fun getDateDif(date1: String, date2: String): Int {
        val cal = Calendar.getInstance()
        cal.time = getDate(date1, "yyyy-MM-dd")
        val time1 = cal.timeInMillis
        cal.time = getDate(date2, "yyyy-MM-dd")
        val time2 = cal.timeInMillis
        return Math.abs((time1 - time2) / (1000 * 3600 * 24)).toInt()
    }
}