package com.jkrm.fupin.widgets;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.jkrm.fupin.R;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyUtil;

/**
 * Created by hzw on 2018/8/15.
 */
public class VideoPlayPopupWindow extends PopupWindow {
    private Activity mActivity;
    private OnItemClickListener mOnItemClickListener;
    private View contentView;


    public VideoPlayPopupWindow(final Activity mActivity, final boolean isCollection) {
        super(mActivity);
        this.mActivity = mActivity;
        contentView = View.inflate(mActivity, R.layout.view_popupwindow_video, null);
        Button btn_collection = (Button) contentView.findViewById(R.id.btn_collection);
        Button btn_download = (Button) contentView.findViewById(R.id.btn_download);
        btn_collection.setText(isCollection ? "取消收藏" : "添加到收藏列表");
        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCollection) {
                    mOnItemClickListener.onCollectionDelete();
                } else {
                    mOnItemClickListener.onCollectionSave();
                }
                VideoPlayPopupWindow.this.dismiss();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onDownload();
                VideoPlayPopupWindow.this.dismiss();
            }
        });
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.sytle_anim_pop_in_out);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.update();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setOverlapAnchor(true);
        }
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_NORMAL;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 展示窗口
     */
    public void show(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        this.showAsDropDown(parent, 0, 0);
    }

    public void showAsLocation(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        int windowPos[] = MyUtil.calculatePopWindowPos(parent, contentView);
        int xOff = 20;// 可以自己调整偏移
        windowPos[0] -= xOff;
        this.showAtLocation(parent, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public void showAsLocationBottom(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * calendar菜单选择监听接口
     */
    public interface OnItemClickListener {
        void onCollectionSave();
        void onCollectionDelete();
        void onDownload();
    }

    /**
     * 设置菜单监听
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

