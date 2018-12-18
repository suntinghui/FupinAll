package com.jkrm.fupin.ui.activity.upnp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.receiver.UpnpReceiver;
import com.jkrm.fupin.ui.activity.MainActivity;
import com.jkrm.fupin.upnp.dmc.DMCControl;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.upnp.util.Action;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUpnpUtil;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.jkrm.fupin.widgets.SimpleVideoView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/16.
 */

public class UpnpControlActivity extends BaseActivity implements IUpnpDeviceChangeListener, Handler.Callback {
    @BindView(R.id.videoView)
    SimpleVideoView mVideoView;
    private boolean isAudio = false;
    private boolean isPrepared = false;
    private boolean isLocalFile = false;
    private Handler mHandler;
    private String videoPath = "";
    private UpnpReceiver mUpnpReceiver;
    private VideoBroadcastReceiver mReceiver;

    @Override
    protected int getContentId() {
        return R.layout.activity_upnp_control;
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler();
        registerUpnpReceiver();
        registerVideoReceiver();
        videoPath = getIntent().getExtras().getString("playURI");
        isAudio = getIntent().getExtras().getBoolean("isAudio", false);
        MyLog.d("手机播放共享 encode前: " + videoPath);
        if(TextUtils.isEmpty(videoPath)){
            showDialogToFinish("文件路径不存在, 无法播放");
        }
        try {
            videoPath = URLEncoder.encode(videoPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        videoPath = videoPath.replace("%3A", ":");
        videoPath = videoPath.replace("%2F", "/");
        MyLog.d("手机播放共享 encode后: " + videoPath);
//        videoPath = "http://192.168.0.103:8192/storage/96107E58107E3F75/sxfp/video/test_001%E5%89%AF%E6%9C%AC.mp4";
        mVideoView.setVideoUri(Uri.parse(videoPath));
        mVideoView.setFullScreen();
        String name = getIntent().getExtras().getString("mame");
        if(TextUtils.isEmpty(name))
            name = getString(R.string.title_video_play);
        mToolbar.setToolbarTitle(name);
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.hideRightView();
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                finish();
            }
        });

        MyUpnpUtil.registerReceiver(mActivity, mUpnpReceiver, this);

    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            showView(mToolbar, true);
            //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//全部显示出来。
        } else {
            showView(mToolbar, false);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            //            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void registerUpnpReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(Action.PLAY_UPDATE);
        localIntentFilter.addAction("com.video.play.error");
        localIntentFilter.addAction("com.connection.failed");
        localIntentFilter.addAction("com.transport.info");
        localIntentFilter.addAction(Action.PLAY_ERR_VIDEO);
        localIntentFilter.addAction(Action.PLAY_ERR_AUDIO);
        registerReceiver(mBroadcastReceiver, localIntentFilter);
    }

    private void registerVideoReceiver() {
        mReceiver = new VideoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_PREPARED);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_START);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_FINISH);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_ERROR);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mReceiver, intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            if (paramIntent.getAction().equals(Action.PLAY_ERR_VIDEO)
                    || paramIntent.getAction().equals(Action.PLAY_ERR_AUDIO)) {
                showDialog(getString(R.string.media_play_err));
            }
        }
    };

    @Override
    protected void onResume() {
        DMCControl.isExit = false;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mBroadcastReceiver != null)
            unregisterReceiver(mBroadcastReceiver);
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        }
        DMCControl.isExit = true;
        MyUpnpUtil.unRegisterReceiver(mActivity, mUpnpReceiver);
        super.onDestroy();
    }

    @Override
    public void deviceUpdate(DeviceItem deviceItem, boolean isAdd) {
        if(null == deviceItem) {
            showMsg("数据源数据更新，请重新加载数据源");
            finish();
        } else {
            if(!isAdd) {
                DeviceItem deviceItemCurrent = MyApp.deviceItem;
                if(deviceItemCurrent != null && deviceItemCurrent.getUdn().equals(deviceItem.getUdn())) {
                    showMsg("请检查数据源是否已关闭");
                    finish();
                    //                toClass(MainActivity.class, null, true);
                    //                try {
                    //                    if(MyUtil.isForeground(mActivity, UpnpControlActivity.class.getName())) {
                    //                        showDialogToFinish("当前数据源设备已断开，请点击确定离开本页面");
                    //                    } else {
                    //                        finish();
                    //                    }
                    //                } catch (Exception e) {
                    //                    e.printStackTrace();
                    //                    showMsg("请检查数据源是否已关闭");
                    //                    finish();
                    //                }
                }
            }
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    class VideoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyConstants.Action.ACTION_VIDEO_PREPARED.equals(intent.getAction())) {
                dismissLoadingDialog();
                if(mVideoView != null) {
//                    mVideoView.setIsLocalFile(false);
                    mVideoView.setIsAudio(isAudio);
                    mVideoView.setPlayBtn();
                    postDelayPrepared();
                }

                //            } else if(MyConstants.Action.ACTION_VIDEO_BUFFERING_START.equals(intent.getAction())) {
                //                showLoadingDialog();
                //            } else if(MyConstants.Action.ACTION_VIDEO_BUFFERING_FINISH.equals(intent.getAction())) {
                //                dismissLoadingDialog();
            } else if(MyConstants.Action.ACTION_VIDEO_ERROR.equals(intent.getAction())) {
                showDialogToFinish("该数据源地址无法播放，请选择其他资源观看");
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
}
