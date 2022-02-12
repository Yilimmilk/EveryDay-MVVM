package cn.mapotofu.everydaymvvm.ui.activity.settings

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseListActivity
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.Const
import cn.mapotofu.everydaymvvm.app.util.getPrefer
import cn.mapotofu.everydaymvvm.app.widget.NormalDailyProvider
import cn.mapotofu.everydaymvvm.ui.activity.settings.items.*
import cn.mapotofu.everydaymvvm.ui.adapter.SettingItemAdapter
import cn.mapotofu.everydaymvvm.viewmodel.state.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tencent.bugly.beta.Beta
import me.hgj.jetpackmvvm.base.appContext
import splitties.snackbar.snack


/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.activity.settings
 * @author milk
 * @date 2021/8/11
 */
class SettingsActivity : BaseListActivity<SettingsViewModel>() {

    private val mAdapter = SettingItemAdapter()
    private val campusList by lazy(LazyThreadSafetyMode.NONE) {
        resources.getStringArray(R.array.campus_setting)
    }
    private var campusListIndex = 0

    override fun layoutId(): Int = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campusListIndex = if (getPrefer().getString(Const.KEY_CAMPUS, "jiayu")=="jiayu") 0 else 1

        val items = mutableListOf<BaseSettingItem>()
        onItemsCreated(items)
        mAdapter.data = items
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.itemAnimator?.changeDuration = 250
        mRecyclerView.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.anko_check_box)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (val item = items[position]) {
                is SwitchItem -> onSwitchItemCheckChange(item, view.findViewById<AppCompatCheckBox>(R.id.anko_check_box).isChecked)
            }
        }
        mAdapter.setOnItemClickListener { _, view, position ->
            when (val item = items[position]) {
                is HorizontalItem -> onHorizontalItemClick(item, position)
                is SwitchItem -> view.findViewById<AppCompatCheckBox>(R.id.anko_check_box).performClick()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {

    }

    private fun onItemsCreated(items: MutableList<BaseSettingItem>) {
        items.add(CategoryItem("常规", true))
        items.add(HorizontalItem("校区", campusList[campusListIndex]))
        items.add(HorizontalItem("退出登录", "啪的一下就退出去了"))
        items.add(CategoryItem("其他", false))
        items.add(HorizontalItem("添加课表到桌面","添加一个桌面小程序"))
        items.add(CategoryItem("更新相关", false))
        items.add(HorizontalItem("检查更新", "如题"))
    }

    private fun onSwitchItemCheckChange(item: SwitchItem, isChecked: Boolean) {
        when (item.title) {
            "xxxx" -> {

            }
        }
        item.checked = isChecked
    }

    private fun onHorizontalItemClick(item: HorizontalItem, position: Int) {
        when (item.title) {
            "校区" -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("设置校区")
                    .setPositiveButton("确定") { _, _ ->
                        item.value = campusList[campusListIndex]
                        mAdapter.notifyItemChanged(position)
                        when (campusListIndex) {
                            0 -> getPrefer().edit{ putString(Const.KEY_CAMPUS,"jiayu") }
                            1 -> getPrefer().edit{ putString(Const.KEY_CAMPUS, "wuchang") }
                        }
                        mRecyclerView.snack("重启App生效")
                    }
                    .setSingleChoiceItems(campusList, campusListIndex) { _, which ->
                        campusListIndex = which
                    }
                    .show()
            }
            "退出登录" -> {
                showMessage(
                    "确定要退出登录而不是手滑？",
                    "二次确认",
                    "是的，我确定",
                    {
                        if (CacheUtil.getIsLogin()) {
                            CacheUtil.setIsLogin(false)
                            Toast.makeText(applicationContext, "重启App生效～", Toast.LENGTH_SHORT).show()
//                            RestartAppUtil.restartAPP(App.context,100)
//                            MainActivity().instanceMainActivity.finish()
//                            startActivity(Intent(this,MainActivity::class.java))
//                            finish(intent)
                        }
                    },
                    "手滑了",
                    {}
                )
            }
            "添加课表到桌面" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val mAppWidgetManager = getSystemService(AppWidgetManager::class.java)
                    val myProvider = ComponentName(appContext, NormalDailyProvider::class.java)
                    val b = Bundle()
                    b.putString("app_widget", "朝暮_每日课程")
                    if (mAppWidgetManager.isRequestPinAppWidgetSupported) {
                        val pinnedWidgetCallbackIntent =
                            Intent(appContext, NormalDailyProvider::class.java)
                        val successCallback = PendingIntent.getBroadcast(
                            appContext, 0,
                            pinnedWidgetCallbackIntent, PendingIntent.FLAG_IMMUTABLE
                        )
                        mAppWidgetManager.requestPinAppWidget(myProvider, b, successCallback)
                    }
                }else {
                    showMessage(
                        resources.getString(R.string.android_version_too_low_please_add_widget_manually),
                        resources.getString(R.string.aha_get_error)
                    )
                }
            }
            "检查更新" -> {
                /**
                 * @param isManual  用户手动点击检查，非用户点击操作请传false
                 * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
                 */
                Beta.checkUpgrade(true,false)
            }
        }
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}