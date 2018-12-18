package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/6.
 */

public class OssTokenBean {

    private String accessid;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private String expire;
    private String appBucket;
    private String appEndPoint;
    private String appExpire;

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

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

    public String getAppExpire() {
        return appExpire;
    }

    public void setAppExpire(String appExpire) {
        this.appExpire = appExpire;
    }

    @Override
    public String toString() {
        return "OssTokenBean{" +
                "accessid='" + accessid + '\'' +
                ", policy='" + policy + '\'' +
                ", signature='" + signature + '\'' +
                ", dir='" + dir + '\'' +
                ", host='" + host + '\'' +
                ", expire='" + expire + '\'' +
                ", appBucket='" + appBucket + '\'' +
                ", appEndPoint='" + appEndPoint + '\'' +
                ", appExpire='" + appExpire + '\'' +
                '}';
    }
}
