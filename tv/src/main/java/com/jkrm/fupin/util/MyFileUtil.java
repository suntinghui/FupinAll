package com.jkrm.fupin.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.upnp.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import okhttp3.internal.io.FileSystem;

/**
 * Created by Mulberry on 2017/12/29.
 */

public class MyFileUtil {

    private static final String TAG = "FileUtil";
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    public static void testAddress(Context mContext) {
        // 获取缓存目录 程序卸载后自动删除
        MyLog.d(TAG, "获取缓存目录路径, 程序卸载后自动删除" + "\n" + "getExternalCacheDir: " + mContext.getExternalCacheDir());
        // 应用在外部存储上的目录的file文件夹
        MyLog.d(TAG, "应用在外部存储上的目录的file文件夹路径" + "\n" + "getExternalFilesDir: " + mContext.getExternalFilesDir(""));
        // 相机拍摄的图片和视频保存的位置
        MyLog.d(TAG, "相机拍摄的图片和视频保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_DCIM>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM));
        // 警报的铃声
        MyLog.d(TAG, "警报的铃声保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_ALARMS>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_ALARMS));
        // 下载文件保存的位置
        MyLog.d(TAG, "下载文件保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_DOWNLOADS>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        // 电影保存的位置
        MyLog.d(TAG, "电影保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_MOVIES>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES));
        // 音乐保存的位置
        MyLog.d(TAG, "音乐保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_MUSIC>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        // 通知音保存的位置
        MyLog.d(TAG, "通知音保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_NOTIFICATIONS>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS));
        // 下载的图片保存的位置
        MyLog.d(TAG, "下载图片保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_PICTURES>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        // 用于保存(博客)的音频文件
        MyLog.d(TAG, "博客等音频保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_PODCASTS>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS));
        // 保存铃声的位置
        MyLog.d(TAG, "铃声保存路径" + "\n" + "getExternalFilesDir<Environment.DIRECTORY_RINGTONES>: " + mContext.getExternalFilesDir(Environment.DIRECTORY_RINGTONES));
        // 获取 Android 数据目录
        MyLog.d(TAG, "Android 数据目录路径" + "\n" + "getDataDirectory: " + Environment.getDataDirectory());
        // 获取 Android 下载/缓存内容目录
        MyLog.d(TAG, "Android 下载/缓存内容目录路径" + "\n" + "getDownloadCacheDirectory: " + Environment.getDownloadCacheDirectory());
        // sdcard路径 常用
        MyLog.d(TAG, "SD卡常用路径" + "\n" + "getExternalStorageDirectory: " + Environment.getExternalStorageDirectory());
        // 同 this.getExternalFilesDir(...)
        MyLog.d(TAG, "应用在外部存储上的目录的file文件夹路径<可直接创建>" + "\n" + "getExternalStoragePublicDirectory: " + Environment.getExternalStoragePublicDirectory(""));
        //agentWeb默认缓存地址测试
        MyLog.d(TAG, "agentWeb getCachePath: " + getCachePath(mContext));
        //agentWeb默认缓存地址测试
        MyLog.d(TAG, "agentWeb getExternalCachePath: " + getExternalCachePath(mContext));
        //data/data
        MyLog.d(TAG, "getDir app_webview: " + mContext.getDir("webview", Context.MODE_PRIVATE));
        MyLog.d(TAG, "getDir app_textures: " + mContext.getDir("textures", Context.MODE_PRIVATE));
        MyLog.d(TAG, "getDir databases: " + mContext.getDir("database", Context.MODE_PRIVATE));
        MyLog.d(TAG, "getDir databases: " + mContext.getDir("database", Context.MODE_PRIVATE));
    }

    static final String FILE_CACHE_PATH = "agentweb-cache";
    static final String AGENTWEB_CACHE_PATCH = File.separator + "agentweb-cache";
    static String AGENTWEB_FILE_PATH;

    static String getAgentWebFilePath(Context context) {
        if (!TextUtils.isEmpty(AGENTWEB_FILE_PATH)) {
            return AGENTWEB_FILE_PATH;
        }
        String dir = getDiskExternalCacheDir(context);
        File mFile = new File(dir, FILE_CACHE_PATH);
        try {
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
        } catch (Throwable throwable) {
            MyLog.d(TAG, "create dir exception");
        }
        MyLog.d(TAG, "path:" + mFile.getAbsolutePath() + "  path:" + mFile.getPath());
        return AGENTWEB_FILE_PATH = mFile.getAbsolutePath();

    }

    static String getDiskExternalCacheDir(Context context) {

        File mFile = context.getExternalCacheDir();
        if (Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(mFile))) {
            return mFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    public static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + AGENTWEB_CACHE_PATCH;
    }

    /**
     * @param context
     * @return AgentWeb 缓存路径
     */
    public static String getExternalCachePath(Context context) {
        return getAgentWebFilePath(context);
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {

        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;// 判断是否是文件
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[10240];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;

    }

    /**
     * 删除文件(不包含目录)
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if(file == null)
            return;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            // file.delete();
        } else {
            // SystemInfo.showToast(GrrzXrzAct.this, "文件不存在!");
        }
    }

    /**
     * 打开SD卡上指定的文件
     */
    public static void browseFile(String filepath, String fileName,
                                  Activity activity) {
        // String filepath = String.format("%s/%s/%s",
        // SystemInfo.getSdCardPath(), dirName, fileName);
        File file = new File(filepath);
        Log.i("test", "filepath: " + filepath + " ,fileName: " + fileName);
        if (file.exists()) {
            // 设置文件类型
            String type = checkFileType(fileName);
            Log.i("test", "type: " + type);
            Log.i("test", "取文件类型完成!");
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            if (!type.equals("")) {
                try {
                    intent.setDataAndType(Uri.fromFile(file), type);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(activity,
                            "文件打开异常,请到SD卡" + "MobilePlatform" + "目录中浏览！",
                            Toast.LENGTH_SHORT).show();
                    ;
                }
            } else
                Toast.makeText(activity, "该文件类型无法识别！", Toast.LENGTH_SHORT)
                        .show();
        }
    }

    /**
     * 缓存写入文件数据data/data目录中
     *
     * @param ser
     * @param file
     * @return
     */
    public static boolean saveObject(Activity activity, Serializable ser,
                                     String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = activity.openFileOutput(file, activity.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 写string到文件
     *
     * @param str
     */
    public static void writePiciFile(String str) {
        FileWriter writer = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/pici.bin");
            if (!f.exists()) {
                f.createNewFile();
            }

            writer = new FileWriter(f, false);
            writer.write(str);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写string到文件
     *
     * @param str
     */
    public static void writeDataToFile(String str, String path) {
        FileWriter writer = null;
        try {
            File f = new File(path);
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            writer = new FileWriter(f, false);
            writer.write(str);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     *
     * @return
     */
    public static String getPiciContent() {
        FileInputStream fileIS = null;
        BufferedReader buf = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/pici.bin");
            if (!f.exists()) {
                f.createNewFile();
            }
            fileIS = new FileInputStream(f.getAbsolutePath());
            buf = new BufferedReader(new InputStreamReader(fileIS));
            String readString = buf.readLine();

            return readString;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 创建目录
     */
    public static void createCatalog(String path) {
        File userFile = new File(path);
        if (userFile.exists()) {
            if (userFile.isFile()) {
                userFile.delete();
                userFile.mkdirs();
            }
        } else {
            userFile.mkdirs();
        }
    }

    /**
     * 写入序列化对象文件
     */
    public static void writeSerFile(String cachePath, Serializable obj) {
        File file = new File(cachePath);
        if (file.exists()) {
            try {
                deleteFile(file);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取序列化对象文件
     */
    public static Serializable readSerFile(String cachePath) {
        File file = new File(cachePath);
        if (file.exists()) {
            ObjectInputStream objectInputStream = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fis);
                return (Serializable) objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取文件中指定文件的指定单位的大小(后面部分方法的总调用)
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 获取下载 URL 的文件的大小
     */
    public static long getFileSize(String address) {
        long filesize = 0;
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(10000);
            urlConn.setReadTimeout(10000);
            urlConn.connect();
            filesize = urlConn.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConn.disconnect();
        }
        return filesize;
    }

    /**
     * 取文件类型
     */
    public static String checkFileType(String fileName) {
        Properties fileType = new Properties();
        Log.i("test", "取文件类型!");
        try {
            fileType.load(FileUtil.class
                    .getResourceAsStream("/assets/andoirfiletype.properties"));
        } catch (IOException e) {
            Log.e("Utils", e.toString());
            e.printStackTrace();
        }
        int length = fileName.length();
        int dotPosition = fileName.lastIndexOf(".");
        String sufix = fileName.substring(dotPosition, length);
        return fileType.getProperty(sufix);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹(目录)大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * @param @param  bytes
     * @param @return
     * @return String
     * @throws
     * @Title: bytes2kb
     * @Description: TODO byte to kb or mb
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1) {

            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    // 可以自己随意添加
    public static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {".tif", "image/tiff"},
            {".tiff", "image/tiff"},
            {".amr", "video/*"},
            //			{ "", "*/*" }
    };


    public static File createFileFromInputStream(Context context, String filePath) {

        try {
            File f = new File(context.getExternalCacheDir() + "/" + filePath);
            if (f.exists()) {
                return f;
            }

            AssetManager am = context.getAssets();
            InputStream inputStream = am.open(filePath);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    public static File createFileFromInputStream(Context context, String filePath, String prefix) {
        try {
            File f = new File(context.getExternalCacheDir() + "/" + prefix + filePath);
            if (f.exists()) {
                return f;
            }

            AssetManager am = context.getAssets();
            InputStream inputStream = am.open(filePath);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    public static String getPinyin(String fileName) {
//        return fileName;//TODO 20181018暂时不做转换了.
        return HanziToPinyin.getPinYin(MyFileUtil.format(fileName)).trim();
    }

    public static String format(String s){
        if(s == null)
            s = "";
        String tempS = MyFileUtil.getFileName(s);
        String str = tempS.replaceAll("[~*《》|-]", "").replaceAll("[·]", "").replaceAll("[：]", "");
        //        String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】《》‘；：”“’。，、？|-]", "");
//        str = str + getFileEnd(s);
        MyLog.d("转换format str: " + str);
        return str;
    }

    public static String getFileEnd(String url) {
        if(MyUtil.isTextNull(url)) {
            return "";
        }
        MyLog.d("转换getFileEnd s: " + url.substring(url.lastIndexOf(".")));
        return url.substring(url.lastIndexOf("."));
    }

    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return path;
        }

    }

    public static String getFileNameWithType(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        int start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1, path.length());
        } else {
            return null;
        }

    }

    //storage/emulated //storage/96107E...
    public static List<File> getExternalStorageHardDisk() {
        List<File> hardDiskFileList = new ArrayList<>();
        File file = new File("/storage");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0)
                return hardDiskFileList;
            else {
                MyLog.d(TAG, "getExternalStorageHardDisk file length: " + file.length());
            }
            //            StatFs
            for (File tempFile1 : files) {
                if (tempFile1 != null && tempFile1.exists() && tempFile1.isDirectory()) {
                    File[] subFile2 = tempFile1.listFiles();
                    if (subFile2 == null || subFile2.length <= 0)
                        continue;
                    for (File tempFile2 : subFile2) {
                        if (tempFile2.isDirectory() && tempFile2.getName().equals("Android")) {
                            hardDiskFileList.add(tempFile1);
                        }
                    }

                }
            }
        }
        for (File temp : hardDiskFileList) {
            MyLog.d(TAG, "getExternalStorageHardDisk: " + temp.getName() + " ,path: " + temp.getAbsolutePath());
        }
        return hardDiskFileList;
    }

    /**
     * 检测本机外接输入设备
     */
    public static void getDeviceInfo() {
        try {

            //获得外接USB输入设备的信息
            Process p = Runtime.getRuntime().exec("cat /proc/bus/input/devices");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                String deviceInfo = line.trim();

                //对获取的每行的设备信息进行过滤，获得自己想要的。
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 获取本机硬盘资源
     *
     * @return
     */
    public static List<File> getVolumeDescription() {
        List<File> hardDiskList = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) MyApp.getInstance().getSystemService(Context.STORAGE_SERVICE);
        Class<?> volumeInfoClazz = null;
        Method getDescriptionComparator = null;
        Method getBestVolumeDescription = null;
        Method getVolumes = null;
        Method isMountedReadable = null;
        Method getType = null;
        Method getPath = null;
        List<?> volumes = null;
        try {
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            getDescriptionComparator = volumeInfoClazz.getMethod("getDescriptionComparator");
            getBestVolumeDescription = StorageManager.class.getMethod("getBestVolumeDescription", volumeInfoClazz);
            getVolumes = StorageManager.class.getMethod("getVolumes");
            isMountedReadable = volumeInfoClazz.getMethod("isMountedReadable");
            getType = volumeInfoClazz.getMethod("getType");
            getPath = volumeInfoClazz.getMethod("getPath");
            volumes = (List<?>) getVolumes.invoke(mStorageManager);

            for (Object vol : volumes) {
                if (vol != null && (boolean) isMountedReadable.invoke(vol) && (int) getType.invoke(vol) == 0) {
                    File file = (File) getPath.invoke(vol);
                    String p1 = (String) getBestVolumeDescription.invoke(mStorageManager, vol);//U盘卷标名称
                    String p2 = file.getPath();//U盘路径
                    MyLog.d(TAG, "getVolumeDescription 卷标p1: " + p1 + " ,卷标p2: " + p2);
                    if (!TextUtils.isEmpty(p1) && p1.contains("新加卷")) {
                        File file2 = new File(file.getAbsolutePath());
                        hardDiskList.add(file2);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hardDiskList;
    }

    /**
     * 根据路径获得，某个文件或文件夹所在的存储器的内存空间总大小
     *
     * @return
     */
    public static long getTotalMemorySize(String path) {

        StatFs stat = new StatFs(path);

        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getBlockCount(); // block总数

        return totalBlocks * blockSize;

    }

    /**
     * 根据路径获得，某个文件或文件夹所在的存储器的内存空间还有多少可用
     *
     * @return
     */
    public static long getAvailableMemorySize(String path) {

        StatFs stat = new StatFs(path);

        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        return availableBlocks * blockSize;

    }

    public static File getUseDiskFile() {
        if(MyUtil.isEmptyList(MyApp.localSaveDiskPath)) {
            return null;
        }
        for(File file : MyApp.localSaveDiskPath) {
            if(getAvailableMemorySize(file.getAbsolutePath()) < 10000) {
                continue;
            }
            return file;
        }
        return null;
    }

    public static String inDb(String filePath) {
        String originName = null;
        for(CacheFileBean bean : MyApp.localDbDataList) {
            if(bean.getFilePath() != null && bean.getFilePath().equals(filePath)) {
                originName = bean.getOriginFileName();
                break;
            }
        }
        return originName;
    }

    public static boolean inDbHistory(String url) {
        for(CacheFileBean bean : MyApp.localDbDataList) {
            if(bean.getUrl() != null && bean.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}
