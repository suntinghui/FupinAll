package com.jkrm.fupin.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jkrm.fupin.R;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.interfaces.IUpdateCancelInterface;
import com.jkrm.fupin.widgets.DialogInfo;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by hzw on 2018/6/4.
 */

public class UpdateUtil {

    /**
     * Manifest.permission.READ_EXTERNAL_STORAGE,
     Manifest.permission.REQUEST_INSTALL_PACKAGES
     * @param activity
     * @param updateBean
     */
    public static void handleUpdate(final Activity activity, final UpdateBean updateBean, final IUpdateCancelInterface iUpdateCancelInterface) {
//        updateBean.set_force_update(2);
        if (updateBean.is_latest()) {
            // 需要版本更新 展示弹框
            final DialogInfo mDialog = new DialogInfo(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.update_version_layout, null);
            TextView textView = (TextView) view.findViewById(R.id.textView11);
            TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
            TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
            Dialog log = mDialog.showCustomeDialogFullForDialog(activity, view, 0.5);
            mDialog.CanceledOnTouchOutside(false);
            if (updateBean.getIs_force_update() == 1) {//强制更新
                if (log != null) {
                    log.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            if (i == KeyEvent.KEYCODE_BACK) {
                                activity.finish();
                            }
                            return false;
                        }
                    });
                }
                PermissionUtil.getInstance().checkPermission(activity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        uploadAppFile(activity, updateBean.getUrl(), iUpdateCancelInterface);
                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        //用户拒绝了此权限
                        Toast.makeText(activity, "APP版本强制更新失败，请您在系统设置授予APP存储权限后才可正常下载安装", Toast.LENGTH_LONG).show();
                        activity.finish();
                        return;
                    }

                    @Override
                    public void needToSetting() {
                        //用户拒绝了此权限
                        Toast.makeText(activity, "APP版本强制更新失败，请您在系统设置授予APP存储权限后才可正常下载安装", Toast.LENGTH_LONG).show();
                        activity.finish();
                        return;
                    }
                });
                tvOk.setVisibility(View.VISIBLE);
                tvNo.setVisibility(View.GONE);
                textView.setText("发现新版本，请您更新至最新版本" + updateBean.getVersion());
                tvOk.setText("强制更新");
                tvOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view1) {
                                                // : 2018/3/8 去下载对应apk
                                                mDialog.hiddleDialog();
                                                uploadAppFile(activity, updateBean.getUrl(), iUpdateCancelInterface);
                                            }
                                        }
                );
            } else {
                textView.setText("发现新版本，是否更新到" + updateBean.getVersion() + "版本?");
                tvOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view1) {
                                                // : 2018/3/8 去下载对应apk
                                                mDialog.hiddleDialog();
                                                PermissionUtil.getInstance().checkPermission(activity, new String[]{
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                }, new IPermissionListener() {
                                                    @Override
                                                    public void granted() {
                                                        uploadAppFile(activity, updateBean.getUrl(), iUpdateCancelInterface);
                                                    }

                                                    @Override
                                                    public void shouldShowRequestPermissionRationale() {
                                                        //用户拒绝了此权限
                                                        Toast.makeText(activity, "新版本下载失败，请您在系统设置授予APP存储权限后才可正常下载安装", Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void needToSetting() {
                                                        //用户拒绝了此权限
                                                        Toast.makeText(activity, "新版本下载失败，请您在系统设置授予APP存储权限后才可正常下载安装", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                );
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        mDialog.hiddleDialog();
                        iUpdateCancelInterface.cancelUpdate();
                    }
                });
            }
        }
    }

    private static void uploadAppFile(final Activity activity, final String url, final IUpdateCancelInterface iUpdateCancelInterface) {
        final DialogInfo mDialog = new DialogInfo(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.download_version_layout, null);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        progressBar.setMax(100);
        final TextView textView = (TextView) view.findViewById(R.id.tv_progress);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileDownloadUtil.get().cancel(activity, url);
                mDialog.hiddleDialog();
                iUpdateCancelInterface.cancelUpdate();
            }
        });
        mDialog.showCustomeDialogFull(activity, view, 0.5);
        mDialog.CanceledOnTouchOutside(false);
        FileDownloadUtil.get().downApk(activity, url, new FileDownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                Observable.just(path).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                                mDialog.hiddleDialog();
                                File apkFile = new File(s);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    // Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri contentUri = FileProvider.getUriForFile(
                                            activity
                                            , "com.jkrm.fupin.DownloadProvider"
                                            , apkFile);
                                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                } else {
                                    //Log.w(TAG, "正常进行安装");
                                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                                }
                                activity.startActivity(intent);
                            }
                        });
            }

            @Override
            public void onDownloading(final int progress) {
                Observable.just(progress).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                progressBar.setProgress(progress);
                                textView.setText(progress + "%");

                            }
                        });
            }

            @Override
            public void onDownloadFailed(String path) {
                Observable.just(0).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                                mDialog.hiddleDialog();
                                iUpdateCancelInterface.cancelUpdate();
                            }
                        });

            }

            @Override
            public void onDownloadIngRejectRepeat() {

            }
        });
    }
}
