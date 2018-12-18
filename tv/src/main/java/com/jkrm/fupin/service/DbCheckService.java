package com.jkrm.fupin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.PollingUtils;

/**
 * Created by hzw on 2018/10/16.
 */

public class DbCheckService extends Service {

    private MyApp mApp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = MyApp.getInstance();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        getLocalDbDownloadFile();
    }

    @Override
    public void onDestroy() {
        PollingUtils.stopPollingService(mApp, DbCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_DB);
        super.onDestroy();
    }

    /**
     * 获取本地下载中或已下载完成的文件
     */
    private void getLocalDbDownloadFile() {
        MyLog.d("DB 检测本地数据库开始");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mApp.localDbDataList = DaoCacheFileUtil.getInstance().queryAllBean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyLog.d("DB 检测本地数据库结束");
            }
        }).start();
    }
}
