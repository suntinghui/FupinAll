package com.jkrm.fupin.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.OssResDownloadAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.bean.OssListLocalConvertBean;
import com.jkrm.fupin.bean.OssListObjectResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.mvp.contracts.OssResDownloadContract;
import com.jkrm.fupin.mvp.presenters.OssResDownloadPresenter;
import com.jkrm.fupin.service.OssResDownloadService;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.PollingUtils;
import com.jkrm.fupin.util.SharePreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzw on 2018/10/24.
 */

public class OssResDownloadActivity extends BaseMvpActivity<OssResDownloadPresenter> implements OssResDownloadContract.View {

    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    @BindView(R.id.tv_currentFolder)
    TextView mTvCurrentFolder;
    @BindView(R.id.tv_downloadOssRes)
    TextView mTvDownloadOssRes;
    @BindView(R.id.rl_ossFolder)
    RelativeLayout mRlOssFolder;

    private OssResDownloadAdapter mAdapter;
    private OssListObjectResultBean mOssListObjectResultBean;
    private List<OssListLocalConvertBean> mOssListLocalConvertBeansList = new ArrayList<>();
    private Bitmap bmpFolder, bmpVideo, bmpAudio, bmpImage;
    /**
     * 当前文件夹层级, 用于返回按钮回退
     */
    private List<String> mLevelList = new ArrayList<>();

