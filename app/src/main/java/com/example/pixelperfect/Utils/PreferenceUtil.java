package com.example.pixelperfect.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    private static final String APP_EDITOR = "APP_EDITOR";

    /**
     * 设置键盘高度到SharedPreferences中。
     *
     * @param context 上下文对象
     * @param height  键盘高度
     */
    public static void setHeightOfKeyboard(Context context, int height) {
        // 获取SharedPreferences.Editor对象并开始编辑
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).edit();

        // 将键值对存入SharedPreferences.Editor对象
        editor.putInt("height_of_keyboard", height);

        // 应用编辑操作
        editor.apply();
    }

    public static int getKeyboard(Context context) {
        return context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).getInt("height_of_keyboard", -1);
    }

    public static void setHeightOfNotch(Context context, int height) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).edit();
        edit.putInt("height_of_notch", height);
        edit.apply();
    }

    public static int getHeightOfNotch(Context context) {
        return context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).getInt("height_of_notch", -1);
    }

    public static boolean isRated(Context context) {
        return context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).getBoolean("is_rated_2", false);
    }

    public static void setRated(Context context, boolean isRate) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).edit();
        edit.putBoolean("is_rated_2", isRate);
        edit.apply();
    }

    public static void increateCounter(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).edit();
        edit.putInt("counter", getCounter(context) + 1);
        edit.apply();
    }

    public static int getCounter(Context context) {
        return context.getSharedPreferences(APP_EDITOR, Context.MODE_PRIVATE).getInt("counter", 1);
    }
}
