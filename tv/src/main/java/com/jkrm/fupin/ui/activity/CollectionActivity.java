package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.CollectionAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.CollectionInfoListBean.CollectionsModelBean;
import com.jkrm.fupin.bean.request.CollectionRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.CollectionContract;
import com.jkrm.fupin.mvp.presenters.CollectionPresenter;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/20.
 */

public class CollectionActivity extends BaseMvpActivity<CollectionPresenter> implements CollectionContract.View{

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    private List<CollectionsModelBean> mList = new ArrayList<>();
    private CollectionAdapter mAdapter;

    @Override
    protected CollectionPresenter createPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText("我的收藏");
        initRcv();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CollectionRequestBean requestBean = new CollectionRequestBean();
        requestBean.setCreateuser(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "-1"));
        mPresenter.getUserCollectionInfoList(requestBean);
    }

    private void initRcv() {
        mAdapter = new CollectionAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mAdapter, true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionsModelBean bean = mList.get(position);
                if (TextUtils.isEmpty(bean.getVodModel().getOsspath())) {
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
    public void getUserCollectionInfoListSuccess(CollectionInfoListBean bean) {
        mList = bean.getCollectionsModel();
        mAdapter.addAllData(mList);
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mList));
    }

    @Override
    public void getUserCollectionInfoListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
    }
}
