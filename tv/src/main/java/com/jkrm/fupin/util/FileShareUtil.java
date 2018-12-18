package com.jkrm.fupin.util;

import android.net.Uri;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.upnp.dms.ContentNode;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.dms.MediaServer;
import com.jkrm.fupin.upnp.util.ImageUtil;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzw on 2018/10/15.
 */

public class FileShareUtil {

    private static int mImageContaierId = Integer.valueOf(ContentTree.IMAGE_ID) + 1;
    public static List<File> mVideoList = new ArrayList<>();
    public static List<File> mAudioList = new ArrayList<>();
    public static List<File> mImageList = new ArrayList<>();

    public static void getLocalDiskResToServer(MediaServer mediaServer) {
        ContentNode rootNode = ContentTree.getRootNode();
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
            if(null != dbData) {
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
            if(null != dbData) {
                title = dbData;
            }
            MyLog.d("查询的音频路径 filePath: " + filePath + " ,title: " + title + " ,creator: " + creator + " ,mimeType: " + mimeType + " ,size: " + size
                    + " ,duration: " + duration + " ,album: " + album + " ,videoPath: " + tempPath);
            Res res = new Res(new MimeType(mimeType.substring(0,
                    mimeType.indexOf('/')), mimeType.substring(mimeType
                    .indexOf('/') + 1)), size, "http://"
                    + mediaServer.getAddress() + "/" + tempPath);

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
                    + mediaServer.getAddress() + "/" + tempPath);
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
            if(isContainChinese(file.getAbsolutePath()))
                return;
            if (MyUtil.isVideoType(file.getName())) {
                mVideoList.add(file);
            } else if (MyUtil.isAudioType(file.getName())) {
                mAudioList.add(file);
            } else  if(MyUtil.isImageType(file.getName())) {
                mImageList.add(file);
            }
        }
    }

    public static void checkFile(List<File> list) {// 遍历文件，在这里是遍历sdcard
        List<File> checkList = new ArrayList<>();
        for(File file : list) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY);
            if(tempFile.exists())
                checkList.add(tempFile);
        }
        for(File file : list) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY_APP);
            if(tempFile.exists())
                checkList.add(tempFile);
        }
        for(File file : checkList) {
            if (file.isDirectory()) {// 判断是否是文件夹
                File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
                if (files != null) {// 如果文件夹不为空
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        if(f.getName().contains("android-sdk-windows"))
                            continue;
                        if(isContainChinese(f.getAbsolutePath()))
                            continue;
                        checkFile(f);// 递归调用
                    }
                }
            } else if (file.isFile()) {// 判断是否是文件
                if(isContainChinese(file.getName()))
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
}
