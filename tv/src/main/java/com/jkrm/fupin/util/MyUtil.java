package com.jkrm.fupin.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.HomeFragmentAdapter;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.widgets.MyCustomDivider;
import com.jkrm.fupin.widgets.RecycleViewDivider;
import com.jkrm.fupin.widgets.RecyclerViewNoBugLinearLayoutManager;
import com.jkrm.fupin.widgets.SearchPopupWindow;
import com.jkrm.fupin.widgets.SpaceItemDecoration;
import com.jkrm.fupin.widgets.VideoPlayPopupWindow;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by hzw on 2018/7/20.
 */

public class MyUtil {

    public static void showMsg(String msg) {
        ToastUtil.showShort(MyApp.getInstance(), msg);
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
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
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
    public static void setRecyclerViewLinearlayout(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter, boolean showItemDecoration, boolean autoSetAdapter) {
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
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

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param showItemDecoration
     */
    public static void setRecyclerViewLinearlayoutHorizontal(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter, LinearLayoutManager layoutManager, boolean showItemDecoration, boolean autoSetAdapter) {
        //        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.HORIZONTAL);
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

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param showItemDecoration
     */
    public static void setRecyclerViewLinearlayoutHorizontal(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter,  boolean showItemDecoration, boolean autoSetAdapter) {
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        if(showItemDecoration) {
            recyclerView.addItemDecoration(new RecycleViewDivider(
                    activity, LinearLayoutManager.HORIZONTAL,
                    DisplayUtil.dip2px(activity, 0.5f), activity.getResources().getColor(R.color.color_ebebeb)));
        }
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        if(autoSetAdapter)
            recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param decorationHeightF
     */
    public static void setRecyclerViewLinearlayoutHorizontal(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter,  float decorationHeightF, boolean autoSetAdapter) {
        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerViewNoBugLinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                activity, LinearLayoutManager.HORIZONTAL,
                DisplayUtil.dip2px(activity, decorationHeightF), activity.getResources().getColor(R.color.color_ebebeb)));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        if(autoSetAdapter)
            recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param mAdapter
     * @param autoSetAdapter
     */
    public static void setRecyclerViewGridlayoutWithDecorationHeight(Activity activity, RecyclerView recyclerView, BaseQuickAdapter mAdapter, boolean autoSetAdapter, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(activity, spanCount);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        int itemSpace = activity.getResources().
                getDimensionPixelSize(R.dimen.recyclerView_item_space);
        recyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));
        if(autoSetAdapter)
            recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param adapter
     * @param autoSetAdapter
     */
    public static void setRecyclerViewGridlayoutCommon(Activity activity, RecyclerView recyclerView, BaseQuickAdapter adapter, boolean autoSetAdapter) {
        GridLayoutManager manager = new GridLayoutManager(activity, MyConstants.GridViewSpanCount.SPAN_COUNT);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.supportsPredictiveItemAnimations();
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration((new MyCustomDivider(DisplayUtil.dip2px(activity, 10))));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 通用recyclerview设置
     * @param activity
     * @param recyclerView
     * @param adapter
     * @param autoSetAdapter
     */
    public static void setRecyclerViewGridlayoutCommon(Activity activity, RecyclerView recyclerView, BaseQuickAdapter adapter, boolean autoSetAdapter, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(activity, spanCount);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.supportsPredictiveItemAnimations();
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration((new MyCustomDivider(DisplayUtil.dip2px(activity, 10))));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
    }

    public static void hideSoftKey(View v) {
        /*隐藏软键盘*/
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param mContext
     */
    public static void hideSoftInput(Activity mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param mContext
     */
    public static void showSoftInput(Activity mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showEmptyView(View emptyView, View normalView, boolean hasData) {
        if(hasData) {
            emptyView.setVisibility(View.GONE);
            normalView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            normalView.setVisibility(View.GONE);
        }
    }

    public static boolean isEmptyList(List list) {
        if(list == null || list.size() == 0)
            return true;
        else
            return false;
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

    public static void showVideoPopupWindow(Activity mActivity, View locationView, boolean isCollection, VideoPlayPopupWindow.OnItemClickListener onItemClickListener) {
        VideoPlayPopupWindow commonListPopupWindow = new VideoPlayPopupWindow(mActivity, isCollection);
        commonListPopupWindow.setOnItemClickListener(onItemClickListener);
        commonListPopupWindow.showAsLocationBottom(locationView);
    }

    public static void showSearchPopupWindow(Activity mActivity, SearchBean searchBean, View locationView, SearchPopupWindow.OnItemClickListener onItemClickListener) {
        SearchPopupWindow commonListPopupWindow = new SearchPopupWindow(mActivity, searchBean, onItemClickListener);
        commonListPopupWindow.setOnItemClickListener(onItemClickListener);
        commonListPopupWindow.showAsLocationBottom(locationView);
    }

    /**
     * 四舍五入保留两位
     *
     * @param params
     *
     * @return
     */
    public static String toDdecimal(float params) {
        java.math.BigDecimal bigDec = new java.math.BigDecimal(params);
        double total = bigDec.setScale(2, java.math.BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    public static boolean checkVideoType(String fileName) {
        if(fileName.endsWith(".mp4") || fileName.endsWith(".rmvb") || fileName.endsWith(".avi") || fileName.endsWith(".3gp")  || fileName.endsWith(".mkv"))
            return true;
        return false;
    }

    public static void setOnFocusListener(final Context context, final View view) {
        if(context == null || view == null)
            return;
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X_BIG2).scaleY(MyConstants.GridViewSpanCount.SCALE_Y_BIG2).setDuration(300).start();
                    view.setBackground(context.getResources().getDrawable(R.mipmap.icon_focus_video));
                } else {
                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X).scaleY(MyConstants.GridViewSpanCount.SCALE_Y).setDuration(300).start();
                    view.setBackground(null);
                }
            }
        });
    }

    public static void setOnFocusListenerNoScale(final Context context, final View view, final TextView tv) {
        if(context == null || view == null)
            return;
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X).scaleY(MyConstants.GridViewSpanCount.SCALE_Y_BIG).setDuration(300).start();
                    tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                } else {
//                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X).scaleY(MyConstants.GridViewSpanCount.SCALE_Y).setDuration(300).start();
                    tv.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        });
    }

