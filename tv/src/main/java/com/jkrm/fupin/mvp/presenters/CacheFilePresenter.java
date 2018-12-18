package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.CommonCallback;
import com.jkrm.fupin.api.CommonSubscriber;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.db.util.DaoCacheFileUtil;
import com.jkrm.fupin.mvp.contracts.CacheFileContract;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by hzw on 2018/8/6.
 */

public class CacheFilePresenter extends CommonPresenter implements CacheFileContract.Presenter {

    private CacheFileContract.View mView;

    public CacheFilePresenter(CacheFileContract.View view) {
        mView = view;
    }

    @Override
    public void queryAllBeans() {
        Observable mObservable = Observable.create(new Observable.OnSubscribe<List<CacheFileBean>>() {
            @Override
            public void call(Subscriber<? super List<CacheFileBean>> subscriber) {
                subscriber.onNext(DaoCacheFileUtil.getInstance().queryAllBean());
            }
        });
        addIOSubscription(mObservable, new CommonSubscriber(new CommonCallback<List<CacheFileBean>>() {

            @Override
            public void onStart() {
//                mView.showLoadingDialog();
            }

            @Override
            public void onSuccess(List<CacheFileBean> data) {
                mView.queryAllBeansSuccess(data);
            }

            @Override
            public void onFailure(String msg) {

            }

        }));
    }
}
