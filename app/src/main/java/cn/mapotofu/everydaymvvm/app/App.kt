package cn.mapotofu.everydaymvvm.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import cat.ereza.customactivityoncrash.config.CaocConfig
import cn.mapotofu.everydaymvvm.BR
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.ui.activity.MainActivity
import cn.mapotofu.everydaymvvm.app.event.AppViewModel
import cn.mapotofu.everydaymvvm.app.event.EventViewModel
import cn.mapotofu.everydaymvvm.app.ext.getProcessName
import cn.mapotofu.everydaymvvm.ui.activity.ErrorActivity
import cn.mapotofu.everydaymvvm.ui.activity.UpdateActivity
import cn.mapotofu.everydaymvvm.viewmodel.state.GradeViewModel
import com.drake.brv.utils.BRV
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import me.hgj.jetpackmvvm.ext.util.jetpackMvvmLog

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
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        //MMKV初始化
        MMKV.initialize(this)
        instance = this
        context = applicationContext
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)

        //初始化Bugly
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        /*在application中初始化时设置监听，监听策略的收取*/
        Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
            if (strategy != null) {
                val i = Intent()
                i.setClass(applicationContext, UpdateActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                //Toast.makeText(applicationContext, "检测到更新！\uD83E\uDD73", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(applicationContext, "没有更新", Toast.LENGTH_LONG).show()
            }
        }

        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = object : UpgradeStateListener {
            /* @param isManual  true:手动检查 false:自动检查 */
            override fun onUpgradeSuccess(isManual: Boolean) {
                //Toast.makeText(applicationContext, "检测到更新！\uD83E\uDD73", Toast.LENGTH_SHORT).show()
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                Toast.makeText(applicationContext, "更新失败～ \uD83D\uDE2D", Toast.LENGTH_SHORT).show()
            }

            override fun onUpgrading(isManual: Boolean) {
                if (isManual) Toast.makeText(applicationContext, "正在检查更新～ \uD83D\uDE2C", Toast.LENGTH_SHORT).show()
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                if (isManual) Toast.makeText(applicationContext, "你已是最新版本了～ \uD83D\uDE0E", Toast.LENGTH_SHORT).show()
            }

            override fun onDownloadCompleted(isManual: Boolean) {
                Toast.makeText(applicationContext, "下载完成！ \uD83E\uDD29", Toast.LENGTH_SHORT).show()
            }
        }
        Bugly.init(context, "7a3a48047c", false)

        jetpackMvvmLog = BuildConfig.DEBUG

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
}