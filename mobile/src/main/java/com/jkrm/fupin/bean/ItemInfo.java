package com.jkrm.fupin.bean;

import android.graphics.Bitmap;

import com.alibaba.sdk.android.vod.upload.common.UploadStateType;
import com.jkrm.fupin.constants.MyConstants;

/**
 * Created by Leigang on 16/11/7.
 */
public class ItemInfo {
    private String file;
    private long progress;
    private String oss;
    private String status;
    private Bitmap mBitmap;
    private String ossName;

    public String getOss() {
        return oss;
    }

    public void setOss(String oss) {
        this.oss = oss;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String title) {
        this.file = title;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getOssName() {
        return ossName;
    }

    public void setOssName(String ossName) {
        this.ossName = ossName;
    }

    public String getStatusString() {
        String statusString = "待上传";
        if(status == null)
            return statusString;
        if(status.equals(MyConstants.Upload.STATUS_BACK_INIT)) {
            statusString = MyConstants.Upload.STATUS_INIT;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_UPLOADING)) {
            statusString = MyConstants.Upload.STATUS_UPLOADING;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_SUCCESS)) {
            statusString = MyConstants.Upload.STATUS_SUCCESS;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_FAIlURE)) {
            statusString = MyConstants.Upload.STATUS_FAIlURE;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_CANCELED)) {
            statusString = MyConstants.Upload.STATUS_BACK_CANCELED;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_PAUSING)) {
            statusString = MyConstants.Upload.STATUS_PAUSING;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_PAUSED)) {
            statusString = MyConstants.Upload.STATUS_PAUSED;
        } else if(status.equals(MyConstants.Upload.STATUS_BACK_DELETED)) {
            statusString = MyConstants.Upload.STATUS_DELETED;
        }
        return statusString;
    }
}
