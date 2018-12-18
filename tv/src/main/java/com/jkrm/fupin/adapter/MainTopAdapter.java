package com.jkrm.fupin.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.interfaces.IMainTopListener;
import com.jkrm.fupin.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class MainTopAdapter extends BaseQuickAdapter<VodTypeBean, BaseViewHolder> {

    private List<VodTypeBean> mList = new ArrayList<>();
    private IMainTopListener mListener;

    public MainTopAdapter(IMainTopListener listener) {
        super(R.layout.item_main_top);
        mListener = listener;
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VodTypeBean bean) {
        helper.setText(R.id.tv_mainTopTitle, bean.getName());
        helper.getView(R.id.fl_main_layout).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bean.setHasFocus(true);
                    MyLog.d("焦点 adapter 有焦点, position: " + helper.getLayoutPosition());
                    helper.getView(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.switch_track_off));
                    mListener.replaceTopFragment(bean, helper.getLayoutPosition());
                } else {
                    bean.setHasFocus(false);
                    helper.getView(R.id.fl_main_layout).setBackground(null);
                }
            }
        });
    }

    public void addAllData(List<VodTypeBean> dataList) {
        this.mList = dataList;
//        mList.addAll(dataList);
        this.setNewData(mList);
        notifyDataSetChanged();
    }

    public void clearData() {
        if(mList != null) {
            int startPosition = 0;//hasHeader is 1
            int preSize = this.mList.size();
            if(preSize > 0) {
                this.mList.clear();
                notifyItemRangeRemoved(startPosition, preSize);
            }
        }
    }

}
