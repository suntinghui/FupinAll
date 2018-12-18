package com.jkrm.fupin.api;

import android.util.Log;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.util.APPUtils;
import com.jkrm.fupin.util.SharePreferencesUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求信息拦截
 */
public class RequestParamInterceptor implements Interceptor {
    private static final String TAG = "RequestParamInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("RequestParamInterceptor", "RequestParamInterceptor_" + System.currentTimeMillis());
        Request oldRequest = chain.request();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());
        String token =
                SharePreferencesUtil.getString(MyConstants.SharedPrefrenceKey.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_INFO_TOKEN, "");

        String version = APPUtils.getAppVersionInfo(MyApp.getInstance(), APPUtils.TYPE_VERSION.TYPE_VERSION_NAME);

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .addHeader("Authorization", token)
                .addHeader("current_version", version)
                .addHeader("os_type","1") //1表示Android
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();
        Log.d(TAG, newRequest.toString());
        //响应信息拦截
        return chain.proceed(newRequest);
    }
}