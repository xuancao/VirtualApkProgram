package com.xuancao.base.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xuancao.base.BaseApp;


/**
 * Toast辅助类，避免重复显示
 */
public class ToastUtil {

    private static Toast mToast;
    private static Context context = BaseApp.getInstance().getContext();

    private static Handler mHandler = new Handler();

    private static Runnable runnable = new Runnable() {
        public void run() {
            if(mToast != null) {
                mToast.cancel();
                mToast = null;
            }
        }
    };



    /**
     * Toast显示
     * @param text 显示内容
     */
    public static void show(String text) {
        show(text, null);
    }

    /**
     * Toast显示
     * @param resId 显示内容资源
     */
    public static void show(int resId) {
        show(context.getResources().getString(resId), null);
    }

    /**
     * Toast显示:可以控制显示时间
     * @param resId 显示内容资源
     * @param duration 显示时间
     */
    public static void show(int resId, int duration) {
        show(context.getResources().getString(resId), duration);
    }


    /**
     * Toast显示:可以控制显示时间
     * @param text 显示内容
     * @param duration 显示时间
     */
    public static void show(String text,Integer duration){
        final int myDuration = (duration==null)? Toast.LENGTH_SHORT:duration;
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, myDuration);
        mHandler.removeCallbacks(runnable);
        int delayMillis = (myDuration== Toast.LENGTH_SHORT)?2000:3000;
        mHandler.postDelayed(runnable, delayMillis);
        mToast.show();
    }

    /**
     * Toast显示:可以显示图片等控件
     * @param text 显示内容
     * @param view 图片View
     */
    public static void showWithView(String text, View view){
        showWithView(text,null,view);
    }

    /**
     * Toast显示:可以显示图片等控件、控制显示时间
     * @param text 显示内容
     * @param duration 显示时间
     * @param view 图片View
     */
    public static void showWithView(String text, Integer duration, View view){
        final int myDuration = (duration==null)? Toast.LENGTH_SHORT:duration;
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, myDuration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) mToast.getView();
        toastView.addView(view);
        mHandler.removeCallbacks(runnable);
        int delayMillis = (myDuration== Toast.LENGTH_SHORT)?2000:3000;
        mHandler.postDelayed(runnable, delayMillis);
        mToast.show();
    }


}
