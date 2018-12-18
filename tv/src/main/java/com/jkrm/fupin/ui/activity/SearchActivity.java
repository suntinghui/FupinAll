package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.VodTypeAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.SearchAddressBean;
import com.jkrm.fupin.bean.SearchPublishTimeBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.SearchContract;
import com.jkrm.fupin.mvp.presenters.SearchPresenter;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.NetworkUtil;
import com.jkrm.fupin.widgets.SearchPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/13.
 */

public class SearchActivity extends BaseMvpActivity<SearchPresenter> implements SearchContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    private List<HomePageBean> mList = new ArrayList<>();
    private VodTypeAdapter mAdapter;

    private SearchBean mSearchBean;
    private String key;
    private int currentPage = 1;
    private int pageSize = 50;
    private int totalNum = 1; //总记录数
    private int totalPage = 1;

    @Override
    protected int getContentId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText(R.string.title_vod_search);
        createSearchBean();
        initRcv();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRcvData.addOnScrollListener(mOnScrollListener);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    key = mEtSearch.getText().toString().trim();
                    mSearchBean.setKeywords(key);
                    mPresenter.searchVodList(mSearchBean);
                    return true;
                }
                return false;
            }
        });
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
                }
            }
        }
    };

    private void createSearchBean() {
        if(mSearchBean == null) {
            mSearchBean = new SearchBean();
            mSearchBean.setCp(currentPage);
            mSearchBean.setPs(pageSize);
            mSearchBean.setVodClassifyId("");
            mSearchBean.setKeywords("");
            mSearchBean.setTimeType("");
            mSearchBean.setDistrict("");
            mSearchBean.setOrderCol("");
            mSearchBean.setMark("");
        } else {
            mSearchBean.setCp(currentPage);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
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
                if(!NetworkUtil.isNetworkAvailable(mActivity)) {
                    showMsg("当前网络异常，无法筛选");
                    return false;
                }
                if(mSearchBean != null) {
                    MyUtil.hideSoftInput(mActivity);
                    MyUtil.showSearchPopupWindow(mActivity, mSearchBean, mTvTitleName, new SearchPopupWindow.OnItemClickListener() {
                        @Override
                        public void onClickMark(String id) {
                            mSearchBean.setMark(id);
                            currentPage = 1;
                            mPresenter.searchVodList(mSearchBean);
                        }

                        @Override
                        public void onClickClassify(String id) {
                            mSearchBean.setVodClassifyId(id);
                            currentPage = 1;
                            mPresenter.searchVodList(mSearchBean);
                        }

                        @Override
                        public void onClickPublishTime(String id) {
                            mSearchBean.setTimeType(id);
                            currentPage = 1;
                            mPresenter.searchVodList(mSearchBean);
                        }

                        @Override
                        public void onClickAddress(String id) {
                            mSearchBean.setDistrict(id);
                            currentPage = 1;
                            mPresenter.searchVodList(mSearchBean);
                        }

                    });
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
