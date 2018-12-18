package com.jkrm.fupin.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.io.File;

/**
 * Created by hzw on 2018/8/16.
 */
@Entity
public class CacheFileBean {

    @Id(autoincrement = true)
    private Long id;
    @Transient
    private File file;
    @Transient
    private boolean isCheck;
    private int progress;
    @NotNull
    private String url;
    private String fileName;
    private long fileSize;
    private long currentSize;
    private String filePath;
    private String videoId;
    private String imgUrl;
    @Generated(hash = 1350706129)
    public CacheFileBean() {
    }
    @Generated(hash = 1560709125)
    public CacheFileBean(Long id, int progress, @NotNull String url,
            String fileName, long fileSize, long currentSize, String filePath,
            String videoId, String imgUrl) {
        this.id = id;
        this.progress = progress;
        this.url = url;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.currentSize = currentSize;
        this.filePath = filePath;
        this.videoId = videoId;
        this.imgUrl = imgUrl;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public boolean getIsCheck() {
        return this.isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "CacheFileBean{" +
                "id=" + id +
                ", file=" + file +
                ", isCheck=" + isCheck +
                ", progress=" + progress +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", currentSize=" + currentSize +
                ", filePath='" + filePath + '\'' +
                ", videoId='" + videoId + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
