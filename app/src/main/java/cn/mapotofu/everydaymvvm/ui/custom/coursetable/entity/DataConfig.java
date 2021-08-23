package cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity;

import static cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil.compareDate;
import static cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil.getWeekDayByString;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil;

/**
 * Created by Surine on 2019/2/26.
 * 课程表数据集配置
 */

public class DataConfig {
    private List<BCourse> courseList;
    private List<BTimeTable> timeTable;
    private String currentMonth;
    public String[] weekDay = new String[7];
    private Boolean isCurrentWeek;

    public List<BCourse> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<BCourse> courseList) {
        this.courseList = courseList;
    }

    public List<BTimeTable> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(List<BTimeTable> timeTable) {
        this.timeTable = timeTable;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String[] getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String[] weekDay) {
        this.weekDay = weekDay;
    }

    public Boolean getIsCurrentWeek() {
        return isCurrentWeek;
    }

    public void setIsCurrentWeek(Boolean isCurrentWeek) {
        this.isCurrentWeek = isCurrentWeek;
    }
}
