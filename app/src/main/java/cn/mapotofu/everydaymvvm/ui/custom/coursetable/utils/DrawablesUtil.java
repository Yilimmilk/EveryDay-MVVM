package cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * @author milk
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.custom.coursetable.utils
 * @date 2021/8/23
 */
public class DrawablesUtil {

    /**
     * 创建一个常规的shape形状
     *
     * @param color       填充颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     * @param strokeColor 描边颜色
     */
    public static Drawable getDrawable(int color, int corner, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }


    /**
     * 创建一个渐变shape形状
     *
     * @param orientation 方向
     * @param colors      颜色值
     * @param strokeColor 描边颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     */
    public static Drawable getDrawable(GradientDrawable.Orientation orientation, int[] colors, int corner, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
        gradientDrawable.setCornerRadius(corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    /**
     * 创建一个bitmap形状
     *
     * @param color 颜色
     */
    public static Bitmap getBitMapBackground(int color) {
        try {
            Bitmap.Config config = Bitmap.Config.ARGB_8888; // Bitmap.Config.ARGB_8888 Bitmap.Config.ARGB_4444 to be used as these two config constant supports transparency
            Bitmap bitmap = Bitmap.createBitmap(2, 2, config); // Create a Bitmap

            Canvas canvas = new Canvas(bitmap); // Load the Bitmap to the Canvas
            canvas.drawColor(color); //Set the color

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
