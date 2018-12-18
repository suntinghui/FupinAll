package com.jkrm.fupin.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.util.ImageUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzw on 2018/8/6.
 */

public class DmsUtil {

    public static void createVideoThumb(final Activity activity) {
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
                cursor = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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

    public static void setIpInfo(InetAddress inetAddress) {
        MyApp.setLocalIpAddress(inetAddress);
        MyApp.setHostName(inetAddress.getHostName());
        MyApp.setHostAddress(inetAddress.getHostAddress());
    }
}
