package com.jkrm.fupin.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.CollectionInfoListBean.CollectionsModelBean;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.VodHistoryBean;
import com.jkrm.fupin.bean.request.AddVodHistoryRequestBean;
import com.jkrm.fupin.bean.request.CollectionRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.mvp.contracts.VideoPlayContract;
import com.jkrm.fupin.mvp.presenters.VideoPlayPresenter;
import com.jkrm.fupin.service.OssResDownloadService;
import com.jkrm.fupin.upnp.util.FixedAndroidHandler;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.PollingUtils;
import com.jkrm.fupin.util.RefreshUtil;
import com.jkrm.fupin.util.SharePreferencesUtil;
import com.jkrm.fupin.util.VideoUtil;
import com.jkrm.fupin.widgets.SimpleVideoView;
import com.jkrm.fupin.widgets.VideoPlayPopupWindow;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.seamless.util.logging.LoggingUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/10.
 */

public class MediaPlayActivity extends BaseMvpActivity<VideoPlayPresenter> implements VideoPlayContract.View, Handler.Callback {

    @BindView(R.id.ll_videoviewLayout)
    LinearLayout mLiVideoviewLayout;
    @BindView(R.id.sfview)
    SurfaceView mSfview;
    @BindView(R.id.videoView)
    SimpleVideoView mVideoView;
    @BindView(R.id.iv_downloadFile)
    ImageView mIvDownloadFile;
    private HomePageBean mHomePageBean; //精选及搜索及其他分类进入
    private VodHistoryBean mVodHistoryBean; //播放记录进入
    private CollectionsModelBean mCollectionsModelBean;//我的收藏进入
    private CacheFileBean mCacheFileBean;//我的下载进入
    private File mLocalFile;//本地资源进入
    private String url = "";
    private String imgUrl = "";
    private String fileName = "";
    private int watchtimeInt;
    private String watchTime = "";
    private VideoBroadcastReceiver mReceiver;
    private String videoId = "";
    private boolean isCollection = false;
    private List<CollectionsModelBean> mCollectionList = new ArrayList<>();
    //下载相关
    private static final int DOWNLOAD_HANDLE = 0;
    private static final int DOWNLOAD_AUTO = 1;
    private static final int CHECK_IS_EXIST = 2;
    private List<CacheFileBean> mList = new ArrayList<>();
    private Handler mHandler;
    private CacheFileBean mDownloadFileBean;
    private boolean isAudio = false;
    private boolean isPrepared = false;
    private boolean isLocalFile = false;

    @Override
    protected int getContentId() {
        return R.layout.activity_media_play;
    }

