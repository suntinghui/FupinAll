package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.HomeFragmentAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.MainFragmentContract;
import com.jkrm.fupin.mvp.presenters.MainFragmentPresenter;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 精选视频/音频
 * Created by hzw on 2018/8/16.
 */

public class ChosenActivity extends BaseMvpActivity<MainFragmentPresenter> implements MainFragmentContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.rcv_data2)
    RecyclerView mRcvData2;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    @BindView(R.id.tv_titleLatest)
    TextView mTvTitleLatest;
    @BindView(R.id.tv_titleHeat)
    TextView mTvTitleHeat;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_emptyView2)
    TextView mTvEmptyView2;
    private HomePageListBean mHomePageListBean;
    private List<HomePageBean> mLatestList = new ArrayList<>();
    private List<HomePageBean> mHeatList = new ArrayList<>();
    private HomeFragmentAdapter mLatestAdapter, mHeatAdapter;

    private String mark;

    @Override
    protected MainFragmentPresenter createPresenter() {
        return new MainFragmentPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_chosen;
    }

    @Override
    protected void initData() {
        super.initData();
        mark = getIntent().getExtras().getString(MyConstants.Params.COMMON_PARAMS);
        mTvTitleName.setText(isVideoRes() ? "视频精选" : "音频精选");
        mTvTitleLatest.setText(isVideoRes() ? "最新视频" : "最新音频");
        mTvTitleHeat.setText(isVideoRes() ? "最热视频" : "最热音频");
        initRcv1();
        initRcv2();
        mPresenter.getHomePageVideoList();
    }

    private void initRcv1() {
        mLatestAdapter = new HomeFragmentAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mLatestAdapter, true);
        mLatestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageBean bean = mLatestList.get(position);
//                showMsg("点击项名称: " + bean.getTitle());
                if (TextUtils.isEmpty(bean.getOsskey())) {
                    showMsg("当前资源URL地址为空, 无法播放");
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

    private void initRcv2() {
        mHeatAdapter = new HomeFragmentAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData2, mHeatAdapter, true);
        mHeatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageBean bean = mHeatList.get(position);
//                showMsg("点击项名称: " + bean.getTitle());
                if (TextUtils.isEmpty(bean.getOsskey())) {
                    showMsg("当前资源URL地址为空, 无法播放");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                toClass(MediaPlayActivity.class, bundle, false);
            }
        });
    }


    @Override
    public void getHomePageVideoListSuccess(HomePageListBean bean) {
        mHomePageListBean = bean;
        if (bean != null) {
            if(isVideoRes()) {
                mLatestList = mHomePageListBean.getLatestVideoList();
                mHeatList = mHomePageListBean.getHeatVideoList();
            } else {
                mLatestList = mHomePageListBean.getLatestAudioList();
                mHeatList = mHomePageListBean.getHeatAudioList();
            }
        }
        mLatestAdapter.addAllData(mLatestList);
        mHeatAdapter.addAllData(mHeatList);
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mLatestList));
        MyUtil.showEmptyView(mTvEmptyView2, mRcvData2, !MyUtil.isEmptyList(mHeatList));

    }

    @Override
    public void getHomePageVideoListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mLatestList));
        MyUtil.showEmptyView(mTvEmptyView2, mRcvData2, !MyUtil.isEmptyList(mHeatList));
    }

    private boolean isVideoRes() {
        return MyConstants.Constant.RES_VIDEO_MARK.equals(mark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
