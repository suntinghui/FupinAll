package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/19.
 */

public class SearchAddressBean {

    private String id;
    private String address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SearchAddressBean{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
