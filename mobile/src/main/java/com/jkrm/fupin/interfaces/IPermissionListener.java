package com.jkrm.fupin.interfaces;

/**
 * Created by hzw on 2018/7/20.
 */

public interface IPermissionListener {

    /**
     * 权限通过
     */
    void granted();

    /**
     * 已拒绝权限
     */
    void shouldShowRequestPermissionRationale();

    /**
     * 设置了拒绝且不再提醒
     */
    void needToSetting();
}
