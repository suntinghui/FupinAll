package com.jkrm.fupin.bean;

import android.graphics.Bitmap;

/**
 * Created by hzw on 2018/10/27.
 */

public class OssListLocalConvertBean {

    private String prefix;
    private Bitmap bitmap;
    private boolean isFolder;
    private boolean isInDb;
    private String status;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public boolean isInDb() {
        return isInDb;
    }

    public void setInDb(boolean inDb) {
        isInDb = inDb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OssListLocalConvertBean{" +
                "prefix='" + prefix + '\'' +
                ", bitmap=" + bitmap +
                ", isFolder=" + isFolder +
                ", isInDb=" + isInDb +
                ", status='" + status + '\'' +
                '}';
    }
}
