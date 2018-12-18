package com.jkrm.fupin.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyLog;

import java.util.ArrayList;
import java.util.List;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean;
import com.jkrm.fupin.util.MyUtil;

/**
 * Created by hzw on 2018/08/17.
 */

public class NewsListAdapter extends BaseQuickAdapter<NoticesListBean, BaseViewHolder> {

    private List<NoticesListBean> mList = new ArrayList<>();

    public NewsListAdapter() {
        super(R.layout.item_common);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NoticesListBean bean) {
        helper.setText(R.id.tv_item_tip, bean.getTitle()).setText(R.id.tv_left, TextUtils.isEmpty(bean.getCreatetime()) ? "" : bean.getCreatetime());;
        List<NoticesListBean.NoticeFilesBean> noticesListBeanList = bean.getNoticeFiles();
        if(!MyUtil.isEmptyList(noticesListBeanList)) {
            Glide.with(mContext).load(bean.getNoticeFiles().get(0).getOsskey()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_item));
        }
        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.rl_itemCommon));
    }

    public void addAllData(List<NoticesListBean> dataList) {
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
