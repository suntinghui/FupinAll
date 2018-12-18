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
import android.view.View;

import com.jkrm.fupin.MainActivity;
import com.jkrm.fupin.R;
import com.jkrm.fupin.ui.activity.LoginActivity;
import com.jkrm.fupin.ui.activity.MainActivity2;
import com.jkrm.fupin.ui.activity.SplashActivity;
import com.jkrm.fupin.util.ActivityCollectorUtil;
import com.jkrm.fupin.util.MyLog;
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
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        if(-1 != getContentId())
            setContentView(getContentId());
        mActivity = this;
        ActivityCollectorUtil.registry(mActivity);
        unbinder = ButterKnife.bind(this);
        setToolbar();
        initView();
        initData();
        initListener();
    }

    @Override
    public void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(mActivity);
                }
                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
            }
        });

    }

    @Override
    public void dismissLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    protected void showDialog(String msg) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg);
    }


    protected void showDialog(String title, String msg, View.OnClickListener listener) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, listener);
    }

    protected void showDialogWithCancelBtn(String title, String msg, View.OnClickListener confirmListener) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, confirmListener, true);
    }

    protected void showDialogWithCancelBtn(String title, String msg, View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
        dismissDialog();
        mDialog = new MyDialog(mActivity, msg, true, confirmListener, cancelListener);
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

    public boolean isViewVisiable(View view) {
        if(view != null) {
            return view.getVisibility() == View.VISIBLE;
        }
        return false;
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
        ActivityCollectorUtil.release();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyLog.d("onBackPressed 退出System.exit 延时3秒 供关闭服务时间");
                System.exit(0);
            }
        }, 3000);
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
            int colorStatusBar = getResources().getColor(R.color.color_E0E0E0);
            if(mToolbar != null) {
                int color = getResources().getColor(R.color.color_FFFFFF);
                mToolbar.setBackgroundColor(color);
            }
            StatusBarUtil.setColor(mActivity, colorStatusBar, 0);//0 透明 255 黑色
        }
    }

    @Override
    public void onBackPressed() {
        if(mActivity instanceof SplashActivity || mActivity instanceof MainActivity || mActivity instanceof LoginActivity || mActivity instanceof MainActivity2) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//                exitAppForced();
                Intent intent = new Intent();
                // 为Intent设置Action、Category属性
                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                startActivity(intent);
//                super.onBackPressed();
                return;
            } else {
                showMsg("再次点击返回键退出");
            }
            mBackPressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
