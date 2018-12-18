package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.request.HomePageListRequestBean;
import com.jkrm.fupin.mvp.contracts.MainContract;
import com.jkrm.fupin.mvp.contracts.MainFragmentContract;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class MainFragmentPresenter extends CommonPresenter implements MainFragmentContract.Presenter {

    private MainFragmentContract.View mView;

    public MainFragmentPresenter(MainFragmentContract.View view) {
        mView = view;
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
