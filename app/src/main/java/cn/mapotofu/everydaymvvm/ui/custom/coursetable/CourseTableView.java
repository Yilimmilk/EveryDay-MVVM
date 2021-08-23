package cn.mapotofu.everydaymvvm.ui.custom.coursetable;

import static cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil.getWeekDay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.mapotofu.everydaymvvm.R;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BCourse;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.BTimeTable;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.DataConfig;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.entity.UIConfig;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.interfaces.OnClickCourseItemListener;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.DrawablesUtil;
import cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils.TimeUtil;

/**
 * @author milk
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.coursetable
 * @date 2021/8/23
 */
public class CourseTableView extends LinearLayout {
    private static final float MAIN_TEXT_SIZE = 12;
    int w = MeasureSpec.makeMeasureSpec(0,
            MeasureSpec.UNSPECIFIED);
    int h = MeasureSpec.makeMeasureSpec(0,
            MeasureSpec.UNSPECIFIED);

    /*UI 配置器*/
    private UIConfig mUiConfig = new UIConfig();
    /*数据集 配置器*/
    private DataConfig mDataConfig = new DataConfig();
    /*点击监听器*/
    private OnClickCourseItemListener mClickCourseItemListener;
    private OnClickCourseItemListener mLongClickCourseItemListener;
    /*周view视图*/
    private LinearLayout mWeekView;
    /*节次view视图*/
    private LinearLayout mSectionView;
    /*Week Panel 视图*/
    private RelativeLayout weekPanel7;
    private RelativeLayout weekPanel1;
    private RelativeLayout weekPanel2;
    private RelativeLayout weekPanel3;
    private RelativeLayout weekPanel4;
    private RelativeLayout weekPanel5;
    private RelativeLayout weekPanel6;

    private List<RelativeLayout> layoutList = new ArrayList<>();
    /*上下文*/
    private Context mContext;
    /*侧边栏是否加载*/
    private boolean isSectionViewInit = false;

