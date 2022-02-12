package cn.mapotofu.everydaymvvm.app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import cat.ereza.customactivityoncrash.config.CaocConfig
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.ui.activity.MainActivity
import cn.mapotofu.everydaymvvm.app.event.AppViewModel
import cn.mapotofu.everydaymvvm.app.event.EventViewModel
import cn.mapotofu.everydaymvvm.ui.activity.ErrorActivity
import com.tencent.bugly.Bugly
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import me.hgj.jetpackmvvm.ext.util.jetpackMvvmLog
import com.tencent.bugly.beta.UpgradeInfo

import android.view.WindowManager
import cn.mapotofu.everydaymvvm.R

import com.tencent.bugly.beta.ui.UILifecycleListener

import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.ui.BetaActivity
import me.hgj.jetpackmvvm.base.appContext


/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app
 * @author milk
 * @date 2021/7/29
 */

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        //MMKV初始化
        MMKV.initialize(this)
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        jetpackMvvmLog = BuildConfig.DEBUG

        //自定义弹窗布局
        Beta.upgradeDialogLayoutId = R.layout.dialog_upgrade
        Beta.upgradeDialogLifecycleListener = object : UILifecycleListener<UpgradeInfo> {
            override fun onCreate(context: Context, view: View, upgradeInfo: UpgradeInfo) {
                //设置状态栏透明
                if (context is BetaActivity) {
                    val window: Window = context.window
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                }
            }
            override fun onStart(context: Context, view: View, upgradeInfo: UpgradeInfo) {}
            override fun onResume(context: Context, view: View, upgradeInfo: UpgradeInfo) {}
            override fun onPause(context: Context, view: View, upgradeInfo: UpgradeInfo) {}
            override fun onStop(context: Context, view: View, upgradeInfo: UpgradeInfo) {}
            override fun onDestroy(context: Context, view: View, upgradeInfo: UpgradeInfo) {}
        }
        Bugly.init(appContext, "7a3a48047c", false)

        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(MainActivity::class.java) // 重启的activity
            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
            .apply()
    }

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}