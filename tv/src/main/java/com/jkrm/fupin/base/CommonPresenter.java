package com.jkrm.fupin.base;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CommonPresenter implements BasePresenter {

    public Gson mGson = new Gson();
    protected CompositeSubscription disposable;

    public void attachView() {
        disposable = new CompositeSubscription();
    }

    public void detachView() {
        disposable.clear();
        if (disposable != null && disposable.hasSubscriptions()) {
            disposable.unsubscribe();
        }
    }

    protected void addIOSubscription(Observable observable, Subscriber subscriber) {
        disposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public RequestBody getBody(Object object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mGson.toJson(object));
    }
}
