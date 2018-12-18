package com.jkrm.fupin.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.interfaces.IMainTopListener;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class MainTypeAdapter extends BaseQuickAdapter<VodTypeBean, BaseViewHolder> {

    private List<VodTypeBean> mList = new ArrayList<>();
    private IMainTopListener mListener;

    public MainTypeAdapter(IMainTopListener listener) {
        super(R.layout.item_main_video);
        mListener = listener;
        if(mList == null)
            mList = new ArrayList<>();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(final BaseViewHolder helper, final VodTypeBean bean) {
        helper.setText(R.id.tv_item_tip, bean.getName());
        MyUtil.setDrawableDirection(mContext, (TextView) helper.getView(R.id.tv_item_tip), 3, bean.getIcon());
        GradientDrawable drawable = (GradientDrawable) helper.getView(R.id.tv_item_tip).getBackground();
        drawable.setColor(ContextCompat.getColor(mContext, bean.getColor()));
        MyUtil.setOnFocusListenerType(mContext, helper.getView(R.id.fl_main_layout), null);
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
