package cn.mapotofu.everydaymvvm.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cn.mapotofu.everydaymvvm.app.App;

public class UisUtil {

    public static int height = getScreenHeight();
    public static int width = getScreenWidth();

    /**
     * 获取反射布局
     *
     * @param context 上下文
     * @param id      layoutId
     */
    public static View inflate(Context context, int id) {
        return LayoutInflater.from(context).inflate(id, null);
    }


    /**
     * 是否是暗黑模式
     */
    public static boolean getDarkModeStatus(Context context) {
        int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth() {
        return getDisplayMetrics(App.context).widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics(App.context).heightPixels;
    }


    private static DisplayMetrics getDisplayMetrics(Context context) {
        final DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }


    /**
     * 给color添加透明度
     *
     * @param alpha     透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }


    /**
     * 显示元素
     */
    public static void show(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 隐藏元素
     */
    public static void hide(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }


    public static float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
}
