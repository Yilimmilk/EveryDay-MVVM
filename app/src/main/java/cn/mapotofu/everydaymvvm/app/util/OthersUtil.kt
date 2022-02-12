package cn.mapotofu.everydaymvvm.app.util

import android.app.ActivityManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.R
import me.hgj.jetpackmvvm.base.appContext
import java.lang.Exception

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.util
 * @author milk
 * @date 2021/8/12
 */
object OthersUtil {

    /**
     * 打开链接
     *
     * @param url
     */
    fun openUrl(url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(appContext,"无法打开浏览器", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 启动QQ
     *
     * @param context
     * @param qqNumber qq号
     */
    fun startQQ(context: Context, qqNumber: String?) {
        val qq = "com.tencent.mobileqq"
        val tim = "com.tencent.tim"
        var intent = Intent()
        val packageManager: PackageManager = context.packageManager
        try {
            val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.text = qqNumber
            intent = packageManager.getLaunchIntentForPackage(qq)!!
        } catch (e: Exception) {
            intent = packageManager.getLaunchIntentForPackage(tim)!!
        }
        context.startActivity(intent)
    }

    /**
     * 打开coolapk个人主页
     *
     * @param str
     */
    fun startCoolApk(str: String) {
        val intent = Intent()
        try {
            intent.setClassName("com.coolapk.market", "com.coolapk.market.view.AppLinkActivity")
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("coolmarket://u/$str")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(appContext, appContext.getString(R.string.no_coolapk),Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * 获取版本号
     */
    val appVersion: String
        get() = BuildConfig.VERSION_NAME

    /**
     * 发邮件
     */
    fun sendEmail(msg: String) {
        // 必须明确使用mailto前缀来修饰邮件地址
        val uri = Uri.parse("mailto:2510355993@qq.com")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //intent.putExtra(Intent.EXTRA_CC, "2510355993@qq.com") // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "朝暮安卓端BUG反馈") // 主题
        intent.putExtra(Intent.EXTRA_TEXT, msg) // 正文
        appContext.startActivity(intent)
    }

    /**
     * 结束App
     */
    fun exitAPP(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appTaskList = activityManager.appTasks
        for (appTask in appTaskList) {
            appTask.finishAndRemoveTask()
        }
    }
}