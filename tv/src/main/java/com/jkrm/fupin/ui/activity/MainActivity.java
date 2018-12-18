package com.jkrm.fupin.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.MainTopAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.interfaces.IMainTopListener;
import com.jkrm.fupin.mvp.contracts.MainContract;
import com.jkrm.fupin.mvp.presenters.MainPresenter;
import com.jkrm.fupin.ui.fragment.HomepageFragment;
import com.jkrm.fupin.ui.fragment.VodTypeFragment;
import com.jkrm.fupin.util.APPUtils;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.RecyclerViewNoBugLinearLayoutManager;
import com.jkrm.fupin.widgets.VideoPlayPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jkrm.fupin.util.APPUtils.TYPE_VERSION.TYPE_VERSION_NAME;

/**
 * Created by hzw on 2018/8/8.
 */

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View, IMainTopListener {

    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_me)
    TextView mTvMe;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.fl_fragment)
    FrameLayout mFlFragment;
    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
//    TvRecyclerView mRcvData;

    private List<VodTypeBean> mList = new ArrayList<>();
    private MainTopAdapter mTopAdapter;
    private int preFocuPosition = 0;
    private RecyclerViewNoBugLinearLayoutManager layoutManager;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mRcvData.requestFocus();
    }

    @Override
    protected void initData() {
        super.initData();
        mTvVersion.setText(getString(R.string.app_name) + " V" + APPUtils.getAppVersionInfo(mActivity, TYPE_VERSION_NAME));
        mTopAdapter = new MainTopAdapter(this);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(mActivity);
//        mTopAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        MyUtil.setRecyclerViewLinearlayoutHorizontal(mActivity, mRcvData, mTopAdapter, layoutManager, false, true);
//        mRcvData.setSelectPadding(32, 30, 32, 35);
        mPresenter.getVodTypeList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClass(SearchActivity.class, null, false);
            }
        });
        mTvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClass(MeActivity.class, null, false);
            }
        });
    }

    @Override
    public void getVodTypeListSuccess(List<VodTypeBean> list) {
        if(mList == null)
            mList = new ArrayList<>();
        VodTypeBean vodTypeBean = new VodTypeBean();
        vodTypeBean.setId("-1");
        vodTypeBean.setName("精选");
        vodTypeBean.setHasFocus(true);
        mList.add(vodTypeBean);
//        if(list != null) {
//            for(int i=0; i<list.size(); i++) {
//                VodTypeBean bean = list.get(i);
//                MyLog.d("getVodTypeListSuccess bean: " + bean.toString());
//            }
//        }
        mList.addAll(list);
        mTopAdapter.addAllData(mList);
        replaceFragment(R.id.fl_fragment, new HomepageFragment(), true);
    }

    @Override
    public void getVodTypeListFail(String msg) {

    }

    @Override
    public void getAudioTypeListSuccess(List<VodTypeBean> list) {

    }

    @Override
    public void getAudioTypeListFail(String msg) {

    }

    @Override
    public void getHomePageVideoListSuccess(HomePageListBean bean) {

    }

    @Override
    public void getHomePageVideoListFail(String msg) {

    }

    @Override
    public void replaceTopFragment(VodTypeBean bean, int position) {
        preFocuPosition = position;
        for(int i=0; i<mList.size(); i++) {
            TextView tv_mainTopTitle = (TextView) layoutManager.getChildAt(i).findViewById(R.id.tv_mainTopTitle);
            if(i == position) {
                tv_mainTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tv_mainTopTitle.setTextColor(getResources().getColor(R.color.white));
            }

        }
        if("-1".equals(bean.getId())) {
            replaceFragment(R.id.fl_fragment, new HomepageFragment(), true);
        } else {
            SearchBean searchBean = new SearchBean();
            searchBean.setCp(1);
            searchBean.setPs(100);
            searchBean.setVodClassifyId(bean.getId());
            searchBean.setKeywords("");
            searchBean.setTimeType("");
            searchBean.setDistrict("");
            searchBean.setOrderCol("");
            replaceFragment(R.id.fl_fragment, VodTypeFragment.getInstance(searchBean), true);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                MyLog.d("焦点 ok确认键");
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                MyLog.d("焦点 ok中心键");
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                MyLog.d("焦点 下键");
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                MyLog.d("焦点 左键");
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                MyLog.d("焦点 右键");
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                MyLog.d("焦点 上键");
                break;

            case KeyEvent.KEYCODE_MENU:
                MyLog.d("焦点 目录键");
                break;

            case KeyEvent.KEYCODE_BACK:
                MyLog.d("焦点 返回键");
                onBackPressed();
                return true;

            case KeyEvent.KEYCODE_HOME:
                MyLog.d("焦点 HOME键");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("dispatchKeyEvent","dispatchKeyEvent(), action=" + event.getAction() + " keycode=" + event.getKeyCode());
        return super.dispatchKeyEvent(event);
    }
}
