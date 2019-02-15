package com.jkrm.fupin.annex;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;

import com.jkrm.fupin.util.MyLog;
import com.koolpos.download.bean.DownloadBean;
import com.koolpos.download.bean.DownloadMsgBean;
import com.koolpos.download.constants.DownloadConstant;
import com.koolpos.download.manager.KoolDownloadManager;

import java.io.File;

/**
 * Created by hzw on 2018/5/19.
 */

public class DownloadUtil {

    public static void downloadFile(Context context, String url, String name) {
        MyLog.d("downloadFile url: " + url);
        String savePath = getDiskExternalCacheDir(context);
        if(savePath == null)
            return;
//        String name = url.substring(url.lastIndexOf(File.separator), url.length());
        savePath = savePath + File.separator + name;
        MyLog.d("downloadFile savePath: " + savePath);
        if(fileIsExists(savePath)) {
            MyLog.d("downloadFile 文件已存在, 不下载了");
            sendDownloadSuccessMsg(context, name, savePath);
            return;
        }
        DownloadBean downloadBean = new DownloadBean(url, savePath);
        KoolDownloadManager.getInstance().startDownload(context, downloadBean);
    }

    private static void sendDownloadSuccessMsg(Context context, String fileName, String filePath) {
        if(context == null)
            return;
        DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(fileName);
        mDownloadMsgBean.setFilePath(filePath);
        Intent mIntent = new Intent();
        mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_SUCCESS);
        mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
        context.sendBroadcast(mIntent);
    }

    private static String getDiskExternalCacheDir(Context context) {

        File mFile = context.getExternalCacheDir();
        if (Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(mFile))) {
            return mFile.getAbsolutePath();
        }
        return null;
    }

    private static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
