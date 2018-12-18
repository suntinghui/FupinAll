package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.MoreNewsListResultBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.request.CollectionRequestBean;
import com.jkrm.fupin.bean.request.MoreNewsInfoRequestBean;
import com.jkrm.fupin.mvp.contracts.NewsListContract;
import com.jkrm.fupin.bean.NewsBean.NoticesListBean;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class NewsListPresenter extends CommonPresenter implements NewsListContract.Presenter {

    private NewsListContract.View mView;

    public NewsListPresenter(NewsListContract.View view) {
        mView = view;
    }

    @Override
    public void getMoreNewsInfoList(MoreNewsInfoRequestBean bean) {
        Observable<ResponseBean<MoreNewsListResultBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getMoreNewsInfoList(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<MoreNewsListResultBean>() {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoadingDialog();
            }

            @Override
            public void onSuccess(MoreNewsListResultBean bean) {
                mView.getMoreNewsInfoListSuccess(bean);
            }


            @Override
            public void onCompleted() {
                super.onCompleted();
                mView.dismissLoadingDialog();
            }


            @Override
            public void onFailure(int code, String msg) {
                mView.getMoreNewsInfoListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
