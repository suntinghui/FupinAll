package com.jkrm.fupin.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseDialog;


/**
 * Created by hzw on 2018/7/18.
 */
public class LoadingDialog extends BaseDialog {

    private Context mContext;

    public LoadingDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_dialog_loading;
    }

    @Override
    public void initLayoutParams(Context context, View view) {
        mDialog = new Dialog(context, R.style.BaseDialog);
        mDialog.setContentView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    public void show() {
        // main.xml中的ImageView
        final ImageView spaceshipImage = (ImageView) mDialog.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                spaceshipImage.clearAnimation();
            }
        });
        mDialog.show();
    }

    public void showLoadingDialog(String msg) {
        TextView tipTextView = (TextView) mDialog.findViewById(R.id.text_view_message);// 提示文字
        if(null != msg) {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(msg);
        }
        // main.xml中的ImageView
        final ImageView spaceshipImage = (ImageView) mDialog.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                spaceshipImage.clearAnimation();
            }
        });
        mDialog.show();
    }

}
