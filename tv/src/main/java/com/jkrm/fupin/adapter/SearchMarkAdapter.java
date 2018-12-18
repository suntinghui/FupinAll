package com.jkrm.fupin.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.SearchMarkBean;
import com.jkrm.fupin.bean.SearchPublishTimeBean;
import com.jkrm.fupin.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class SearchMarkAdapter extends BaseQuickAdapter<SearchMarkBean, BaseViewHolder> {

    private List<SearchMarkBean> mList = new ArrayList<>();

    public SearchMarkAdapter() {
        super(R.layout.item_search_mark);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SearchMarkBean bean) {
        helper.setText(R.id.tv_item, bean.getMark());
        TextView tv_item = helper.getView(R.id.tv_item);
        if(bean.isSelected()) {
            tv_item.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else{
            tv_item.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        helper.getView(R.id.fl_main_layout).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bean.setHasFocus(true);
                    MyLog.d("焦点 adapter 有焦点, position: " + helper.getLayoutPosition());
                    helper.getView(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.switch_track_off));
                } else {
                    bean.setHasFocus(false);
                    helper.getView(R.id.fl_main_layout).setBackground(null);
                }
            }
        });
    }

    public void addAllData(List<SearchMarkBean> dataList) {
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
