package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/19.
 */

public class SearchPublishTimeBean {

    private String id;
    private String publishTime;
    private boolean isSelected;
    private boolean hasFocus;

    public boolean isHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "SearchPublishTimeBean{" +
                "id='" + id + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
