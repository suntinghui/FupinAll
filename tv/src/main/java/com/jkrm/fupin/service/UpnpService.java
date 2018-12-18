package com.jkrm.fupin.service;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.ui.activity.MainActivity2;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.upnp.dmr.ZxtMediaRenderer;
import com.jkrm.fupin.upnp.dms.ContentNode;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.dms.MediaServer;
import com.jkrm.fupin.upnp.util.FileUtil;
import com.jkrm.fupin.upnp.util.FixedAndroidHandler;
import com.jkrm.fupin.upnp.util.ImageUtil;
import com.jkrm.fupin.util.FileShareUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.jkrm.fupin.util.PollingUtils;

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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzw on 2018/10/16.
 */

public class UpnpService extends Service implements Handler.Callback {

    public static UpnpService instance;
    public static final int GET_IP_FAIL = 0;
    public static final int GET_IP_SUC = 1;
    public static final int CREATE_THUMB_SUCCESS = 2;
    public static final int CREATE_THUMB_FAIL = 3;
    private static final int SEARCH_START_LOCAL_DISK = 4;
    private static final int SEARCH_FINISH_LOCAL_DISK = 5;
    private static final int UPNP_DEVICE_ADD = 6;
    private static final int UPNP_DEVICE_REMOVE = 7;
    private static final int UPNP_DMR_ADD = 8;
    private static final int UPNP_DMR_REMOVE = 9;
    public static List<File> mVideoList = new ArrayList<>();
    public static List<File> mAudioList = new ArrayList<>();
    public static List<File> mImageList = new ArrayList<>();
    private MyApp mApp;
    private Handler mHandler;

    /**************************************upnp**********************************/

    private String hostName;
    private String hostAddress;
    // DMS + DMR
    public static int mImageContaierId = Integer.valueOf(ContentTree.IMAGE_ID) + 1;
    public static MediaServer mediaServer;
    private ArrayList<DeviceItem> mDevList = new ArrayList<>();
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
                    mediaServer = new MediaServer(mApp);
                    upnpService.getRegistry()
                            .addDevice(mediaServer.getDevice());
                    DeviceItem localDevItem = new DeviceItem(
                            mediaServer.getDevice());

