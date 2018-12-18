package com.jkrm.fupin.widgets;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.SearchAddressAdapter;
import com.jkrm.fupin.adapter.SearchClassifyAdapter;
import com.jkrm.fupin.adapter.SearchMarkAdapter;
import com.jkrm.fupin.adapter.SearchPublishTimeAdapter;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.SearchAddressBean;
import com.jkrm.fupin.bean.SearchMarkBean;
import com.jkrm.fupin.bean.SearchPublishTimeBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/8/15.
 */
public class SearchPopupWindow extends PopupWindow {
    private Activity mActivity;
    private OnItemClickListener mOnItemClickListener;
    private View contentView;
    private SearchMarkAdapter mSearchMarkAdapter;
    private SearchClassifyAdapter mSearchClassifyAdapter;
    private SearchPublishTimeAdapter mSearchPublishTimeAdapter;
    private SearchAddressAdapter mSearchAddressAdapter;
    private List<SearchMarkBean> mSearchMarkList;
    private List<VodTypeBean> mVodTypeBeanList;
    private List<SearchPublishTimeBean> mPublishTimeList;
    private List<SearchAddressBean> mAddressList;

    public SearchPopupWindow(final Activity mActivity, final SearchBean searchBean, final OnItemClickListener listener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.mOnItemClickListener = listener;
        contentView = View.inflate(mActivity, R.layout.view_popupwindow_search, null);
        RecyclerView rcv_dataMark = (RecyclerView) contentView.findViewById(R.id.rcv_dataMark);
        RecyclerView rcv_dataClassifY = (RecyclerView) contentView.findViewById(R.id.rcv_dataClassifY);
        RecyclerView rcv_dataPubishTime = (RecyclerView) contentView.findViewById(R.id.rcv_dataPubishTime);
        RecyclerView rcv_dataAddress = (RecyclerView) contentView.findViewById(R.id.rcv_dataAddress);

        mSearchMarkAdapter = new SearchMarkAdapter();
        mSearchClassifyAdapter = new SearchClassifyAdapter();
        mSearchPublishTimeAdapter = new SearchPublishTimeAdapter();
        mSearchAddressAdapter = new SearchAddressAdapter();

        MyUtil.setRecyclerViewLinearlayoutHorizontal(mActivity, rcv_dataMark, mSearchMarkAdapter, false, true);
        MyUtil.setRecyclerViewLinearlayoutHorizontal(mActivity, rcv_dataClassifY, mSearchClassifyAdapter, false, true);
        MyUtil.setRecyclerViewLinearlayoutHorizontal(mActivity, rcv_dataPubishTime, mSearchPublishTimeAdapter, false, true);
        MyUtil.setRecyclerViewLinearlayoutHorizontal(mActivity, rcv_dataAddress, mSearchAddressAdapter, false, true);

        createMarkList();
        refreshMarkData(searchBean);
        createVodTypeList(searchBean, true);
        refreshClassifyData(searchBean);
        createPublishTimeList();
        refreshPublishTimeData(searchBean);
        createAddressList();
        refreshAddressData(searchBean);

        mSearchMarkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchMarkBean bean = mSearchMarkAdapter.getItem(position);
                mOnItemClickListener.onClickMark(bean.getCode());
                refreshMarkData(searchBean);
                createVodTypeList(searchBean, false);
                refreshClassifyData(searchBean);
            }
        });

        mSearchClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VodTypeBean bean = mSearchClassifyAdapter.getItem(position);
                mOnItemClickListener.onClickClassify(bean.getId());
                refreshClassifyData(searchBean);
            }
        });
        mSearchPublishTimeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchPublishTimeBean bean = mSearchPublishTimeAdapter.getItem(position);
                mOnItemClickListener.onClickPublishTime(bean.getId());
                refreshPublishTimeData(searchBean);
            }
        });
        mSearchAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchAddressBean bean = mSearchAddressAdapter.getItem(position);
                mOnItemClickListener.onClickAddress(bean.getId());
                refreshAddressData(searchBean);
            }
        });

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.sytle_anim_pop_in_out);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.update();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setOverlapAnchor(true);
        }
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_NORMAL;
                mActivity.getWindow().setAttributes(lp);
            }
        });


    }

    /**
     * 展示窗口
     */
    public void show(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        this.showAsDropDown(parent, 0, 0);
    }

    public void showAsLocation(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        int windowPos[] = MyUtil.calculatePopWindowPos(parent, contentView);
        int xOff = 20;// 可以自己调整偏移
        windowPos[0] -= xOff;
        this.showAtLocation(parent, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public void showAsLocationBottom(View parent) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = MyConstants.Popupwindow.POPUPWINDOW_DARK;
        mActivity.getWindow().setAttributes(lp);
        this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * calendar菜单选择监听接口
     */
    public interface OnItemClickListener {
        void onClickMark(String id);
        void onClickClassify(String id);
        void onClickPublishTime(String id);
        void onClickAddress(String id);
    }

    private void refreshMarkData(SearchBean searchBean) {
        for(SearchMarkBean bean : mSearchMarkList) {
            if(bean.getCode().equals(searchBean.getMark())) {
                bean.setSelected(true);
            } else {
                bean.setSelected(false);
            }
        }
        mSearchMarkAdapter.addAllData(mSearchMarkList);
    }

    private void refreshClassifyData(SearchBean searchBean) {
        for(VodTypeBean temp : mVodTypeBeanList) {
            if(temp.getId().equals(searchBean.getVodClassifyId())) {
                temp.setSelected(true);
            } else {
                temp.setSelected(false);
            }
        }
        mSearchClassifyAdapter.addAllData(mVodTypeBeanList);
    }

    private void refreshPublishTimeData(SearchBean searchBean) {
        for(SearchPublishTimeBean bean : mPublishTimeList) {
            if(bean.getId().equals(searchBean.getTimeType())) {
                bean.setSelected(true);
            } else {
                bean.setSelected(false);
            }
        }
        mSearchPublishTimeAdapter.addAllData(mPublishTimeList);
    }

    private void refreshAddressData(SearchBean searchBean) {
        for(SearchAddressBean bean : mAddressList) {
            if(bean.getId().equals(searchBean.getDistrict())) {
                bean.setSelected(true);
            } else {
                bean.setSelected(false);
            }
        }
        mSearchAddressAdapter.addAllData(mAddressList);
    }

    /**
     * 设置菜单监听
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private List<VodTypeBean> createVodTypeList(SearchBean searchBean, boolean isInit) {
        mVodTypeBeanList = new ArrayList<>();

        if("0".equals(searchBean.getMark())) {
            //视频
            checkVodTypeList(MyApp.vodTypeBeanList);
            mVodTypeBeanList = MyApp.vodTypeBeanList;
        } else if("1".equals(searchBean.getMark())) {
            //音频
            checkVodTypeList(MyApp.audTypeBeanList);
            mVodTypeBeanList = MyApp.audTypeBeanList;
        } else {
            //全部
            checkVodTypeList(MyApp.vodTypeBeanList);
            checkVodTypeList(MyApp.audTypeBeanList);
            mVodTypeBeanList.addAll(MyApp.vodTypeBeanList);
            mVodTypeBeanList.addAll(MyApp.audTypeBeanList);
        }
        VodTypeBean vodTypeBean = new VodTypeBean();
        vodTypeBean.setId("");
        vodTypeBean.setName("全部");
        if(isInit) {
            vodTypeBean.setHasFocus(true);
        }
        mVodTypeBeanList.add(0, vodTypeBean);
        return mVodTypeBeanList;
    }

    private void checkVodTypeList(List<VodTypeBean> list) {
        if(MyUtil.isEmptyList(list))
            return;
        if(list.get(0).getId().equals("")) {
            list.remove(0);
        }
    }

    private List<SearchMarkBean> createMarkList() {
        mSearchMarkList = new ArrayList<>();
        SearchMarkBean searchMarkBean = new SearchMarkBean();
        searchMarkBean.setCode("");
        searchMarkBean.setMark("全部");

        SearchMarkBean searchMarkBean2 = new SearchMarkBean();
        searchMarkBean2.setCode("0");
        searchMarkBean2.setMark("视频");

        SearchMarkBean searchMarkBean3 = new SearchMarkBean();
        searchMarkBean3.setCode("1");
        searchMarkBean3.setMark("音乐");

        mSearchMarkList.add(searchMarkBean);
        mSearchMarkList.add(searchMarkBean2);
        mSearchMarkList.add(searchMarkBean3);
        return mSearchMarkList;
    }

    private List<SearchPublishTimeBean> createPublishTimeList() {
        mPublishTimeList = new ArrayList<>();
        SearchPublishTimeBean searchPublishTimeBean = new SearchPublishTimeBean();
        searchPublishTimeBean.setId("");
        searchPublishTimeBean.setPublishTime("全部");

        SearchPublishTimeBean searchPublishTimeBean2 = new SearchPublishTimeBean();
        searchPublishTimeBean2.setId("1");
        searchPublishTimeBean2.setPublishTime("最近一周");

        SearchPublishTimeBean searchPublishTimeBean3 = new SearchPublishTimeBean();
        searchPublishTimeBean3.setId("2");
        searchPublishTimeBean3.setPublishTime("最近一月");

        mPublishTimeList.add(searchPublishTimeBean);
        mPublishTimeList.add(searchPublishTimeBean2);
        mPublishTimeList.add(searchPublishTimeBean3);
        return mPublishTimeList;
    }

    private List<SearchAddressBean> createAddressList() {
        mAddressList = new ArrayList<>();
        SearchAddressBean searchAddressBean = new SearchAddressBean();
        searchAddressBean.setId("");
        searchAddressBean.setAddress("全部");

        SearchAddressBean searchAddressBean2 = new SearchAddressBean();
        searchAddressBean2.setId("0");
        searchAddressBean2.setAddress("省内");

        SearchAddressBean searchAddressBean3 = new SearchAddressBean();
        searchAddressBean3.setId("1");
        searchAddressBean3.setAddress("省外");

        mAddressList.add(searchAddressBean);
        mAddressList.add(searchAddressBean2);
        mAddressList.add(searchAddressBean3);
        return mAddressList;
    }



    public void setVodTypeList(List<VodTypeBean> list) {
        this.mVodTypeBeanList = list;
        mSearchClassifyAdapter.addAllData(mVodTypeBeanList);
    }
    public void setSearchPublishTimeList(List<SearchPublishTimeBean> list) {
        this.mPublishTimeList = list;
        mSearchPublishTimeAdapter.addAllData(mPublishTimeList);
    }
    public void setAddressList(List<SearchAddressBean> list) {
        this.mAddressList = list;
        mSearchAddressAdapter.addAllData(mAddressList);
    }

}

