package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.HomePageListRequestBean;
import com.jkrm.fupin.mvp.contracts.MainContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class MainPresenter extends CommonPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
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
    public void getAudioTypeList() {
        Observable<ResponseBean<List<VodTypeBean>>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getAudioTypeList();
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
                mView.getAudioTypeListSuccess(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getAudioTypeListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }

    @Override
    public void getHomePageVideoList() {
        HomePageListRequestBean bean = new HomePageListRequestBean();
        bean.setCp(1);
        bean.setPs(10);
        Observable<ResponseBean<HomePageListBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getHomePageVideoList(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<HomePageListBean>() {

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
            public void onSuccess(HomePageListBean bean) {
                mView.getHomePageVideoListSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getHomePageVideoListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
