package com.jkrm.fupin.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.MainTypeAdapterAudio;
import com.jkrm.fupin.adapter.MainTypeAdapterVideo;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IMainTopListener;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.mvp.contracts.MainContract;
import com.jkrm.fupin.mvp.presenters.MainPresenter;
import com.jkrm.fupin.service.DbCheckService;
import com.jkrm.fupin.service.MediaCheckService;
import com.jkrm.fupin.service.OssResDownloadService;
import com.jkrm.fupin.service.UpnpService;
import com.jkrm.fupin.ui.activity.news.NewsMainActivity;
import com.jkrm.fupin.util.APPUtils;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.NetworkUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.PollingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jkrm.fupin.util.APPUtils.TYPE_VERSION.TYPE_VERSION_NAME;

/**
 * Created by hzw on 2018/8/8.
 */

public class MainActivity2 extends BaseMvpActivity<MainPresenter> implements MainContract.View, IMainTopListener  {

    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_me)
    TextView mTvMe;
    @BindView(R.id.tv_news)
    TextView mTvNews;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.rcv_dataVideo)
    RecyclerView mRcvDataVideo;
    @BindView(R.id.rcv_dataAudio)
    RecyclerView mRcvDataAudio;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_emptyView2)
    TextView mTvEmptyView2;

    public static MainActivity2 instance = null;
    private List<VodTypeBean> mVideoList = new ArrayList<>();
    private List<VodTypeBean> mAudioList = new ArrayList<>();
    private MainTypeAdapterVideo mVideoAdapter;
    private MainTypeAdapterAudio mAudioAdapter;