    public CourseTableView(Context context) {
        super(context);
        initView(context);
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CourseTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CourseTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 更新UI及数据集
     *
     * @param uiConfig   UI配置
     * @param dataConfig
     * @return this
     */
    public void update(UIConfig uiConfig, DataConfig dataConfig) {
        mUiConfig = uiConfig;
        mDataConfig = dataConfig;

        updateWeekView();  //加载周视图
        updateSectionView(); //加载侧边栏
        updateData();  //加载数据
    }

    /**
     * 更新数据集
     *
     * @param dataConfig 数据集
     * @return this
     */
    public void update(DataConfig dataConfig) {
        mDataConfig = dataConfig;
        //数据加载
        //如果侧边栏未加载则进行加载
        if (!isSectionViewInit) {
            updateSectionView();
        }

        //顶部栏要跟着更新时间数据
        updateWeekView();
        updateData();
    }

    /**
     * 更新UI
     *
     * @param uiConfig UI配置
     * @return this
     */
    public void update(UIConfig uiConfig) {
        update(uiConfig, mDataConfig);
    }

    /**
     * 获取UI配置
     *
     * @return UIConfig
     */
    public UIConfig getUiConfig() {
        return mUiConfig;
    }

    /**
     * 获取课程数据配置
     *
     * @return DataConfig
     */
    public DataConfig getDataConfig() {
        return mDataConfig;
    }

    /*初始化*/
    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.main_course_view, this);
        mWeekView = findViewById(R.id.weekView);
        mSectionView = findViewById(R.id.sectionView);
        weekPanel7 = findViewById(R.id.weekPanel_1);
        weekPanel1 = findViewById(R.id.weekPanel_2);
        weekPanel2 = findViewById(R.id.weekPanel_3);
        weekPanel3 = findViewById(R.id.weekPanel_4);
        weekPanel4 = findViewById(R.id.weekPanel_5);
        weekPanel5 = findViewById(R.id.weekPanel_6);
        weekPanel6 = findViewById(R.id.weekPanel_7);
        if (mWeekView == null || mSectionView == null
                || weekPanel1 == null || weekPanel2 == null
                || weekPanel3 == null || weekPanel4 == null
                || weekPanel5 == null || weekPanel6 == null
                || weekPanel7 == null
        ) {
            throw new NullPointerException("childView is null");
        }
        layoutList.add(weekPanel7);
        layoutList.add(weekPanel1);
        layoutList.add(weekPanel2);
        layoutList.add(weekPanel3);
        layoutList.add(weekPanel4);
        layoutList.add(weekPanel5);
        layoutList.add(weekPanel6);
    }

    /*初始化课节次布局*/
    @SuppressLint("SetTextI18n")
    private void initSectionView(int maxSection) {

        LayoutParams lpx = new LayoutParams(mUiConfig.getSectionViewWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        mSectionView.setLayoutParams(lpx);
        mSectionView.removeViews(0, mSectionView.getChildCount());

        List<BTimeTable> bTimeTable = mDataConfig.getTimeTable();

        //计算当前时间
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(date);
        long curTimeNum = TimeUtil.transTime(time);

        for (int i = 1; i <= maxSection; i++) {
            TextView tvSection = new TextView(mContext);
            tvSection.setTextColor(mUiConfig.getColorUI());
            //侧边栏单项高度 = 小课课高度 + padding
            int height = mUiConfig.getSectionHeight() + mUiConfig.getItemTopMargin();
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
            lp.gravity = Gravity.CENTER;
            tvSection.setGravity(Gravity.CENTER);
            tvSection.setTextSize(12);

            int numberLen = String.valueOf(i).length();
            SpannableString ss;
            if (bTimeTable != null && bTimeTable.size() > 0 && mUiConfig.isShowTimeTable()) {
                try {
                    String startTime = bTimeTable.get(i - 1).startTime;
                    String endTime = bTimeTable.get(i - 1).endTime;
                    ss = new SpannableString(i + "\n" + startTime + "\n" + endTime);
                    if (curTimeNum > TimeUtil.transTime(startTime) && curTimeNum < TimeUtil.transTime(endTime)) {
                        tvSection.setTextColor(mUiConfig.getChooseWeekColor());
                    }
                } catch (Exception e) {
                    ss = new SpannableString(String.valueOf(i));
                }
            } else {
                ss = new SpannableString(String.valueOf(i));
            }
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.8f);
            ss.setSpan(relativeSizeSpan, numberLen, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSection.setText(ss);
            tvSection.setLayoutParams(lp);
            mSectionView.addView(tvSection);
        }
    }

    /*初始化周视图*/
    @SuppressLint("SetTextI18n")
    private void initWeekView(int maxClassDay) {
        //清空
        mWeekView.removeViews(0, mWeekView.getChildCount());
        for (int i = 0; i <= maxClassDay; i++) {
            TextView tvWeekName = new TextView(mContext);

            if (i == 0) {
                LayoutParams lp0 = new LayoutParams(mUiConfig.getSectionViewWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                lp0.gravity = Gravity.CENTER;
                tvWeekName.setText(mDataConfig.getCurrentMonth() + "\n月");
                tvWeekName.setTextColor(mUiConfig.getColorUI());
                tvWeekName.setTextSize(MAIN_TEXT_SIZE);
                tvWeekName.setGravity(Gravity.CENTER);
                tvWeekName.setLayoutParams(lp0);

                //显示月份
                tvWeekName.setVisibility(mUiConfig.isShowCurrentMonth() ? VISIBLE : INVISIBLE);
            } else {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.weight = 1f;
                String day = mDataConfig.getWeekDay()[i - 1];
                boolean isCurrentWeek = (i == getWeekDay() && mDataConfig.getIsCurrentWeek());
                tvWeekName.setText(mUiConfig.getWeekInfoStyle()[i - 1] + "\n" + day);
                int color = (isCurrentWeek) ? mUiConfig.getChooseWeekColor() : mUiConfig.getColorUI();
                tvWeekName.setTextColor(color);

                if (isCurrentWeek) {
                    tvWeekName.setPadding(0, 5, 0, 5);
                    tvWeekName.setBackground(DrawablesUtil.getDrawable(getResources().getColor(R.color.choose_tag), 20, 0, 0));
                    tvWeekName.setTextColor(Color.WHITE);
                }

                tvWeekName.setTextSize(MAIN_TEXT_SIZE);
                tvWeekName.getPaint().setFakeBoldText(i == getWeekDay());
                tvWeekName.setGravity(Gravity.CENTER);
                tvWeekName.setLayoutParams(lp);
            }
            mWeekView.addView(tvWeekName);
        }

    }

    /*加载课程项目信息  */
    @SuppressLint("SetTextI18n")
    private void initCourseItemView() {

        //清除布局
        for (int j = 0; j < 7; j++) {
            layoutList.get(j).removeAllViews();
        }

        List<BCourse> bCourseList = mDataConfig.getCourseList();

        if (bCourseList.size() <= 0) {
            return;
        }

        loadData(bCourseList);

        //课表显示与隐藏
        if (mUiConfig.getMaxClassDay() == 5) {
            layoutList.get(5).setVisibility(GONE);
            layoutList.get(6).setVisibility(GONE);
        } else {
            layoutList.get(5).setVisibility(VISIBLE);
            layoutList.get(6).setVisibility(VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadData(List<BCourse> bCourseList) {
        int itemPosition;

        //height flag position,day flag position
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < bCourseList.size(); i++) {
            itemPosition = i;
            BCourse bCourse = bCourseList.get(itemPosition);
            if (bCourse == null || bCourse.getSectionStart() >= mUiConfig.getMaxSection() + 1) {
                continue;
            }

            //check the position
            if (bCourse.getSectionStart() + bCourse.getSectionContinue() > mUiConfig.getMaxSection() + 1) {
                bCourse.setSectionContinue(mUiConfig.getMaxSection() + 1 - bCourse.getSectionStart());
            }

            //is this week,If you want to set the first day of the week as Monday，please set "thisDay = bCourse.getDay() - 1;"
            int thisDay;
            if (bCourse.getDay() == 7) thisDay = 0;
            else thisDay = bCourse.getDay();
            RelativeLayout curDayLayout = layoutList.get(thisDay);

            FrameLayout frameLayout = new FrameLayout(getContext());
            TextView tv = new TextView(mContext);
            //course height = class continue * little section height + gap * padding
            int bCourseHeight = bCourse.getSectionContinue() * mUiConfig.getSectionHeight()
                    + (bCourse.getSectionContinue() - 1) * mUiConfig.getItemTopMargin();
            RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    bCourseHeight);
            flp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams tlp = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            //class gap
            int topMargin = (bCourse.getSectionStart() - 1) * mUiConfig.getSectionHeight()
                    + bCourse.getSectionStart() * mUiConfig.getItemTopMargin();
            flp.setMargins(0, topMargin, 0, 0);
            //is cur week
            if (bCourse.getColor() != null) {
                frameLayout.setBackground(DrawablesUtil.getDrawable(Color.parseColor(bCourse.getColor()), 20, 3, Color.WHITE));
            } else {
                frameLayout.setBackground(DrawablesUtil.getDrawable(Color.TRANSPARENT, 20, 3, Color.WHITE));
            }
            tv.setTextColor(Color.WHITE);
            tv.setText(bCourse.getSimpleName() + "@" + bCourse.getPosition());
            tv.setLayoutParams(tlp);
            tv.setPadding(10, 10, 10, 10);
            tv.getPaint().setFakeBoldText(true);
            tv.setGravity(Gravity.START);
            tv.setTextSize(mUiConfig.getItemTextSize());

            frameLayout.setLayoutParams(flp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            curDayLayout.setPadding(mUiConfig.getItemSideMargin(), 0, mUiConfig.getItemSideMargin(), 0);
            curDayLayout.addView(frameLayout);
            int sectionStart = bCourse.getSectionStart();
            int sectionContinue = bCourse.getSectionContinue();

            while (sectionContinue > 0) {
                map.put((sectionStart + sectionContinue - 1) + "/" + thisDay, 0);
                sectionContinue--;
            }

            final int finalItemPostion = itemPosition;
            final boolean tempIsCurWeek = mDataConfig.getIsCurrentWeek();
            //单击监听
            frameLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickCourseItemListener == null) {
                        return;
                    }
                    mClickCourseItemListener.onClickItem(view, mDataConfig.getCourseList(), finalItemPostion, tempIsCurWeek);
                }
            });
            frameLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickCourseItemListener != null) {
                        mLongClickCourseItemListener.onClickItem(v, mDataConfig.getCourseList(), finalItemPostion, tempIsCurWeek);
                    }
                    return true;
                }
            });
        }

        //not show non cur week
        if (!mUiConfig.isShowNotCurWeekCourse()) {
            return;
        }

        for (int i = 0; i < bCourseList.size(); i++) {
            itemPosition = i;
            boolean isCurWeek = mDataConfig.getIsCurrentWeek();
            BCourse bCourse = bCourseList.get(i);
            if (bCourse == null) {
                continue;
            }
            //check the position
            if (bCourse.getSectionStart() + bCourse.getSectionContinue() > mUiConfig.getMaxSection() + 1) {
                bCourse.setSectionContinue(mUiConfig.getMaxSection() + 1 - bCourse.getSectionStart());
            }
            //is this week,If you want to set the first day of the week as Monday，please set "thisDay = bCourse.getDay() - 1;"
            int thisDay;
            if (bCourse.getDay() == 7) thisDay = 0;
            else thisDay = bCourse.getDay();
            RelativeLayout curDayLayout = layoutList.get(thisDay);
            //first to load curweek class
            if (isCurWeek) {
                continue;
            }
            //if this position has a course,skip it
            if (map.containsKey(bCourse.getSectionStart() + "/" + thisDay)) {
                continue;
            }

            RelativeLayout frameLayout = new RelativeLayout(getContext());
            TextView tv = new TextView(mContext);
            //course height = class continue * little section height + gap * padding
            int bCourseHeight = bCourse.getSectionContinue() * mUiConfig.getSectionHeight()
                    + (bCourse.getSectionContinue() - 1) * mUiConfig.getItemTopMargin();
            RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    bCourseHeight);
            flp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            LayoutParams tlp = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            //class gap
            int topMargin = (bCourse.getSectionStart() - 1) * mUiConfig.getSectionHeight()
                    + bCourse.getSectionStart() * mUiConfig.getItemTopMargin();
            flp.setMargins(0, topMargin, 0, 0);
            //is non cur week
            frameLayout.setBackground(DrawablesUtil.getDrawable(mUiConfig.getItemNotCurWeekCourseColor(), 20, 3, mUiConfig.getColorUI()));
            tv.setTextColor(Color.BLACK);
            tv.setText(bCourse.getSimpleName() + "@" + bCourse.getPosition() + "[非本周]");
            tv.setLayoutParams(tlp);
            tv.setPadding(10, 10, 10, 10);
            tv.setGravity(Gravity.CENTER);
            tv.getPaint().setFakeBoldText(true);
            tv.setTextSize(mUiConfig.getItemTextSize());
            frameLayout.setLayoutParams(flp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            curDayLayout.setPadding(mUiConfig.getItemSideMargin(), 0, mUiConfig.getItemSideMargin(), 0);
            curDayLayout.addView(frameLayout);
            final int finalItemPostion = itemPosition;
            //单击监听
            frameLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickCourseItemListener == null) {
                        return;
                    }
                    mClickCourseItemListener.onClickItem(view, mDataConfig.getCourseList(), finalItemPostion, isCurWeek);
                }
            });

            frameLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

    }

    //加载侧边栏
    private void updateSectionView() {
        isSectionViewInit = false;
        //初始化上课节数视图
        initSectionView(mUiConfig.getMaxSection());
        isSectionViewInit = true;
    }

    //加载顶部栏
    private void updateWeekView() {
        //初始化课表上课日
        initWeekView(mUiConfig.getMaxClassDay());
    }

    /*更新数据*/
    private void updateData() {
        initCourseItemView();
    }

    /**
     * 配置监听器
     *
     * @param mClickCourseItemListener 监听器
     */
    public void setClickCourseItemListener(OnClickCourseItemListener mClickCourseItemListener) {
        this.mClickCourseItemListener = mClickCourseItemListener;
    }

    public void setLongClickCourseItemListener(OnClickCourseItemListener mLongClickCourseItemListener) {
        this.mLongClickCourseItemListener = mLongClickCourseItemListener;
    }
}
