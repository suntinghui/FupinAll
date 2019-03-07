package com.jkrm.fupin.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.jkrm.fupin.R;
import com.jkrm.fupin.annex.DownloadUtil;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.ui.activity.upnp.UpnpMainActivity;
import com.jkrm.fupin.util.FileDownloadUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.NetWatchdog;
import com.jkrm.fupin.util.NetworkUtil;
import com.jkrm.fupin.util.PermissionUtil;
import com.just.library.AgentWeb;
import com.just.library.WebDefaultSettingsManager;
import com.koolpos.download.bean.DownloadMsgBean;
import com.koolpos.download.constants.DownloadConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Handler.Callback {

    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;
    private BridgeWebView mBridgeWebView;
    private AgentWeb mAgentWeb;
//    private static final String URL = "http://111.13.56.38:9999/fupin/index.html";
    private static final String URL = "http://47.92.175.158:8080/WebApp/";
//    private static final String URL = "file:///android_asset/normal/index.html";
//    private static final String URL = "file:///android_asset/dist/index.html";
//    private static final String URL = "file:///android_asset/html/index.html";
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    private static final int DOWNLOAD_HANDLE = 0;
    private static final int DOWNLOAD_AUTO = 1;
    private List<CacheFileBean> mList = new ArrayList<>();
    private Handler mHandler;
    private String downloadUrl = "";
    private String imgUrl = "";
    private String videoId = "";
    private String resTitle = "";
    private NetWatchdog netWatchdog;

    private DownloadReceiver mDownloadReceiver;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler(this);
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {

            }

            @Override
            public void onNetUnConnected() {

            }
        });
        netWatchdog.startWatch();
        if(netWatchdog.hasNet(mActivity) && !netWatchdog.is4GConnected(mActivity)) {
            getLocalDbDownloadFile(false);
        }

        mBridgeWebView = new BridgeWebView(mActivity);
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(mLlRoot, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()
                .setIndicatorColor(getResources().getColor(R.color.color_0160D4))
                //.setIndicatorColorWithHeight(-1, 2)//
                .setAgentWebSettings(WebDefaultSettingsManager.getInstance())//
                .setWebViewClient(new BridgeWebViewClient(mBridgeWebView))
                .setWebChromeClient(mWebChromeClient)
                // .setReceivedTitleCallback(mCallback)
                .setWebView(mBridgeWebView)
                .createAgentWeb()//
                .ready()
                .go(URL);

        /**
         * JS调用JAVA
         * 参数
         * 第一个：订阅的方法名
         * 第二个: 回调Handler , 参数返回js请求的resqustData,function.onCallBack（）回调到js，调用function(responseData)
         */
        mBridgeWebView.registerHandler("downloadNewsFile", new BridgeHandler() {

            @Override
            public void handler(final String data, CallBackFunction function) {
                MyLog.d("downloadNewsFile data: " + data);
                function.onCallBack("H5调用android上传回调");
                JSONObject json = null;
                try {
                    json = new JSONObject(data);
                    final String url = json.optString("url");
                    final String originName = json.optString("originName");
                    final Bundle bundle = new Bundle();
//                    bundle.putString(MyConstants.Params.COMMON_PARAMS, userId);
                    PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, new IPermissionListener() {
                        @Override
                        public void granted() {
//                            showMsg("下载附件url: " + url + " ,originName: " + originName);
                            DownloadUtil.downloadFile(mActivity, url, originName);
                        }

                        @Override
                        public void shouldShowRequestPermissionRationale() {
                            PermissionUtil.toAppSetting(mActivity);
                        }

                        @Override
                        public void needToSetting() {
                            PermissionUtil.toAppSetting(mActivity);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("下载附件出错");
                }

            }

        });

        /**
         * JS调用JAVA
         * 参数
         * 第一个：订阅的方法名
         * 第二个: 回调Handler , 参数返回js请求的resqustData,function.onCallBack（）回调到js，调用function(responseData)
         */
        mBridgeWebView.registerHandler("uploadFile", new BridgeHandler() {

            @Override
            public void handler(final String data, CallBackFunction function) {
                MyLog.d("upload data: " + data);
                function.onCallBack("H5调用android上传回调");
                JSONObject json = null;
                try {
                    json = new JSONObject(data);
                    final String userId = json.optString("id");
                    final Bundle bundle = new Bundle();
                    bundle.putString(MyConstants.Params.COMMON_PARAMS, userId);
                    PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, new IPermissionListener() {
                        @Override
                        public void granted() {
                            toClass(OssUploadActivity.class, bundle, false);
                            //                toClass(MyUploadActivity.class, null, false);

                        }

                        @Override
                        public void shouldShowRequestPermissionRationale() {
                            PermissionUtil.toAppSetting(mActivity);
                        }

                        @Override
                        public void needToSetting() {
                            PermissionUtil.toAppSetting(mActivity);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("调用视频页面出现错误");
                }

            }

        });
        mBridgeWebView.registerHandler("downloadFile", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                MyLog.d("download data: " + data);
                function.onCallBack("H5调用android下载回调");
                if(MyApp.downCalls.size() > 3) {
                    showDialog("已达到最大同时下载限制，请等待其他视频下载结束后再尝试下载。");
                    return;
                }
                try {
                    JSONObject json = new JSONObject(data);
                    downloadUrl = json.optString("url");
                    imgUrl = json.optString("imgUrl");
                    videoId = json.optString("videoId");
                    resTitle = json.optString("title");
                    MyLog.d("download url: " + downloadUrl);
                    PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, new IPermissionListener() {
                        @Override
                        public void granted() {
                            getLocalDbDownloadFile(true);
//                            getLocalVideoFile();
                            FileDownloadUtil.get().downImg(mActivity, imgUrl, videoId, new FileDownloadUtil.OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess(String path) {
                                    MyLog.d("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                                }

                                @Override
                                public void onDownloading(int progress) {
                                    MyLog.d("当前下载进度: " + progress + "%");
                                }

                                @Override
                                public void onDownloadFailed(String path) {
                                    MyLog.d("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败");
                                }

                                @Override
                                public void onDownloadIngRejectRepeat() {
                                    MyLog.d("文件正在下载中, 请勿重复点击下载");
                                }
                            });
                        }

                        @Override
                        public void shouldShowRequestPermissionRationale() {
                            PermissionUtil.toAppSetting(mActivity);
                        }

                        @Override
                        public void needToSetting() {
                            PermissionUtil.toAppSetting(mActivity);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        mBridgeWebView.registerHandler("cacheFile", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("H5调用android离线缓存回调");
                PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        toClass(CacheFileActivity.class, null, false);
                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        PermissionUtil.toAppSetting(mActivity);
                    }

                    @Override
                    public void needToSetting() {
                        PermissionUtil.toAppSetting(mActivity);
                    }
                });
            }

        });
        mBridgeWebView.registerHandler("dlnaTv", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("H5调用android投屏回调");
                showMsg("未开发");
//                toClass(TvShareActivity.class, null, false);
            }

        });
        //upnpFun
        mBridgeWebView.registerHandler("boxShare", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("H5调用android upnp回调");
//                showDialog("开发中");
                PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new IPermissionListener() {
                    @Override
                    public void granted() {
                        if(NetworkUtil.isWifi(mActivity)) {
                            toClass(UpnpMainActivity.class, null, false);
                        } else {
                            showDialog("请连接WIFI才可使用数据共享功能");
                        }

                    }

                    @Override
                    public void shouldShowRequestPermissionRationale() {
                        PermissionUtil.toAppSetting(mActivity);
                    }

                    @Override
                    public void needToSetting() {
                        PermissionUtil.toAppSetting(mActivity);
                    }
                });

            }

        });
        /**
         * JAVA调用JS
         */