    @Override
    protected void initData() {
        super.initData();
        showLoadingDialog();
        mDownloadFileBean = new CacheFileBean();
        mHandler = new Handler(this);
        mReceiver = new VideoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_PREPARED);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_START);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_FINISH);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mReceiver, intentFilter);
        if (getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN) instanceof HomePageBean) {
            mHomePageBean = (HomePageBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
            url = mHomePageBean.getOsskey();
            videoId = mHomePageBean.getId();
            imgUrl = mHomePageBean.getImgPath();
            fileName = mHomePageBean.getTitle();
            MyLog.d("MediaPlayActivity  is HomePageBean and url: " + url);
            checkFormat();
            //需先获取已收藏列表
            requestCollectionInfoList();
        } else if (getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN) instanceof VodHistoryBean) {
            mVodHistoryBean = (VodHistoryBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
            url = mVodHistoryBean.getVossPath();
            videoId = mVodHistoryBean.getVid();
            imgUrl = mVodHistoryBean.getVimgPath();
            fileName = mVodHistoryBean.getVtitle();
            watchTime = mVodHistoryBean.getWatchtime();
            watchtimeInt = VideoUtil.convertStringTimeToLong(watchTime);
            MyLog.d("MediaPlayActivity watchtime: " + watchTime + " ,watchtimeInt: " + watchtimeInt);
            MyLog.d("MediaPlayActivity  is VodHistoryBean and url: " + url);
            checkFormat();
            //需先获取已收藏列表
            requestCollectionInfoList();
        } else if(getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN) instanceof CollectionsModelBean) {
            mCollectionsModelBean = (CollectionsModelBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
            url = mCollectionsModelBean.getVodModel().getOsspath();
            videoId = mCollectionsModelBean.getVid();
            imgUrl = mCollectionsModelBean.getVodModel().getImgurl();
            fileName = mCollectionsModelBean.getVodModel().getTitle();
            isCollection = true;
            checkFormat();
            //直接播放, 在收藏列表里面的肯定是已收藏的
            videoPlay(url, true);
            MyLog.d("MediaPlayActivity is CollectionsModelBean and url: " + url);
        } else if(getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN) instanceof CacheFileBean) {
            mCacheFileBean = (CacheFileBean) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
            url = mCacheFileBean.getFilePath();
            videoId = "";
            imgUrl = "";
            fileName = "";
            isCollection = false;
            checkFormat();
            //本地下载的文件没有vid等信息, 不进行收藏等操作
            videoPlay(url, false);
            MyLog.d("MediaPlayActivity is CacheFileBean and url: " + url);
        } else if(getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN) instanceof File) {
            mLocalFile = (File) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
            url = mLocalFile.getAbsolutePath();
            videoId = "";
            imgUrl = "";
            fileName = "";
            isCollection = false;
            checkFormat();
            //本机资源文件没有vid等信息, 不进行收藏等操作
            videoPlay(url, false);
            MyLog.d("MediaPlayActivity is CacheFileBean and url: " + url);
        } else {
            url = "";
            videoId = "";
            imgUrl = "";
            fileName = "";
            showDialog("提示", "该视频暂不支持播放", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    finish();
                }
            });
            return;
        }
        mDownloadFileBean.setUrl(url);
        mDownloadFileBean.setVideoId(videoId);
        mDownloadFileBean.setImgUrl(imgUrl);
        mDownloadFileBean.setFileName(fileName);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void videoPlay(String url, boolean checkLocal) {
//        url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        if(TextUtils.isEmpty(url)) {
            showDialog("播放地址为空, 无法解析");
        } else {
            if(checkLocal)
                getLocalDbDownloadFile(CHECK_IS_EXIST);
            else {
                isLocalFile = true;
                mVideoView.setVideoUri(Uri.parse(url));
            }
//            mVideoView.setVideoUri(Uri.parse(url));
//            mVideoView.setLoop();
        }
