package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/14.
 */

public class OssTokenBeanNew {

    private String appBucket;
    private String appEndPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
    private String dir;

    public String getAppBucket() {
        return appBucket;
    }

    public void setAppBucket(String appBucket) {
        this.appBucket = appBucket;
    }

    public String getAppEndPoint() {
        return appEndPoint;
    }

    public void setAppEndPoint(String appEndPoint) {
        this.appEndPoint = appEndPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "OssTokenBeanNew{" +
                "appBucket='" + appBucket + '\'' +
                ", appEndPoint='" + appEndPoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", securityToken='" + securityToken + '\'' +
                ", expiration='" + expiration + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}
