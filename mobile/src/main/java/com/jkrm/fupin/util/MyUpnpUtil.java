package com.jkrm.fupin.util;

import android.app.Activity;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.receiver.UpnpReceiver;
import com.jkrm.fupin.upnp.dmp.DeviceItem;

/**
 * Created by hzw on 2018/8/29.
 */

public class MyUpnpUtil {

    public static void registerReceiver(Activity activity, UpnpReceiver upnpReceiver, IUpnpDeviceChangeListener listener) {
        upnpReceiver = new UpnpReceiver(activity, listener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstants.Action.ACTION_DMS_ADD);
        intentFilter.addAction(MyConstants.Action.ACTION_DMS_REMOVE);
        intentFilter.addAction(MyConstants.Action.ACTION_DMS_DATA_GET_FAIL);
        LocalBroadcastManager.getInstance(activity).registerReceiver(upnpReceiver, intentFilter);
    }

    public static void unRegisterReceiver(Activity activity, UpnpReceiver receiver) {
        if(receiver != null)
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }
}
