package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/14.
 */

public class UpdateBean {

    private String version;//当前版本
    private String id;
    private String mobile_phone_system;//类型
    private String url; //下载地址
    private String now_versions_content;//描述
    private String update_time;//时间
    private boolean is_latest;
    private int is_force_update;//是否强制跟新  1强制, 0不强制

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_phone_system() {
        return mobile_phone_system;
    }

    public void setMobile_phone_system(String mobile_phone_system) {
        this.mobile_phone_system = mobile_phone_system;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNow_versions_content() {
        return now_versions_content;
    }

    public void setNow_versions_content(String now_versions_content) {
        this.now_versions_content = now_versions_content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean is_latest() {
        return is_latest;
    }

    public void setIs_latest(boolean is_latest) {
        this.is_latest = is_latest;
    }

    public boolean isIs_latest() {
        return is_latest;
    }

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "version='" + version + '\'' +
                ", id='" + id + '\'' +
                ", mobile_phone_system='" + mobile_phone_system + '\'' +
                ", url='" + url + '\'' +
                ", now_versions_content='" + now_versions_content + '\'' +
                ", update_time='" + update_time + '\'' +
                ", is_latest=" + is_latest +
                ", is_force_update=" + is_force_update +
                '}';
    }
}
