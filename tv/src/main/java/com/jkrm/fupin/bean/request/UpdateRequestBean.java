package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/21.
 */

public class UpdateRequestBean {

    public static final String SYSTEM_ANDROID_MOBILE = "1";
    public static final String SYSTEM_ANDROID_BOX = "2";
    public static final String SYSTEM_ANDROID_IOS = "3";

    //"1":Android 版本,"2":IOS_VERSION, "3":盒子版
    private String versionType;

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }
}
