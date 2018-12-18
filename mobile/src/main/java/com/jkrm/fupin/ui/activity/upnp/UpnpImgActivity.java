package com.jkrm.fupin.ui.activity.upnp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.receiver.UpnpReceiver;
import com.jkrm.fupin.upnp.com.zxt.dlna.activity.SettingActivity;
import com.jkrm.fupin.upnp.com.zxt.dlna.application.ConfigData;
import com.jkrm.fupin.upnp.dmc.DMCControl;
import com.jkrm.fupin.upnp.dmc.GenerateXml;
import com.jkrm.fupin.upnp.dmp.ContentItem;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.upnp.dmp.SuperImageView;
import com.jkrm.fupin.upnp.util.FileUtil;
import com.jkrm.fupin.upnp.util.ImageUtil;
import com.jkrm.fupin.util.MyUpnpUtil;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.widgets.UpnpImgPopupWindow;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.fourthline.cling.android.AndroidUpnpService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/8/16.
 */

public class UpnpImgActivity extends BaseActivity implements Handler.Callback, View.OnTouchListener, IUpnpDeviceChangeListener {

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final String TAG = "ImageDisplay";
    protected static final int MSG_SLIDE_START = 1000;
    private int mode = NONE;
    private float oldDist;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private SuperImageView mImageView;
    private Button mPreBtn;
    private Button mNextBtn;
    private Button mDownloadBtn;
    private Button mSharedBtn;
    private Button mSlideBtn;
    private Button mRotateBtn;
    private LinearLayout mButtonLayout;
    private String mPlayUri = null;
    private String currentContentFormatMimeType = "";
    private String metaData = "";
    private DeviceItem dmrDeviceItem = null;
    private boolean isLocalDmr = true;
    private DMCControl dmcControl = null;
    private AndroidUpnpService upnpService = null;
    private ArrayList<ContentItem> mListPhotos = new ArrayList<ContentItem>();
    private ProgressBar mSpinner;
    DisplayImageOptions options;
    private int mCurrentPosition;
    private boolean isSlidePlaying = false;
    private volatile Bitmap mCurrentBitmap;
    private Handler mHandler;
    private UpnpImgPopupWindow popupWindow;
    private List<String> mMenuList = new ArrayList<>();
    private UpnpReceiver mUpnpReceiver;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SLIDE_START: {
                if (!nextImage()) {
                    int time = SettingActivity.getSlideTime(mActivity);
                    if (time < 5) {
                        time = 5;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_SLIDE_START,
                            time * 1000);
                }
                break;
            }

