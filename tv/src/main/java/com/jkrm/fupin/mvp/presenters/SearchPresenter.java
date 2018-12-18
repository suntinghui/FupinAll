package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.bean.request.SearchBean;
import com.jkrm.fupin.mvp.contracts.LoginContract;
import com.jkrm.fupin.mvp.contracts.SearchContract;

import java.util.List;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class SearchPresenter extends CommonPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;

    public SearchPresenter(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void searchVodList(SearchBean searchBean) {
        Observable<ResponseBean<SearchResultBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).searchVodListByKeyword(getBody(searchBean));
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<SearchResultBean>() {

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
            public void onSuccess(SearchResultBean bean) {
                mView.searchVodListSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.searchVodListFail(msg);
                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
