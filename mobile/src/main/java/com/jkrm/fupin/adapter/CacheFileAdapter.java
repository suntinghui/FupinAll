package com.jkrm.fupin.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.util.MyFileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class CacheFileAdapter extends BaseQuickAdapter<CacheFileBean, BaseViewHolder> {

    private List<CacheFileBean> mList = new ArrayList<>();
    public boolean isEdit = false;

    public CacheFileAdapter() {
        super(R.layout.item_cache_file);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, final CacheFileBean bean) {
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(bean.getFilePath(), MINI_KIND);
        Bitmap bitmap  = null;
        try {
            FileInputStream fis = new FileInputStream(mContext.getExternalFilesDir("") + File.separator + bean.getVideoId() + ".png");
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper.setText(R.id.item_file, MyFileUtil.getFileName(bean.getFilePath()));
        helper.setText(R.id.item_size, MyFileUtil.getFileOrFilesSize(bean.getFilePath(), MyFileUtil.SIZETYPE_MB) + "M");
        helper.setText(R.id.item_progress, bean.getProgress() + "%").setGone(R.id.item_progress, bean.getProgress() != 100);
        if(isEdit) {
            helper.getView(R.id.iv_select).setSelected(bean.isCheck() ? true : false);
            helper.getView(R.id.iv_select).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_select).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.iv_select);
        if(bitmap != null)
            helper.setImageBitmap(R.id.iv_thumb, bitmap);
        else
            Glide.with(mContext).load(bean.getImgUrl()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_thumb));
    }

    public void addAllData(List<CacheFileBean> dataList) {
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

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }
}
