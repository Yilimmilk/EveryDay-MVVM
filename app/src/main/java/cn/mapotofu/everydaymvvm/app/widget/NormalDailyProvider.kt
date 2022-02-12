package cn.mapotofu.everydaymvvm.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.services.NormalDailyService
import cn.mapotofu.everydaymvvm.app.util.Const
import cn.mapotofu.everydaymvvm.app.util.DateUtil
import cn.mapotofu.everydaymvvm.app.util.WidgetUtil
import cn.mapotofu.everydaymvvm.app.util.getPrefer

class NormalDailyProvider : CoreWidgetProvider() {
    //这里更新UI
    public override fun updateUi(context: Context, appWidgetManager: AppWidgetManager, appId: Int): RemoteViews {
        val remoteViews = WidgetUtil.getRoot(context, R.layout.widget_normal_daily_class)
        //标题配置
        remoteViews.setTextViewText(R.id.widget_day_class_title, if (context.getPrefer().getBoolean(Const.NEXT_DAY_STATUS + appId, false)) "明日课程" else "今日课程")
        remoteViews.setTextViewText(R.id.widget_day_class_subtitle, if (context.getPrefer().getBoolean(Const.NEXT_DAY_STATUS + appId, false)) DateUtil.getDateFormat(DateUtil.getDateBeforeOfAfter(DateUtil.getDate("yyyy-MM-dd"), 1), "MM月dd E") else DateUtil.getDate("MM月dd E"))
        //配置今日明日
        WidgetUtil.setDay(this.javaClass, context, remoteViews, appId, R.id.widget_day_class_next_day, intArrayOf(R.drawable.ic_arrow_right_24dp, R.drawable.ic_arrow_left_24dp))
        WidgetUtil.setDay(this.javaClass, context, remoteViews, appId, R.id.widget_day_class_title)
        WidgetUtil.setDay(this.javaClass, context, remoteViews, appId, R.id.widget_day_class_subtitle)
        //点击打开主界面
        WidgetUtil.toMain(remoteViews, context, R.id.root)
        //配置列表
        WidgetUtil.addList(NormalDailyService::class.java, context, appWidgetManager, remoteViews, R.id.listview, appId)
        return remoteViews
    }
}