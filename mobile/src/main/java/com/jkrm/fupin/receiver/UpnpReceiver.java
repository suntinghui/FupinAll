package com.jkrm.fupin.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.upnp.dmp.DeviceItem;

/**
 * Created by hzw on 2018/8/29.
 */

public class UpnpReceiver extends BroadcastReceiver {

    private Activity mActivity;
    private IUpnpDeviceChangeListener mListener;

    public UpnpReceiver(Activity activity, IUpnpDeviceChangeListener listener) {
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(MyConstants.Action.ACTION_DMS_REMOVE.equals(intent.getAction())) {
            DeviceItem deviceItem = (DeviceItem) intent.getSerializableExtra(MyConstants.Params.COMMON_PARAMS_BEAN);
            mListener.deviceUpdate(deviceItem, false);
        } else if(MyConstants.Action.ACTION_DMS_ADD.equals(intent.getAction())) {
            DeviceItem deviceItem = (DeviceItem) intent.getSerializableExtra(MyConstants.Params.COMMON_PARAMS_BEAN);
            mListener.deviceUpdate(deviceItem, true);
        } else if(MyConstants.Action.ACTION_DMS_DATA_GET_FAIL.equals(intent.getAction())) {
            mListener.deviceUpdate(null, false);
        }
    }


}
