package cn.mapotofu.everydaymvvm.app.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.DisplayMetrics




/**
 * @author milk
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils
 * @date 2021/8/23
 */
object ViewsUtil {
    /**
     * 创建一个常规的shape形状
     *
     * @param color       填充颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     * @param strokeColor 描边颜色
     */
    fun getDrawable(color: Int, corner: Int, strokeWidth: Int, strokeColor: Int): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(color)
        gradientDrawable.cornerRadius = corner.toFloat()
        gradientDrawable.setStroke(strokeWidth, strokeColor)
        return gradientDrawable
    }

    /**
     * 创建一个渐变shape形状
     * @param orientation 方向
     * @param colors 颜色值
     * @param strokeColor 描边颜色
     * @param corner 圆角
     * @param strokeWidth 描边宽度
     */
    fun getDrawable(
        orientation: GradientDrawable.Orientation,
        colors: IntArray,
        corner: Int,
        strokeWidth: Int,
        strokeColor: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable(orientation, colors)
        gradientDrawable.cornerRadius = corner.toFloat()
        gradientDrawable.setStroke(strokeWidth, strokeColor)
        return gradientDrawable
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixels(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}