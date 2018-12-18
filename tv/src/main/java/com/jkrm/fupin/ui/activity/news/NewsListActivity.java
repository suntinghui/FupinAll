package com.jkrm.fupin.ui.activity.news;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.NewsListAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.MoreNewsListResultBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.request.MoreNewsInfoRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.NewsListContract;
import com.jkrm.fupin.mvp.presenters.NewsListPresenter;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/20.
 */

public class NewsListActivity extends BaseMvpActivity<NewsListPresenter> implements NewsListContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    private NewsBean mNewsBean;
    private MoreNewsInfoRequestBean mRequestBean;
    private List<NoticesListBean> mList;
    private NewsListAdapter mAdapter;
    private int currentPage = 1;
    private int pageSize = 50;
    private int totalNum = 1; //总记录数
    private int totalPage = 1;

    @Override
    protected int getContentId() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void initData() {
        super.initData();
        initRcv();
        mNewsBean = (NewsBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
        mTvTitleName.setText("资讯 · " + mNewsBean.getTypeName());
        createRequestBean();
        mPresenter.getMoreNewsInfoList(mRequestBean);
//        mList = mNewsBean.getNoticesList();

    }

    @Override
    protected void initListener() {
        super.initListener();
        mRcvData.addOnScrollListener(mOnScrollListener);
    }

    private void initRcv() {
        mAdapter = new NewsListAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NoticesListBean bean = mList.get(position);
//                showMsg("点击项名称: " + bean.getTitle());
                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                toClass(NewsDetailActivity.class, bundle, false);
            }
        });
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                if(MyUtil.isEmptyList(mList)) {
                    return;
                }
                if(currentPage < totalPage) {
                    currentPage++;
                    createRequestBean();
                    mPresenter.getMoreNewsInfoList(mRequestBean);
                } else {
                    showMsg("数据已全部加载完成");
                }

            }
        }
    };

    private void createRequestBean() {
        if(mRequestBean == null) {
            mRequestBean = new MoreNewsInfoRequestBean();
            mRequestBean.setCp(currentPage);
            mRequestBean.setPs(pageSize);
            mRequestBean.setMid(mNewsBean.getTypeId());
        } else {
            mRequestBean.setCp(currentPage);
        }
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    public void getMoreNewsInfoListSuccess(MoreNewsListResultBean bean) {
        if (bean != null) {
            if(currentPage == 1) {
                mList = new ArrayList<>();
            }
            totalNum = bean.getTotalNum();
            totalPage = totalNum % pageSize == 0 ? totalNum/pageSize : (totalNum/pageSize + 1);
            MyLog.d("总页数: " + totalPage + " ,总数据量: " + totalNum);
            mList.addAll(bean.getNoticesList());
            mAdapter.addAllData(mList);
            MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
        } else {
            if(currentPage == 1) {
                MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
            }
        }
    }

    @Override
    public void getMoreNewsInfoListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }
}
