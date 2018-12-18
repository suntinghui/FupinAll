package com.jkrm.fupin.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.LocalResFileBean;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

/**
 * Created by hzw on 2018/08/17.
 */

public class LocalResAdapter extends BaseQuickAdapter<LocalResFileBean, BaseViewHolder> {

    private List<LocalResFileBean> mList = new ArrayList<>();

    public LocalResAdapter() {
        super(R.layout.item_common);
        if(mList == null)
            mList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final LocalResFileBean bean) {
        MyLog.d("LocalResAdapter file bean: " + bean.getFile().getName());
        helper.setText(R.id.tv_item_tip, bean.getOriginName())
            .setText(R.id.tv_left, MyFileUtil.getFileOrFilesSize(bean.getFile().getAbsolutePath(), MyFileUtil.SIZETYPE_MB) + "M");
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(bean.getAbsolutePath(), MINI_KIND);
        if(bean.getBitmap() != null) {
            ImageView imageView = helper.getView(R.id.iv_item);
            imageView.setImageBitmap(bean.getBitmap());
        }
        MyUtil.setOnFocusListener(mContext, helper.getView(R.id.rl_itemCommon));
    }

    public void addAllData(List<LocalResFileBean> dataList) {
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
