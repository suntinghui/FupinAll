package com.jkrm.fupin.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jkrm.fupin.ui.activity.MainActivity;
import com.jkrm.fupin.R;
import com.jkrm.fupin.util.StatusBarUtil;
import com.jkrm.fupin.util.ToastUtil;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.jkrm.fupin.widgets.LoadingDialog;
import com.jkrm.fupin.widgets.MyDialog;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by hzw on 2018/7/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, View.OnClickListener {
    protected Activity mActivity = BaseActivity.this;
    public String TAG = BaseActivity.class.getSimpleName();
    private LoadingDialog mLoadingDialog;
    private MyDialog mDialog;
    private Unbinder unbinder;

    protected CustomToolbar mToolbar;
    protected abstract int getContentId();
    protected void initData() {}
    protected void initView() {}
    protected void initListener() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        if(-1 != getContentId())
            setContentView(getContentId());
        mActivity = this;
        unbinder = ButterKnife.bind(this);
        setToolbar();
        initView();
        initData();
        initListener();
    }

    @Override
    public void showLoadingDialog() {
        dismissLoadingDialog();
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void dismissLoadingDialogDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        }, 500);

    }

    protected void showDialog(String msg) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg);
    }

    protected void showDialog(String msg, View.OnClickListener listener) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, listener);
    }

    protected void showDialogToFinish(String msg) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                finish();
            }
        });
    }

    protected void showDialogWithCancelBtn(String msg, View.OnClickListener confirmListener) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, confirmListener, true);
    }

    protected void dismissDialog() {
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showMsg(final String msg) {
//        LogUtil.i(TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showLong(mActivity, msg);
            }
        });

    }

    public void showMsg(final int msgId) {
//        LogUtil.i(TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showLong(mActivity, getString(msgId));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void toClass(Class<? extends Activity> classTo, Bundle bundle, boolean needFinish) {
        if (classTo == null)
            return;
        Intent intent = new Intent(this, classTo);
        if(bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        if(needFinish)
            finish();
    }

    public void showView(View v, boolean isShow) {
        if (v != null) {
            if (isShow)
                v.setVisibility(View.VISIBLE);
            else
                v.setVisibility(View.GONE);
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void toBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 强制退出程序
     */
    public void exitAppForced() {
        dismissLoadingDialog();
//        ActCollector.release();
//        mApp.clearService();
        System.exit(0);
    }

    public void replaceFragment(int id, Fragment fragment, boolean isAddBack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        if (isAddBack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public void replaceFragmentArgs(int id, Fragment fragment, Object obj,
                                    boolean isAddBack) {
        Bundle b = new Bundle();
        b.putSerializable("params", (Serializable) obj);
        fragment.setArguments(b);
        replaceFragment(id, fragment, isAddBack);
    }

    public void setToolbar() {
        mToolbar = (CustomToolbar) findViewById(R.id.toolbar);
        if(mToolbar != null) {
            mToolbar.hideMiddleTitle();
            if(mActivity instanceof MainActivity) {
                mToolbar.setRightImg(R.mipmap.icon_menu);
            } else {
                mToolbar.setRightImg(R.mipmap.icon_menu);
            }
            mToolbar.setLeftImg(R.mipmap.icon_toolbar_return);
        }
        setStatusColor();
    }

    private void setStatusColor() {
        if(filterActivity()) {
//            Random random = new Random();
//            int color = 0xff000000 | random.nextInt(0xffffff);
            int colorStatusBar = getResources().getColor(R.color.colorPrimary);
            if(mToolbar != null) {
                int color = getResources().getColor(R.color.colorPrimary);
                mToolbar.setBackgroundColor(color);
            }
            StatusBarUtil.setColor(mActivity, colorStatusBar, 0);//0 透明 255 黑色
        }
    }

    /**
     * 筛选部分页面, 不允许更改状态栏颜色值
     * @return
     */
    private boolean filterActivity() {
//        if(mActivity instanceof MainActivity) {
//            return false;
//        }
        return true;
    }

    /**
     * View是否可见
     * @param v
     * @return
     */
    protected boolean isViewVisiable(View v) {
        if(null == v)
            return false;
        if(View.VISIBLE == v.getVisibility())
            return true;
        else
            return false;
    }

    protected String getText(TextView tv) {
        if(tv == null || TextUtils.isEmpty(tv.getText()))
            return "";
        return tv.getText().toString();
    }

    /**
     * 设置TextView 值
     * @param tv
     * @param content
     */
    protected void setText(TextView tv, String content) {
        if(TextUtils.isEmpty(content))
            content = "";
        tv.setText(content);
    }

    /**
     * 设置TextView 值
     * @param tv
     * @param content
     */
    protected void setText(TextView tv, int content) {
        tv.setText(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
