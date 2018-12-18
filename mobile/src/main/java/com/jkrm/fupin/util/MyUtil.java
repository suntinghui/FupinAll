package com.jkrm.fupin.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.widgets.CommonListPopupWindow;
import com.jkrm.fupin.widgets.RecycleViewDivider;
import com.jkrm.fupin.widgets.RecyclerViewNoBugLinearLayoutManager;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hzw on 2018/7/20.
 */

public class MyUtil {

    public static void showMsg(String msg) {
        ToastUtil.showShort(MyApp.getInstance(), msg);
    }

    public static boolean isEmptyList(List list) {
        if(list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean isTextNull(String string) {
        if(string == null || string.trim().equals("") || string.trim().equals("null"))
            return true;
        else
            return false;
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param showItemDecoration
     */
    public static void setRecyclerViewLinearlayout(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter, boolean showItemDecoration) {
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if(showItemDecoration) {
            recyclerView.addItemDecoration(new RecycleViewDivider(
                    activity, LinearLayoutManager.HORIZONTAL,
                    DisplayUtil.dip2px(activity, 0.5f), activity.getResources().getColor(R.color.color_ebebeb)));
        }
        //        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param decorationHeightF
     * @param autoSetAdapter
     */
    public static void setRecyclerViewLinearlayoutWithDecorationHeight(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter,
                                                                       float decorationHeightF, boolean autoSetAdapter) {
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                activity, LinearLayoutManager.HORIZONTAL,
                DisplayUtil.dip2px(activity, decorationHeightF), activity.getResources().getColor(R.color.color_ebebeb)));
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        if(autoSetAdapter)
            recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param showItemDecoration
     */
    public static void setRecyclerViewLinearlayout(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter, LinearLayoutManager layoutManager, boolean showItemDecoration, boolean autoSetAdapter) {
        //        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if(showItemDecoration) {
            recyclerView.addItemDecoration(new RecycleViewDivider(
                    activity, LinearLayoutManager.HORIZONTAL,
                    DisplayUtil.dip2px(activity, 0.5f), activity.getResources().getColor(R.color.color_ebebeb)));
        }
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        if(autoSetAdapter)
            recyclerView.setAdapter(mAdapter);
    }

    public static void showCommonListPopupWindow(Activity mActivity, List<? extends Object> list, View locationView, CommonListPopupWindow.OnItemClickListener onItemClickListener) {
        CommonListPopupWindow commonListPopupWindow = new CommonListPopupWindow(mActivity);
        commonListPopupWindow.setListData(list);
        commonListPopupWindow.setOnItemClickListener(onItemClickListener);
        commonListPopupWindow.showAsLocationBottom(locationView);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        //获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    public static List<String> removeDataFromList(List<String> list1, List<String> list2) {
        if(isEmptyList(list1))
            return null;
        for (Iterator it = list1.iterator(); it.hasNext(); ) {
            String temp1 = (String) it.next();
            for(String temp2 : list2) {
                if(temp1.equals(temp2)) {
                    it.remove();
                }
            }
        }
        return list1;
    }

    public static boolean isVideoType(String fileName) {
        if(fileName == null)
            return false;
        if(fileName.endsWith(".mp4") ||  fileName.endsWith(".avi") || fileName.endsWith(".3gp")  || fileName.endsWith(".mkv")
                || fileName.endsWith(".rm") || fileName.endsWith(".rmvb") || fileName.endsWith(".flv") || fileName.endsWith("f4v")
                || fileName.endsWith(".mov") || fileName.endsWith("mpg"))
            return true;
        return false;
    }

    public static boolean isAudioType(String fileName) {
        if(fileName == null)
            return false;
        if(fileName.endsWith(".mp3") || fileName.endsWith(".wma") || fileName.endsWith(".wav")  || fileName.endsWith(".ogg"))
            return true;
        return false;
    }

    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * 权限 <uses-permission android:name="android.permission.GET_TASKS"/>
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }
}
