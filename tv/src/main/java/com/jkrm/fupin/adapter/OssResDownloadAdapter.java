package com.jkrm.fupin.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.OssListLocalConvertBean;
import com.jkrm.fupin.util.MyUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class OssResDownloadAdapter extends BaseQuickAdapter<OssListLocalConvertBean, BaseViewHolder> {

    private List<OssListLocalConvertBean> mList = new ArrayList<>();

    public OssResDownloadAdapter() {
        super(R.layout.item_common_linearlayout);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OssListLocalConvertBean bean) {
        if(bean != null) {
            View view = helper.getView(R.id.rl_itemCommon);
            TextView tv = helper.getView(R.id.tv_item_tip);
            helper.setText(R.id.tv_item_tip, bean.getPrefix());
            ImageView iv = helper.getView(R.id.iv_item);
            iv.setImageBitmap(bean.getBitmap());
            helper.setText(R.id.tv_item_status, bean.getStatus());
            MyUtil.setOnFocusListenerNoScale(mContext, view, tv);
        }
    }

    public void addAllData(List<OssListLocalConvertBean> dataList) {
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
