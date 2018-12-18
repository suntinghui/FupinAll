package com.jkrm.fupin.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.VodTypeAdapter;
import com.jkrm.fupin.base.BaseMvpFragment;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.SearchContract;
import com.jkrm.fupin.mvp.presenters.SearchPresenter;
import com.jkrm.fupin.ui.activity.MediaPlayActivity;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/9.
 */

public class VodTypeFragment extends BaseMvpFragment<SearchPresenter> implements SearchContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    private List<HomePageBean> mList = new ArrayList<>();
    private VodTypeAdapter mAdapter;

    private SearchBean mSearchBean;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_vodtype;
    }

    public static VodTypeFragment getInstance(SearchBean searchBean) {
        VodTypeFragment fragment = new VodTypeFragment();
        fragment.mSearchBean = searchBean;
        return fragment;
    }


    @Override
    protected void initData() {
        super.initData();
        initRcv();
        mPresenter.searchVodList(mSearchBean);
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
        if(bean != null) {
            mList.addAll(bean.getVodList());
            mAdapter.addAllData(mList);
        }
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }

    @Override
    public void searchVodListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
    }
}
