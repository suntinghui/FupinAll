package com.jkrm.fupin.ui.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.interfaces.IUpdateCancelInterface;
import com.jkrm.fupin.mvp.contracts.UpdateContract;
import com.jkrm.fupin.mvp.presenters.UpdatePresenter;
import com.jkrm.fupin.util.APPUtils;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.UpdateUtil;
import com.jkrm.fupin.util.VersionUtil;

/**
 * Created by hzw on 2018/8/8.
 */

public class SplashActivity extends BaseMvpActivity<UpdatePresenter> implements UpdateContract.View {

    @Override
    protected int getContentId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getLatestAppVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected UpdatePresenter createPresenter() {
        return new UpdatePresenter(this);
    }

    @Override
    public void getLatestAppVersionSuccess(UpdateBean bean) {
        bean.setIs_latest(true);
        bean.setIs_force_update(0);
        if(TextUtils.isEmpty(bean.getUrl())) {
            toNext();
            return;
        }
        if(VersionUtil.compareVersions(APPUtils.getAppVersionInfo(mActivity, APPUtils.TYPE_VERSION.TYPE_VERSION_NAME), bean.getVersion()) > 0) {
            MyLog.d("update 需更新");
            UpdateUtil.handleUpdate(mActivity, bean, new IUpdateCancelInterface() {
                @Override
                public void cancelUpdate() {
                    toNext();
                }
            });
        } else {
            MyLog.d("update 不需更新");
            toNext();
        }
    }

    @Override
    public void getLatestAppVersionFail(String msg) {
        toNext();
    }

    private void toNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toClass(MainActivity.class, null, true);
            }
        }, 500);
    }
}
