package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.mvp.contracts.LoginContract;
import com.jkrm.fupin.mvp.contracts.NewsContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class NewsPresenter extends CommonPresenter implements NewsContract.Presenter {

    private NewsContract.View mView;

    public NewsPresenter(NewsContract.View view) {
        mView = view;
    }

    @Override
    public void getNewsList() {
        Observable<ResponseBean<List<NewsBean>>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getNewsInfoListForApp();
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<List<NewsBean>>() {

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
            public void onSuccess(List<NewsBean> model) {
                mView.getNewsListSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getNewsListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