    @Override
    protected int getContentId() {
        return R.layout.activity_oss_res_download;
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitleName.setText("资源下载");
        initRcv();
        bmpFolder = BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder);
        bmpVideo = BitmapFactory.decodeResource(getResources(), R.drawable.icon_video);
        bmpAudio = BitmapFactory.decodeResource(getResources(), R.drawable.icon_audio);
        bmpImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
        mPresenter.getAppOssUrl(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "0"), "0");

    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvDownloadOssRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionUtil.hasStoragePermission(mActivity)) {
                    MyLog.d("OssRes mTvDownloadOssRes onClick 有存储权限");
                    if(MyUtil.isEmptyList(MyApp.localSaveDiskPath) || null == MyApp.localSaveDiskFilePath) {
                        showDialog("未发现硬盘设备，无法下载资源。");
                        return;
                    }
                    //                        if(MyApp.downCalls.size() > 3) {
                    //                            showDialog("已达到最大同时下载限制，请等待其他视频下载结束后再尝试下载。");
                    //                            return;
                    //                        }
                    for(OssListLocalConvertBean temp : mOssListLocalConvertBeansList) {
                        if(temp.isFolder() || MyApp.downCalls.containsKey(temp.getPrefix())) {
                            MyLog.d("OssRes mTvDownloadOssRes onClick 不符合temp: " + temp.toString());
                            continue;
                        }
                        File file = FileDownloadUtil.getFile(mActivity, temp.getPrefix(), MyFileUtil.getFileNameWithType(temp.getPrefix()));
                        if(MyFileUtil.inDb(file.getAbsolutePath()) != null || MyFileUtil.inDbHistory(temp.getPrefix())) {
                            MyLog.d("OssRes mTvDownloadOssRes onClick 之前已加入过数据库: " + temp.toString());
                            continue;
                        }
                        CacheFileBean cacheFileBean = new CacheFileBean();
                        cacheFileBean.setOriginFileName(MyFileUtil.getFileName(temp.getPrefix()));
                        cacheFileBean.setProgress(0);
                        cacheFileBean.setUrl(temp.getPrefix());
                        cacheFileBean.setCurrentSize(0);
                        cacheFileBean.setFileName(MyFileUtil.getFileNameWithType(temp.getPrefix()));
                        cacheFileBean.setFile(file);
                        cacheFileBean.setFileSize(0);
                        cacheFileBean.setFilePath(file.getAbsolutePath());
                        cacheFileBean.setVideoId(temp.getPrefix());//指定目录下载的资源无videoID, 本地使用url做标识
                        cacheFileBean.setImgUrl("");
                        boolean isInsert = DaoCacheFileUtil.getInstance().insertBean(cacheFileBean);
                        if(!isInsert) {
                            continue;
                        }
                        MyLog.d("OssRes mTvDownloadOssRes onClick 添加到下载队列temp: " + temp.toString());
                    }
                    showMsg("已添加到下载队列");
                    startService(new Intent(mActivity, OssResDownloadService.class));
//                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_OSS_DOWNLOAD_SERVICE, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);
                } else {
                    MyLog.d("OssRes mTvDownloadOssRes onClick 无存储权限");
                }
            }
        });
    }

    @Override
    protected OssResDownloadPresenter createPresenter() {
        return new OssResDownloadPresenter(this);
    }

    private void initRcv() {
        mAdapter = new OssResDownloadAdapter();
        MyUtil.setRecyclerViewLinearlayout(mActivity, mRcvData, mAdapter, false, true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OssListLocalConvertBean bean = mAdapter.getItem(position);
                if(bean.isFolder()) {
                    String prefix = bean.getPrefix();
                    mLevelList.add(prefix);
                    mPresenter.listObjectsFromOss(prefix);
                }
            }
        });
    }

    @Override
    public void getAppOssUrlSuccess(OssTokenBeanNew bean, String type) {
        if (bean == null) {
            showDialog("获取OSS服务器地址信息失败，无法提供资源下载");
            MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
            showView(mRlOssFolder, false);
            return;
        }
        //首先遍历一级文件夹, 不用传参
        mLevelList.add("");
        mPresenter.listObjectsFromOss("");
        showView(mRlOssFolder, true);
    }

    @Override
    public void getAppOssUrlFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
        showView(mRlOssFolder, false);
    }

    @Override
    public void listObjectsFromOssSuccess(OssListObjectResultBean bean) {
        mOssListLocalConvertBeansList = new ArrayList<>();
        if(!MyUtil.isEmptyList(mLevelList)) {
            StringBuffer folders = new StringBuffer();
            for(int i=0; i<mLevelList.size(); i++) {
                if(i == 0) {
                    folders.append("/../");
                } else {
                    folders.append(mLevelList.get(i));
                }
            }
            mTvCurrentFolder.setText(folders.toString());
        }
        mOssListObjectResultBean = bean;
        if (mOssListObjectResultBean == null) {
            MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
            showView(mTvDownloadOssRes, false);
            return;
        }
        //资源文件集合
        List<String> objectList = mOssListObjectResultBean.getObjectList();
        if(MyUtil.isEmptyList(objectList)) {
            showView(mTvDownloadOssRes, false);
        } else {
            showView(mTvDownloadOssRes, true);
        }
        //目录文件集合
        List<String> folderList = mOssListObjectResultBean.getFolderList();
        for (String temp : folderList) {
            OssListLocalConvertBean ossListLocalConvertBean = new OssListLocalConvertBean();
            ossListLocalConvertBean.setPrefix(temp);
            ossListLocalConvertBean.setFolder(true);
            ossListLocalConvertBean.setBitmap(bmpFolder);
            ossListLocalConvertBean.setStatus("");
            mOssListLocalConvertBeansList.add(ossListLocalConvertBean);
        }
        for (String temp : objectList) {
            OssListLocalConvertBean ossListLocalConvertBean = new OssListLocalConvertBean();
            ossListLocalConvertBean.setPrefix(temp);
            ossListLocalConvertBean.setFolder(false);
            if(MyFileUtil.inDbHistory(temp)) {
                ossListLocalConvertBean.setStatus("已下载");
            } else {
                ossListLocalConvertBean.setStatus("未下载");
            }
            //仅添加视频,音频及图片类型资源
            if (MyUtil.isVideoType(temp)) {
                ossListLocalConvertBean.setBitmap(bmpVideo);
            } else if (MyUtil.isAudioType(temp)) {
                ossListLocalConvertBean.setBitmap(bmpAudio);
            } else if (MyUtil.isImageType(temp)) {
                ossListLocalConvertBean.setBitmap(bmpImage);
            } else {
                return;
            }
            mOssListLocalConvertBeansList.add(ossListLocalConvertBean);
            mAdapter.addAllData(mOssListLocalConvertBeansList);
        }
    }

    @Override
    protected void onDestroy() {
        if (bmpFolder != null) {
            bmpFolder.recycle();
            bmpFolder = null;
        }
        if (bmpVideo != null) {
            bmpVideo.recycle();
            bmpVideo = null;
        }
        if (bmpAudio != null) {
            bmpAudio.recycle();
            bmpAudio = null;
        }
        if (bmpImage != null) {
            bmpImage.recycle();
            bmpImage = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: //返回键
                if (MyUtil.isEmptyList(mLevelList)) {
                    finish();
                } else if (mLevelList.size() > 0) {
                    if (mLevelList.size() > 1) {
                        //返回上一级目录下. 需重新获取
                        mPresenter.listObjectsFromOss(mLevelList.get(mLevelList.size() - 2));
                        //移除最后一项目录
                        mLevelList.remove(mLevelList.size() - 1);
                    } else {
                        //已到总目录下
                        finish();
                    }
                } else {
                    finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
