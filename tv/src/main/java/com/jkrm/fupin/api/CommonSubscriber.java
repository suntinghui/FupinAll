package com.jkrm.fupin.api;

import rx.Subscriber;

public class CommonSubscriber<T> extends Subscriber<T> {

    private CommonCallback<T> mCommonCallback;

    public CommonSubscriber(CommonCallback mCommonCallback) {
        this.mCommonCallback = mCommonCallback;
    }

    @Override
    public void onStart() {
        if (null == mCommonCallback) {
            return;
        }
        mCommonCallback.onStart();
    }

    @Override
    public void onCompleted() {
        //TODO ...
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (null == mCommonCallback) {
            return;
        }
        mCommonCallback.onFailure(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        if (null == mCommonCallback) {
            return;
        }
        mCommonCallback.onSuccess(t);
    }
}
