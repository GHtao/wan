package com.gt.wan_gt.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by long on 2016/6/6.
 * <p>
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtil {

    private static Context sContext;
    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;

    private ToastUtil() {
        throw new RuntimeException("ToastUtils cannot be initialized!");
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static void showToast(String s) {
        showToast(s, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(sContext.getString(resId));
    }

    private static void showToast(String s, int duration) {
        if (toast == null) {
            toast = Toast.makeText(sContext, s, duration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            toast.setDuration(duration);
            long twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {//两次内容一样 判断时间
                if (twoTime - oneTime > duration) {
                    toast.show();
                }
            } else {//内容不一样就直接显示
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }
}
