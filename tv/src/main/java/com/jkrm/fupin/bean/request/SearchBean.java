package com.jkrm.fupin.bean.request;

import java.io.Serializable;

/**
 * Created by hzw on 2018/8/14.
 */

public class SearchBean implements Serializable{

    private int cp; //当前页，默认 1
    private int ps; //每页显示数量，默认 10
    private String keywords; //模糊检索关键字
    private String vodClassifyId; //视频类别，对应视频分类的ID字段
    private String timeType; //视频发布时间(空：全部 1：最近一周 2：最近一月)
    private String district; //所属地区(0：省内 1：省外),默认全部
    private String orderCol; //排序，默认按时间降序排序，如果是搜索最热视频的话，该字段必须填写，任意字符串就行。
    private String mark; //0视频 1音频

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

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getOrderCol() {
        return orderCol;
    }

    public void setOrderCol(String orderCol) {
        this.orderCol = orderCol;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "cp=" + cp +
                ", ps=" + ps +
                ", keywords='" + keywords + '\'' +
                ", vodClassifyId='" + vodClassifyId + '\'' +
                ", timeType='" + timeType + '\'' +
                ", district='" + district + '\'' +
                ", orderCol='" + orderCol + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
