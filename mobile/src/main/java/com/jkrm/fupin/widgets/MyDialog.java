package com.jkrm.fupin.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.util.DisplayUtil;


/**
 * Created by hzw on 2018/7/20.
 */
public class MyDialog {
    private TextView mTvContent, mTvCancel,  mTvConfirm;
    private View mSpiltLine;
    private Dialog dialog;

    public MyDialog(Context context, String msg) {
        dialog = new Dialog(context, R.style.prompt_dialog);
        dialog.setContentView(R.layout.view_prompt_dialog);
        initView(dialog);
        mTvContent.setText(msg);
        mTvCancel.setVisibility(View.GONE);
        mTvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setDialogSize(context);
        dialog.show();
    }

    public MyDialog(Context context, String msg, OnClickListener confirmListener) {
        dialog = new Dialog(context, R.style.prompt_dialog);
        dialog.setContentView(R.layout.view_prompt_dialog);
        initView(dialog);
        mTvContent.setText(msg);
        mTvCancel.setVisibility(View.GONE);
        mTvConfirm.setOnClickListener(confirmListener);
        setDialogSize(context);
        dialog.show();
    }

    public MyDialog(Context context, String msg, OnClickListener confirmListener, boolean showCancelBtn) {
        dialog = new Dialog(context, R.style.prompt_dialog);
        dialog.setContentView(R.layout.view_prompt_dialog);
        initView(dialog);
        mTvContent.setText(msg);
        mTvCancel.setVisibility(showCancelBtn ? View.VISIBLE : View.GONE);
        mTvConfirm.setOnClickListener(confirmListener);
        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setDialogSize(context);
        dialog.show();
    }

    public MyDialog(Context context, String msg, boolean showCancel, OnClickListener confirmListener, OnClickListener cancelListener) {
        dialog = new Dialog(context, R.style.prompt_dialog);
        dialog.setContentView(R.layout.view_prompt_dialog);
        initView(dialog);
        mTvContent.setText(msg);
        mTvCancel.setOnClickListener(cancelListener);
        mTvConfirm.setOnClickListener(confirmListener);
        setDialogSize(context);
        dialog.show();
    }

    private void initView(Dialog dialog) {
        mTvContent = (TextView) dialog.findViewById(R.id.tv_content);
        mTvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        mSpiltLine = dialog.findViewById(R.id.spilt_line);
        mTvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
    }

    public void dismiss() {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setDialogSize(Context context) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的weight
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }
}
