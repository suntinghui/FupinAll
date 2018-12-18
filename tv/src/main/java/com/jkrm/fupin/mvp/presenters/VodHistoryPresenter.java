package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.VodHistoryBean;
import com.jkrm.fupin.bean.request.VodHistoryRequestBean;
import com.jkrm.fupin.mvp.contracts.LoginContract;
import com.jkrm.fupin.mvp.contracts.VodHistoryContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class VodHistoryPresenter extends CommonPresenter implements VodHistoryContract.Presenter {

    private VodHistoryContract.View mView;

    public VodHistoryPresenter(VodHistoryContract.View view) {
        mView = view;
    }

    @Override
    public void getVodHistoryList(VodHistoryRequestBean bean) {
        Observable<ResponseBean<List<VodHistoryBean>>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getVodHistoryList(getBody(bean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<List<VodHistoryBean>>() {

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
            public void onSuccess(List<VodHistoryBean> list) {
                mView.getVodHistoryListSuccess(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getVodHistoryListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
