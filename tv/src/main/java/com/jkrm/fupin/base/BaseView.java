package com.jkrm.fupin.base;

/**
 * Created by hzw on 2018/7/18.
 */
public interface BaseView {

    void showLoadingDialog();
    void dismissLoadingDialog();
    void showMsg(String msg);
}
