package com.jkrm.fupin.api;


import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.constants.MyConstants;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 *
 */
public class ApiSubscriber<T> extends Subscriber<ResponseBean<T>> {

    public static int UNKNOWN_CODE = -1;
    private ApiCallBack apiCallback;

    public ApiSubscriber(ApiCallBack apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onCompleted() {
        apiCallback.onCompleted();
    }

    @Override
    public void onStart() {
        super.onStart();
        apiCallback.onStart();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException || e instanceof ConnectException) {
            apiCallback.onFailure(UNKNOWN_CODE, MyConstants.Msg.NET_ERROR_COMMON);
        } else if( e instanceof UnknownHostException) {
            apiCallback.onFailure(UNKNOWN_CODE, MyConstants.Msg.NET_ERROR_SERVER);
        } else {
            apiCallback.onFailure(UNKNOWN_CODE, MyConstants.Msg.NET_ERROR_UNKNOWN);
        }
        apiCallback.onCompleted();
    }


    @Override
    public void onNext(ResponseBean<T> httpBean) {
        if (httpBean.getCode() == MyConstants.Code.CODE_SUCCESS) {
            apiCallback.onSuccess(httpBean.getData());
        } else if (httpBean.getCode()== MyConstants.Code.CODE_FAILURE_TOKEN){
            //令牌无效重新登录
//            RxBus.getInstance().post(new LoginTimeOutBus());
        } else {
            apiCallback.onFailure(httpBean.getCode(), httpBean.getMsg());
        }
    }
}
