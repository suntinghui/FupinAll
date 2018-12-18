package com.jkrm.fupin.ui.activity;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.ui.activity.upnp.UpnpMainActivity;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.SharePreferencesUtil;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/13.
 */

public class MeActivity extends BaseActivity {

    @BindView(R.id.tv_playRecord)
    TextView mTvPlayRecord;
    @BindView(R.id.tv_collection)
    TextView mTvCollection;
    @BindView(R.id.tv_cacheFile)
    TextView mTvCacheFile;
    @BindView(R.id.tv_localFile)
    TextView mTvLocalFile;
    @BindView(R.id.tv_downloadOssRes)
    TextView mTvDownloadOssRes;
    @BindView(R.id.tv_fileShare)
    TextView mTvFileShare;
    @BindView(R.id.tv_appDownload)
    TextView mTvAppDownload;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;

    @Override
    protected int getContentId() {
        return R.layout.activity_me;
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText(R.string.title_me);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvPlayRecord.setOnClickListener(this);
        mTvCollection.setOnClickListener(this);
        mTvCacheFile.setOnClickListener(this);
        mTvLocalFile.setOnClickListener(this);
        mTvFileShare.setOnClickListener(this);
        mTvDownloadOssRes.setOnClickListener(this);
        mTvAppDownload.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_playRecord:
                PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        toClass(VodHistoryActivity.class, null, false);
                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        PermissionUtil.toAppSetting(mActivity);
                    }

                    @Override
                    public void needToSetting() {
                        PermissionUtil.toAppSetting(mActivity);
                    }
                });
                break;
            case R.id.tv_collection:
                toClass(CollectionActivity.class, null, false);
                break;
            case R.id.tv_cacheFile:
                PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        toClass(CacheFileActivity.class, null, false);
                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        PermissionUtil.toAppSetting(mActivity);
                    }

                    @Override
                    public void needToSetting() {
                        PermissionUtil.toAppSetting(mActivity);
                    }
                });
                break;
            case R.id.tv_fileShare:
                PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        toClass(UpnpMainActivity.class, null, false);
                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        PermissionUtil.toAppSetting(mActivity);
                    }

                    @Override
                    public void needToSetting() {
                        PermissionUtil.toAppSetting(mActivity);
                    }
                });
                break;
            case R.id.tv_logout:
                SharePreferencesUtil.clearAll(MyConstants.SharedPrefrenceXml.USER_INFO_XML);
                toClass(LoginActivity.class, null, true);
                break;
            case R.id.tv_localFile:
                toClass(LocalResActivity.class, null, false);
                break;
            case R.id.tv_downloadOssRes:
                toClass(OssResDownloadActivity.class, null, false);
                break;
            case R.id.tv_appDownload:
                toClass(AppDownloadActivity.class, null, false);
                break;
        }
    }
}
