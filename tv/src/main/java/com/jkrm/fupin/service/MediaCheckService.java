package com.jkrm.fupin.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PollingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/10/16.
 */

public class MediaCheckService extends Service {

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
        sendCheckMediaStoreBroad();
    }

    @Override
    public void onDestroy() {
        PollingUtils.stopPollingService(mApp, MediaCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_MEDIA);
        super.onDestroy();
    }

    private void sendCheckMediaStoreBroad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyLog.d("MeidaCheck 检测本地数据库开始");
                if(MyUtil.isEmptyList(MyApp.localSaveDiskPath)) {
                    MyLog.d("MeidaCheck 检测本地媒体库结束, 硬盘不存在");
                    return;
                }
                List<File> sxfpFileList = new ArrayList<>();
                for(int i=0; i<MyApp.localSaveDiskPath.size(); i++) {
                    File file = MyApp.localSaveDiskPath.get(i);
                    File newFile = new File(file.getAbsoluteFile() + MyConstants.Constant.DISK_DIRECTORY);
                    MyLog.d("MeidaCheck upnp service sendCheckMediaStoreBroad: newFile Path: " + newFile.getAbsolutePath());
                    sxfpFileList.add(newFile);
                    File appFile = new File(file.getAbsoluteFile() + MyConstants.Constant.DISK_DIRECTORY_APP);
                    MyLog.d("MeidaCheck upnp service sendCheckMediaStoreBroad: newFile Path: " + appFile.getAbsolutePath());
                    sxfpFileList.add(appFile);
                }
                for(File file : sxfpFileList) {
                    Uri contentUri = Uri.fromFile(file);
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
                    sendBroadcast(mediaScanIntent);
                    MyLog.d("MeidaCheck upnp service sendCheckMediaStoreBroad 检测路径 uri: " + contentUri);
                }
                MyLog.d("MeidaCheck 检测本地媒体库结束");
            }
        }).start();
    }
}
