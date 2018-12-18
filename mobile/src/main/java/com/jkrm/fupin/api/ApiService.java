package com.jkrm.fupin.api;

import com.jkrm.fupin.bean.AppUploadVideoResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.bean.VodTypeBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hzw on 2018/7/18.
 */
public interface ApiService {

    @POST("account/getLatestAppVersion")
    Observable<ResponseBean<UpdateBean>> getLatestAppVersion(@Query("versionType") String versionType);
//    Observable<ResponseBean<UpdateBean>> getLatestAppVersion(@Body RequestBody body);

    @GET("commons/getAppOssUrl")
    Observable<ResponseBean<OssTokenBeanNew>> getApiOssUrl(@Query("userid") String userid, @Query("type") String type);

    @GET("commons/getAppOssUrl")
    Observable<ResponseBean<OssTokenBeanNew>> getAppOssUrl(@Query("userid") String userid, @Query("type") String type);

    @POST("account/appUploadVideo")
    Observable<ResponseBean<AppUploadVideoResultBean>> appUploadVideo(@Body RequestBody body);

    /**
     * 首页视频分类列表
     * @return
     */
    @POST("api/getVodTypeList")
    Observable<ResponseBean<List<VodTypeBean>>> getVodTypeList();

}
