package com.jkrm.fupin.ui.activity.upnp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.UpnpDeviceAdapter;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.receiver.UpnpReceiver;
import com.jkrm.fupin.ui.activity.MainActivity;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUpnpUtil;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.CustomToolbar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 媒体共享页面
 * Created by hzw on 2018/8/2.
 */

public class UpnpDmrActivity extends BaseActivity implements IUpnpDeviceChangeListener {

    @BindView(R.id.lv_data)
    ListView mLvData;
    @BindView(R.id.tv_noDmsSource)
    TextView mTvNoDmsSource;

    private ArrayList<DeviceItem> mDevList = new ArrayList<DeviceItem>();
    private UpnpDeviceAdapter mDevAdapter;
    private UpnpReceiver mUpnpReceiver;

    @Override
    protected int getContentId() {
        return R.layout.activity_upnp_device_dms;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar.setToolbarTitle(getString(R.string.title_box_source));
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.hideRightView();
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        MyUpnpUtil.registerReceiver(mActivity, mUpnpReceiver, this);

        mDevList = MyApp.getmDevList();
        showOrHideEmptyView();
        init();
    }

    private void init() {
        mDevAdapter = new UpnpDeviceAdapter(mActivity, 0, mDevList);
        mLvData.setAdapter(mDevAdapter);
        mLvData.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                MyApp.deviceItem = mDevList.get(position);
                mDevAdapter.notifyDataSetChanged();
                toClass(UpnpContentActivity.class, null, false);
            }
        });

    }

    @Override
    public void deviceUpdate(DeviceItem deviceItem, boolean isAdd) {
        if(null == deviceItem) {
            showMsg("数据源数据更新，请重新加载数据源");
            finish();
        } else {
            if(!isAdd) {
                DeviceItem deviceItemCurrent = MyApp.deviceItem;
                if(deviceItemCurrent != null && deviceItemCurrent.getUdn().equals(deviceItem.getUdn())) {
                    showMsg("请检查数据源是否已关闭");
                    finish();
                }
            }
        }
    }

    private void showOrHideEmptyView() {
        if(MyUtil.isEmptyList(mDevList)) {
            showView(mTvNoDmsSource, true);
            showView(mLvData, false);
            mDevList = new ArrayList<>();
        } else {
            showView(mTvNoDmsSource, false);
            showView(mLvData, true);
        }
    }

    @Override
    protected void onDestroy() {
        MyUpnpUtil.unRegisterReceiver(mActivity, mUpnpReceiver);
        super.onDestroy();
    }
}
