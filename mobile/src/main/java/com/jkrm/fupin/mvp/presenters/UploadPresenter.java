package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.AppUploadVideoResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.AppUploadVideoRequestBean;
import com.jkrm.fupin.mvp.contracts.UploadContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class UploadPresenter extends CommonPresenter implements UploadContract.Presenter {

    private UploadContract.View mView;

    public UploadPresenter(UploadContract.View view) {
        mView = view;
    }

    @Override
    public void getVodTypeList() {
        Observable<ResponseBean<List<VodTypeBean>>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getVodTypeList();
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<List<VodTypeBean>>() {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoadingDialog();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mView.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(List<VodTypeBean> list) {
                mView.getVodTypeListSuccess(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getVodTypeListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }


    @Override
    public void getAppOssUrl(String userid, final String type) {
        Observable<ResponseBean<OssTokenBeanNew>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getAppOssUrl(userid, type);
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<OssTokenBeanNew>() {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoadingDialog();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mView.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(OssTokenBeanNew bean) {
                mView.getAppOssUrlSuccess(bean, type);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getAppOssUrlFail(msg);
//                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }

    @Override
    public void appUploadVideo(AppUploadVideoRequestBean bean) {
        Observable<ResponseBean<AppUploadVideoResultBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).appUploadVideo(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<AppUploadVideoResultBean>() {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoadingDialog();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mView.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(AppUploadVideoResultBean bean) {
                mView.appUploadVideoSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.appUploadVideoFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }

}
