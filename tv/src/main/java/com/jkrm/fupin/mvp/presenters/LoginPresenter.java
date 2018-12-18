package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.mvp.contracts.LoginContract;
import com.jkrm.fupin.mvp.contracts.MainContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class LoginPresenter extends CommonPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void login(String userName, String password) {
        Observable<ResponseBean<LoginBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).login(userName, password);
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<LoginBean>() {

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
            public void onSuccess(LoginBean bean) {
                mView.loginSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.loginFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }

    @Override
    public void loginBody(LoginRequestBean bean) {
        Observable<ResponseBean<LoginBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).loginBody(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<LoginBean>() {

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
            public void onSuccess(LoginBean bean) {
                mView.loginSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.loginFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