//    private MainTypeAdapter mVideoAdapter, mAudioAdapter;
    private int[] imgsVideo = new int[] {
            R.drawable.bg_item_0, R.drawable.bg_item_1, R.drawable.bg_item_2, R.drawable.bg_item_3,
            R.drawable.bg_item_4, R.drawable.bg_item_5, R.drawable.bg_item_6, R.drawable.bg_item_7, R.drawable.bg_item_8
    };
    private int[] imgsAudio = new int[] {
            R.mipmap.icon_music_type_jingxuan, R.mipmap.icon_music_type_minyao, R.mipmap.icon_music_type_shuqing, R.mipmap.icon_music_type_jingdian,
            R.mipmap.icon_music_type_yingshi, R.mipmap.icon_music_type_xiqu, R.mipmap.icon_music_type_yingwen, R.mipmap.icon_music_type_dalu, R.mipmap.icon_music_type_gangtai
    };
    private int[] colors = new int[] {
            R.color.color_4D98F6, R.color.color_B13CEB, R.color.color_F05274, R.color.color_20C3BF, R.color.color_F2C339,
            R.color.color_4CD824, R.color.color_EC7E21, R.color.color_2AB9F4, R.color.color_F152EE
    };
    private int[] icons = new int[] {
            R.mipmap.icon_video_type_jingxuan, R.mipmap.icon_video_type_ziran, R.mipmap.icon_video_type_zhiye, R.mipmap.icon_video_type_jingdian,
            R.mipmap.icon_video_type_wenhua, R.mipmap.icon_video_type_ertong, R.mipmap.icon_video_type_fazhi, R.mipmap.icon_video_type_nongye, R.mipmap.icon_video_type_baixing
    };

    private static final int SEARCH_START_LOCAL_DISK = 0;
    private static final int SEARCH_FINISH_LOCAL_DISK = 1;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initView() {
        super.initView();
        instance = this;
    }

    @Override
    protected void initData() {
        super.initData();
//        MyLog.d("测试转换: " + MyFileUtil.getPinyin("/test - 《哈哈哈》.mp4"));
        //手动加个写操作, 使app缓存目录同步到硬盘等设备, 防止下载无权限无目录存在
        MyFileUtil.writeDataToFile("test", mActivity.getExternalFilesDir("").getAbsolutePath() + File.separator + "testWrite.text");
        mTvVersion.setText(getString(R.string.app_name) + " V" + APPUtils.getAppVersionInfo(mActivity, TYPE_VERSION_NAME));
        initRcvVideo();
        initRcvAudio();
        if(MyConstants.Setting.isAllowUpnp) {
            PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, new IPermissionListener() {
                @Override
                public void granted() {
                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_DB_SERVICE, DbCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_DB);
                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_MEIDA_SERVICE, MediaCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_MEDIA);
                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_UPNP_SERVICE, UpnpService.class, MyConstants.Action.ACTION_SERVICE_CHECK_UPNP);
                    PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_OSS_DOWNLOAD_SERVICE, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);
                    getLocalDisk();
                }

                @Override
                public void shouldShowRequestPermissionRationale() {
                    showDialog("下载资源及数据共享服务需本机存储权限，如需使用相关功能，请通过该权限验证");
                    //                                    PermissionUtil.toAppSetting(mActivity);
                }

                @Override
                public void needToSetting() {
                    showDialog("下载资源及数据共享服务需本机存储权限，如需使用相关功能，请通过该权限验证");
                    //                                    PermissionUtil.toAppSetting(mActivity);
                }
            });
        }
    }

    private void initRcvVideo() {
        mVideoAdapter = new MainTypeAdapterVideo(this);
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvDataVideo, mVideoAdapter, true);
        mPresenter.getVodTypeList();
    }

    private void initRcvAudio() {
        mAudioAdapter = new MainTypeAdapterAudio(this);
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvDataAudio, mAudioAdapter, true);
        mPresenter.getAudioTypeList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtil.isNetworkAvailable(mActivity)) {
                    toClass(SearchActivity.class, null, false);
                } else {
                    showMsg("当前网络异常，无法搜索");
                }
            }
        });
        mTvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClass(MeActivity.class, null, false);
            }
        });
        mTvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClass(NewsMainActivity.class, null, false);
            }
        });
        mVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                VodTypeBean bean = mVideoAdapter.getData().get(position);
                String classify = bean.getId();
                if("-1".equals(classify)) {
                    bundle.putString(MyConstants.Params.COMMON_PARAMS, MyConstants.Constant.RES_VIDEO_MARK);
                    toClass(ChosenActivity.class, bundle, false);
                } else {
                    bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                    toClass(ClassifyActivity.class, bundle, false);
                }
            }
        });
        mAudioAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                VodTypeBean bean = mAudioAdapter.getData().get(position);
                String classify = bean.getId();
                if("-1".equals(classify)) {
                    bundle.putString(MyConstants.Params.COMMON_PARAMS, MyConstants.Constant.RES_AUDIO_MARK);
                    toClass(ChosenActivity.class, bundle, false);
                } else {
                    bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean);
                    toClass(ClassifyActivity.class, bundle, false);
                }
            }
        });
    }

    @Override
    public void getVodTypeListSuccess(List<VodTypeBean> list) {
        MyApp.vodTypeBeanList = list;
        if(mVideoList == null)
            mVideoList = new ArrayList<>();
        VodTypeBean vodTypeBean = new VodTypeBean();
        vodTypeBean.setId("-1");
        vodTypeBean.setName("精选视频");
        vodTypeBean.setImg(imgsVideo[0]);
        vodTypeBean.setColor(colors[0]);
        vodTypeBean.setIcon(icons[0]);
        vodTypeBean.setHasFocus(true);
        mVideoList.add(vodTypeBean);
        if(list != null) {
            for(int i=0; i<list.size(); i++) {
                VodTypeBean bean = list.get(i);
                if(i+1 >= imgsVideo.length) {
                    bean.setImg(imgsVideo[i + 1 - imgsVideo.length]);
                } else {
                    bean.setImg(imgsVideo[i + 1]);
                }
                if(i+1 >= colors.length) {
                    bean.setColor(colors[i + 1 - colors.length]);
                } else {
                    bean.setColor(colors[i + 1]);
                }
                if(i+1 >= icons.length) {
                    bean.setIcon(icons[i + 1 - icons.length]);
                } else {
                    bean.setIcon(icons[i + 1]);
                }
                MyLog.d("getVodTypeListSuccess bean: " + bean.toString());
            }
        }
        mVideoList.addAll(list);
        mVideoAdapter.addAllData(mVideoList);
        MyUtil.showEmptyView(mTvEmptyView, mRcvDataVideo, !MyUtil.isEmptyList(mVideoList));
    }

    @Override
    public void getVodTypeListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView, mRcvDataVideo, !MyUtil.isEmptyList(mVideoList));
    }

    @Override
    public void getAudioTypeListSuccess(List<VodTypeBean> list) {
        MyApp.audTypeBeanList = list;
        if(mAudioList == null)
            mAudioList = new ArrayList<>();
        VodTypeBean vodTypeBean = new VodTypeBean();
        vodTypeBean.setId("-1");
        vodTypeBean.setName("精选音乐");
        vodTypeBean.setImg(imgsAudio[0]);
        vodTypeBean.setColor(colors[0]);
        vodTypeBean.setIcon(icons[0]);
        vodTypeBean.setHasFocus(true);
        mAudioList.add(vodTypeBean);
        if(list != null) {
            for(int i=0; i<list.size(); i++) {
                VodTypeBean bean = list.get(i);
                if(i+1 >= imgsAudio.length) {
                    bean.setImg(imgsAudio[i + 1 - imgsAudio.length]);
                } else {
                    bean.setImg(imgsAudio[i + 1]);
                }
//                if(i+1 > colors.length) {
//                    bean.setColor(colors[i + 1 - colors.length]);
//                } else {
//                    bean.setColor(colors[i + 1]);
//                }
                if(i+1 >= icons.length) {
                    bean.setIcon(icons[i + 1 - icons.length]);
                } else {
                    bean.setIcon(icons[i + 1]);
                }
                MyLog.d("getAudTypeListSuccess bean: " + bean.toString());
            }
        }
        mAudioList.addAll(list);
        mAudioAdapter.addAllData(mAudioList);
        MyUtil.showEmptyView(mTvEmptyView2, mRcvDataAudio, !MyUtil.isEmptyList(mAudioList));
    }

    @Override
    public void getAudioTypeListFail(String msg) {
        MyUtil.showEmptyView(mTvEmptyView2, mRcvDataAudio, !MyUtil.isEmptyList(mAudioList));
    }

    @Override
    public void getHomePageVideoListSuccess(HomePageListBean bean) {

    }

    @Override
    public void getHomePageVideoListFail(String msg) {

    }

    @Override
    public void replaceTopFragment(VodTypeBean bean, int position) {
    }


    private void getLocalDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApp.localSaveDiskPath = MyFileUtil.getVolumeDescription();
                MyApp.localSaveDiskFilePath = MyFileUtil.getUseDiskFile();
            }
        }).start();
    }
}
