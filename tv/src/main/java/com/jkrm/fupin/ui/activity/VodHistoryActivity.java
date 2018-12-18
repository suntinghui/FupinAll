package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.VodHistoryAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.VodHistoryBean;
import com.jkrm.fupin.bean.request.VodHistoryRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.VodHistoryContract;
import com.jkrm.fupin.mvp.presenters.VodHistoryPresenter;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/13.
 */

public class VodHistoryActivity extends BaseMvpActivity<VodHistoryPresenter> implements VodHistoryContract.View, Handler.Callback {
    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_noData)
    TextView mTvNoData;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    private static final int MSG_CHECK_WATCH_PERCENT_START = 0;
    private static final int MSG_CHECK_WATCH_PERCENT_FINISH = 1;
    private Handler mHandler;
    private List<VodHistoryBean> mList = new ArrayList<>();
    private VodHistoryAdapter mAdapter;

    @Override
    protected int getContentId() {
        return R.layout.activity_vod_history;
    }

    @Override
    protected VodHistoryPresenter createPresenter() {
        return new VodHistoryPresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText(R.string.title_vod_history);
        mHandler = new Handler(this);
        initRcv();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList = new ArrayList<>();
        VodHistoryRequestBean requestBean = new VodHistoryRequestBean(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "0"));
        mPresenter.getVodHistoryList(requestBean);
    }

    private void initRcv() {
        mAdapter = new VodHistoryAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VodHistoryBean bean = mList.get(position);
//                showMsg("点击项名称: " + bean.getVtitle());
                if (TextUtils.isEmpty(bean.getVossPath())) {
                    showMsg("当前视频URL地址为空, 无法播放");
                    return;
                }
                //                Intent intent = new Intent("com.aliyun.vodplayerview.activity.player");
                //                startActivity(intent);

                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                toClass(MediaPlayActivity.class, bundle, false);
            }
        });
    }

    @Override
    public void getVodHistoryListSuccess(List<VodHistoryBean> list) {
        mList.addAll(list);
        if (mList != null && mList.size() > 0) {
            //目前不需要显示已播放进度百分比
            mAdapter.addAllData(mList);
            showEmptyView(true);
//            mHandler.sendEmptyMessage(MSG_CHECK_WATCH_PERCENT_START);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(VodHistoryBean bean : mList) {
//                        int totalDurationInt = VideoUtil.getVideoDurationInt(bean.getVossPath());
//                        MyLog.d("所有视频的长度分别是: " + totalDurationInt);
//                        String watchtime = bean.getWatchtime();
//                        int watchtimeInt = VideoUtil.convertStringTimeToLong(watchtime);
//                        NumberFormat nt = NumberFormat.getPercentInstance();
//                        //设置百分数精确度2即保留两位小数
//                        nt.setMinimumFractionDigits(2);
//                        float baifen = (float)watchtimeInt/totalDurationInt;
//                        String percent = MyUtil.toDdecimal(baifen * 100);
//                        if("0.00".equals(percent))
//                            percent = "0.01";
//                        bean.setPercent("已观看" + percent + "%");
//                        MyLog.d("所有视频的已观看长度分别是: " + watchtimeInt + " ,百分比: " + baifen + " ,转换后: " + percent + "%");
//                    }
//                    mHandler.sendEmptyMessage(MSG_CHECK_WATCH_PERCENT_FINISH);
//                }
//            }).start();
        } else {
            showEmptyView(false);
        }
    }

    @Override
    public void getVodHistoryListFail(String msg) {
        showEmptyView(false);
    }

    private void showEmptyView(boolean hasData) {
        showView(mTvNoData, !hasData);
        showView(mRcvData, hasData);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CHECK_WATCH_PERCENT_START:
                showLoadingDialog();
                break;
            case MSG_CHECK_WATCH_PERCENT_FINISH:
                dismissLoadingDialog();
                mAdapter.addAllData(mList);
                showEmptyView(true);
                break;
        }
        return false;
    }
}
