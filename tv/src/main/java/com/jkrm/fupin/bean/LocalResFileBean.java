package com.jkrm.fupin.bean;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by hzw on 2018/10/16.
 */

public class LocalResFileBean {

    private File file;
    private Bitmap bitmap;
    private String originName;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
