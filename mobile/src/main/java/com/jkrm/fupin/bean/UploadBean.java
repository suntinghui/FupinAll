package com.jkrm.fupin.bean;

import android.graphics.Bitmap;

import com.jkrm.fupin.constants.MyConstants;

/**
 * Created by hzw on 2018/8/14.
 */

public class UploadBean {
    private String fileName; //文件名 序列号+时间戳
    private String normalFileName; //原文件名, 未更改序号加时间戳前
    private long progress; //上传进度
    private String status; //上传状态
    private Bitmap thumbnail; //缩略图
    private String thumbnailPath; //缩略图本地路径
    private String thumbnailName; //缩略图新命名 序列号+时间戳
    private String filePath; //上传文件路径
    private String ossName; //oss文件名
    private String ossImgName; //oss文件名
    private String duration; //视频时长
    private String metatype; //视频类型

    public String getUploadStatus() {
        status = MyConstants.Upload.STATUS_INIT;
        if(progress == 0) {
        } else if(progress == 100) {
            status = MyConstants.Upload.STATUS_SUCCESS;
        } else {
            status = MyConstants.Upload.STATUS_UPLOADING;
        }
        return status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getOssName() {
        return ossName;
    }

    public void setOssName(String ossName) {
        this.ossName = ossName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public String getNormalFileName() {
        return normalFileName;
    }

    public void setNormalFileName(String normalFileName) {
        this.normalFileName = normalFileName;
    }

    public String getMetatype() {
        return metatype;
    }

    public void setMetatype(String metatype) {
        this.metatype = metatype;
    }

    public String getOssImgName() {
        return ossImgName;
    }

    public void setOssImgName(String ossImgName) {
        this.ossImgName = ossImgName;
    }

    @Override
    public String toString() {
        return "UploadBean{" +
                "fileName='" + fileName + '\'' +
                ", normalFileName='" + normalFileName + '\'' +
                ", progress=" + progress +
                ", status='" + status + '\'' +
                ", thumbnail=" + thumbnail +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", thumbnailName='" + thumbnailName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", ossName='" + ossName + '\'' +
                ", duration='" + duration + '\'' +
                ", metatype='" + metatype + '\'' +
                '}';
    }
}
