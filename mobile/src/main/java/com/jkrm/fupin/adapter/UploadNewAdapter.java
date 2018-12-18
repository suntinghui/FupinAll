package com.jkrm.fupin.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.ItemInfo;
import com.jkrm.fupin.bean.UploadBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class UploadNewAdapter extends BaseQuickAdapter<UploadBean, BaseViewHolder> {

    private List<UploadBean> mList = new ArrayList<>();

    public UploadNewAdapter() {
        super(R.layout.item_upload);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, final UploadBean bean) {
        helper.setText(R.id.item_file, bean.getFileName());
        helper.setText(R.id.item_progress, bean.getProgress() + "%");
        helper.setText(R.id.item_status, bean.getUploadStatus());
        helper.setImageBitmap(R.id.iv_thumb, bean.getThumbnail());
    }

    public void addAllData(List<UploadBean> dataList) {
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
