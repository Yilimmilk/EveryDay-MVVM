package cn.mapotofu.everydaymvvm.ui.custom.coursetable.view;

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

    /*默认数据*/
    private final int CURRENTWEEK = 1;
    private final int SELECTED_POSITION = 0;
    private final List<BCourse> NORMAL_COURSE_TABLE = new ArrayList<>();
    private final int MAXWEEK = 24;
    private final String NORMAL_CUR_TERM_DATE = "2018-1-1";

    /*当前周*/
    private int currentWeek = CURRENTWEEK;
    /*viewpager位置*/
    private int selectedPosition = SELECTED_POSITION;
    /*课表数据集*/
    private List<BCourse> courseList = NORMAL_COURSE_TABLE;
    /*最大周*/
    private int maxWeek = MAXWEEK;
    private String curTermStartDate = NORMAL_CUR_TERM_DATE;

    private BTimeTable timeTable;

    public String getCurTermStartDate() {
        return curTermStartDate;
    }

    /**
     * 设置当前学期开始时间
     *
     * @param curTermStartDate 当前学期起始日期 ，且必须是周一
     * @return this
     */
    public DataConfig setCurTermStartDate(String curTermStartDate) {
        //日期需要大于2000-1-1
        if (TimeUtil.isVaild(curTermStartDate)) {
            if (compareDate(curTermStartDate, "2000-1-1", TimeUtil.yyyyMMdd) == 1) {
                if (getWeekDayByString(curTermStartDate) == 1) {
                    this.curTermStartDate = curTermStartDate;
                } else {
                    throw new IllegalArgumentException("this date is not sunday");
                }
            }
        } else {
            throw new IllegalArgumentException("please set a vaild time format : yyyy-MM-dd");
        }
        return this;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }


    /**
     * 设置当前周
     *
     * @param currentWeek 当前周
     */
    public DataConfig setCurrentWeek(int currentWeek) {
        //在后面判断周是否合法
        if (currentWeek >= 1) {
            this.currentWeek = currentWeek;
        }
        return this;
    }

    public List<BCourse> getCourseList() {
        return courseList;
    }

    /**
     * 设置当前课表数据
     *
     * @param courseList 所有数据即可，View会自动进行分类。
     */
    public DataConfig setCourseList(List<BCourse> courseList) {
        if (courseList != null) {
            this.courseList = courseList;
        }
        return this;
    }


    public int getMaxWeek() {
        return maxWeek;
    }

    public DataConfig setSelectedPosition(int position) {
        this.selectedPosition = position;
        return this;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }


    /**
     * 设置最大上课周
     *
     * @param maxWeek 最大上课周
     */
    public DataConfig setMaxWeek(int maxWeek) {
        if (maxWeek >= 1 && maxWeek < 50) {
            this.maxWeek = maxWeek;
        }
        return this;
    }

    public BTimeTable getTimeTable() {
        return timeTable;
    }

    public DataConfig setTimeTable(BTimeTable timeTable) {
        this.timeTable = timeTable;
        return this;
    }

    public int curWeek() {
        try {
            return TimeUtil.getRealWeek(curTermStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
