package com.jkrm.fupin.ui.activity.news;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean.NoticeFilesBean;
import com.jkrm.fupin.bean.request.NewDetailRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.NewsDetailContract;
import com.jkrm.fupin.mvp.presenters.NewsDetailPresenter;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.MyListView;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/20.
 */

public class NewsDetailActivity extends BaseMvpActivity<NewsDetailPresenter> implements NewsDetailContract.View {

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
    @BindView(R.id.lv_list)
    MyListView mLvList;
    @BindView(R.id.ll_annex)
    LinearLayout mLlAnnex;

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
        NewDetailRequestBean requestBean = new NewDetailRequestBean();
        requestBean.setId(mNoticesListBean.getId());
        mPresenter.getNewsDetail(requestBean);
    }

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    public void getNewsDetailSuccess(NoticesListBean bean) {
        List<NoticeFilesBean> noticeFilesBeanList = bean.getNoticeFiles();
        if (!MyUtil.isEmptyList(noticeFilesBeanList)) {
            for (NoticeFilesBean tempBean : noticeFilesBeanList) {
                ImageView imageView = new ImageView(mActivity);
                Glide.with(mActivity).load(tempBean.getOsskey()).into(imageView);
                mLlOutLayout.addView(imageView);
            }
        }
        RichText.from("").into(mTvNewsDetail);
        RichText.from(mNoticesListBean.getContent()).into(mTvNewsDetail);
        if(MyUtil.isEmptyList(bean.getZipFiles())){
            mLlAnnex.setVisibility(View.GONE);
        } else {
            List<String> annexNameList = new ArrayList<>();
            for(NoticesListBean.ZipFilesBean zipFilesBean : bean.getZipFiles()) {
                annexNameList.add(zipFilesBean.getOrginname());
            }
            MyLog.d("新闻资讯详情, 附件列表: " + annexNameList);
            mLlAnnex.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    mActivity, R.layout.item_new_detail, annexNameList);
            mLvList.setAdapter(adapter);
        }
    }

    @Override
    public void getNewsDetailFail(String msg) {

    }
}