//        mBridgeWebView.callHandler("functionInJs", "APP端调用JS端传参了", new CallBackFunction() {
//            @Override
//            public void onCallBack(String data) {
//                showMsg("APP端收到了来自H5 js的数据data： " + data);
//                MyLog.d("callHandler data:" + data);
//            }
//        });
//
//        mBridgeWebView.send("hello");
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            MyLog.d(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
            }
//            mToolbarTitle.setText(title);
            MyLog.d("title: " + title);
        }


    };


    @Override
    public void onBackPressed() {
        if(mBridgeWebView.canGoBack()) {
            mBridgeWebView.goBack();
            return;
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                //            if(MyApp.downCalls != null && MyApp.downCalls.size() > 0) {
                //                showDialogWithCancelBtn("提示", "当前仍有视频文件下载中, 退出后将删除未下载完成的视频文件, 确认退出吗?", new DialogInterface.OnClickListener() {
                //                    @Override
                //                    public void onClick(DialogInterface dialog, int which) {
                //                        Set<Map.Entry<String, Call>> set = MyApp.downCalls.entrySet();
                //                        // 遍历键值对对象的集合，得到每一个键值对对象
                //                        for (Map.Entry<String, Call> entry : set) {
                //                            // 根据键值对对象获取键和值
                //                            String key = entry.getKey();
                //                            Call value = entry.getValue();
                //                            FileDownloadUtil.get().cancel(mActivity, key);
                //                        }
                //                        exitAppForced();
                //                    }
                //                });
                //            } else {
                //                exitAppForced();
                //            }
                super.onBackPressed();
                return;
            } else {
                showMsg("再次点击返回键退出");
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    private void getLocalVideoFile() {
        showLoadingDialog();
        mList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = getExternalFilesDir("");
                MyLog.d("离线缓存路径: " + file.getAbsolutePath());
                if (!file.exists()) {
                    mHandler.sendEmptyMessage(0);
                    return;
                }
                for (File tempFile : file.listFiles()) {
                    String fileName = tempFile.getName();
                    if (MyUtil.isVideoType(fileName)) {
                        CacheFileBean cacheFileBean = new CacheFileBean();
                        cacheFileBean.setFile(tempFile);
                        cacheFileBean.setCheck(false);
                        mList.add(cacheFileBean);
                    }
                }
                MyLog.d("离线缓存文件总数: " + mList.size());
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 获取本地下载中或已下载完成的文件
     */
    private void getLocalDbDownloadFile(final boolean isHandle) {
        showLoadingDialog();
        mList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList = DaoCacheFileUtil.getInstance().queryAllBean();
                mHandler.sendEmptyMessage(isHandle ? DOWNLOAD_HANDLE : DOWNLOAD_AUTO);
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DOWNLOAD_HANDLE: //手动点击下载
                boolean isExistInCacheList = false;
                for(CacheFileBean bean : mList) {
                    if(bean.getUrl() != null && bean.getUrl().equals(downloadUrl)) {
                        MyLog.d("download url 相同, 检测时控制不下载, 数据库的url: " + bean.getUrl() + " ,当前下载的url: " + downloadUrl);
                        isExistInCacheList = true;
                        break;
                    }
                    //            if(bean.getFile().getName().equals(MyFileUtil.getFileNameWithType(downloadUrl))) {
                    //                isExistInCacheList = true;
                    //                break;
                    //            }
                }
                if(isExistInCacheList) {
                    showDialog("该视频已在离线缓存列表，请勿重复下载。");
                    dismissLoadingDialog();
                    return false;
                }
                CacheFileBean mDownloadBean = new CacheFileBean();
                mDownloadBean.setUrl(downloadUrl);
                mDownloadBean.setImgUrl(imgUrl);
                mDownloadBean.setVideoId(videoId);
                mDownloadBean.setFileName(resTitle);
                showMsg("已添加到下载队列，您可到我的-离线缓存查看下载进度");
                MyLog.d("该视频不在当前缓存中, 执行下载");
                FileDownloadUtil.get().down(mActivity, mDownloadBean, new FileDownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String path) {
                        showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                        MyLog.d("下载成功");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_SUCCESS));
                    }

                    @Override
                    public void onDownloading(int progress) {
                        MyLog.d("当前下载进度: " + progress + "%");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_PROGRESS));
                    }

                    @Override
                    public void onDownloadFailed(String path) {
                        showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_FAIL));
                        MyLog.d("下载失败");
                    }

                    @Override
                    public void onDownloadIngRejectRepeat() {
                        showDialog("该视频正在下载中, 请勿重复点击下载");
                    }
                });
                break;
            case DOWNLOAD_AUTO: //自动下载之前未完成的文件
                if(!MyUtil.isEmptyList(mList)) {
                    for(CacheFileBean cacheFileBean : mList) {
                        if(cacheFileBean.getProgress() != 100 && !TextUtils.isEmpty(cacheFileBean.getUrl())) {
                            FileDownloadUtil.get().downAuto(mActivity, cacheFileBean, new FileDownloadUtil.OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess(String path) {
                                    showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载完成");
                                    MyLog.d("下载成功");
                                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_SUCCESS));
                                }

                                @Override
                                public void onDownloading(int progress) {
                                    MyLog.d("当前下载进度: " + progress + "%");
                                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_PROGRESS));
                                }

                                @Override
                                public void onDownloadFailed(String path) {
                                    showMsg("文件 " + MyFileUtil.getFileNameWithType(path) + " 下载失败, 文件自动删除");
                                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(MyConstants.Action.ACTION_DOWNLOAD_FAIL));
                                    MyLog.d("下载失败");
                                }

                                @Override
                                public void onDownloadIngRejectRepeat() {
                                    showDialog("该视频正在下载中, 请勿重复点击下载");
                                }
                            });
                        }
                    }
                }
                break;
        }
        dismissLoadingDialog();
        return false;
    }

    class CustomWebViewClient extends WebViewClient {
        private BridgeWebView webView;

        public CustomWebViewClient(BridgeWebView webView) {
            this.webView = webView;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            try {
//                url = URLDecoder.decode(url, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            return super.shouldOverrideUrlLoading(view, url);
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        unregisterReceiver();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        registerReceiver();
        super.onResume();
    }

    private void registerReceiver() {
        mDownloadReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadConstant.ACTION_DOWNLOAD_ING);
        intentFilter.addAction(DownloadConstant.ACTION_DOWNLOAD_SUCCESS);
        intentFilter.addAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
        registerReceiver(mDownloadReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if(mDownloadReceiver != null) {
            unregisterReceiver(mDownloadReceiver);
            mDownloadReceiver = null;
        }
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DownloadMsgBean downloadMsgBean = (DownloadMsgBean) intent.getSerializableExtra(DownloadConstant.COMMON_PARAMS);
            if (DownloadConstant.ACTION_DOWNLOAD_ING.equals(action)) {
                MyLog.d("DownloadReceiver 文件下载中, 下载进度" + downloadMsgBean.getDownloadSize());
                showLoadingDialog("文件已加载" + downloadMsgBean.getDownloadSize() + "%");
            } else if (DownloadConstant.ACTION_DOWNLOAD_SUCCESS.equals(action)) {
                MyLog.d("DownloadReceiver 文件下载中, 下载成功");
                showMsg("文件加载成功");
                dismissLoadingDialog();
                String filePath = downloadMsgBean.getFilePath();
                File file = new File((filePath));
                try {
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(context, "com.jkrm.fupin.DownloadProvider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    Intent intentOpen = new Intent(Intent.ACTION_VIEW);
                    intentOpen.addCategory(Intent.CATEGORY_DEFAULT);
                    intentOpen.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentOpen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentOpen.setDataAndType(uri, MyFileUtil.getMIMEType(file));
                    startActivity(intentOpen);
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog("本机无匹配的软件可查看");
                }
            } else if (DownloadConstant.ACTION_DOWNLOAD_FAIL.equals(action)) {
                MyLog.d("DownloadReceiver 文件下载中, 下载失败, 原因: " + downloadMsgBean.getMessage());
                showMsg("文件加载失败");
                dismissLoadingDialog();
            }
        }
    }
}
