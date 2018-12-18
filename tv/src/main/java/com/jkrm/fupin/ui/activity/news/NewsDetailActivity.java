package com.jkrm.fupin.ui.activity.news;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean.NoticeFilesBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyUtil;
import com.zzhoujay.richtext.RichText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hzw on 2018/8/20.
 */

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_publishTime)
    TextView mTvPublishTime;
    @BindView(R.id.tv_newsDetail)
    TextView mTvNewsDetail;
    @BindView(R.id.ll_outLayout)
    LinearLayout mLlOutLayout;

    private NoticesListBean mNoticesListBean;

    @Override
    protected int getContentId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        mNoticesListBean = (NoticesListBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
        mTvTitleName.setText("资讯 · 详情");
        mTvTitle.setText(mNoticesListBean.getTitle());
        mTvPublishTime.setText("发布时间: " + mNoticesListBean.getCreatetime());
        List<NoticeFilesBean> noticeFilesBeanList = mNoticesListBean.getNoticeFiles();
        if (!MyUtil.isEmptyList(noticeFilesBeanList)) {
            for (NoticeFilesBean tempBean : noticeFilesBeanList) {
                ImageView imageView = new ImageView(mActivity);
                Glide.with(mActivity).load(tempBean.getOsskey()).into(imageView);
                mLlOutLayout.addView(imageView);
            }
        }
        RichText.from("").into(mTvNewsDetail);
        RichText.from(mNoticesListBean.getContent()).into(mTvNewsDetail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