//        mVideoView.setVideoURI(Uri.parse(url));
//        mVideoView.start();
    }


    @Override
    protected VideoPlayPresenter createPresenter() {
        return new VideoPlayPresenter(this);
    }

    @Override
    public void onBackPressed() {
        String watchtime = mVideoView.getWatchTime();
        MyLog.d("watchtime: " + watchtime);
        AddVodHistoryRequestBean requestBean = new AddVodHistoryRequestBean(
                videoId,
                SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "0"),
                watchtime
        );
        mPresenter.addVodHistory(requestBean);
    }

    private void requestCollectionInfoList() {
        CollectionRequestBean requestBean = new CollectionRequestBean();
        requestBean.setCreateuser(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "-1"));
        mPresenter.getUserCollectionInfoList(requestBean);
    }

    @Override
    public void getUserCollectionInfoListSuccess(CollectionInfoListBean bean) {
        mCollectionList = bean.getCollectionsModel();
        if(MyUtil.isEmptyList(mCollectionList)) {
            isCollection = false;
        }
        for(CollectionsModelBean tempBean : mCollectionList) {
            if(tempBean.getVid().equals(videoId)) {
                isCollection = true;
                break;
            }
        }
        videoPlay(url, true);
    }

    @Override
    public void getUserCollectionInfoListFail(String msg) {
        isCollection = false;
        videoPlay(url, true);
    }

    @Override
    public void addVodHistorySuccess() {
        MyLog.d("视频播放 添加记录完成");
        finish();
    }

    @Override
    public void addVodHistoryFail(String msg) {
        MyLog.d("视频播放 添加记录失败");
        finish();
    }

    @Override
    public void saveUserCollectionSuccess() {
        isCollection = true;
        showMsg("添加到收藏列表成功");
    }

    @Override
    public void saveUserCollectionFail(String msg) {
        isCollection = false;
    }

    @Override
    public void deleteUserCollectionSuccess() {
        isCollection = false;
        showMsg("已从收藏列表删除");
    }

    @Override
    public void deleteUserCollectionFail(String msg) {
        isCollection = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
                if(isPrepared) {
                    mVideoView.setPlayBtn();
                } else {
                    showMsg("资源加载中，请稍候");
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if(isPrepared) {
                    mVideoView.setPlayBtn();
                } else {
                    showMsg("资源加载中，请稍候");
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(isPrepared) {
                    mVideoView.sendHideControlPanelMessage();
                } else {
                    showMsg("资源加载中，请稍候");
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(isPrepared) {
                    mVideoView.seekProgress(false);
                } else {
                    showMsg("资源加载中，请稍候");
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(isPrepared) {
                    mVideoView.seekProgress(true);
                } else {
                    showMsg("资源加载中，请稍候");
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                mVideoView.sendShowControlPanelMessage();
                break;

            case KeyEvent.KEYCODE_MENU:
                if(!TextUtils.isEmpty(url) && !MyUtil.isTextNull(videoId)) {
                    MyUtil.showVideoPopupWindow(mActivity, mLiVideoviewLayout, isCollection, new VideoPlayPopupWindow.OnItemClickListener() {

                        @Override
                        public void onCollectionSave() {
                            CollectionRequestBean requestBean = new CollectionRequestBean();
                            requestBean.setVid(videoId);
                            requestBean.setCreateuser(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "-1"));
                            mPresenter.saveUserCollection(requestBean);
                        }

                        @Override
                        public void onCollectionDelete() {
                            CollectionRequestBean requestBean = new CollectionRequestBean();
                            requestBean.setVid(videoId);
                            requestBean.setCreateuser(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "-1"));
                            mPresenter.deleteUserCollection(requestBean);
                        }

                        @Override
                        public void onDownload() {
                            PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            }, new IPermissionListener() {
                                @Override
                                public void granted() {
                                    if(MyUtil.isEmptyList(MyApp.localSaveDiskPath) || null == MyApp.localSaveDiskFilePath) {
                                        showDialog("未发现硬盘设备，无法下载资源。");
                                        return;
                                    }
                                    if(MyApp.downCalls.size() > 3) {
                                        showDialog("已达到最大同时下载限制，请等待其他视频下载结束后再尝试下载。");
                                        return;
                                    }
                                    if(MyApp.downCalls.containsKey(url)) {
                                        showDialog("已在下载队列中, 请勿重复下载");
                                        MyLog.d("MediaPlayActivity 不符合temp: " + mDownloadFileBean.toString());
                                        return;
                                    }
                                    final File file = FileDownloadUtil.getFile(mActivity, url, fileName + FileDownloadUtil.getFileEnd(url));
                                    if(mCacheFileBean == null) {
                                        mCacheFileBean = new CacheFileBean();
                                    }
                                    if(MyFileUtil.inDb(file.getAbsolutePath()) != null || MyFileUtil.inDbHistory(url)) {
                                        showDialog("已在下载历史中, 请勿重复下载");
                                        MyLog.d("MediaPlayActivity 之前已加入过数据库url: " + url);
                                        return;
                                    }
                                    mCacheFileBean.setOriginFileName(fileName);
                                    mCacheFileBean.setProgress(0);
                                    mCacheFileBean.setUrl(url);
                                    mCacheFileBean.setCurrentSize(0);
                                    mCacheFileBean.setFileName(fileName + FileDownloadUtil.getFileEnd(url));
                                    mCacheFileBean.setFile(file);
                                    mCacheFileBean.setFileSize(0);
                                    mCacheFileBean.setFilePath(file.getAbsolutePath());
                                    mCacheFileBean.setVideoId(videoId);
                                    mCacheFileBean.setImgUrl(imgUrl);
                                    boolean isInsert = DaoCacheFileUtil.getInstance().insertBean(mCacheFileBean);
                                    if(!isInsert) {
//                                        showMsg("加入下载队列失败，请稍后重试");
                                        return;
                                    }
                                    showMsg("已添加到下载队列");
                                    MyLog.d("MediaPlayActivity 添加到下载队列temp: " + mCacheFileBean.toString());
                                    startService(new Intent(mActivity, OssResDownloadService.class));
//                                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_OSS_DOWNLOAD_SERVICE, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);

                                }

                                @Override
                                public void shouldShowRequestPermissionRationale() {
                                    showDialog("下载需读写存储卡权限，已被禁用， 请至系统设置打开该APP的读写权限后才可使用");
//                                    PermissionUtil.toAppSetting(mActivity);
                                }

                                @Override
                                public void needToSetting() {
                                    showDialog("下载需读写存储卡权限，已被禁用， 请至系统设置打开该APP的读写权限后才可使用");
//                                    PermissionUtil.toAppSetting(mActivity);
                                }
                            });

                        }
                    });

                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CHECK_IS_EXIST:
                String playUrl = "";
                boolean isExist = false;
                for(CacheFileBean bean : mList) {
                    if(bean.getUrl() != null && bean.getUrl().equals(url)
                            && bean.getProgress() == 100) {
                        isExist = true;
                        playUrl = bean.getFilePath();
                        break;
                    }
                }
                MyLog.d("本地是否存在该资源: " + isExist + " ,url: " + url + " ,playUrl: " + playUrl);
                if(isExist) {
                    isLocalFile = true;
                    //本地已下载完成的, 使用本地的资源文件
                    mVideoView.setVideoUri(Uri.parse(playUrl));
                    //            mVideoView.setLoop();
                } else {
                    isLocalFile = false;
                    mVideoView.setVideoUri(Uri.parse(url));
                    //            mVideoView.setLoop();
                }
                break;
        }
        dismissLoadingDialog();
        return false;
    }

    class VideoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyConstants.Action.ACTION_VIDEO_PREPARED.equals(intent.getAction())) {
                dismissLoadingDialog();
                if(mVideoView != null) {
                    mVideoView.setIsLocalFile(isLocalFile);
                    mVideoView.setIsAudio(isAudio);
                    if(watchtimeInt != 0) {
                        showDialogWithCancelBtn("提示", "您上次观看到" + watchTime + "，是否继续观看？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                MyLog.d("VideoBroadcastReceiver watchtimeInt!=0 进度设置");
                                mVideoView.setVideoProgress(watchtimeInt, mVideoView.isPlaying(), true);
                                postDelayPrepared();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                mVideoView.setPlayBtn();
                                postDelayPrepared();
                            }
                        });
                    } else {
                        mVideoView.setPlayBtn();
                        postDelayPrepared();
                    }
                }

//            } else if(MyConstants.Action.ACTION_VIDEO_BUFFERING_START.equals(intent.getAction())) {
//                showLoadingDialog();
//            } else if(MyConstants.Action.ACTION_VIDEO_BUFFERING_FINISH.equals(intent.getAction())) {
//                dismissLoadingDialog();
            }
        }
    }

    /**
     * 初始不允许点击播放, 待资源加载准备
     */
    private void postDelayPrepared() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isPrepared = true;
            }
        }, 2000);
    }

    /**
     * 获取本地下载中或已下载完成的文件
     */
    private void getLocalDbDownloadFile(final int tag) {
        showLoadingDialog();
        mList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList = DaoCacheFileUtil.getInstance().queryAllBean();
                MyApp.localDbDataList = mList;
                mHandler.sendEmptyMessage(tag);
            }
        }).start();
    }

    private void checkFormat() {
        isAudio = MyUtil.isAudioType(MyFileUtil.getFileNameWithType(url));
        if(!isAudio && !MyUtil.isVideoType(MyFileUtil.getFileNameWithType(url))) {
            showDialog("提示", "该视频暂不支持播放", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    finish();
                }
            });
            return;
        }
    }

    @Override
    protected void onDestroy() {
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
