package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/9/6.
 */

public class MoreNewsInfoRequestBean {

    private int cp;//第几页
    private int ps;//每页几条
    private String mid;//资讯类别id

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "MoreNewsInfoRequestBean{" +
                "cp='" + cp + '\'' +
                ", ps='" + ps + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
