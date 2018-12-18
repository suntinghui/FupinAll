package com.jkrm.fupin.util;

import com.jkrm.fupin.service.UpnpService;

/**
 * Created by hzw on 2018/10/16.
 */

public class RefreshUtil {

    public static void refershUpnpRes() {
        MyLog.d("RefreshUtil 刷新upnp资源 --- 暂不刷新, 没多大用处, 需重启盒子才可能生效");
        if(UpnpService.instance != null) {
            UpnpService.instance.prepareMediaServerNew();
        }
//        PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_UPNP_SERVICE, UpnpService.class, MyConstants.Action.ACTION_SERVICE_CHECK_UPNP);

    }
}
