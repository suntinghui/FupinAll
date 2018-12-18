package com.jkrm.fupin.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.VodTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/03/29.
 */

public class CommonListAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private List<? extends Object> mList = new ArrayList<>();

    public CommonListAdapter() {
        super(R.layout.view_item_common_pupupwidow);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, final Object bean) {
        if(bean == null)
            return;
        if(bean instanceof VodTypeBean) {
            VodTypeBean oppBean = (VodTypeBean) bean;
            helper.setText(R.id.tv_name, oppBean.getName())
                    .setGone(R.id.iv_select, false);
            //        helper.getView(R.id.iv_select).setSelected(bean.isSelect());
        }
    }

    public void addAllData(List<? extends Object> dataList) {
        this.mList = dataList;
        this.setNewData((List<Object>) mList);
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
