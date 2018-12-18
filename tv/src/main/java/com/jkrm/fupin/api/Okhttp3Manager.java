package com.jkrm.fupin.api;



import com.jkrm.fupin.util.MyLog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hzw on 2018/6/29.
 */

public class Okhttp3Manager {

    public static OkHttpClient.Builder getOkhttp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                MyLog.d("okhttp", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.writeTimeout(1, TimeUnit.MINUTES);
        //        httpClient.retryOnConnectionFailure(true);
        httpClient.addInterceptor(logging);
        //加入拦截器
        //        RequestParamInterceptor requestParamInterceptor = new RequestParamInterceptor();
        //        httpClient.addNetworkInterceptor(requestParamInterceptor);
        //加入https的证书
  /*      int[] certficates = new int[]{R.raw.app_key};
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(AppContext.getContext(), certficates);
        if (sslSocketFactory != null) {
            httpClient.sslSocketFactory(sslSocketFactory);
        }
*/
        return httpClient;
    }
}

