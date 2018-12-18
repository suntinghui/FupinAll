package com.jkrm.fupin.widgets;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.MyLog;

/**
 * Created by hzw on 2018/8/13.
 */
public class SimpleVideoView extends RelativeLayout implements OnClickListener {

    private Context context;
    private View mView;
    private VideoView mVideoView;//视频控件
    private ImageView mBigPlayBtn;//大的播放按钮
    private ImageView mPlayBtn;//播放按钮
    private ImageView mFullScreenBtn;//全屏按钮
    private SeekBar mPlayProgressBar;//播放进度条
    private TextView mPlayTime;//播放时间
    private LinearLayout mControlPanel;
    private ImageView iv_audio;//音乐播放状态显示

    private Uri mVideoUri = null;

    private Animation outAnima;//控制面板出入动画
    private Animation inAnima;//控制面板出入动画

    private int mVideoDuration;//视频毫秒数
    private int mCurrentProgress;//毫秒数
    private int mSecondProgress;//百分数
    private int preProgress; //之前的毫秒数

    private Runnable mUpdateTask;
    private Thread mUpdateThread;

    private final int UPDATE_PROGRESS = 0;
    private final int EXIT_CONTROL_PANEL = 1;
    private final int UPDATE_SECOND_PROGRESS = 3;
    private boolean stopThread = true;//停止更新进度线程标志

    private Point screenSize = new Point();//屏幕大小
    private boolean mIsFullScreen = false;//是否全屏标志

    private int mWidth;//控件的宽度
    private int mHeigth;//控件的高度

    private int seekTime = 60000; //拖动间隔时间毫秒级
    private boolean isAudio = false; //是否播放的是音频
    private boolean isAllowPlay = false; //播放器是否准备好可播放?
    private boolean isLocalFile = false;//是否是本地的资源文件

