package com.jkrm.fupin.ui.activity.upnp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

import com.jkrm.fupin.MainActivity;
import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.upnp.com.zxt.dlna.activity.IndexActivity;
import com.jkrm.fupin.upnp.com.zxt.dlna.activity.StartActivity;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.util.FileUtil;
import com.jkrm.fupin.upnp.util.ImageUtil;
import com.jkrm.fupin.util.PermissionUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * DMS 媒体共享
 * Created by hzw on 2018/8/6.
 */

public class DmsActivity extends BaseActivity implements Handler.Callback {

    public static final int GET_IP_FAIL = 0;
    public static final int GET_IP_SUC = 1;
    private Handler mHandler = new Handler(this);

    @Override
    protected int getContentId() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();
        //检测权限
        PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new IPermissionListener() {
            @Override
            public void granted() {
                showMsg("读写权限通过，开始获取ip");
                createFolder();
                createVideoThumb();
                getIp();
            }

            @Override
            public void shouldShowRequestPermissionRationale() {
                showMsg("请开启文件读写权限，以便获取共享数据内容");
                PermissionUtil.toAppSetting(mActivity);
            }

            @Override
            public void needToSetting() {
                showMsg("请开启文件读写权限，以便获取共享数据内容");
                PermissionUtil.toAppSetting(mActivity);
            }
        });
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
                    message.obj = inetAddress;
                    message.what = GET_IP_SUC;
                    mHandler.sendMessage(message);
                } catch (UnknownHostException e) {
                    mHandler.sendEmptyMessage(GET_IP_FAIL);
                }
            }
        }).start();

    }

    private void createVideoThumb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, String>> mVideoFilePaths = new ArrayList<Map<String, String>>();
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

                if (null != mVideoFilePaths && mVideoFilePaths.size() > 0) {
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
                                }
                            }
                        }
                    }
                }

            }
        }).start();
    }

    private void setIpInfo(InetAddress inetAddress) {
        MyApp.setLocalIpAddress(inetAddress);
        MyApp.setHostName(inetAddress.getHostName());
        MyApp.setHostAddress(inetAddress.getHostAddress());
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case GET_IP_FAIL:
                showMsg(getString(R.string.ip_get_fail));
                break;
            case GET_IP_SUC:
                if (null != msg.obj) {
                    showMsg(getString(R.string.ip_get_success));
                    InetAddress inetAddress = (InetAddress) msg.obj;
                    if (null != inetAddress) {
                        setIpInfo(inetAddress);
                        //ip获取地址成功, 开始连接UPNP
                        jumpToMain();
                    }
                } else {
                    showMsg(getString(R.string.ip_get_fail));
                }
                break;
        }
        return false;
    }

    private void jumpToMain() {
        Intent intent = new Intent(mActivity, IndexActivity.class);
        startActivity(intent);
        this.finish();
    }
}
