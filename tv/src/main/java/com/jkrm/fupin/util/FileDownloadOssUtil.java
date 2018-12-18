package com.jkrm.fupin.util;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.constants.MyConstants;

import java.io.IOException;
import java.io.InputStream;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hzw on 2018/10/27.
 */

public class FileDownloadOssUtil {

    private OSS oss;

    public void getOssInfo() {
        RetrofitClient.builderRetrofit()
                .create(ApiService.class)
                .getAppOssUrl(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, "0"), "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber(new ApiCallBack<OssTokenBeanNew>() {

                    @Override
                    public void onSuccess(OssTokenBeanNew bean) {
                        initOss(bean.getAccessKeyId(), bean.getAccessKeySecret(), bean.getSecurityToken(), bean.getAppEndPoint());
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }
                }));

    }

    private void initOss(String accessKeyId, String accessKeySecret, String secretToken, String endpoint) {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, secretToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setHttpDnsEnable(false);//默认是true 开启，需要关闭可以设置false
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();  //调用此方法即可开启日志
        MyLog.d("oss initOss endpoint: " + endpoint);
        oss = new OSSClient(MyApp.getInstance(), endpoint, credentialProvider, conf);
    }

    GetObjectRequest get = new GetObjectRequest("<bucketName>", "<objectKey>");

    private void startDownload() {
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                OSSLog.logDebug("getobj_progress: " + currentSize+"  total_size: " + totalSize, false);
            }
        });
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();

                byte[] buffer = new byte[2048];
                int len;

                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 处理下载的数据
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

}
