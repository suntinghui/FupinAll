package com.jkrm.fupin.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/08/17.
 */

public class CacheFileAdapter extends BaseQuickAdapter<CacheFileBean, BaseViewHolder> {

    private List<CacheFileBean> mList = new ArrayList<>();

    public CacheFileAdapter() {
        super(R.layout.item_common);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CacheFileBean bean) {
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(bean.getFilePath(), MINI_KIND);
        Bitmap bitmap  = null;
        try {
            if(MyApp.localSaveDiskFilePath != null) {
                FileInputStream fis = new FileInputStream(MyApp.localSaveDiskFilePath + File.separator + bean.getVideoId() + ".png");
                //            FileInputStream fis = new FileInputStream(mContext.getExternalFilesDir("") + File.separator + bean.getVideoId() + ".png");
                bitmap = BitmapFactory.decodeStream(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_item_tip, bean.getOriginFileName());
//        helper.setText(R.id.tv_item_tip, MyFileUtil.getFileName(bean.getFilePath()));
        helper.setText(R.id.tv_left, MyFileUtil.getFileOrFilesSize(bean.getFilePath(), MyFileUtil.SIZETYPE_MB) + "M");
        helper.setText(R.id.tv_right, bean.getProgress() + "%").setGone(R.id.tv_right, bean.getProgress() != 100);
//        helper.addOnClickListener(R.id.iv_thumb);
        if(bitmap != null)
            helper.setImageBitmap(R.id.iv_item, bitmap);
        else
            Glide.with(mContext).load(bean.getImgUrl()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_item));
        //资源为图片格式
        if(MyUtil.isImageType(bean.getUrl())) {
            Glide.with(mContext).load(bean.getFilePath()).asBitmap().error(R.mipmap.cover).placeholder(R.mipmap.cover).into((ImageView) helper.getView(R.id.iv_item));
        }
        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.rl_itemCommon));
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
}
