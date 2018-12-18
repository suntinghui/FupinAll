package com.jkrm.fupin.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.jkrm.fupin.interfaces.IPermissionListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by hzw on 2018/7/20.
 */

public class PermissionUtil {

    private static PermissionUtil instance;

    /**
     * 读写权限  自己可以添加需要判断的权限
     */
    public static String[] permissionsStorage = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public static boolean hasStoragePermission (Context mContexts) {
        for (String permission : permissionsStorage) {
            if (lacksPermission(mContexts,permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private PermissionUtil() {

    }

    public static PermissionUtil getInstance() {
        if(instance == null) {
            instance = new PermissionUtil();
        }
        return instance;
    }

    public void checkSinglePermission(Activity activity, String[] permissions, final IPermissionListener listener) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean) {
                            listener.granted();
                        } else {
                            listener.needToSetting();
                        }
                    }
                });
    }

    public void checkPermission(Activity activity, String[] permissions, final IPermissionListener listener) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            listener.granted();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            listener.shouldShowRequestPermissionRationale();
                        } else {
                            listener.needToSetting();
                        }
                    }
                });
    }

    public static void toAppSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }
}
