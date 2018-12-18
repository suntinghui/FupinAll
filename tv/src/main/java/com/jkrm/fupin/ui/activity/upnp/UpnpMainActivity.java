package com.jkrm.fupin.ui.activity.upnp;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.upnp.dmr.ZxtMediaRenderer;
import com.jkrm.fupin.upnp.dms.ContentNode;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.dms.MediaServer;
import com.jkrm.fupin.upnp.util.FileUtil;
import com.jkrm.fupin.upnp.util.FixedAndroidHandler;
import com.jkrm.fupin.upnp.util.ImageUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.SharePreferencesUtil;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;
import org.seamless.util.logging.LoggingUtil;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hzw on 2018/8/15.
 */

public class UpnpMainActivity extends BaseActivity implements Handler.Callback {
    public static final int GET_IP_FAIL = 0;
    public static final int GET_IP_SUC = 1;
    public static final int CREATE_THUMB_SUCCESS = 2;
    public static final int CREATE_THUMB_FAIL = 3;
    @BindView(R.id.cb_boxShare)
    CheckBox mCbBoxShare;
    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    private String hostName;
    private String hostAddress;
    private List<Map<String, String>> mVideoFilePaths;
    private Handler mHandle;
    // DMS + DMR
    private static boolean serverPrepared = false;
    private String fileName;
    private int mImageContaierId = Integer.valueOf(ContentTree.IMAGE_ID) + 1;
    private MediaServer mediaServer;
    private ArrayList<DeviceItem> mDevList = new ArrayList<DeviceItem>();
    private ArrayList<DeviceItem> mDmrList = new ArrayList<DeviceItem>();
    private AndroidUpnpService upnpService;
    private DeviceListRegistryListener deviceListRegistryListener;
    private String[] imageThumbColumns = new String[]{
            MediaStore.Images.Thumbnails.IMAGE_ID,
            MediaStore.Images.Thumbnails.DATA};
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {

            mDevList.clear();
            mDmrList.clear();

            upnpService = (AndroidUpnpService) service;
            MyApp.upnpService = upnpService;

            MyLog.d("Connected to UPnP Service");

            if (mediaServer == null && MyConstants.Setting.isAllowDMS) {
                try {
                    mediaServer = new MediaServer(mActivity);
                    upnpService.getRegistry()
                            .addDevice(mediaServer.getDevice());
                    DeviceItem localDevItem = new DeviceItem(
                            mediaServer.getDevice());

                    deviceListRegistryListener.deviceAdded(localDevItem);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            prepareMediaServer();
                        }
                    }).start();

                } catch (Exception ex) {
                    // TODO: handle exception
                    MyLog.e("Creating demo device failed" + ex);
                    showMsg(getString(R.string.create_demo_failed));
                    return;
                }
            }

            if (MyConstants.Setting.isAllowRender) {
                ZxtMediaRenderer mediaRenderer = new ZxtMediaRenderer(1, mActivity);
                upnpService.getRegistry().addDevice(mediaRenderer.getDevice());
                deviceListRegistryListener.dmrAdded(new DeviceItem(
                        mediaRenderer.getDevice()));
            }

            // xgf
            for (Device device : upnpService.getRegistry().getDevices()) {
                if (device.getType().getNamespace().equals("schemas-upnp-org")
                        && device.getType().getType().equals("MediaServer")) {
                    final DeviceItem display = new DeviceItem(device, device
                            .getDetails().getFriendlyName(),
                            device.getDisplayString(), "(REMOTE) "
                            + device.getType().getDisplayString());
                    deviceListRegistryListener.deviceAdded(display);
                }
            }

            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(deviceListRegistryListener);
            // Refresh device list
            upnpService.getControlPoint().search();

            // select first device by default
            if (null != mDevList && mDevList.size() > 0
                    && null == MyApp.deviceItem) {
                MyApp.deviceItem = mDevList.get(0);
            }
            if (null != mDmrList && mDmrList.size() > 0
                    && null == MyApp.dmrDeviceItem) {
                MyApp.dmrDeviceItem = mDmrList.get(0);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    protected int getContentId() {
        return R.layout.activity_upnp_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitleName.setText("数据共享");
//        mCbBoxShare.setFocusable(true);
//        mCbBoxShare.requestFocus();
    }

    @Override
    protected void initData() {
        super.initData();
        createFolder();
        getVideoFilePaths();
        createVideoThumb();
        getIp();
        mHandle = new Handler(this);
        LoggingUtil.resetRootHandler(new FixedAndroidHandler());
        Logger.getLogger("org.teleal.cling").setLevel(Level.INFO);
//        mCbBoxShare.setChecked(SharePreferencesUtil.getBoolean(MyConstants.SharedPrefrenceXml.USER_INFO_XML,
//                MyConstants.SharedPrefrenceKey.UPNP_SETTING_ALLOW_SHARE, true));
        deviceListRegistryListener = new DeviceListRegistryListener();
        if (mCbBoxShare.isChecked()) {
            getApplicationContext().bindService(
                    new Intent(mActivity, AndroidUpnpServiceImpl.class),
                    serviceConnection, Context.BIND_AUTO_CREATE);
        }
        mCbBoxShare.setClickable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
//        mCbBoxShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharePreferencesUtil.saveBoolean(MyConstants.SharedPrefrenceXml.USER_INFO_XML,
//                        MyConstants.SharedPrefrenceKey.UPNP_SETTING_ALLOW_SHARE, isChecked);
//                if (isChecked) {
//                    getApplicationContext().bindService(
//                            new Intent(mActivity, AndroidUpnpServiceImpl.class),
//                            serviceConnection, Context.BIND_AUTO_CREATE);
//                } else {
//                    showDialogWithCancelBtn("提示", "确定要关闭数据共享么?", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                getApplicationContext().unbindService(serviceConnection);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case GET_IP_FAIL:
                showGetIpFailDialog();
                break;
            case GET_IP_SUC:
                if (null != msg.obj) {
                    InetAddress inetAddress = (InetAddress) msg.obj;
                    if (null != inetAddress) {
                        setIp(inetAddress);
                        setIpInfo();
                    }
                } else {
                    showGetIpFailDialog();
                }
                break;
            case CREATE_THUMB_SUCCESS:
                dismissLoadingDialog();
                break;
            case CREATE_THUMB_FAIL:
                dismissLoadingDialog();
                showCreateThumbFailDialog();
                break;
        }
        return false;
    }

    private void createFolder() {
        FileUtil.createSDCardDir(true);
    }

    private void getIp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();

                InetAddress inetAddress;
                Message message = new Message();
                try {
                    inetAddress = InetAddress.getByName(String.format("%d.%d.%d.%d",
                            (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                            (ipAddress >> 24 & 0xff)));

                    hostName = inetAddress.getHostName();
                    hostAddress = inetAddress.getHostAddress();
                    message.obj = inetAddress;
                    message.what = GET_IP_SUC;
                    mHandle.sendMessage(message);
                } catch (UnknownHostException e) {
                    mHandle.sendEmptyMessage(GET_IP_FAIL);
                }
            }
        }).start();

    }

    private void getVideoFilePaths() {
        showLoadingDialog();
        mVideoFilePaths = new ArrayList<Map<String, String>>();
        Cursor cursor;
        String[] videoColumns = {
                MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION, MediaStore.Video.Media.RESOLUTION
        };
        cursor = mActivity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumns, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                String id = ContentTree.VIDEO_PREFIX
                        + cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Map<String, String> fileInfoMap = new HashMap<String, String>();
                fileInfoMap.put(id, filePath);
                mVideoFilePaths.add(fileInfoMap);
                // Log.v(LOGTAG, "added video item " + title + "from " +
                // filePath);
            } while (cursor.moveToNext());
        }
        if (null != cursor) {
            cursor.close();
        }

    }

    private void createVideoThumb() {
        if (null != mVideoFilePaths && mVideoFilePaths.size() > 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < mVideoFilePaths.size(); i++) {
                        Set entries = mVideoFilePaths.get(i).entrySet();
                        if (entries != null) {
                            Iterator iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                Object id = entry.getKey();
                                Object filePath = entry.getValue();

                                Bitmap videoThumb = ImageUtil.getThumbnailForVideo(filePath
                                        .toString());
                                String videoSavePath = ImageUtil.getSaveVideoFilePath(
                                        filePath.toString(), id.toString());
                                try {
                                    ImageUtil.saveBitmapWithFilePathSuffix(videoThumb,
                                            videoSavePath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mHandle.sendEmptyMessage(CREATE_THUMB_FAIL);
                                }
                            }
                        }
                    }
                    mHandle.sendEmptyMessage(CREATE_THUMB_SUCCESS);
                }

            }).start();
        }
    }

    private void setIp(InetAddress inetAddress) {
        MyApp.setLocalIpAddress(inetAddress);
    }

    private void setIpInfo() {
        MyApp.setHostName(hostName);
        MyApp.setHostAddress(hostAddress);
    }

    private void showGetIpFailDialog() {
        showDialog("提示", getString(R.string.ip_get_fail), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                finish();
            }
        });
    }

    private void showCreateThumbFailDialog() {
        showDialog("创建本地视频缩略图失败, 视频文件有可能无法打开");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class DeviceListRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry,
                                                 RemoteDevice device) {
            MyLog.d("UPNP remoteDeviceDiscoveryStarted RemoteDevice: " + device.toString() + device.getType().getType());
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry,
                                                final RemoteDevice device, final Exception ex) {
            MyLog.d("UPNP remoteDeviceDiscoveryFailed RemoteDevice: " + device.toString() + device.getType().getType());
        }

		/*
         * End of optimization, you can remove the whole block if your Android
		 * handset is fast (>= 600 Mhz)
		 */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            MyLog.d("UPNP RemoteDevice RemoteDevice: " + device.toString() + device.getType().getType());

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaServer")) {
                final DeviceItem display = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                deviceAdded(display);
            }

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaRenderer")) {
                final DeviceItem dmrDisplay = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                dmrAdded(dmrDisplay);
            }
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            MyLog.d("UPNP RemoteDevice RemoteDevice: " + device.toString() + device.getType().getType());
            final DeviceItem display = new DeviceItem(device,
                    device.getDisplayString());
            deviceRemoved(display);

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaRenderer")) {
                final DeviceItem dmrDisplay = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                dmrRemoved(dmrDisplay);
            }
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            MyLog.d("UPNP localDeviceAdded LocalDevice: " + device.toString() + device.getType().getType());

            final DeviceItem display = new DeviceItem(device, device
                    .getDetails().getFriendlyName(), device.getDisplayString(),
                    "(REMOTE) " + device.getType().getDisplayString());
            deviceAdded(display);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            MyLog.d("UPNP localDeviceRemoved LocalDevice: " + device.toString() + device.getType().getType());

            final DeviceItem display = new DeviceItem(device,
                    device.getDisplayString());
            deviceRemoved(display);
        }

        public void deviceAdded(final DeviceItem di) {
            MyLog.d("UPNP deviceAdded DeviceItem: " + di.toString());
            if (!mDevList.contains(di)) {
                mDevList.add(di);
            }
            if (null != mDevList && mDevList.size() > 0
                    && null == MyApp.deviceItem) {
                MyApp.deviceItem = mDevList.get(0);
            }
        }

        public void deviceRemoved(final DeviceItem di) {
            MyLog.d("UPNP deviceRemoved DeviceItem: " + di.toString());
            runOnUiThread(new Runnable() {
                public void run() {
                    mDevList.remove(di);
                }
            });

        }

        public void dmrAdded(final DeviceItem di) {
            MyLog.d("UPNP dmrAdded DeviceItem: " + di.toString());
            if (!mDmrList.contains(di)) {
                mDmrList.add(di);
            }
            if (null != mDmrList && mDmrList.size() > 0
                    && null == MyApp.dmrDeviceItem) {
                MyApp.dmrDeviceItem = mDmrList.get(0);
            }
        }

        public void dmrRemoved(final DeviceItem di) {
            runOnUiThread(new Runnable() {
                public void run() {
                    mDmrList.remove(di);
                }
            });
        }

    }

    private void prepareMediaServer() {

        if (serverPrepared)
            return;
        ContentNode rootNode = ContentTree.getRootNode();
        // Video Container
        Container videoContainer = new Container();
        videoContainer.setClazz(new DIDLObject.Class("object.container"));
        videoContainer.setId(ContentTree.VIDEO_ID);
        videoContainer.setParentID(ContentTree.ROOT_ID);
        videoContainer.setTitle("视频");
        videoContainer.setRestricted(true);
        videoContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
        videoContainer.setChildCount(0);

        rootNode.getContainer().addContainer(videoContainer);
        rootNode.getContainer().setChildCount(
                rootNode.getContainer().getChildCount() + 1);
        ContentTree.addNode(ContentTree.VIDEO_ID, new ContentNode(
                ContentTree.VIDEO_ID, videoContainer));

        Cursor cursor;
        String[] videoColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DESCRIPTION};
        cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = ContentTree.VIDEO_PREFIX
                        + cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String creator = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                long size = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long duration = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String resolution = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));

                String description = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));

                Res res = new Res(new MimeType(mimeType.substring(0,
                        mimeType.indexOf('/')), mimeType.substring(mimeType
                        .indexOf('/') + 1)), size, "http://"
                        + mediaServer.getAddress() + "/" + id);

                res.setDuration(duration / (1000 * 60 * 60) + ":"
                        + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                        + (duration % (1000 * 60)) / 1000);
                res.setResolution(resolution);

                VideoItem videoItem = new VideoItem(id, ContentTree.VIDEO_ID,
                        title, creator, res);

                // add video thumb Property
                String videoSavePath = ImageUtil.getSaveVideoFilePath(filePath,
                        id);
                DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
                        URI.create("http://" + mediaServer.getAddress()
                                + videoSavePath));
                DIDLObject.Property[] properties = {albumArtURI};
                videoItem.addProperties(properties);
                videoItem.setDescription(description);
                videoContainer.addItem(videoItem);
                videoContainer
                        .setChildCount(videoContainer.getChildCount() + 1);
                ContentTree.addNode(id,
                        new ContentNode(id, videoItem, filePath));

                // Log.v(LOGTAG, "added video item " + title + "from " +
                // filePath);
            } while (cursor.moveToNext());
        }

        // Audio Container
        Container audioContainer = new Container(ContentTree.AUDIO_ID,
                ContentTree.ROOT_ID, "音频", "GNaP MediaServer",
                new DIDLObject.Class("object.container"), 0);
        audioContainer.setRestricted(true);
        audioContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
        rootNode.getContainer().addContainer(audioContainer);
        rootNode.getContainer().setChildCount(
                rootNode.getContainer().getChildCount() + 1);
        ContentTree.addNode(ContentTree.AUDIO_ID, new ContentNode(
                ContentTree.AUDIO_ID, audioContainer));

        String[] audioColumns = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM};
        cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audioColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = ContentTree.AUDIO_PREFIX
                        + cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String creator = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                long size = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                long duration = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String album = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                Res res = null;
                try {
                    res = new Res(new MimeType(mimeType.substring(0,
                            mimeType.indexOf('/')), mimeType.substring(mimeType
                            .indexOf('/') + 1)), size, "http://"
                            + mediaServer.getAddress() + "/" + id);
                } catch (Exception e) {
                    MyLog.e("Exception1" + e);
                }

                if (null == res) {
                    break;
                }
                res.setDuration(duration / (1000 * 60 * 60) + ":"
                        + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                        + (duration % (1000 * 60)) / 1000);

                // Music Track must have `artist' with role field, or
                // DIDLParser().generate(didl) will throw nullpointException
                MusicTrack musicTrack = new MusicTrack(id,
                        ContentTree.AUDIO_ID, title, creator, album,
                        new PersonWithRole(creator, "Performer"), res);
                audioContainer.addItem(musicTrack);
                audioContainer
                        .setChildCount(audioContainer.getChildCount() + 1);
                ContentTree.addNode(id, new ContentNode(id, musicTrack,
                        filePath));

                // Log.v(LOGTAG, "added audio item " + title + "from " +
                // filePath);
            } while (cursor.moveToNext());
        }

        // get image thumbnail
        Cursor thumbCursor = this.managedQuery(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                imageThumbColumns, null, null, null);
        HashMap<Integer, String> imageThumbs = new HashMap<Integer, String>();
        if (null != thumbCursor && thumbCursor.moveToFirst()) {
            do {
                imageThumbs
                        .put(thumbCursor.getInt(0), thumbCursor.getString(1));
            } while (thumbCursor.moveToNext());

            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                thumbCursor.close();
            }
        }

        // Image Container
        Container imageContainer = new Container(ContentTree.IMAGE_ID,
                ContentTree.ROOT_ID, "图片", "GNaP MediaServer",
                new DIDLObject.Class("object.container"), 0);
        imageContainer.setRestricted(true);
        imageContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
        rootNode.getContainer().addContainer(imageContainer);
        rootNode.getContainer().setChildCount(
                rootNode.getContainer().getChildCount() + 1);
        ContentTree.addNode(ContentTree.IMAGE_ID, new ContentNode(
                ContentTree.IMAGE_ID, imageContainer));

        String[] imageColumns = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DESCRIPTION};
        cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, MediaStore.Images.Media.DATA);

        Container typeContainer = null;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Images.Media._ID));
                String id = ContentTree.IMAGE_PREFIX
                        + cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Images.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                String creator = "unkown";
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                long size = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

                String description = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION));

                String url = "http://" + mediaServer.getAddress() + "/"
                        + filePath;
                Res res = new Res(new MimeType(mimeType.substring(0,
                        mimeType.indexOf('/')), mimeType.substring(mimeType
                        .indexOf('/') + 1)), size, url);

                Container tempTypeContainer = null;
                if (null != typeContainer) {
                    tempTypeContainer = typeContainer;
                }
                if (TextUtils.isEmpty(fileName)) {
                    fileName = FileUtil.getFoldName(filePath);
                    typeContainer = new Container(
                            String.valueOf(mImageContaierId),
                            ContentTree.IMAGE_ID, fileName, "GNaP MediaServer",
                            new DIDLObject.Class("object.container"), 0);
                    typeContainer.setRestricted(true);
                    typeContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);

                    tempTypeContainer = typeContainer;
                    imageContainer.addContainer(tempTypeContainer);
                    imageContainer
                            .setChildCount(imageContainer.getChildCount() + 1);
                    ContentTree.addNode(String.valueOf(mImageContaierId),
                            new ContentNode(String.valueOf(mImageContaierId),
                                    tempTypeContainer));

                    ImageItem imageItem = new ImageItem(id,
                            String.valueOf(mImageContaierId), title, creator,
                            res);

                    if (imageThumbs.containsKey(imageId)) {
                        String thumb = imageThumbs.get(imageId);
                        MyLog.d(" image thumb:" + thumb);
                        // set albumArt Property
                        DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
                                URI.create("http://" + mediaServer.getAddress()
                                        + thumb));
                        DIDLObject.Property[] properties = {albumArtURI};
                        imageItem.addProperties(properties);
                    }
                    imageItem.setDescription(description);

                    tempTypeContainer.addItem(imageItem);
                    tempTypeContainer.setChildCount(tempTypeContainer
                            .getChildCount() + 1);
                    ContentTree.addNode(id, new ContentNode(id, imageItem,
                            filePath));
                } else {
                    if (!fileName.equalsIgnoreCase(FileUtil
                            .getFoldName(filePath))) {
                        mImageContaierId++;
                        fileName = FileUtil.getFoldName(filePath);

                        typeContainer = new Container(
                                String.valueOf(mImageContaierId),
                                ContentTree.IMAGE_ID, fileName,
                                "GNaP MediaServer", new DIDLObject.Class(
                                "object.container"), 0);
                        typeContainer.setRestricted(true);
                        typeContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);

                        tempTypeContainer = typeContainer;
                        imageContainer.addContainer(tempTypeContainer);
                        imageContainer.setChildCount(imageContainer
                                .getChildCount() + 1);
                        ContentTree.addNode(
                                String.valueOf(mImageContaierId),
                                new ContentNode(String
                                        .valueOf(mImageContaierId),
                                        tempTypeContainer));

                        ImageItem imageItem = new ImageItem(id,
                                String.valueOf(mImageContaierId), title,
                                creator, res);

                        if (imageThumbs.containsKey(imageId)) {
                            String thumb = imageThumbs.get(imageId);
                            MyLog.d(" image thumb:" + thumb);
                            // set albumArt Property
                            DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
                                    URI.create("http://"
                                            + mediaServer.getAddress() + thumb));
                            DIDLObject.Property[] properties = {albumArtURI};
                            imageItem.addProperties(properties);
                        }
                        imageItem.setDescription(description);
                        tempTypeContainer.addItem(imageItem);
                        tempTypeContainer.setChildCount(typeContainer
                                .getChildCount() + 1);
                        ContentTree.addNode(id, new ContentNode(id, imageItem,
                                filePath));
                    } else {
                        ImageItem imageItem = new ImageItem(id,
                                String.valueOf(mImageContaierId), title,
                                creator, res);

                        if (imageThumbs.containsKey(imageId)) {
                            String thumb = imageThumbs.get(imageId);
                            MyLog.d(" image thumb:" + thumb);
                            // set albumArt Property
                            DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
                                    URI.create("http://"
                                            + mediaServer.getAddress() + thumb));
                            DIDLObject.Property[] properties = {albumArtURI};
                            imageItem.addProperties(properties);
                        }
                        imageItem.setDescription(description);
                        tempTypeContainer.addItem(imageItem);
                        tempTypeContainer.setChildCount(typeContainer
                                .getChildCount() + 1);
                        ContentTree.addNode(id, new ContentNode(id, imageItem,
                                filePath));
                    }
                }

                // imageContainer.addItem(imageItem);
                // imageContainer
                // .setChildCount(imageContainer.getChildCount() + 1);
                // ContentTree.addNode(id,
                // new ContentNode(id, imageItem, filePath));

                MyLog.d("added image item " + title + "from " + filePath);
            } while (cursor.moveToNext());
        }
        serverPrepared = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry()
                    .removeListener(deviceListRegistryListener);
        }
        try {
            getApplicationContext().unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
