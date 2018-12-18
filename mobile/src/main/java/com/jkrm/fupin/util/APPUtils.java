package com.jkrm.fupin.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 应用辅助工具栏
 * 如：设备号，应用版本号以及版本名字
 *
 * @author lh
 * @version 1.0
 */
public class APPUtils {

    public static final String APP_FOLDER = "ShareFun";

    /**
     * 类型，获取版本信息
     */
    public enum TYPE_VERSION {
        /**
         * 版本名称
         */
        TYPE_VERSION_NAME,
        /**
         * 版本号
         */
        TYPE_VERSION_CODE
    }


    /**
     * 获取唯一设备id
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static final String getUniqueID(Context context) {
        if (context == null) {
            return null;
        }
        Context appContent = context.getApplicationContext();
        final TelephonyManager tm = (TelephonyManager) appContent.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(appContent.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    /**
     * 获取version_name或者version_code
     *
     * @param context
     * @param version_type {}
     * @return
     */
    public static final String getAppVersionInfo(Context context, TYPE_VERSION version_type) {
        if (context == null) {
            return null;
        }
        String versionCode = null;
        String versionName = null;
        Context appContent = context.getApplicationContext();

        PackageManager pm = appContent.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(appContent.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            versionName = null;
            versionCode = null;
        }
        if (version_type == TYPE_VERSION.TYPE_VERSION_CODE) {
            return versionCode;
        } else {
            return versionName;
        }

    }


    /**
     * 格式化文件大小单位
     *
     * @param size 单位 KB
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        BigDecimal result2 = new BigDecimal(Double.toString(kiloByte));
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB";

    }

    public static boolean isTopActivity(Context context, String activityName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString().contains(activityName);
        } else {
            return false;
        }
    }

    /**
     * 检查是否有新版本
     *
     * @param localVersionName
     * @param serviceVersionName
     * @return
     */
    public static boolean hasNewVersion(String localVersionName, String serviceVersionName) {
        localVersionName = localVersionName.replace(".", "_");
        serviceVersionName = serviceVersionName.replace(".", "_");
        boolean hasNew = false;
        if (TextUtils.isEmpty(localVersionName) || TextUtils.isEmpty(serviceVersionName)) {
            hasNew = false;
        } else {
            String localArrays[] = localVersionName.split("_");
            String servicesArrays[] = serviceVersionName.split("_");
            try {
                if (localArrays != null && localArrays.length > 0 && servicesArrays != null && servicesArrays.length > 0) {
                    int localLen = localArrays.length;
                    int servicesLen = servicesArrays.length;

                    for (int i = 0; i < servicesLen; i++) {
                        int li = 0;
                        if (localLen > i) {
                            li = Integer.parseInt(localArrays[i]);
                        }
                        int si = Integer.parseInt(servicesArrays[i]);
                        if (si > li) {
                            hasNew = true;
                            break;
                        } else if (si < li) {
                            hasNew = false;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else {
                    hasNew = false;
                }
            } catch (NumberFormatException e) {

            }

        }
        return hasNew;
    }


    /**
     * 获取已经安装的app_name
     *
     * @return
     */
    public static boolean getInstalledApp(Context context, String app_Name) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<PackageInfo> pinfo = pm.getInstalledPackages(0);
        List<String> listAppName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                PackageInfo info = pinfo.get(i);
                String appName = info.applicationInfo.loadLabel(pm).toString();
                listAppName.add(appName);
            }
        }
        return listAppName.contains(app_Name);
    }


    /**
     * 是否是横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }


    /**
     * 是否是竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1) {
            flag = false;
        }
        return flag;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param file
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 修改Activity的状态栏颜色
     *
     * @param activity   Activity
     * @param colorResId Color
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                Log.e("APPUtils", "FlymeSetStatusBarLightMode: ", e);
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                Log.e("APPUtils", "MIUISetStatusBarLightMode: ", e);
            }
        }
        return /*result*/false;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                MyLog.d(applicationInfo.uid + "");
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param uid 已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos){
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkAppRun(Context context, String packageName) {
        int uid = getPackageUid(context, packageName);
        if(uid > 0){
            boolean rstA = isAppRunning(context, packageName);
            boolean rstB = isProcessRunning(context, uid);
            if(rstA||rstB){
                //指定包名的程序正在运行中
            }else{
                //指定包名的程序未在运行中
            }
        }else{
            //应用未安装
        }
    }

    public static void toOtherApp(Context context, String packname) {
        PackageManager packageManager = context.getPackageManager();
        if(getPackageUid(context, packname) != -1) {
            Intent intent = packageManager.getLaunchIntentForPackage(packname);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有安装" + packname, 1).show();
        }
    }
}
