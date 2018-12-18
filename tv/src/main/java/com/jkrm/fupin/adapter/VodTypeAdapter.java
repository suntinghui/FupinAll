package com.jkrm.fupin.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class VodTypeAdapter extends BaseQuickAdapter<HomePageBean, BaseViewHolder> {

    private List<HomePageBean> mList = new ArrayList<>();

    public VodTypeAdapter() {
        super(R.layout.item_common);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomePageBean bean) {
        helper.setText(R.id.tv_item_tip, bean.getTitle())
                .setText(R.id.tv_left, TextUtils.isEmpty(bean.getCreatetime()) ? "" : bean.getCreatetime());
        Glide.with(mContext).load(bean.getImgPath()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_item));
        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.rl_itemCommon));
    }

    public void addAllData(List<HomePageBean> dataList) {
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
