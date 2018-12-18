package com.jkrm.fupin.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

public class MainTypeAdapterAudio extends BaseQuickAdapter<VodTypeBean, BaseViewHolder> {

    private List<VodTypeBean> mList = new ArrayList<>();
    private IMainTopListener mListener;

    public MainTypeAdapterAudio(IMainTopListener listener) {
        super(R.layout.item_main_audio);
        mListener = listener;
        if(mList == null)
            mList = new ArrayList<>();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(final BaseViewHolder helper, final VodTypeBean bean) {
        helper.setText(R.id.tv_item_tip, bean.getName());
//        helper.getView(R.id.iv_icon).setBackground(mContext.getResources().getDrawable(bean.getIcon()));
        Glide.with(mContext).load(bean.getImg()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_item));
        MyUtil.setOnFocusListenerType(mContext, helper.getView(R.id.fl_main_layout), null);
//        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.fl_main_layout));
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
