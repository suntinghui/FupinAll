package com.jkrm.fupin.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jkrm.fupin.R;


/**
 * Created by hzw on 2018/7/18.
 */

public class CustomToolbar extends Toolbar {

    private TextView mTvLeftView;
    private TextView mTvMiddleView;
    private TextView mTvRightView;

    private Toolbar mToolbar;

    OnLeftClickListener mOnLeftClickListener;
    OnRightClickListener mOnRightClickListener;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_toolbar,this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTvLeftView = (TextView) findViewById(R.id.toolbar_left);
        mTvMiddleView = (TextView) findViewById(R.id.toolbar_title);
        mTvRightView = (TextView) findViewById(R.id.toolbar_right);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mTvLeftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLeftClickListener.onLeftTextClick();
            }
        });

        mTvRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRightClickListener.onRightTextClick();
            }
        });
    }


    public void setToolbarBackgroundColor(int color) {

        mToolbar.setBackgroundResource(color);

    }

    /**
     * 隐藏中间的标题
     */
    public void hideMiddleTitle() {
        mTvMiddleView.setVisibility(View.GONE);
    }

    /**
     * 设置只显示标题
     */
    public void setOnlyTitle() {

        mTvLeftView.setVisibility(INVISIBLE);
        mTvRightView.setVisibility(INVISIBLE);
    }

    /**
     * 设置左侧不显示
     */
    public void hideLeftView() {

        mTvLeftView.setVisibility(INVISIBLE);
    }

    /**
     * 设置右侧不显示
     */
    public void hideRightView() {

        mTvRightView.setVisibility(INVISIBLE);
    }


    /**
     * 设置标题
     * @param text
     */
    public void setToolbarTitle(String text) {

        this.setTitle("");
        mTvMiddleView.setVisibility(View.VISIBLE);
        mTvMiddleView.setText(text);


    }

    public View getRightView() {
        return mTvRightView;
    }

    public String getRightText() {
        return mTvRightView.getText().toString();
    }

    /**
     * 设置左侧文本
     * @param text
     */
    public void setLeftText(String text) {

        mTvLeftView.setVisibility(VISIBLE);
        mTvLeftView.setText(text);

        //设置文本则不显示图片
        mTvLeftView.setCompoundDrawables(null,null,null,null);

    }

    /**
     * 设置左侧文本
     * @param text
     */
    public void setLeftText(String text, int id) {

        mTvLeftView.setVisibility(VISIBLE);
        mTvLeftView.setText(text);

        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

    }

    /**
     * 设置右边文本
     * @param text
     */
    public void setRightText(String text) {

        mTvRightView.setVisibility(VISIBLE);
        mTvRightView.setText(text);

        //设置文本则不显示图片
        mTvRightView.setCompoundDrawables(null,null,null,null);

    }


    /**
     * 设置左侧图片
     * @param id
     */
    public void setLeftImg(int id) {

        Drawable drawable = getResources().getDrawable(id);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        //设置图片则不显示文字
        mTvLeftView.setText("");

        mTvLeftView.setCompoundDrawables(drawable,null,null,null);


    }


    /**
     * 设置右侧图片
     * @param id
     */
    public void setRightImg(int id) {

        Drawable drawable = getResources().getDrawable(id);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        //设置图片则不显示文字
        mTvRightView.setText("");
        mTvRightView.setCompoundDrawables(null,null,drawable,null);

    }

    //左侧文本回调接口
    public interface OnLeftClickListener {
        void onLeftTextClick();
    }

    /**
     * 设置左侧文本回调
     * @param listener
     */
    public void setOnLeftClickListener(OnLeftClickListener listener) {
        this.mOnLeftClickListener = listener;
    }

    //右侧文本回调接口
    public interface OnRightClickListener {
        void onRightTextClick();
    }

    /**
     * 设置右侧文本回调
     * @param litener
     */
    public void setOnRightClickListener(OnRightClickListener litener) {
        this.mOnRightClickListener = litener;
    }

    /**
     * 设置返回图片
     * @param id 图片的id
     */
    public void setbackIcon(int id) {

        this.setNavigationIcon(id);

        mTvLeftView.setVisibility(GONE);
        //左侧文本不设置draw
        mTvLeftView.setCompoundDrawables(null,null,null,null);
    }

}
