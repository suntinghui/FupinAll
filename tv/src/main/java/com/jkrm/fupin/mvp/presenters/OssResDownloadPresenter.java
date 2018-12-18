package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.OssListObjectResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.mvp.contracts.OssResDownloadContract;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class OssResDownloadPresenter extends CommonPresenter implements OssResDownloadContract.Presenter {

    private OssResDownloadContract.View mView;

    public OssResDownloadPresenter(OssResDownloadContract.View view) {
        mView = view;
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
    public void listObjectsFromOss(String prefix) {
        Observable<ResponseBean<OssListObjectResultBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).listObjectsFromOss(prefix);
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<OssListObjectResultBean>() {

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
            public void onSuccess(OssListObjectResultBean model) {
                mView.listObjectsFromOssSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
