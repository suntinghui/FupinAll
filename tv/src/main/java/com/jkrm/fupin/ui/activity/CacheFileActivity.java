package com.jkrm.fupin.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.CacheFileAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.CacheFileContract;
import com.jkrm.fupin.mvp.presenters.CacheFilePresenter;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.RefreshUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by hzw on 2018/8/20.
 */

public class CacheFileActivity extends BaseMvpActivity<CacheFilePresenter> implements CacheFileContract.View, Handler.Callback {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    @BindView(R.id.tv_hideView)
    TextView mTvHideView;

    private List<CacheFileBean> mList = new ArrayList<>();
    private CacheFileAdapter mAdapter;
    private Handler mHandler;
    private long recevieTime;

    @Override
    protected int getContentId() {
        return R.layout.activity_cache_file;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvHideView.requestFocus();
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText("下载历史");
        mHandler = new Handler(this);
        initRcv();
        mPresenter.queryAllBeans();
        registerDownloadReceiver();
//        getLocalVideoFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CacheFileBean bean = mAdapter.getData().get(position);
                if(bean.getProgress() != 100) {
                    showDialog("资源正在下载中, 请耐心等待下载完成再观看");
                    return;
                }
                if(MyUtil.isImageType(bean.getUrl())) {
                    showDialog("该资源为图片，暂不支持大图浏览");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                toClass(MediaPlayActivity.class, bundle, false);
            }
        });
    }

    private void initRcv() {
        mAdapter = new CacheFileAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true);
    }

    private void getLocalVideoFile() {
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = getExternalFilesDir("");
                MyLog.d("离线缓存路径: " + file.getAbsolutePath());
                if (!file.exists()) {
                    mHandler.sendEmptyMessage(0);
                    return;
                }
                for (File tempFile : file.listFiles()) {
                    String fileName = tempFile.getName();
                    if (MyUtil.checkVideoType(fileName)) {
                        CacheFileBean cacheFileBean = new CacheFileBean();
                        cacheFileBean.setFile(tempFile);
                        cacheFileBean.setCheck(false);
                        mList.add(cacheFileBean);
                    }
                }
                MyLog.d("离线缓存文件总数: " + mList.size());
                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void showEmptyView(boolean hasData) {
        showView(mRcvData, hasData);
        showView(mTvEmptyView, !hasData);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mList != null && mList.size() > 0) {
            showEmptyView(true);
            mAdapter.addAllData(mList);
        } else {
            showEmptyView(false);
        }
        dismissLoadingDialog();
        return false;
    }

    private boolean downloading(String fileName) {
        // 获取所有键值对对象的集合
        Set<Map.Entry<String, Call>> set = MyApp.downCalls.entrySet();
        // 遍历键值对对象的集合，得到每一个键值对对象
        for (Map.Entry<String, Call> entry : set) {
            // 根据键值对对象获取键和值
            String key = entry.getKey();
            Call value = entry.getValue();
            if(MyFileUtil.getFileNameWithType(key).equals(fileName)) {
                return true;
            }
        }
        return false;

    }

    private void registerDownloadReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(MyConstants.Action.ACTION_DOWNLOAD_PROGRESS);
        localIntentFilter.addAction(MyConstants.Action.ACTION_DOWNLOAD_SUCCESS);
        localIntentFilter.addAction(MyConstants.Action.ACTION_DOWNLOAD_FAIL);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mBroadcastReceiver, localIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent intent) {
            if (intent.getAction().equals(MyConstants.Action.ACTION_DOWNLOAD_PROGRESS)) {
                if(System.currentTimeMillis() - recevieTime > 5000) {
                    recevieTime = System.currentTimeMillis();
                    mPresenter.queryAllBeans();
                }
            } else if(intent.getAction().equals(MyConstants.Action.ACTION_DOWNLOAD_SUCCESS)
                    || intent.getAction().equals(MyConstants.Action.ACTION_DOWNLOAD_FAIL)) {
                mPresenter.queryAllBeans();
            }
        }
    };

    @Override
    protected CacheFilePresenter createPresenter() {
        return new CacheFilePresenter(this);
    }

    @Override
    public void queryAllBeansSuccess(List<CacheFileBean> list) {
        mTvHideView.requestFocus();
        mList = list;
        if(MyUtil.isEmptyList(mList)) {
            showEmptyView(false);
        } else {
            showEmptyView(true);
            mAdapter.addAllData(mList);
            for(CacheFileBean cacheFileBean : mList) {
                MyLog.d("缓存list bean: " + cacheFileBean.toString());
            }
        }
    }

    @Override
    public void queryAllBeansFail(String msg) {

    }

    @Override
    protected void onDestroy() {
        if(mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                break;

            case KeyEvent.KEYCODE_MENU:
                for(int i=0; i<mList.size(); i++) {
                    if(mRcvData.getLayoutManager().getChildAt(i).hasFocus()) {
                        final CacheFileBean cacheFileBean = mList.get(i);
                        showDialogWithCancelBtn("提示", "确认删除该下载记录吗?", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FileDownloadUtil.get().deleteCacheFile(mActivity, cacheFileBean);
                                MyUtil.refreshLocalDbDownloadFile();
                                dismissDialog();
                                mPresenter.queryAllBeans();
                                RefreshUtil.refershUpnpRes();
                            }
                        });
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
