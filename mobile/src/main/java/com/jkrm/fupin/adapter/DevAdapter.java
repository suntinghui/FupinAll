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

public class DevAdapter extends ArrayAdapter<DeviceItem> {

    private static final String TAG = "DeviceAdapter";

    private LayoutInflater mInflater;

    public int dmrPosition = 0;

    private List<DeviceItem> deviceItems;

    public DevAdapter(Context context, int textViewResourceId,
                      List<DeviceItem> objects) {
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
            view = this.mInflater.inflate(R.layout.dmr_item, null);
            holder = new DevHolder();
            holder.filename = ((TextView) view
                    .findViewById(R.id.dmr_name_tv));
            holder.checkBox = ((CheckBox) view.findViewById(R.id.dmr_cb));
            view.setTag(holder);
        } else {
            holder = (DevHolder) view.getTag();
        }

        DeviceItem item = (DeviceItem) this.deviceItems.get(position);
        holder.filename.setText(item.toString());
        if (null != MyApp.deviceItem
                && MyApp.deviceItem.equals(item)) {
            holder.checkBox.setChecked(true);
        } else if (null != MyApp.dmrDeviceItem
                && MyApp.dmrDeviceItem.equals(item)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        return view;
    }

    public final class DevHolder {
        public TextView filename;
        public CheckBox checkBox;
    }

}
