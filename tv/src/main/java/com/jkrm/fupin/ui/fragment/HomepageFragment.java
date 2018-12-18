package com.jkrm.fupin.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.HomeFragmentAdapter;
import com.jkrm.fupin.base.BaseMvpFragment;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.MainFragmentContract;
import com.jkrm.fupin.mvp.presenters.MainFragmentPresenter;
import com.jkrm.fupin.ui.activity.MediaPlayActivity;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/9.
 */

public class HomepageFragment extends BaseMvpFragment<MainFragmentPresenter> implements MainFragmentContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.rcv_data2)
    RecyclerView mRcvData2;
    private HomePageListBean mHomePageListBean;
    private List<HomePageBean> mLatestList = new ArrayList<>();
    private List<HomePageBean> mHeatList = new ArrayList<>();
    private HomeFragmentAdapter mLatestAdapter, mHeatAdapter;

    @Override
    protected MainFragmentPresenter createPresenter() {
        return new MainFragmentPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initData() {
        super.initData();
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
                if(TextUtils.isEmpty(bean.getOsskey())) {
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

    private void initRcv2() {
        mHeatAdapter = new HomeFragmentAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData2, mHeatAdapter, true);
        mHeatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageBean bean = mHeatList.get(position);
//                showMsg("点击项名称: " + bean.getTitle());
                if(TextUtils.isEmpty(bean.getOsskey())) {
                    showMsg("当前视频URL地址为空, 无法播放");
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
        if(bean != null) {
            mLatestList = mHomePageListBean.getLatestVideoList();
            mHeatList = mHomePageListBean.getHeatVideoList();
        }
        mLatestAdapter.addAllData(mLatestList);
        mHeatAdapter.addAllData(mHeatList);

    }

    @Override
    public void getHomePageVideoListFail(String msg) {

    }
}
