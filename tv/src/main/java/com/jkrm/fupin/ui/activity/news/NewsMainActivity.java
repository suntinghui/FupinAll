package com.jkrm.fupin.ui.activity.news;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.NewsListAdapter;
import com.jkrm.fupin.adapter.NewsMainAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.NewsContract;
import com.jkrm.fupin.mvp.presenters.NewsPresenter;
import com.jkrm.fupin.ui.activity.MediaPlayActivity;
import com.jkrm.fupin.util.MyUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/20.
 */

public class NewsMainActivity extends BaseMvpActivity<NewsPresenter> implements NewsContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    private List<NewsBean> mList;
    private NewsMainAdapter mAdapter;

    @Override
    protected NewsPresenter createPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_news_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText("资讯列表");
        initRcv();
        mPresenter.getNewsList();
    }

    private void initRcv() {
        mAdapter = new NewsMainAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true, 6);
//        MyUtil.setRecyclerViewLinearlayoutWithDecorationHeight(mActivity, mRcvData, mAdapter, 0.5F, true);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item:
                        NewsBean bean = mList.get(position);
                        //                showMsg("点击项名称: " + bean.getTypeName());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                        toClass(NewsListActivity.class, bundle, false);
                        break;
                }
            }
        });
    }

    @Override
    public void getNewsListSuccess(List<NewsBean> list) {
        mList = list;
        mAdapter.addAllData(mList);
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }

    @Override
    public void getNewsListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }
}
