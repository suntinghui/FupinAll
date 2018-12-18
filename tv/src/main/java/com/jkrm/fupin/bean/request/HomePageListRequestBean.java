package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/9/14.
 */

public class HomePageListRequestBean {

    private int cp;
    private int ps;
    private String keywords;
    private String vodClassifyId;

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVodClassifyId() {
        return vodClassifyId;
    }

    public void setVodClassifyId(String vodClassifyId) {
        this.vodClassifyId = vodClassifyId;
    }
}
