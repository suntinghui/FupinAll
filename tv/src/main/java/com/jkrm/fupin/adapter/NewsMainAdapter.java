package com.jkrm.fupin.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class NewsMainAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    private List<NewsBean> mList = new ArrayList<>();

    public NewsMainAdapter() {
        super(R.layout.item_news_main);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NewsBean bean) {
        helper.setText(R.id.tv_item, bean.getTypeName())
            .addOnClickListener(R.id.tv_item);
        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.fl_main_layout));
    }

    public void addAllData(List<NewsBean> dataList) {
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
