package com.jkrm.fupin.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.jkrm.fupin.interfaces.IPermissionListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by hzw on 2018/7/20.
 */

public class PermissionUtil {

    private static PermissionUtil instance;

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
