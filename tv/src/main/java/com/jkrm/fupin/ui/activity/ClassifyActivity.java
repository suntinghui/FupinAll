package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.VodTypeAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.SearchContract;
import com.jkrm.fupin.mvp.presenters.SearchPresenter;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hzw on 2018/8/16.
 */

public class ClassifyActivity extends BaseMvpActivity<SearchPresenter> implements SearchContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    private List<HomePageBean> mList = new ArrayList<>();
    private VodTypeAdapter mAdapter;
    private VodTypeBean mVodTypeBean;
    private SearchBean mSearchBean;
    private int currentPage = 1;
    private int pageSize = 50;
    private int totalNum = 1; //总记录数
    private int totalPage = 1;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initData() {
        super.initData();
        mVodTypeBean = (VodTypeBean) getIntent().getSerializableExtra(MyConstants.Params.COMMON_PARAMS_BEAN);
        mTvTitleName.setText(MyUtil.isTextNull(mVodTypeBean.getName()) ? "分类视频" : mVodTypeBean.getName());
        createSearchBean();
        initRcv();
        mPresenter.searchVodList(mSearchBean);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRcvData.addOnScrollListener(mOnScrollListener);
    }

    private void initRcv() {
        mAdapter = new VodTypeAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageBean bean = mList.get(position);
//                showMsg("点击项名称: " + bean.getTitle());
                if (TextUtils.isEmpty(bean.getOsskey())) {
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
    public void searchVodListSuccess(SearchResultBean bean) {
        if (bean != null) {
            if(currentPage == 1) {
                mList = new ArrayList<>();
            }
            totalNum = bean.getTotalNum();
            totalPage = totalNum % pageSize == 0 ? totalNum/pageSize : (totalNum/pageSize + 1);
            MyLog.d("总页数: " + totalPage + " ,总数据量: " + totalNum);
            mList.addAll(bean.getVodList());
            mAdapter.addAllData(mList);
            MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
        } else {
            if(currentPage == 1) {
                MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
            }
        }
    }

    @Override
    public void searchVodListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                    createSearchBean();
                    mPresenter.searchVodList(mSearchBean);
                } else {
                    showMsg("数据已全部加载完成");
                }

            }
        }
    };

    private void createSearchBean() {
        if(mSearchBean == null) {
            mSearchBean = new SearchBean();
            mSearchBean.setCp(currentPage);
            mSearchBean.setPs(pageSize);
            mSearchBean.setVodClassifyId(mVodTypeBean.getId());
            mSearchBean.setKeywords("");
            mSearchBean.setTimeType("");
            mSearchBean.setDistrict("");
            mSearchBean.setOrderCol("");
        } else {
            mSearchBean.setCp(currentPage);
        }
    }
}