            default:
                break;
        }
        return false;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_upnp_img;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar.setToolbarTitle(getString(R.string.title_file_img_show));
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.setRightImg(R.mipmap.icon_menu);
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                finish();
            }
        });
        mToolbar.setOnRightClickListener(new CustomToolbar.OnRightClickListener() {
            @Override
            public void onRightTextClick() {
                popupWindow.setOnMenuListener(new UpnpImgPopupWindow.OnMenuListener() {
                    @Override
                    public void onSlide() {
                        if (!isSlidePlaying) {
                            isSlidePlaying = true;
                            mSlideBtn.setBackgroundResource(R.drawable.ic_slide_pause);
                            mHandler.sendEmptyMessageDelayed(MSG_SLIDE_START, 5000);
                            showMsg(R.string.info_image_slide_start);
                            popupWindow.setSlideText("幻灯片停止");
                        } else {
                            isSlidePlaying = false;
                            mSlideBtn.setBackgroundResource(R.drawable.ic_slide_start);
                            mHandler.removeMessages(MSG_SLIDE_START);
                            showMsg(R.string.info_image_slide_pause);
                            popupWindow.setSlideText("幻灯片播放");
                        }
                    }

                    @Override
                    public void onDownload() {
                        String path = saveCurrentBitmap();
                        if (!TextUtils.isEmpty(path)) {
                            showMsg(getString(R.string.info_download_image) + path);
                        } else {
                            showMsg(R.string.info_download_image_error);
                        }
                    }
                });
                popupWindow.show(mToolbar.getRightView());
            }
        });
        mImageView = (SuperImageView) this.findViewById(R.id.imageView);
        mPreBtn = (Button) this.findViewById(R.id.preButton);
        mNextBtn = (Button) this.findViewById(R.id.nextButton);
        mButtonLayout = (LinearLayout) this.findViewById(R.id.buttonLayout);
        mSpinner = (ProgressBar) findViewById(R.id.loading);
        mDownloadBtn = (Button) this.findViewById(R.id.downloadButton);
        mSharedBtn = (Button) this.findViewById(R.id.sharedButton);
        mSlideBtn = (Button) this.findViewById(R.id.slideButton);
        mRotateBtn = (Button) this.findViewById(R.id.rotateButton);
    }

    @Override
    protected void initData() {
        super.initData();
        popupWindow = new UpnpImgPopupWindow(mActivity);
        mHandler = new Handler(this);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        Intent localIntent = getIntent();
        mPlayUri = localIntent.getStringExtra("playURI");

        mCurrentPosition = ConfigData.photoPosition;
        mListPhotos = ConfigData.listPhotos;

        dmrDeviceItem = MyApp.dmrDeviceItem;
        upnpService = MyApp.upnpService;

        isLocalDmr = MyApp.isLocalDmr;
        if (!isLocalDmr) {
            currentContentFormatMimeType = localIntent
                    .getStringExtra("currentContentFormatMimeType");
            metaData = localIntent.getStringExtra("metaData");
            dmcControl = new DMCControl(this, 1, dmrDeviceItem, upnpService,
                    mPlayUri, metaData);
        }
        showImage(mPlayUri);
        MyUpnpUtil.registerReceiver(mActivity, mUpnpReceiver, this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mImageView.setOnTouchListener(this);
        mPreBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mDownloadBtn.setOnClickListener(this);
        mSharedBtn.setOnClickListener(this);
        mSlideBtn.setOnClickListener(this);
        mRotateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.preButton: {
                prevImage();

                break;
            }
            case R.id.nextButton: {
                nextImage();

                break;
            }
        }
    }

    private boolean nextImage() {
        boolean isLast;
        if (mCurrentPosition >= mListPhotos.size() - 1) {
            isLast = true;
            showMsg(getString(R.string.info_last_image));
        } else {
            isLast = false;
            mCurrentPosition = mCurrentPosition + 1;
            String uri = ((ContentItem) mListPhotos.get(mCurrentPosition))
                    .getItem().getFirstResource().getValue();
            if (!TextUtils.isEmpty(uri)) {
                mPlayUri = uri;
                showImage(mPlayUri);

                if (!isLocalDmr) {
                    dmcControl.stop(true);
                    try {
                        dmcControl.setCurrentPlayPath(mPlayUri,
                                new GenerateXml()
                                        .generate((ContentItem) mListPhotos
                                                .get(mCurrentPosition)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dmcControl.getProtocolInfos(currentContentFormatMimeType);
                }
            }
        }
        return isLast;
    }

    private boolean prevImage() {
        boolean isFirst;
        if (mCurrentPosition == 0) {
            isFirst = true;
            showMsg(getString(R.string.info_first_image));
        } else {
            isFirst = false;
            mCurrentPosition = mCurrentPosition - 1;
            String uri = ((ContentItem) mListPhotos.get(mCurrentPosition))
                    .getItem().getFirstResource().getValue();
            if (!TextUtils.isEmpty(uri)) {
                mPlayUri = uri;
                showImage(mPlayUri);
                if (!isLocalDmr) {
                    dmcControl.stop(true);
                    try {
                        dmcControl.setCurrentPlayPath(mPlayUri,
                                new GenerateXml()
                                        .generate((ContentItem) mListPhotos
                                                .get(mCurrentPosition)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dmcControl.getProtocolInfos(currentContentFormatMimeType);
                }
            }
        }
        return isFirst;
    }

    private void showImage(String url) {
        fetchBitmap2(url);
        if (!isLocalDmr) {
            try {
                dmcControl.setCurrentPlayPath(mPlayUri, new GenerateXml()
                        .generate((ContentItem) mListPhotos
                                .get(mCurrentPosition)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            dmcControl.getProtocolInfos(currentContentFormatMimeType);
        }
    }

    private void fetchBitmap2(String url) {
        ImageLoader.getInstance().displayImage(url, mImageView, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mSpinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        int message = R.string.network_denied;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = R.string.io_error;
                                break;
                            case DECODING_ERROR:
                                message = R.string.decoding_error;
                                break;
                            case NETWORK_DENIED:
                                message = R.string.network_denied;
                                break;
                            case OUT_OF_MEMORY:
                                message = R.string.oom_error;
                                break;
                            case UNKNOWN:
                                message = R.string.unknown_error;
                                break;
                        }
                        showMsg(getString(message));
                        mSpinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        mSpinner.setVisibility(View.GONE);
                        mCurrentBitmap = loadedImage;
                    }
                });
    }

    private String saveCurrentBitmap() {
        String path = "";
        if (null != mCurrentBitmap && !mCurrentBitmap.isRecycled()) {
            if (null != FileUtil.getSDPath()) {
                String filename = mPlayUri.substring(mPlayUri.lastIndexOf("/"));
                if (FileUtil.getFileSuffix(filename).equals("")) {
                    filename = filename + ".jpg";
                }

                path = FileUtil.getSDPath() + FileUtil.IMAGE_DOWNLOAD_PATH;
                File path1 = new File(path);
                if (!path1.exists()) {
                    path1.mkdirs();
                }
                path = path + filename;
                try {
                    ImageUtil
                            .saveBitmapWithFilePathSuffix(mCurrentBitmap, path);

                } catch (Exception e) {
                    path = "";
                    Log.w(TAG, "saveCurrentBitmap", e);
                }
            }
        }
        return path;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mButtonLayout.getVisibility() == View.VISIBLE) {
                    mButtonLayout.setVisibility(View.GONE);
                } else {
                    mButtonLayout.setVisibility(View.VISIBLE);
                }
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP:
                if (mode == DRAG) {
                    if (event.getX() - start.x > 100) {
                        // go to prev pic
                        prevImage();
                    } else if (event.getX() - start.x < -100) {
                        // go to next pic
                        nextImage();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }

        // view.setImageMatrix(matrix);
        return false;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        MyUpnpUtil.unRegisterReceiver(mActivity, mUpnpReceiver);
        if (!isLocalDmr) {
            dmcControl.stop(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isLocalDmr) {
            dmcControl.stop(true);
        }
    }


    @Override
    public void deviceUpdate(DeviceItem deviceItem, boolean isAdd) {
        if(!isAdd) {
            DeviceItem deviceItemCurrent = MyApp.deviceItem;
            if(deviceItemCurrent != null && deviceItemCurrent.getUdn().equals(deviceItem.getUdn())) {
                if(MyUtil.isForeground(mActivity, UpnpImgActivity.class.getName())) {
                    showDialogToFinish("当前数据源设备已断开，请点击确定离开本页面");
                } else {
                    finish();
                }
            }
        }
    }
}
