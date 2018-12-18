package com.jkrm.fupin.interfaces;

import com.jkrm.fupin.upnp.dmp.DeviceItem;

/**
 * Created by hzw on 2018/8/29.
 */

public interface IUpnpDeviceChangeListener {

    void deviceUpdate(DeviceItem deviceItem, boolean isAdd);
}
