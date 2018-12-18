package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/13.
 */

public class VodHistoryRequestBean {

    private String userid;

    public VodHistoryRequestBean(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
