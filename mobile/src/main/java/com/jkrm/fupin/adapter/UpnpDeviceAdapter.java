package com.jkrm.fupin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.upnp.dmp.DeviceItem;

import java.util.List;

/**
 * Created by hzw on 2018/8/2.
 */

public class UpnpDeviceAdapter extends ArrayAdapter<DeviceItem> {

    private LayoutInflater mInflater;
    private List<DeviceItem> deviceItems;
    public UpnpDeviceAdapter(Context context, int textViewResourceId, List<DeviceItem> objects) {
        super(context, textViewResourceId, objects);
        this.mInflater = LayoutInflater.from(context);
        this.deviceItems = objects;
    }

    public int getCount() {
        return this.deviceItems.size();
    }

    public DeviceItem getItem(int paramInt) {
        return this.deviceItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        DevHolder holder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.item_upnp_device, null);
            holder = new DevHolder();
            holder.tv_upnpDeviceName = ((TextView) view
                    .findViewById(R.id.tv_upnpDeviceName));
            view.setTag(holder);
        } else {
            holder = (DevHolder) view.getTag();
        }

        DeviceItem item = deviceItems.get(position);
        holder.tv_upnpDeviceName.setText(item.toString());
        return view;
    }

    public final class DevHolder {
        public TextView tv_upnpDeviceName;
    }

}
