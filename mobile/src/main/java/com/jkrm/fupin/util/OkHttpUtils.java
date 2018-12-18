package com.jkrm.fupin.util;

/**
 * Created by hzw on 2018/8/13.
 */

import android.os.Handler;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class OkHttpUtils {

    public static volatile OkHttpUtils instance;
    Handler handler = new Handler();

    private OkHttpUtils() {

    }
    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public void uploadFile(String url, Map<String,Object> paramsMap, final CallBack callBack) {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        //form 表单上传
        multipartBody.setType(MultipartBody.FORM);
        //拼接参数
        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if(object instanceof String){
                multipartBody.addFormDataPart(key,object.toString());
            }else if(object instanceof File){
                File file = (File) object;
                multipartBody.addFormDataPart(key,file.getName(),MultipartBody.create(MediaType.parse("multipart/form-data"),file));
            }
        }
        RequestBody requestBody=multipartBody.build();
        //创建Request对象
        Request request=new Request.Builder().url(url).post(requestBody).build();
        new OkHttpClient.Builder()
                //设置最长读写时间
                .readTimeout(100000, TimeUnit.SECONDS)
                .writeTimeout(100000, TimeUnit.SECONDS)
                .connectTimeout(100000, TimeUnit.SECONDS).build()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onSuccess(e.getMessage());
//                            }
//                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String str = response.body().string();
                        //解析
//                        final MsgBean msgBean = new Gson().fromJson(str, MsgBean.class);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onSuccess(msgBean);
//                            }
//                        });
                    }
                });
    }

    public void downloadFile(final File file, String url, final CallBackPro callBack) {
        // 父目录是否存在
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdir();
        }
        // 文件是否存在
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Request request=new Request.Builder().url(url).get().build();
        new OkHttpClient.Builder()
                .readTimeout(100000, TimeUnit.SECONDS)
                .writeTimeout(100000, TimeUnit.SECONDS)
                .connectTimeout(100000, TimeUnit.SECONDS).build()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(e);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            // 获取文件总长度
                            final long totalLength = body.contentLength();
                            //以流的方式进行读取
                            InputStream inputStream = body.byteStream();
                            FileOutputStream outputStream = new FileOutputStream(file);
                            byte[] buffer = new byte[2048];
                            int len = 0;
                            int num = 0;
                            while ((len = inputStream.read(buffer)) != -1){
                                num+=len;
                                outputStream.write(buffer,0,len);
                                final int finalNum = num;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onProgressBar( finalNum *100/totalLength);
                                    }
                                });
                            }
                            //读取完关闭流
                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(file.exists()){
                                        //返回下载文件路径
                                        callBack.onSuccess(file.getPath());
                                    }
                                }
                            });
                        }
                    }
                });
    }


    //get请求
    public void get(String url, Map<String,String> map, final CallBack callBack, final Class c){
        //对url和参数做拼接处理
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(url);
        //判断是否存在?   if中是存在
        if(stringBuffer.indexOf("?")!=-1 ){
            //判断?是否在最后一位    if中是不在最后一位
            if(stringBuffer.indexOf("?")!=stringBuffer.length()-1){
                stringBuffer.append("&");
            }
        }else{
            stringBuffer.append("?");
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        //判断是否存在&   if中是存在
        if(stringBuffer.indexOf("&")!=-1){
            stringBuffer.deleteCharAt(stringBuffer.lastIndexOf("&"));
        }


        //1:创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2:创建Request对象
        final Request request = new Request.Builder()
                .get()
                .url(stringBuffer.toString())
                .build();
        //3:创建Call对象
        Call call = okHttpClient.newCall(request);
        //4:请求网络
        call.enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }
            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //拿到数据解析
                final Object o = new Gson().fromJson(result, c);
                //当前是在子线程,回到主线程中
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //回调
                        callBack.onSuccess(o);
                    }
                });
            }
        });

    }

    public interface CallBack {
        void onSuccess(Object o);
        void onFailed(Exception e);
    }


    public interface CallBackPro {
        void onSuccess(Object o);
        void onFailed(Exception e);
        void onProgressBar(Long i);//用来显示下载进度
    }

}
