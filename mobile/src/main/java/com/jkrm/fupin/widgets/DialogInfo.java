package com.jkrm.fupin.widgets;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jkrm.fupin.R;
import com.jkrm.fupin.util.DisplayUtil;


public class DialogInfo {

    private Dialog pd;
    private Display display;

    public Dialog getPd() {
        return pd;
    }

    public void setPd(Dialog pd) {
        this.pd = pd;
    }

    private Context mContext;

    public DialogInfo(Context mContext) {
        this.mContext = mContext;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        if (null == pd) {
            pd = new Dialog(mContext, R.style.dialog);
        }

    }

    /**
     * 控制点击空白处  dialog 消失不消失用的
     *
     * @param a
     */
    public void CanceledOnTouchOutside(boolean a) {
        pd.setCanceledOnTouchOutside(a);
        pd.setCancelable(a);
    }


    /**
     * 关闭dialog框
     */
    public void hiddleDialog() {
        pd.dismiss();
    }


    /**
     * 自定义布局dialog
     *
     * @param view
     */
    public void showCustomeDialog(Context context, View view, double weight) {

        pd.setContentView(view);
        Window window = pd.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * weight); // 宽度设置为屏幕的weight
        window.setAttributes(p);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
    }

    /**
     * 自定义布局dialog
     *
     * @param view
     */
    public void showCustomeDialog(Context context, View view) {

        pd.setContentView(view);
        Window window = pd.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = d.getWidth(); // 宽度设置为屏幕的weight
        p.height = d.getHeight();
        window.setAttributes(p);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
    }

    /**
     * 自定义布局dialog
     *
     * @param view
     */
    public void showCustomeDialogFull(Context context, View view, double weight) {

        pd.setContentView(view);
        Window window = pd.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//        p.width = d.getWidth(); // 宽度设置为屏幕的weight
//        p.height = d.getHeight();
        p.width = (int) (DisplayUtil.deviceWidthPX(context) * weight); // 宽度设置为屏幕的weight
        p.height = (int) (DisplayUtil.deviceHeightPX(context)  * 0.2);
        window.setAttributes(p);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
    }



    /**
     * 自定义布局dialog
     *
     * @param view
     */
    public Dialog showCustomeDialogFullForDialog(Context context, View view, double weight) {

        pd.setContentView(view);
        Window window = pd.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//        p.width = d.getWidth(); // 宽度设置为屏幕的weight
//        p.height = d.getHeight();
        p.width = (int) (DisplayUtil.deviceWidthPX(context) * weight); // 宽度设置为屏幕的weight
        p.height = (int) (DisplayUtil.deviceHeightPX(context) * 0.2);
        window.setAttributes(p);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
        return pd;
    }


    public interface TwoLayoutOnclick {

        public void left(View left);

        public void right(View right, String input);

    }


}
