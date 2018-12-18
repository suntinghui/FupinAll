package com.jkrm.fupin.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.bean.OssListLocalConvertBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.util.MyFileUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.support.model.DIDLContent;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hzw on 2018/7/18.
 */
public class MyApp extends Application {

    private static MyApp instance;

    private static final String weixinId = "wx85457c561b68e6f4";

    public static MyApp getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        initImageLoader(getApplicationContext());
        //初始化播放器
        AliVcMediaPlayer.init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /************************************************* other **************************************************/
    public static HashMap<String, Call> downCalls = new HashMap<>();
    public static List<File> localSaveDiskPath = new ArrayList<>();
    public static File localSaveDiskFilePath;
    public static List<CacheFileBean> localDbDataList = new ArrayList<>();
    public static List<OssListLocalConvertBean> ossDownloadList = new ArrayList<>();
    public static boolean isShutDown = false;

    /************************************************ SEARCH *************************************************/
    public static List<VodTypeBean> vodTypeBeanList;
    public static List<VodTypeBean> audTypeBeanList;


    /************************************************* upnp ***************************************************/
    public static DeviceItem deviceItem;

    public DIDLContent didl;

    public static DeviceItem dmrDeviceItem;

    public static boolean isLocalDmr = true;
    public static AndroidUpnpService upnpService;

    public static Context mContext;

    private static InetAddress inetAddress;

    private static String hostAddress;

    private static String hostName;

    public static Context getContext() {
        return mContext;
    }

    public static void setLocalIpAddress(InetAddress inetAddr) {
        inetAddress = inetAddr;

    }

    public static InetAddress getLocalIpAddress() {
        return inetAddress;
    }

    public static String getHostAddress() {
        return hostAddress;
    }

    public static void setHostAddress(String hostAddress) {
        MyApp.hostAddress = hostAddress;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        MyApp.hostName = hostName;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