    public SimpleVideoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mPlayProgressBar.setProgress(mCurrentProgress);
                    setPlayTime(mCurrentProgress);
                    break;
                case EXIT_CONTROL_PANEL:
                    //执行退出动画
                    if (mControlPanel.getVisibility() != View.GONE) {
                        mControlPanel.startAnimation(outAnima);
                        mControlPanel.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    //初始化控件
    private void init(final Context context, AttributeSet attrs, int defStyleAttr) {
        isAllowPlay = false;
        this.context = context;
        mView = LayoutInflater.from(context).inflate(R.layout.simple_video_view, this);
        mBigPlayBtn = (ImageView) mView.findViewById(R.id.big_play_button);
        mPlayBtn = (ImageView) mView.findViewById(R.id.play_button);
        mFullScreenBtn = (ImageView) mView.findViewById(R.id.full_screen_button);
        mPlayProgressBar = (SeekBar) mView.findViewById(R.id.progress_bar);
        mPlayTime = (TextView) mView.findViewById(R.id.time);
        mControlPanel = (LinearLayout) mView.findViewById(R.id.control_panel);
        mVideoView = (VideoView) mView.findViewById(R.id.video_view);
        iv_audio = (ImageView) mView.findViewById(R.id.iv_audio);
        //获取屏幕大小
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        //加载动画
        outAnima = AnimationUtils.loadAnimation(context, R.anim.exit_from_bottom);
        inAnima = AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom);
        //设置控制面板初始不可见
        mControlPanel.setVisibility(View.GONE);
        //设置大的播放按钮可见
        mBigPlayBtn.setVisibility(View.VISIBLE);
        //设置媒体控制器
        //		mMediaController = new MediaController(context);
        //		mMediaController.setVisibility(View.GONE);
        //		mVideoView.setMediaController(mMediaController);
        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //视频加载完成后才能获取视频时长
                initVideo();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MyConstants.Action.ACTION_VIDEO_PREPARED));
                isAllowPlay = true;
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        MyLog.d("视频准备完毕, 监听缓冲, 当前回调缓冲值: " + percent);
                        mSecondProgress = percent;
                        sendUpdateSecondProgress();
                        //                        mPlayProgressBar.setSecondaryProgress(percent);
                        //                        // 获得当前播放时间和当前视频的长度
                        //                        currentPosition = videoView.getCurrentPosition();
                        //                        duration = videoView.getDuration();
                        //                        int time = ((currentPosition * 100) / duration);
                        //                        // 设置进度条的主要进度，表示当前的播放时间
                        //                        SeekBar seekBar = new SeekBar(EsActivity.this);
                        //                        seekBar.setProgress(time);
                        //                        // 设置进度条的次要进度，表示视频的缓冲进度
                        //                        seekBar.setSecondaryProgress(percent);
                    }
                });
            }
        });
        //视频播放完成监听器
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //                mPlayBtn.setImageResource(R.drawable.play_icon);
                mPlayBtn.setImageResource(android.R.drawable.ic_media_play);
                mVideoView.seekTo(0);
                mPlayProgressBar.setProgress(0);
                setPlayTime(0);
                stopThread = true;
                sendHideControlPanelMessage();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MyConstants.Action.ACTION_VIDEO_ERROR));
                return true;
            }
        });
        mView.setOnClickListener(this);
    }

    //初始化视频，设置视频时间和进度条最大值
    private void initVideo() {
        //初始化时间和进度条
        mVideoDuration = mVideoView.getDuration();//毫秒数
        int seconds = mVideoDuration / 1000;
        mPlayTime.setText("00:00/" +
                ((seconds / 60 > 9) ? (seconds / 60) : ("0" + seconds / 60)) + ":" +
                ((seconds % 60 > 9) ? (seconds % 60) : ("0" + seconds % 60)));
        mPlayProgressBar.setMax(mVideoDuration);
        mPlayProgressBar.setProgress(0);
        //更新进度条和时间任务
        mUpdateTask = new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
                    mCurrentProgress = mVideoView.getCurrentPosition();
                    handler.sendEmptyMessage(UPDATE_PROGRESS);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mBigPlayBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        //进度条进度改变监听器
        mPlayProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(EXIT_CONTROL_PANEL);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress);//设置视频
                    setPlayTime(progress);//设置时间
                }
            }
        });
        mWidth = this.getWidth();
        mHeigth = this.getHeight();
    }

    @Override
    public void onClick(View v) {
        if (v == mView) {
            if (mBigPlayBtn.getVisibility() == View.VISIBLE) {
                MyLog.d("播放, 点击大播放按钮, 当前可播放状态值: " + isAllowPlay);
                if(isAllowPlay) {
                    setPlayBtn();
                } else {
                    Toast.makeText(context, "播放地址正在解析中，请稍等片刻", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (mControlPanel.getVisibility() == View.VISIBLE) {
                //执行退出动画
                mControlPanel.startAnimation(outAnima);
                mControlPanel.setVisibility(View.GONE);
            } else {
                //执行进入动画
                mControlPanel.startAnimation(inAnima);
                mControlPanel.setVisibility(View.VISIBLE);
                sendHideControlPanelMessage();
            }
        } else if (v.getId() == R.id.big_play_button) {//大的播放按钮
            MyLog.d("播放, 点击大播放按钮, 当前可播放状态值: " + isAllowPlay);
            if(isAllowPlay) {
                setPlayBtn();
            } else {
                Toast.makeText(context, "资源正在加载，请等待稍后播放", Toast.LENGTH_SHORT).show();
            }
//            mBigPlayBtn.setVisibility(View.GONE);
//            mVideoView.setBackground(null);
//            if (!mVideoView.isPlaying()) {
//                mVideoView.start();
//                //                mPlayBtn.setImageResource(R.drawable.pause_icon);
//                mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);
//                //开始更新进度线程
//                mUpdateThread = new Thread(mUpdateTask);
//                stopThread = false;
//                mUpdateThread.start();
//            }
        } else if (v.getId() == R.id.play_button) {//播放/暂停按钮
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                //                mPlayBtn.setImageResource(R.drawable.play_icon);
                mPlayBtn.setImageResource(android.R.drawable.ic_media_play);
            } else {
                if (mUpdateThread == null || !mUpdateThread.isAlive()) {
                    //开始更新进度线程
                    mUpdateThread = new Thread(mUpdateTask);
                    stopThread = false;
                    mUpdateThread.start();
                }
                mVideoView.start();
                //                mPlayBtn.setImageResource(R.drawable.pause_icon);
                mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);
            }
            sendHideControlPanelMessage();
        } else if (v.getId() == R.id.full_screen_button) {//全屏
            if (context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                setFullScreen();
            } else {
                setNoFullScreen();
            }
            sendHideControlPanelMessage();
        }
    }

    /**
     * ok中心键
     *      播放 -> 暂停,显示中间的大播放按钮
     *      暂停 -> 播放,隐藏中间的大播放按钮
     */
    public void setPlayBtn() {
        if (mVideoView.isPlaying()) {
            videoPauseStatus();
        } else {
            videoPlayStatus();
        }

    }

    public void videoPauseStatus() {
        mBigPlayBtn.setVisibility(View.VISIBLE);
        iv_audio.setVisibility(View.GONE);
        mVideoView.pause();
        //                mPlayBtn.setImageResource(R.drawable.play_icon);
        mPlayBtn.setImageResource(android.R.drawable.ic_media_play);
//        sendShowControlPanelMessage();
        handler.removeCallbacks(mPlayRunnable);
    }

    public void videoPlayStatus() {
        if (mUpdateThread == null || !mUpdateThread.isAlive()) {
            //开始更新进度线程
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.start();
        //播放添加进度状态监听
        handler.postDelayed(mPlayRunnable, 5000);
        //                mPlayBtn.setImageResource(R.drawable.pause_icon);
        mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);
        mBigPlayBtn.setVisibility(View.GONE);
        sendHideControlPanelMessage();
        if(isAudio) {
            iv_audio.setVisibility(View.VISIBLE);
        } else {
            iv_audio.setVisibility(View.GONE);
        }
    }

    /**
     * 设置循环
     */
    public void setLoop() {
        //监听视频播放完的代码
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }

    /**
     * 设置当前是否播放的是音频
     * @param flag
     */
    public void setIsAudio(boolean flag) {
        isAudio = flag;
    }

    /**
     * 设置当前是否播放的本地资源
     * @param flag
     */
    public void setIsLocalFile(boolean flag) {
        isLocalFile = flag;
    }

    private Runnable mPlayRunnable = new Runnable() {
        @Override
        public void run() {
            //本地资源无需检测
            if(isLocalFile)
                return;
            //间隔5秒检测进度
            handler.postDelayed(this, 5000);
            if(isPlaying()) {
                MyLog.d("视频播放, 循环检测前preProgress: " + preProgress + " ,currentProgress: " + mVideoView.getCurrentPosition());
                int currentPosition = mVideoView.getCurrentPosition();
                if((currentPosition - preProgress) < 5) {
                    Toast.makeText(context, "当前网络不畅, 建议您检查网络后观看", Toast.LENGTH_SHORT).show();
                }
                preProgress = currentPosition;
                MyLog.d("视频播放, , 循环检测后preProgress: " + preProgress);
            }
        }
    };

    //设置当前时间
    private void setPlayTime(int millisSecond) {
        try {
            int currentSecond = millisSecond / 1000;
            String currentTime = ((currentSecond / 60 > 9) ? (currentSecond / 60 + "") : ("0" + currentSecond / 60)) + ":" +
                    ((currentSecond % 60 > 9) ? (currentSecond % 60 + "") : ("0" + currentSecond % 60));
            StringBuilder text = new StringBuilder(mPlayTime.getText().toString());
//            MyLog.d("setPlayTime text: " + text + " mPlayTime.getText: " + mPlayTime.getText().toString() + " ,currentTime: " + currentTime);
            try {
                text.replace(0, text.indexOf("/"), currentTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlayTime.setText(text);
        } catch (Exception e) {

        }

    }

    //设置控件的宽高
    private void setSize() {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        if (mIsFullScreen) {
            lp.width = screenSize.y;
            lp.height = screenSize.x;
        } else {
            lp.width = mWidth;
            lp.height = mHeigth;
        }
        this.setLayoutParams(lp);
    }

    //两秒后隐藏控制面板
    private void sendHideControlPanelMessage() {
        handler.removeMessages(EXIT_CONTROL_PANEL);
        handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
    }

    //设置视频路径
    public void setVideoUri(Uri uri) {
        this.mVideoUri = uri;
        mVideoView.setVideoURI(mVideoUri);
    }

    //获取视频路径
    public Uri getVideoUri() {
        return mVideoUri;
    }

    //设置视频初始画面
    public void setInitPicture(Drawable d) {
        mVideoView.setBackground(d);
    }

    //挂起视频
    public void suspend() {
        if (mVideoView != null) {
            mVideoView.suspend();
        }
    }

    /**
     * 设置视频进度
     * @param millisSecond
     * @param isPlaying
     */
    public void setVideoProgress(int millisSecond, boolean isPlaying, boolean autoStart) {
        MyLog.d("SimpleVideoView setVideoProgress millisSecond: " + millisSecond + " ,isPlaying: " + isPlaying);
        mVideoView.setBackground(null);
        mBigPlayBtn.setVisibility(View.GONE);
        mPlayProgressBar.setProgress(millisSecond);
        setPlayTime(millisSecond);
        if (mUpdateThread == null || !mUpdateThread.isAlive()) {
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.seekTo(millisSecond);
        if (isPlaying) {
            mVideoView.start();
            //            mPlayBtn.setImageResource(R.drawable.pause_icon);
            mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mVideoView.pause();
            //            mPlayBtn.setImageResource(R.drawable.play_icon);
            mPlayBtn.setImageResource(android.R.drawable.ic_media_play);
        }
        if(autoStart) {
            setPlayBtn();
        }
    }

    /**
     * 获取当前观看时间
     * @return
     */
    public String getWatchTime() {
        return mPlayTime.getText().toString().split("/")[0];
    }

    /**
     * 获取视频进度
     * @return
     */
    public int getVideoProgress() {
        return mVideoView.getCurrentPosition();
    }

    //判断视频是否正在播放
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    //判断是否为全屏状态
    public boolean isFullScreen() {
        return mIsFullScreen;
    }

    public void sendUpdateSecondProgress() {
        handler.sendEmptyMessageDelayed(UPDATE_SECOND_PROGRESS, 0);
    }

    //设置竖屏
    public void setNoFullScreen() {
        this.mIsFullScreen = false;
        Activity ac = (Activity) context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ac.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
    }

    //设置横屏
    public void setFullScreen() {
        this.mIsFullScreen = true;
        Activity ac = (Activity) context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Toast.makeText(context, "中心键", Toast.LENGTH_SHORT).show();
                if(mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mBigPlayBtn.setVisibility(View.VISIBLE);
                } else {
                    mVideoView.start();
                    mBigPlayBtn.setVisibility(View.GONE);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                Toast.makeText(context, "下键", Toast.LENGTH_SHORT).show();
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast.makeText(context, "左键", Toast.LENGTH_SHORT).show();
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast.makeText(context, "右键", Toast.LENGTH_SHORT).show();
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                Toast.makeText(context, "上键", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
