package com.jkrm.fupin.ui.activity;

import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by hzw on 2018/12/13.
 */

public class AppDownloadActivity extends BaseActivity {

    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    @Override
    protected int getContentId() {
        return R.layout.activity_appdownload;
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText(R.string.title_app_download);
    }
}
