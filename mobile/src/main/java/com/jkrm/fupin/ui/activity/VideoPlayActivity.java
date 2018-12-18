package com.jkrm.fupin.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.jkrm.fupin.widgets.SimpleVideoView;

import java.io.File;

import butterknife.BindView;

/**
 * Created by hzw on 2018/8/13.
 */

public class VideoPlayActivity extends BaseActivity implements Handler.Callback {
    @BindView(R.id.videoView)
    SimpleVideoView mVideoView;
    @BindView(R.id.iv_downloadFile)
    ImageView mIvDownloadFile;

    private String videoPath = "";
    private boolean isAudio = false;
    private boolean isPrepared = false;
    private boolean isLocalFile = false;
    private Handler mHandler;
    private VideoBroadcastReceiver mReceiver;

    @Override
    protected int getContentId() {
        return R.layout.activity_media_play;
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler();
        registerVideoReceiver();
        File file = (File) getIntent().getExtras().getSerializable(MyConstants.Params.COMMON_PARAMS_BEAN);
        if(file != null) {
            checkFormat(MyFileUtil.getFileNameWithType(file.getAbsolutePath()));
            videoPath = file.getAbsolutePath();
            MyLog.d("手机播放videoPath: " + videoPath + " ,parse: " + Uri.parse(videoPath));
            mVideoView.setVideoUri(Uri.parse(videoPath));
            mVideoView.setFullScreen();
            mToolbar.setToolbarTitle(MyFileUtil.getFileNameWithType(file.getAbsolutePath()));
            mToolbar.setLeftImg(R.mipmap.back);
            mToolbar.hideRightView();
            mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
                @Override
                public void onLeftTextClick() {
                    finish();
                }
            });
        } else {
            mToolbar.setToolbarTitle(getString(R.string.title_video_play));
            mToolbar.setLeftImg(R.mipmap.back);
            mToolbar.hideRightView();
            mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
                @Override
                public void onLeftTextClick() {
                    finish();
                }
            });
            showDialogToFinish("文件路径不存在,无法播放");
        }
    }

    private void checkFormat(String name) {
        isAudio = MyUtil.isAudioType(name);
        if(!isAudio && !MyUtil.isVideoType(name)) {
            showDialogToFinish("该视频暂不支持播放");
            return;
        }
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

    private void registerVideoReceiver() {
        mReceiver = new VideoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_PREPARED);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_START);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_BUFFERING_FINISH);
        intentFilter.addAction(MyConstants.Action.ACTION_VIDEO_ERROR);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        }
        super.onDestroy();
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
