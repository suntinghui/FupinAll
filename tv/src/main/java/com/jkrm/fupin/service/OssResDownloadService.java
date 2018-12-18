package com.jkrm.fupin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.PollingUtils;
import com.jkrm.fupin.util.RefreshUtil;

/**
 * Created by hzw on 2018/10/16.
 */

public class OssResDownloadService extends Service implements Handler.Callback {

    private MyApp mApp;
    private Handler mHandler;
    private static final int MSG_CHECKDB_SUCCESS = 0;
    private static final int MSG_CHECKDB_FAIL = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("OssResDownloadService onCreate");
        mApp = MyApp.getInstance();
        mHandler = new Handler(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        MyLog.d("OssResDownloadService onStart");
//        getLocalDbDownloadFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("OssResDownloadService onStartCommand");
        getLocalDbDownloadFile();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MyLog.d("OssResDownloadService onDestroy");
//        PollingUtils.stopPollingService(mApp, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);
        super.onDestroy();
    }

    /**
     * 获取本地下载中或已下载完成的文件
     */
    private void getLocalDbDownloadFile() {
        MyLog.d("OssResDownloadService DB 检测本地数据库开始");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mApp.localDbDataList = DaoCacheFileUtil.getInstance().queryAllBean();
                    mHandler.sendEmptyMessage(MSG_CHECKDB_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_CHECKDB_FAIL);
                }
                MyLog.d("OssResDownloadService DB 检测本地数据库结束");
            }
        }).start();
    }



    private void toDownload() {
        if(!PermissionUtil.hasStoragePermission(mApp)) {
            if(MyUtil.isAllowHint()) {
                Toast.makeText(mApp, "无存储权限，无法下载资源", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if(MyUtil.isEmptyList(mApp.localDbDataList)) {
            MyLog.d("OssResDownloadService 数据库为空");
            return;
        }
        for(final CacheFileBean cacheFileBean : mApp.localDbDataList) {
            if(cacheFileBean.getProgress() != 100 && !TextUtils.isEmpty(cacheFileBean.getUrl())) {
                MyLog.d("OssResDownloadService 文件: " + cacheFileBean.getFileName() + "未完成, 开始下载");
                FileDownloadUtil.get().down(mApp, cacheFileBean, new FileDownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String path) {
                        MyLog.d("OssResDownloadService 文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                        LocalBroadcastManager.getInstance(mApp).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_SUCCESS));
                        MyUtil.refreshLocalDbDownloadFile();
                        RefreshUtil.refershUpnpRes();
                    }

                    @Override
                    public void onDownloading(int progress) {
                        MyLog.d("OssResDownloadService 当前下载进度: " + progress + "%");
                        LocalBroadcastManager.getInstance(mApp).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_PROGRESS));
                    }

                    @Override
                    public void onDownloadFailed(String path) {
                        MyLog.d("OssResDownloadService 文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败");
                        LocalBroadcastManager.getInstance(mApp).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_FAIL));
                        MyUtil.refreshLocalDbDownloadFile();
                    }

                    @Override
                    public void onDownloadIngRejectRepeat() {
                        MyLog.d("OssResDownloadService 视频 " + cacheFileBean.getFileName() + " 已下载或正在下载, 不重复下载了");
                    }
                });
                if(!TextUtils.isEmpty(cacheFileBean.getImgUrl())) {
                    FileDownloadUtil.get().downImg(mApp, cacheFileBean.getImgUrl(), cacheFileBean.getVideoId(), new FileDownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(String path) {
                            if(MyUtil.isAllowHint()) {
                                MyUtil.showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                            }
                            MyLog.d("OssResDownloadService 文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                            MyUtil.refreshLocalDbDownloadFile();
                        }

                        @Override
                        public void onDownloading(int progress) {
                            MyLog.d("当前下载进度: " + progress + "%");
                        }

                        @Override
                        public void onDownloadFailed(String path) {
                            if(MyUtil.isAllowHint()) {
                                MyUtil.showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败");
                            }
                            MyLog.d("OssResDownloadService 文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败");
                            MyUtil.refreshLocalDbDownloadFile();
                        }

                        @Override
                        public void onDownloadIngRejectRepeat() {
                            MyLog.d("OssResDownloadService 视频缩略图 " + cacheFileBean.getFileName() + " 已下载或正在下载, 不重复下载了");
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CHECKDB_SUCCESS:
                MyLog.d("OssResDownloadService DB check success");
                toDownload();
                break;
            case MSG_CHECKDB_FAIL:
                MyLog.d("OssResDownloadService DB check fail");
                if(MyUtil.isAllowHint()) {
                    Toast.makeText(mApp, "检测下载文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }
}
