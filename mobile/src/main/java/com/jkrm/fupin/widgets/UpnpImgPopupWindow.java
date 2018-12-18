package com.jkrm.fupin.widgets;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aliyun.vod.common.utils.DensityUtil;
import com.jkrm.fupin.R;
import com.jkrm.fupin.constants.MyConstants;

/**
 * Created by hzw on 2017/12/8.
 */
public class UpnpImgPopupWindow extends PopupWindow {
    private Activity mActivity;
    private OnMenuListener mMenuListener;
    private TextView tv_slide;

    public UpnpImgPopupWindow(final Activity mActivity) {
        this.mActivity = mActivity;
        View contentView = View.inflate(mActivity, R.layout.view_popupwindow_upnp_img, null);
        tv_slide = (TextView) contentView.findViewById(R.id.tv_slide);
        TextView tv_download = (TextView) contentView.findViewById(R.id.tv_download);
        tv_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mMenuListener) {
                    mMenuListener.onSlide();
                }

                dismiss();
            }
        });
        tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mMenuListener)
                    mMenuListener.onDownload();
                dismiss();
            }
        });
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.update();
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_NORMAL;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    public void setSlideText(String text) {
        tv_slide.setText(text);
    }

    /**
     * 展示窗口
     */
    public void show(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        this.showAsDropDown(parent, DensityUtil.dip2px(mActivity, -129), 20);
    }

    /**
     * calendar菜单选择监听接口
     */
    public interface OnMenuListener {
        void onSlide();
        void onDownload();
    }

    /**
     * 设置菜单监听
     */
    public void setOnMenuListener(OnMenuListener listener) {
        this.mMenuListener = listener;
    }

}

