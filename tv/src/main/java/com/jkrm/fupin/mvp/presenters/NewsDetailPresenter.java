package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.request.NewDetailRequestBean;
import com.jkrm.fupin.mvp.contracts.NewsDetailContract;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class NewsDetailPresenter extends CommonPresenter implements NewsDetailContract.Presenter {

    private NewsDetailContract.View mView;

    public NewsDetailPresenter(NewsDetailContract.View view) {
        mView = view;
    }

    @Override
    public void getNewsDetail(NewDetailRequestBean requestBean) {
        Observable<ResponseBean<NewsBean.NoticesListBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getNoticesDetailById(getBody(requestBean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<NewsBean.NoticesListBean>() {

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
            public void onSuccess(NewsBean.NoticesListBean model) {
                mView.getNewsDetailSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getNewsDetailFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
