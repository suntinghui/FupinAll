package com.jkrm.fupin.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.support.model.DIDLContent;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

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

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    /************************************************* other **************************************************/
    public static HashMap<String, Call> downCalls = new HashMap<>();

    /************************************************* upnp ***************************************************/
    public static DeviceItem deviceItem;
    public DIDLContent didl;
    public static DeviceItem dmrDeviceItem;
    public static boolean isLocalDmr = true;
    public static AndroidUpnpService upnpService;
    public static ArrayList<DeviceItem> mDevList;
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

    public static ArrayList<DeviceItem> getmDevList() {
        return mDevList;
    }

    public static void setmDevList(ArrayList<DeviceItem> mDevList) {
        MyApp.mDevList = mDevList;
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