                    deviceListRegistryListener.deviceAdded(localDevItem);
//                    prepareMediaServer();
                    prepareMediaServerNew();

                } catch (Exception ex) {
                    // TODO: handle exception
                    MyLog.e("Creating demo device failed" + ex);
                    return;
                }
            }

            if (MyConstants.Setting.isAllowRender) {
                ZxtMediaRenderer mediaRenderer = new ZxtMediaRenderer(1, mApp);
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case GET_IP_FAIL:
//                showGetIpFailDialog();
                break;
            case GET_IP_SUC:
                if (null != msg.obj) {
                    InetAddress inetAddress = (InetAddress) msg.obj;
                    if (null != inetAddress) {
                        setIp(inetAddress);
                        setIpInfo();
                        getLocalDisk();
                    }
                } else {
//                    showGetIpFailDialog();
                }
                break;
            case SEARCH_START_LOCAL_DISK:
                MyLog.d("upnp 检测本地硬盘开始");
                break;
            case SEARCH_FINISH_LOCAL_DISK:
                MyLog.d("upnp 检测本地硬盘结束");
                sendCheckMediaStoreBroad();
//                String[] mineTypes = new String[]{
//                  "video/mp4", "video/avi", "audio/mp3", "mp3/wav", "image/jpeg"
//                };
//                String[] checkPath = new String[MyApp.localSaveDiskPath.size()];
//                for(int i=0; i<checkPath.length; i++) {
//                    File file = MyApp.localSaveDiskPath.get(i);
//                    checkPath[i] = file + MyConstants.Constant.DISK_DIRECTORY;
//                }
//                MediaScannerConnection.scanFile(mApp, checkPath, mineTypes, new MediaScannerConnection.MediaScannerConnectionClient() {
//                    @Override
//                    public void onMediaScannerConnected() {
//                        MyLog.d("upnp service scanFile onMediaScannerConnected");
//                    }
//
//                    @Override
//                    public void onScanCompleted(String path, Uri uri) {
//                        MyLog.d("upnp service scanFile onScanCompleted path: " + path + " ,Uri: " + uri);
//                    }
//                });
                getApplicationContext().bindService(
                        new Intent(mApp, AndroidUpnpServiceImpl.class),
                        serviceConnection, Context.BIND_AUTO_CREATE);
//                checkFile(MyApp.localSaveDiskPath);
                break;
            case UPNP_DEVICE_ADD:
                DeviceItem di1 = (DeviceItem) msg.obj;
                if (!mDevList.contains(di1)) {
                    mDevList.add(di1);
                }
                if (null != mDevList && mDevList.size() > 0
                        && null == MyApp.deviceItem) {
                    MyApp.deviceItem = mDevList.get(0);
                }
                break;
            case UPNP_DEVICE_REMOVE:
                DeviceItem di2 = (DeviceItem) msg.obj;
                mDevList.remove(di2);
                break;
            case UPNP_DMR_ADD:
                DeviceItem di3 = (DeviceItem) msg.obj;
                if (!mDmrList.contains(di3)) {
                    mDmrList.add(di3);
                }
                if (null != mDmrList && mDmrList.size() > 0
                        && null == MyApp.dmrDeviceItem) {
                    MyApp.dmrDeviceItem = mDmrList.get(0);
                }
                break;
            case UPNP_DMR_REMOVE:
                DeviceItem di4 = (DeviceItem) msg.obj;
                mDmrList.remove(di4);
                break;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = MyApp.getInstance();
        mHandler = new Handler(this);
        instance = this;
    }

    @Override
    public void onDestroy() {
        if (upnpService != null) {
            upnpService.getRegistry()
                    .removeListener(deviceListRegistryListener);
        }
        try {
            getApplicationContext().unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deviceListRegistryListener = null;
        mHandler.removeMessages(UPNP_DEVICE_ADD);
        mHandler.removeMessages(UPNP_DEVICE_REMOVE);
        mHandler.removeMessages(UPNP_DMR_ADD);
        mHandler.removeMessages(UPNP_DMR_REMOVE);
        PollingUtils.stopPollingService(mApp, UpnpService.class, MyConstants.Action.ACTION_SERVICE_CHECK_UPNP);
//        stopSelf();
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        if(PermissionUtil.hasStoragePermission(mApp)) {
            LoggingUtil.resetRootHandler(new FixedAndroidHandler());
            Logger.getLogger("org.teleal.cling").setLevel(Level.INFO);
            deviceListRegistryListener = new DeviceListRegistryListener();
            getIp();
        }
    }

    private void getIp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) mApp.getApplicationContext().getSystemService(WIFI_SERVICE);
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
                    mHandler.sendMessage(message);
                } catch (UnknownHostException e) {
                    mHandler.sendEmptyMessage(GET_IP_FAIL);
                }
            }
        }).start();

    }


    private void getLocalDisk() {
        mHandler.sendEmptyMessage(SEARCH_START_LOCAL_DISK);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApp.localSaveDiskPath = MyFileUtil.getVolumeDescription();
                mHandler.sendEmptyMessage(SEARCH_FINISH_LOCAL_DISK);
            }
        }).start();
    }

    private void setIp(InetAddress inetAddress) {
        MyApp.setLocalIpAddress(inetAddress);
    }

    private void setIpInfo() {
        MyApp.setHostName(hostName);
        MyApp.setHostAddress(hostAddress);
    }

    private void showGetIpFailDialog() {
//        showDialog("提示", getString(R.string.ip_get_fail), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismissDialog();
//                finish();
//            }
//        });
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
            sendMsg(UPNP_DEVICE_ADD, di);
        }

        public void deviceRemoved(final DeviceItem di) {
            MyLog.d("UPNP deviceRemoved DeviceItem: " + di.toString());
            sendMsg(UPNP_DEVICE_REMOVE, di);
        }

        public void dmrAdded(final DeviceItem di) {
            MyLog.d("UPNP dmrAdded DeviceItem: " + di.toString());
            sendMsg(UPNP_DMR_ADD, di);
        }

        public void dmrRemoved(final DeviceItem di) {
            MyLog.d("UPNP dmrRemoved DeviceItem: " + di.toString());
            sendMsg(UPNP_DMR_REMOVE, di);
        }

    }

    public void prepareMediaServerNew() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(PermissionUtil.hasStoragePermission(MyApp.getInstance()) && mediaServer != null && !MyUtil.isEmptyList(MyApp.localSaveDiskPath)) {
                    checkFile(MyApp.localSaveDiskPath);
//                    getLocalDiskResToServer(mediaServer);
                    prepareMediaServer();
                }
            }
        }).start();
    }

    public static void getLocalDiskResToServer(MediaServer mediaServer) {
        ContentNode rootNode = ContentTree.resetRootNode();
//        ContentNode rootNode = ContentTree.getRootNode();
        // 视频
        getLocalDiskVideoResToServer(rootNode, mediaServer);
        // 音频
        getLocalDiskAudioResToServer(rootNode, mediaServer);
        // 图片
        getLocalDiskImageResToServer(rootNode, mediaServer);
    }

    public static void getLocalDiskVideoResToServer(ContentNode rootNode, MediaServer mediaServer) {
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
        for (File file : mVideoList) {
            String title = file.getName();
            String creator = "数字服务";
            String filePath = file.getAbsolutePath();
            String mimeType = MyFileUtil.getMIMEType(file);
            long size = (long) MyFileUtil.getFileOrFilesSize(filePath, MyFileUtil.SIZETYPE_B);
            long duration = 0;
            String resolution = "";
            String description = "";
            String tempPath = "";
            try {
                tempPath = URLEncoder.encode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tempPath = tempPath.replace("%3A", ":");
            tempPath = tempPath.replace("%2F", "/");
            //检测是否本机下载过
            String dbData = MyFileUtil.inDb(filePath);
            if (null != dbData) {
                title = dbData;
            }
            //            boolean isExistInCacheList = false;
            //            String originName = "";
            //            for(CacheFileBean bean : MyApp.localDbDataList) {
            //                if(bean.getFilePath() != null && bean.getFilePath().equals(filePath)) {
            //                    isExistInCacheList = true;
            //                    originName = bean.getOriginFileName();
            //                    break;
            //                }
            //            }
            //            if(isExistInCacheList) {
            //                title = originName;
            //            }
            MyLog.d("查询的视频路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                    + " ,duration: " + duration + " ,resolution: " + resolution + " ,description: " + description + " ,tempPath: " + tempPath);
            Res res = new Res(new MimeType(mimeType.substring(0,
                    mimeType.indexOf('/')), mimeType.substring(mimeType
                    .indexOf('/') + 1)), size, "http://"
                    + mediaServer.getAddress() + "/" + filePath);

            res.setDuration(duration / (1000 * 60 * 60) + ":"
                    + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                    + (duration % (1000 * 60)) / 1000);
            res.setResolution(resolution);

            VideoItem videoItem = new VideoItem(title, ContentTree.VIDEO_ID,
                    title, creator, res);

            // add video thumb Property
            //            String videoSavePath = ImageUtil.getSaveVideoFilePath(filePath,
            //                    title);
            //            DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
            //                    URI.create("http://" + mediaServer.getAddress()
            //                            + videoSavePath));
            //            DIDLObject.Property[] properties = {albumArtURI};
            //            videoItem.addProperties(properties);
            videoItem.setDescription(description);
            videoContainer.addItem(videoItem);
            videoContainer
                    .setChildCount(videoContainer.getChildCount() + 1);
            ContentTree.addNode(title,
                    new ContentNode(title, videoItem, filePath));
        }
    }

    public static void getLocalDiskAudioResToServer(ContentNode rootNode, MediaServer mediaServer) {
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

        for (File file : mAudioList) {
            String title = file.getName();
            //            try {
            //                String title1 = new String(title.getBytes("ISO8859-1"));
            //                String title2 = new String(title.getBytes("GBK"));
            //                String title3 = new String(title.getBytes("UTF-8"));
            //                String title4 = URLEncoder.encode(file.getName(), "ISO8859_1");
            //                String title5 = URLEncoder.encode(file.getName(), "GBK");
            //                String title6 = URLEncoder.encode(file.getName(), "UTF-8");
            //                MyLog.d("查询的音频名称转换 title1: " + title1 + " ,title2: " + title2 + " ,title3: " + title3
            //                    + " ,title4: " + title4 + " ,title5: " + title5 + " ,title6: " + title6);
            //            } catch (UnsupportedEncodingException e) {
            //                e.printStackTrace();
            //                title = "未知";
            //            }
            String creator = "数字服务";
            String filePath = file.getAbsolutePath();
            String mimeType = MyFileUtil.getMIMEType(file);
            long size = (long) MyFileUtil.getFileOrFilesSize(filePath, MyFileUtil.SIZETYPE_B);
            long duration = 0;
            String resolution = "";
            String description = "";
            String album = "";
            String tempPath = "";
            try {
                tempPath = URLEncoder.encode(tempPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tempPath = tempPath.replace("%3A", ":");
            tempPath = tempPath.replace("%2F", "/");
            //检测是否本机下载过
            String dbData = MyFileUtil.inDb(filePath);
            if (null != dbData) {
                title = dbData;
            }
            MyLog.d("查询的音频路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                    + " ,duration: " + duration + " ,album: " + album + " ,videoPath: " + tempPath);
            Res res = new Res(new MimeType(mimeType.substring(0,
                    mimeType.indexOf('/')), mimeType.substring(mimeType
                    .indexOf('/') + 1)), size, "http://"
                    + mediaServer.getAddress() + "/" + filePath);

            res.setDuration(duration / (1000 * 60 * 60) + ":"
                    + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                    + (duration % (1000 * 60)) / 1000);
            MusicTrack musicTrack = new MusicTrack(title,
                    ContentTree.AUDIO_ID, title, creator, album,
                    new PersonWithRole(creator, "Performer"), res);
            audioContainer.addItem(musicTrack);
            audioContainer
                    .setChildCount(audioContainer.getChildCount() + 1);
            ContentTree.addNode(title, new ContentNode(title, musicTrack,
                    filePath));
        }
    }

    public static void getLocalDiskImageResToServer(ContentNode rootNode, MediaServer mediaServer) {
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

        for (File file : mImageList) {
            String title = file.getName();
            //            try {
            //                title = URLEncoder.encode(file.getName(), "ISO8859_1");
            ////                title = URLEncoder.encode(file.getName(), "UTF-8");
            //            } catch (UnsupportedEncodingException e) {
            //                e.printStackTrace();
            //                title = "未知";
            //            }
            String creator = "数字服务";
            String filePath = file.getAbsolutePath();
            String mimeType = MyFileUtil.getMIMEType(file);
            long size = (long) MyFileUtil.getFileOrFilesSize(filePath, MyFileUtil.SIZETYPE_B);
            long duration = 0;
            String resolution = "";
            String description = "";
            String tempPath = "";
            try {
                tempPath = URLEncoder.encode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tempPath = tempPath.replace("%3A", ":");
            tempPath = tempPath.replace("%2F", "/");
            MyLog.d("查询的图片路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                    + " ,duration: " + duration + " ,resolution: " + resolution + " ,description: " + description + " ,tempPath: " + tempPath);
            Res res = new Res(new MimeType(mimeType.substring(0,
                    mimeType.indexOf('/')), mimeType.substring(mimeType
                    .indexOf('/') + 1)), size, "http://"
                    + mediaServer.getAddress() + "/" + filePath);
            //            Res res = new Res(new MimeType(mimeType.substring(0,
            //                    mimeType.indexOf('/')), mimeType.substring(mimeType
            //                    .indexOf('/') + 1)), size, "http://"
            //                    + mediaServer.getAddress() + "/" + title);

            res.setDuration(duration / (1000 * 60 * 60) + ":"
                    + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                    + (duration % (1000 * 60)) / 1000);
            res.setResolution(resolution);

            ImageItem imageItem = new ImageItem(title,
                    String.valueOf(mImageContaierId), title, creator,
                    res);
            imageItem.setDescription(description);

            imageContainer.addItem(imageItem);
            imageContainer.setChildCount(imageContainer
                    .getChildCount() + 1);
            ContentTree.addNode(title, new ContentNode(title, imageItem,
                    filePath));

        }
    }


    public static void checkFile(File file) {// 遍历文件，在这里是遍历sdcard
        if (file.isDirectory()) {// 判断是否是文件夹
            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
            if (files != null) {// 如果文件夹不为空
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    checkFile(f);// 递归调用
                }
            }
        } else if (file.isFile()) {// 判断是否是文件
            if (isContainChinese(file.getAbsolutePath()))
                return;
            if (MyUtil.isVideoType(file.getName())) {
                mVideoList.add(file);
            } else if (MyUtil.isAudioType(file.getName())) {
                mAudioList.add(file);
            } else if (MyUtil.isImageType(file.getName()) && !file.getAbsolutePath().contains(MyConstants.Constant.DISK_DIRECTORY_APP)) {//过滤应用app内部存储的图片. 该部分图片为下载的缩略图等, 非客户提供
                mImageList.add(file);
            }
        }
    }

    public static void checkFile(List<File> list) {// 遍历文件，在这里是遍历sdcard
        List<File> checkList = new ArrayList<>();
        mVideoList = new ArrayList<>();
        mAudioList = new ArrayList<>();
        mImageList = new ArrayList<>();
        for (File file : list) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY);
            if (tempFile.exists())
                checkList.add(tempFile);
        }
        for (File file : list) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY_APP);
            if (tempFile.exists())
                checkList.add(tempFile);
        }
        for (File file : checkList) {
            if (file.isDirectory()) {// 判断是否是文件夹
                File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
                if (files != null) {// 如果文件夹不为空
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        if (f.getName().contains("android-sdk-windows"))
                            continue;
                        if (isContainChinese(f.getAbsolutePath()))
                            continue;
                        checkFile(f);// 递归调用
                    }
                }
            } else if (file.isFile()) {// 判断是否是文件, 基本不会执行到这里, 与客户约定的存储结构
                if (isContainChinese(file.getName()))
                    continue;
                if (MyUtil.isVideoType(file.getName())) {
                    mVideoList.add(file);
                } else if (MyUtil.isAudioType(file.getName())) {
                    mAudioList.add(file);
                }
            }
        }
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private void sendMsg(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    private void prepareMediaServer() {
//        ContentNode rootNode = ContentTree.getRootNode();
        ContentNode rootNode = ContentTree.resetRootNode();
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
        for (File file : mVideoList) {
            String titleLocalVideo = file.getName();
            String creatorLocalVideo = "数字服务";
            String filePathLocalVideo = file.getAbsolutePath();
            String mimeTypeLocalVideo = MyFileUtil.getMIMEType(file);
            long sizeLocalVideo = (long) MyFileUtil.getFileOrFilesSize(filePathLocalVideo, MyFileUtil.SIZETYPE_B);
            long durationLocalVideo = 0;
            String resolutionLocalVideo = "";
            String descriptionLocalVideo = "";
            //检测是否本机下载过
            String dbDataLocalVideo = MyFileUtil.inDb(filePathLocalVideo);
            if (null != dbDataLocalVideo) {
                titleLocalVideo = dbDataLocalVideo;
            }
            MyLog.d("查询的视频路径 filePath: " + filePathLocalVideo + " ,title: " + titleLocalVideo + " ,creator: " + creatorLocalVideo + " ,mimeType: " + mimeTypeLocalVideo + " ,size: " + sizeLocalVideo
                    + " ,duration: " + durationLocalVideo + " ,resolution: " + resolutionLocalVideo + " ,description: " + descriptionLocalVideo);
            Res res = new Res(new MimeType(mimeTypeLocalVideo.substring(0,
                    mimeTypeLocalVideo.indexOf('/')), mimeTypeLocalVideo.substring(mimeTypeLocalVideo
                    .indexOf('/') + 1)), sizeLocalVideo, "http://"
                    + mediaServer.getAddress() + "/" + filePathLocalVideo);

            res.setDuration(durationLocalVideo / (1000 * 60 * 60) + ":"
                    + (durationLocalVideo % (1000 * 60 * 60)) / (1000 * 60) + ":"
                    + (durationLocalVideo % (1000 * 60)) / 1000);
            res.setResolution(resolutionLocalVideo);

            VideoItem videoItem = new VideoItem(titleLocalVideo, ContentTree.VIDEO_ID,
                    titleLocalVideo, creatorLocalVideo, res);

            // add video thumb Property
            //            String videoSavePath = ImageUtil.getSaveVideoFilePath(filePath,
            //                    title);
            //            DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
            //                    URI.create("http://" + mediaServer.getAddress()
            //                            + videoSavePath));
            //            DIDLObject.Property[] properties = {albumArtURI};
            //            videoItem.addProperties(properties);
            videoItem.setDescription(descriptionLocalVideo);
            videoContainer.addItem(videoItem);
            videoContainer
                    .setChildCount(videoContainer.getChildCount() + 1);
            ContentTree.addNode(titleLocalVideo,
                    new ContentNode(titleLocalVideo, videoItem, filePathLocalVideo));
        }
        Cursor cursor;
        String[] videoColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DESCRIPTION };
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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

                if(!checkPathFilter(filePath, 0)) {
                    continue;
                }
                if(mVideoList.contains(new File(filePath))) {
                    continue;
                }
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
                MyLog.d("查询的视频路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                        + " ,duration: " + duration + " ,resolution: " + resolution + " ,description: " + description);
                VideoItem videoItem = new VideoItem(id, ContentTree.VIDEO_ID,
                        title, creator, res);

                // add video thumb Property
                String videoSavePath = ImageUtil.getSaveVideoFilePath(filePath,
                        id);
                DIDLObject.Property albumArtURI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(
                        URI.create("http://" + mediaServer.getAddress()
                                + videoSavePath));
                DIDLObject.Property[] properties = { albumArtURI };
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

        for (File audioFile : mAudioList) {
            String titleLocalAudio = audioFile.getName();
            String creatorLocalAudio = "数字服务";
            String filePathLocalAudio = audioFile.getAbsolutePath();
            String mimeTypeLocalAudio = MyFileUtil.getMIMEType(audioFile);
            long sizeLocalAudio = (long) MyFileUtil.getFileOrFilesSize(filePathLocalAudio, MyFileUtil.SIZETYPE_B);
            long durationLocalAudio = 0;
            String resolutionLocalAudio = "";
            String descriptionLocalAudio = "";
            String albumLocalAudio = "";
            //检测是否本机下载过
            String dbDataAudio = MyFileUtil.inDb(filePathLocalAudio);
            if (null != dbDataAudio) {
                titleLocalAudio = dbDataAudio;
            }
            MyLog.d("查询的音频路径 filePath: " + filePathLocalAudio + " ,title: " + titleLocalAudio + " ,creator: " + creatorLocalAudio + " ,mimeType: " + mimeTypeLocalAudio + " ,size: " + sizeLocalAudio
                    + " ,duration: " + durationLocalAudio + " ,album: " + albumLocalAudio);
            Res res = new Res(new MimeType(mimeTypeLocalAudio.substring(0,
                    mimeTypeLocalAudio.indexOf('/')), mimeTypeLocalAudio.substring(mimeTypeLocalAudio
                    .indexOf('/') + 1)), sizeLocalAudio, "http://"
                    + mediaServer.getAddress() + "/" + filePathLocalAudio);

            res.setDuration(durationLocalAudio / (1000 * 60 * 60) + ":"
                    + (durationLocalAudio % (1000 * 60 * 60)) / (1000 * 60) + ":"
                    + (durationLocalAudio % (1000 * 60)) / 1000);
            MusicTrack musicTrack = new MusicTrack(titleLocalAudio,
                    ContentTree.AUDIO_ID, titleLocalAudio, creatorLocalAudio, albumLocalAudio,
                    new PersonWithRole(creatorLocalAudio, "Performer"), res);
            audioContainer.addItem(musicTrack);
            audioContainer
                    .setChildCount(audioContainer.getChildCount() + 1);
            ContentTree.addNode(titleLocalAudio, new ContentNode(titleLocalAudio, musicTrack,
                    filePathLocalAudio));
        }

        String[] audioColumns = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM };
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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

                if (!checkPathFilter(filePath, 1)) {
                    continue;
                }

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
                    e.printStackTrace();
                }

                if (null == res) {
                    break;
                }
                res.setDuration(duration / (1000 * 60 * 60) + ":"
                        + (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
                        + (duration % (1000 * 60)) / 1000);

                MyLog.d("查询的音频路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                        + " ,duration: " + duration + " ,album: " + album);

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

        //image Container
        Cursor thumbCursor = getContentResolver().query(
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
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                if (!checkPathFilter(filePath, 2)) {
                    continue;
                }
                MyLog.d("查询的图片路径 filePath: " + filePath);
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

                imageContainer.addItem(imageItem);
                imageContainer.setChildCount(imageContainer
                        .getChildCount() + 1);
                ContentTree.addNode(id, new ContentNode(id, imageItem,
                        filePath));
            } while (cursor.moveToNext());
        }
        try {
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCheckMediaStoreBroad() {
        List<File> sxfpFileList = new ArrayList<>();
        for(int i=0; i<MyApp.localSaveDiskPath.size(); i++) {
            File file = MyApp.localSaveDiskPath.get(i);
            File newFile = new File(file.getAbsoluteFile() + MyConstants.Constant.DISK_DIRECTORY);
            MyLog.d("upnp service sendCheckMediaStoreBroad: newFile Path: " + newFile.getAbsolutePath());
            sxfpFileList.add(newFile);
            File appFile = new File(file.getAbsoluteFile() + MyConstants.Constant.DISK_DIRECTORY_APP);
            MyLog.d("upnp service sendCheckMediaStoreBroad: newFile Path: " + appFile.getAbsolutePath());
            sxfpFileList.add(appFile);
        }
        for(File file : sxfpFileList) {
            Uri contentUri = Uri.fromFile(file);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
            sendBroadcast(mediaScanIntent);
            MyLog.d("upnp service sendCheckMediaStoreBroad 检测路径 uri: " + contentUri);
        }
    }

    /**
     *
     * @param path 文件路径
     * @param checkTag 检测类型 视频0 音频1 图片2
     * @return
     */
    private boolean checkPathFilter(String path, int checkTag) {
        MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path);
        if(path == null)
            return false;
        if(path.contains("android-sdk-windows")) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path路径不符合规则1, 返回false");
            return false;
        }
        if(!path.contains(MyConstants.Constant.DISK_DIRECTORY) && !path.contains(MyConstants.Constant.DISK_DIRECTORY_APP)) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path路径不符合规则2, 返回false");
            return false;
        }
        //MediaStore 存在垃圾数据, 已删除的有可能仍然存在, 检测在存储空间是否存在
        File file = new File(path);
        if(!file.exists()) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path 文件不存在, 返回false");
            return false;
        }
        switch (checkTag) {
            case 0:
                if(!MyUtil.isVideoType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是视频, 返回false");
                    return false;
                }
                break;
            case 1:
                if(!MyUtil.isAudioType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是音频, 返回false");
                    return false;
                }
                break;
            case 2:
                if(!MyUtil.isImageType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是图片, 返回false");
                    return false;
                }
                break;
        }
        return true;
    }
}