    public static void setOnFocusListenerType(final Context context, final View view, final View view2) {
        if(context == null || view == null)
            return;
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X_BIG2).scaleY(MyConstants.GridViewSpanCount.SCALE_Y_BIG2).setDuration(300).start();
                    view.setBackground(context.getResources().getDrawable(R.mipmap.icon_focus_type));
                } else {
                    view.animate().scaleX(MyConstants.GridViewSpanCount.SCALE_X).scaleY(MyConstants.GridViewSpanCount.SCALE_Y).setDuration(300).start();
                    view.setBackground(null);
                }
                if(view2 != null) {
                    GradientDrawable drawable = (GradientDrawable) view2.getBackground();
                    if(drawable != null) {
                        drawable.setCornerRadius(MyConstants.Setting.CORNER_COMMON);
                    }
                }
            }
        });
    }

    /**
     * 左1右2上3下4
     */
    public static void setDrawableDirection(Context context, TextView tv, int tag, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (tag) {
            case 1:
                tv.setCompoundDrawables(drawable, null, null, null);
                break;
            case 2:
                tv.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                tv.setCompoundDrawables(null, drawable, null, null);
                break;
            case 4:
                tv.setCompoundDrawables(null, null, null, drawable);
                break;
        }
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

    public static boolean isImageType(String fileName) {
        if(fileName == null)
            return false;
        if(fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".JPEG"))
            return true;
        return false;
    }

    public static boolean isAllowHint() {
        if(APPUtils.isAppRunning(MyApp.getInstance(), MyApp.getInstance().getPackageName()) && !APPUtils.isBackground(MyApp.getInstance())) {
            return true;
        }
        return false;
    }

    /**
     * 刷新本地下载中或已下载完成的文件
     */
    public static void refreshLocalDbDownloadFile() {
        MyLog.d("OssResDownloadService DB 刷新检测本地数据库开始");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyApp.localDbDataList = DaoCacheFileUtil.getInstance().queryAllBean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyLog.d("OssResDownloadService DB 刷新检测本地数据库结束");
            }
        }).start();
    }
}
