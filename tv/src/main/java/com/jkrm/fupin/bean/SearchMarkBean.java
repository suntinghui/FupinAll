package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/27.
 */

public class SearchMarkBean {

    private String code;
    private String mark;
    private boolean isSelected;
    private boolean hasFocus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    @Override
    public String toString() {
        return "SearchMarkBean{" +
                "code='" + code + '\'' +
                ", mark='" + mark + '\'' +
                ", isSelected=" + isSelected +
                ", hasFocus=" + hasFocus +
                '}';
    }
}

