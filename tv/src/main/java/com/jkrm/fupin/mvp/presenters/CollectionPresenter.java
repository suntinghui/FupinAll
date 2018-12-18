package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.request.CollectionRequestBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.mvp.contracts.CollectionContract;
import com.jkrm.fupin.mvp.contracts.LoginContract;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class CollectionPresenter extends CommonPresenter implements CollectionContract.Presenter {

    private CollectionContract.View mView;

    public CollectionPresenter(CollectionContract.View view) {
        mView = view;
    }

    @Override
    public void getUserCollectionInfoList(CollectionRequestBean bean) {
        Observable<ResponseBean<CollectionInfoListBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getUserCollectionInfoList(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<CollectionInfoListBean>() {

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
            public void onSuccess(CollectionInfoListBean bean) {
                mView.getUserCollectionInfoListSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getUserCollectionInfoListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
