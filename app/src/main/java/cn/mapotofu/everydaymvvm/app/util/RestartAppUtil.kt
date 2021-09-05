package cn.mapotofu.everydaymvvm.app.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import cn.mapotofu.everydaymvvm.app.services.KillSelfService


/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.util
 * @author milk
 * @date 2021/8/31
 */
object RestartAppUtil {
    /**
     * 重启整个APP
     * @param context
     * @param Delayed 延迟多少毫秒
     */
    fun restartAPP(context: Context, Delayed: Long) {
        /**开启一个新的服务，用来重启本APP */
        val intent1 = Intent(context, KillSelfService::class.java)
        intent1.putExtra("PackageName", context.packageName)
        intent1.putExtra("Delayed", Delayed)
        context.startService(intent1)
        /**杀死整个进程 */
        Process.killProcess(Process.myPid())
    }

    /***重启整个APP */
    fun restartAPP(context: Context) {
        restartAPP(context, 2000)
    }
}