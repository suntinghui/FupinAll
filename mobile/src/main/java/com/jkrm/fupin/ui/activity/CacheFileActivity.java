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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.jkrm.fupin.widgets.CustomToolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by hzw on 2018/8/13.
 */

public class CacheFileActivity extends BaseMvpActivity<CacheFilePresenter> implements CacheFileContract.View, Handler.Callback{

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_noData)
    TextView mTvNoData;
    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    @BindView(R.id.btn_selectAll)
    Button mBtnSelectAll;
    @BindView(R.id.ll_edit)
    LinearLayout mLlEdit;
    @BindView(R.id.line_bottom)
    View mLineBottom;

    private List<CacheFileBean> mList = new ArrayList<>();
    private CacheFileAdapter mAdapter;
    private Handler mHandler;
    private boolean isInit = true;
    private long recevieTime;
    private boolean isSelectAll = false;

    @Override
    protected int getContentId() {
        return R.layout.activity_cache_file;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar.setToolbarTitle(getString(R.string.title_cache_file));
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.setRightText("编辑");
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                finish();
            }
        });
        mToolbar.setOnRightClickListener(new CustomToolbar.OnRightClickListener() {
            @Override
            public void onRightTextClick() {
                if(mToolbar.getRightText().equals("编辑")) {
                    mToolbar.setRightText("完成");
                    setEditStatus(true);
                } else {
                    mToolbar.setRightText("编辑");
                    setEditStatus(false);
                    setListSelected(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler(this);
        mAdapter = new CacheFileAdapter();
        MyUtil.setRecyclerViewLinearlayoutWithDecorationHeight(mActivity, mRcvData, mAdapter, 1, true);
        mPresenter.queryAllBeans();
//        getLocalVideoFile();
        registerDownloadReceiver();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelectAll) {
                    setListSelected(false);
                } else {
                    setListSelected(true);
                }
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                boolean hasChcek = false;
                for(CacheFileBean tempBean : mList) {
                    if(tempBean.isCheck()) {
                        hasChcek = true;
                    }
                }
                if(!hasChcek) {
                    showDialog("请先选择需要删除的视频文件!");
                    dismissLoadingDialog();
                    return;
                }

                if(MyUtil.isEmptyList(mList)) {
                    dismissLoadingDialog();
                    return;
                }
                for (Iterator it = mList.iterator(); it.hasNext(); ) {
                    CacheFileBean bean = (CacheFileBean) it.next();
                    if(bean.isCheck()) {
//                        if(bean.getProgress() != 100) {
//                            showDialog("视频 " + bean.getFile().getName() + " 正在下载, 目前不允许执行删除操作");
//                            continue;
//                        }
                        FileDownloadUtil.get().deleteCacheFile(mActivity, bean);
                        it.remove();
                    }
                }
                mAdapter.addAllData(mList);
                isSelectAll = false;
                setSelectAllText();
                setDeleteText();
                dismissLoadingDialogDelay();
                if(MyUtil.isEmptyList(mList)) {
                    showEmptyView(false);
                    mToolbar.hideRightView();
                    setEditStatus(false);
                } else {
                    showEmptyView(true);
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CacheFileBean bean = mAdapter.getData().get(position);
                if(bean.getProgress() != 100) {
                    showDialog("该视频正在下载中, 请耐心等待下载完成再观看");
                    return;
                }
                Bundle bundle = new Bundle();
                File file = new File(bean.getFilePath());
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, file);
                toClass(VideoPlayActivity.class, bundle, false);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_select:
                        if (view.isSelected()) {
                            view.setSelected(false);
                            CacheFileBean bean = (CacheFileBean) adapter.getData().get(position);
                            bean.setCheck(false);
                            isSelectAll = false;
                        } else {
                            view.setSelected(true);
                            CacheFileBean bean = (CacheFileBean) adapter.getData().get(position);
                            bean.setCheck(true);
                        }
                        setSelectAllText();
                        setDeleteText();
                        break;
                }
            }
        });
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
                    if (MyUtil.isVideoType(fileName)) {
                        CacheFileBean cacheFileBean = new CacheFileBean();
                        cacheFileBean.setFile(tempFile);
                        cacheFileBean.setCheck(false);
                        if(!downloading(fileName)) {
                            cacheFileBean.setProgress(100);
                        }
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
        showView(mTvNoData, !hasData);
        if(!hasData) {
            mToolbar.hideRightView();
            setEditStatus(false);
            showView(mBtnDelete, false);
        } else {
            if(mAdapter.isEdit) {
                mToolbar.setRightText("完成");
                setEditStatus(true);
            } else {
                mToolbar.setRightText("编辑");
                setEditStatus(false);
            }
        }
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
    protected void onDestroy() {
        if(mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void queryAllBeansSuccess(List<CacheFileBean> list) {
        mList = list;
        if(MyUtil.isEmptyList(mList)) {
            showEmptyView(false);
        } else {
            showEmptyView(true);
            mAdapter.addAllData(mList);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    showEmptyView(true);
//                    mAdapter.addAllData(mList);
//                }
//            }, isInit ? 0 : 5000);
            for(CacheFileBean cacheFileBean : mList) {
                MyLog.d("缓存list bean: " + cacheFileBean.toString());
            }
        }
    }

    @Override
    public void queryAllBeansFail(String msg) {

    }

    private void setEditStatus(boolean isEdit) {
        mAdapter.setIsEdit(isEdit);
        showView(mLlEdit, isEdit);
        showView(mLineBottom, isEdit);
    }

    private void setDeleteText() {
        int selectSize = 0;
        for(int i=0; i<mList.size(); i++) {
            CacheFileBean bean = mList.get(i);
            if(bean.isCheck()) {
                selectSize++;
            }
        }
        if(selectSize != 0) {
            setText(mBtnDelete, "删除(" + selectSize + ")");
        } else {
            setText(mBtnDelete, "删除");
        }
    }

    private void setSelectAllText() {
        if(MyUtil.isEmptyList(mList)) {
            mBtnSelectAll.setEnabled(false);
            setText(mBtnSelectAll, "全选");
        } else {
            mBtnSelectAll.setEnabled(true);
        }
        if(isSelectAll) {
            setText(mBtnSelectAll, "取消全选");
        } else {
            setText(mBtnSelectAll, "全选");
        }
    }

    private void setListSelected(boolean selectAllFlag) {
        showLoadingDialog();
        for(int i=0; i<mList.size(); i++) {
            CacheFileBean bean = mList.get(i);
            bean.setCheck(selectAllFlag);
        }
        this.isSelectAll = selectAllFlag;
        setSelectAllText();
        setDeleteText();
        mAdapter.addAllData(mList);
        dismissLoadingDialogDelay();
    }
}
