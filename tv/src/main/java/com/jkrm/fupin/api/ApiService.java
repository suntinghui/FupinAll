package com.jkrm.fupin.api;




import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.MoreNewsListResultBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.OssListObjectResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.bean.VodHistoryBean;
import com.jkrm.fupin.bean.VodTypeBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    /**
     * 登录
     * @return
     */
    @FormUrlEncoded
    @POST("account/login")
    Observable<ResponseBean<LoginBean>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 登录 --- 盒子端的登录(限管理员)
     * @return
     */
    @POST("box/login")
    Observable<ResponseBean<LoginBean>> loginBody(@Body RequestBody body);

    /**
     * 保存我的收藏
     * @return
     */
    @POST("account/saveUserCollection")
    Observable<ResponseBean> saveUserCollection(@Body RequestBody body);

    /**
     * 取消我的收藏
     * @return
     */
    @POST("account/deleteUserCollection")
    Observable<ResponseBean> deleteUserCollection(@Body RequestBody body);

    /**
     * 获取我的收藏
     * @return
     */
    @POST("account/getUserCollectionInfoList")
    Observable<ResponseBean<CollectionInfoListBean>> getUserCollectionInfoList(@Body RequestBody body);

    /**
     * 首页视频分类列表
     * @return
     */
    @POST("api/getVodTypeList")
    Observable<ResponseBean<List<VodTypeBean>>> getVodTypeList();

    /**
     * 首页音频分类列表
     * @return
     */
    @POST("api/getAudioTypeList")
    Observable<ResponseBean<List<VodTypeBean>>> getAudioTypeList();

    /**
     * 首页精选
     * @return
     */
    @POST("api/getHomePageVideoList")
    Observable<ResponseBean<HomePageListBean>> getHomePageVideoList(@Body RequestBody body);

    /**
     * 播放记录
     * @return
     */
    @POST("account/getVodHistoryList")
    Observable<ResponseBean<List<VodHistoryBean>>> getVodHistoryList(@Body RequestBody body);

    /**
     * 添加播放记录
     * @return
     */
    @POST("account/addVodHistory")
    Observable<ResponseBean> addVodHistory(@Body RequestBody body);

    /**
     * 搜索/各item列表
     * @return
     */
    @POST("api/searchVodListByKeyword")
    Observable<ResponseBean<SearchResultBean>> searchVodListByKeyword(@Body RequestBody body);

    /**
     * APP 端新闻资讯列表展示接口
     * @return
     */
    @POST("api/getNewsInfoListForApp")
    Observable<ResponseBean<List<NewsBean>>> getNewsInfoListForApp();

    /**
     * APP 端新闻资讯列表展示接口
     * @return
     */
    @POST("api/getMoreNewsInfoList")
    Observable<ResponseBean<MoreNewsListResultBean>> getMoreNewsInfoList(@Body RequestBody body);

    /**
     * 获取OSS指定bucket下的文件对象及子文件夹对象列表
     * @param prefix bucket下的文件夹名字, 可不传
     * @return
     */
    @POST("commons/listObjectsFromOss")
    Observable<ResponseBean<OssListObjectResultBean>> listObjectsFromOss(@Query("prefix") String prefix);

    /**
     * 获取oss url 信息
     * @param userid
     * @param type 视频0 音频1 图片2 头像3 视频缩略图12
     * @return
     */
    @GET("commons/getAppOssUrl")
    Observable<ResponseBean<OssTokenBeanNew>> getAppOssUrl(@Query("userid") String userid, @Query("type") String type);

}
