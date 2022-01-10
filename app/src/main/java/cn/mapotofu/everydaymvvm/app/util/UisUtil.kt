package cn.mapotofu.everydaymvvm.app.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import cn.mapotofu.everydaymvvm.app.App
import android.util.DisplayMetrics
import android.view.WindowManager
import android.util.TypedValue
import android.view.View
import androidx.core.text.HtmlCompat

object UisUtil {
    var height = screenHeight
    var width = screenWidth

    /**
     * 获取反射布局
     *
     * @param context 上下文
     * @param id      layoutId
     */
    fun inflate(context: Context?, id: Int): View {
        return LayoutInflater.from(context).inflate(id, null)
    }

    /**
     * 是否是暗黑模式
     */
    fun getDarkModeStatus(context: Context): Boolean {
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    val screenWidth: Int
        get() = getDisplayMetrics(App.context).widthPixels
    val screenHeight: Int
        get() = getDisplayMetrics(App.context).heightPixels

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val metrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            metrics
        )
        return metrics
    }

    /**
     * 给color添加透明度
     *
     * @param alpha     透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = Math.min(255, Math.max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }

    /**
     * 显示元素
     */
    fun show(vararg views: View?) {
        for (view in views) {
            if (view != null) {
                view.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 隐藏元素
     */
    fun hide(vararg views: View?) {
        for (view in views) {
            if (view != null) {
                view.visibility = View.GONE
            }
        }
    }

    fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            Resources.getSystem().displayMetrics
        )
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun resizeStatusBar(context: Context, view: View) {
        val layoutParams = view.layoutParams
        layoutParams.height = getStatusBarHeight(context.applicationContext)
        view.layoutParams = layoutParams
    }

    fun getHtmlSpannedString(str: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(str, HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(str)
        }
    }
}