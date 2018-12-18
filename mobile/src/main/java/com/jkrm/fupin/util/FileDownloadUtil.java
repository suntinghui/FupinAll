package com.jkrm.fupin.util;


import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hzw on 2018/08/13
 */

public class FileDownloadUtil {

    private static FileDownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static FileDownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new FileDownloadUtil();
        }
        return downloadUtil;
    }

    private FileDownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void downImg(final Context context, final String imgUrl, final String videoId, final OnDownloadListener listener) {
        if(TextUtils.isEmpty(imgUrl)) {
            return;
        }
        if(MyApp.downCalls.containsKey(imgUrl)) {
            return;
        }
        final File file = createImgFile(context, videoId);
        get(imgUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                cancel(context, imgUrl);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    //view.setMax(total);//为progressDialog设置大小
                    if (is != null) {
                        Log.d("SettingPresenter", "onResponse: 不为空");
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[2048];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            int progress = (int) (process * 1.0f / total * 100);
                        }
                    }
                    fos.flush();
                    MyApp.downCalls.remove(imgUrl);
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    //view.downFial();
                    Log.d("SettingPresenter", e.toString());
                    cancel(context, imgUrl);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            //private void down() {
            // progressDialog.cancel();
            // }
        });
    }


    public void down(final Context context, final CacheFileBean cacheFileBean, final OnDownloadListener listener) {
        final String url = cacheFileBean.getUrl();
        if(MyApp.downCalls.containsKey(url)) {
            listener.onDownloadIngRejectRepeat();
            return;
        }
        final File file = getFile(context, url, cacheFileBean.getFileName() + getFileEnd(url));
        cacheFileBean.setProgress(0);
        cacheFileBean.setUrl(url);
        cacheFileBean.setCurrentSize(0);
        cacheFileBean.setFileName(cacheFileBean.getFileName() + getFileEnd(url));
        cacheFileBean.setFile(file);
        cacheFileBean.setFileSize(0);
        cacheFileBean.setFilePath(file.getAbsolutePath());
        boolean isInsert = DaoCacheFileUtil.getInstance().insertBean(cacheFileBean);
        if(!isInsert) {
            failWithFileName(context, cacheFileBean, listener);
            return;
        }
        get(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                failWithFileName(context, cacheFileBean, listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    cacheFileBean.setFileSize(total);
                    //view.setMax(total);//为progressDialog设置大小
                    if (is != null) {
                        Log.d("SettingPresenter", "onResponse: 不为空");
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[2048];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            int progress = (int) (process * 1.0f / total * 100);
                            int preProgress = cacheFileBean.getProgress();
//                            MyLog.d("下载progress: " + progress + " ,preProgress: " + preProgress);
                            if(progress != 100) {
                                if(progress < 5) {
                                    cacheFileBean.setCurrentSize((long)process);
                                    progress(cacheFileBean, listener, progress);
                                } else {
                                    if(progress -  preProgress > 20) {
                                        cacheFileBean.setCurrentSize((long)process);
                                        progress(cacheFileBean, listener, progress);
                                    }
                                }
                            } else {
                                cacheFileBean.setCurrentSize((long)process);
                                progress(cacheFileBean, listener, progress);
                            }
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    success(url, listener, file.getAbsolutePath());
                } catch (Exception e) {
                    //view.downFial();
                    Log.d("SettingPresenter", e.toString());
                    failWithFileName(context, cacheFileBean, listener);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            //private void down() {
            // progressDialog.cancel();
            // }
        });
    }

    public void downApk(final Context context, final String url, final OnDownloadListener listener) {
        if(MyApp.downCalls.containsKey(url)) {
            listener.onDownloadIngRejectRepeat();
            return;
        }
        final File file = getFile(context, url);
        get(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                fail(context, url, listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    //view.setMax(total);//为progressDialog设置大小
                    if (is != null) {
                        Log.d("SettingPresenter", "onResponse: 不为空");
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[2048];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            int progress = (int) (process * 1.0f / total * 100);
                            listener.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    success(url, listener, file.getAbsolutePath());
                } catch (Exception e) {
                    //view.downFial();
                    Log.d("SettingPresenter", e.toString());
                    fail(context, url, listener);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            //private void down() {
            // progressDialog.cancel();
            // }
        });
    }

    public void get(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES);
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .build();
        Call call = client.newCall(request);
        MyApp.downCalls.put(address, call);//把这个添加到call里,方便取消
        call.enqueue(callback);
    }


    public void downAuto(final Context context, final CacheFileBean cacheFileBean, final OnDownloadListener listener) {
        final String url = cacheFileBean.getUrl();
        MyFileUtil.deleteFile(getFile(context, url, cacheFileBean.getFileName()));//先删除本地之前的视频 以免资源异常
        if(MyApp.downCalls.containsKey(url)) {
            MyLog.d("启动自检是否需要继续下载, 已在下载队列, 停住下载");
            return;
        }
        final File file = getFile(context, url, cacheFileBean.getFileName());
        cacheFileBean.setFile(file);
        getAuto(cacheFileBean, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                failWithFileName(context, cacheFileBean, listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    cacheFileBean.setFileSize(total);
                    //view.setMax(total);//为progressDialog设置大小
                    if (is != null) {
                        Log.d("SettingPresenter", "onResponse: 不为空");
                        fos = new FileOutputStream(file, true);
                        byte[] buf = new byte[2048];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            int progress = (int) (process * 1.0f / total * 100);
                            int preProgress = cacheFileBean.getProgress();
                            if(progress != 100) {
                                if(progress < 5) {
                                    cacheFileBean.setCurrentSize((long)process);
                                    progress(cacheFileBean, listener, progress);
                                } else {
                                    if(progress -  preProgress > 20) {
                                        cacheFileBean.setCurrentSize((long)process);
                                        progress(cacheFileBean, listener, progress);
                                    }
                                }
                            } else {
                                cacheFileBean.setCurrentSize((long)process);
                                progress(cacheFileBean, listener, progress);
                            }
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    success(url, listener, file.getAbsolutePath());
                } catch (Exception e) {
                    //view.downFial();
                    Log.d("SettingPresenter", e.toString());
                    failWithFileName(context, cacheFileBean, listener);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            //private void down() {
            // progressDialog.cancel();
            // }
        });
    }


    public void getAuto(CacheFileBean cacheFileBean, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES);
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(cacheFileBean.getUrl())
                .addHeader("RANGE", "bytes=" + cacheFileBean.getCurrentSize() + "-" + cacheFileBean.getFileSize())
                .build();
        Call call = client.newCall(request);
        MyApp.downCalls.put(cacheFileBean.getUrl(), call);//把这个添加到call里,方便取消
        call.enqueue(callback);
    }


    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    public String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String path);

        /**
         * 正在下载, 拒绝重复
         */
        void onDownloadIngRejectRepeat();
    }

    public void cancel(Context context, String url) {
        MyFileUtil.deleteFile(getFile(context, url));
        if(MyApp.downCalls == null)
            return;
        Call call = MyApp.downCalls.get(url);
        if (call != null) {
            call.cancel();//取消
            MyApp.downCalls.remove(url);
            MyLog.d("下载取消了");
        }
    }

    public void cancelWithFileName(Context context, String url, String fileName) {
        MyFileUtil.deleteFile(getFile(context, url, fileName));
        if(MyApp.downCalls == null)
            return;
        Call call = MyApp.downCalls.get(url);
        if (call != null) {
            call.cancel();//取消
            MyApp.downCalls.remove(url);
            MyLog.d("下载取消了");
        }
    }

    public void progress(CacheFileBean bean, OnDownloadListener listener, int progress) {
        MyLog.d("缓存progress: " + progress + " ,之前的progress: " + bean.getProgress());
        listener.onDownloading(progress);
        bean.setProgress(progress);
        DaoCacheFileUtil.getInstance().updateBean(bean);
    }

    public void success(String url, OnDownloadListener listener, String path) {
        listener.onDownloadSuccess(path);
//        DaoCacheFileUtil.getInstance().updateBean(url);
        MyApp.downCalls.remove(url);
    }

    public void fail(Context context, String url, OnDownloadListener listener) {
        listener.onDownloadFailed(url);
        cancel(context, url);
    }

    public void fail(Context context, CacheFileBean bean, OnDownloadListener listener) {
        listener.onDownloadFailed(bean.getFilePath());
        DaoCacheFileUtil.getInstance().deleteBean(bean);
        cancel(context, bean.getUrl());
    }

    public void failWithFileName(Context context, CacheFileBean bean, OnDownloadListener listener) {
        listener.onDownloadFailed(bean.getFilePath());
        DaoCacheFileUtil.getInstance().deleteBean(bean);
        cancelWithFileName(context, bean.getUrl(), bean.getFileName());
    }

    public void deleteCacheFile(Context context, CacheFileBean cacheFileBean) {
        DaoCacheFileUtil.getInstance().deleteBean(cacheFileBean);
        MyFileUtil.deleteFile(new File(cacheFileBean.getFilePath()));
        cancelWithFileName(context, cacheFileBean.getUrl(), cacheFileBean.getFileName());
    }

    public File getFile(Context context, String url) {
        String fileName = "";
        File file = null;
        if(url.contains(".apk")) {
            file = new File(context.getExternalFilesDir(""), "fupin.apk");
        } else {
            fileName = MyFileUtil.getFileNameWithType(url);
            file = new File(context.getExternalFilesDir(""), fileName);
        }
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public File getFile(Context context, String url, String fileName) {
        File file = null;
        if(url.contains(".apk")) {
            file = new File(context.getExternalFilesDir(""), "fupin.apk");
        } else {
            file = new File(context.getExternalFilesDir(""), fileName);
        }
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public String getFileEnd(String url) {
        if(MyUtil.isTextNull(url)) {
            return "";
        }
        return url.substring(url.lastIndexOf("."));
    }


    public File createImgFile(Context context, String imgName) {
        return new File(context.getExternalFilesDir(""), imgName + ".png");
    }
}
