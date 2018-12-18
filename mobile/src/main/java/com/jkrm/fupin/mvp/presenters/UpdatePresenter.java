package com.jkrm.fupin.mvp.presenters;

import com.jkrm.fupin.api.ApiCallBack;
import com.jkrm.fupin.api.ApiService;
import com.jkrm.fupin.api.ApiSubscriber;
import com.jkrm.fupin.api.RetrofitClient;
import com.jkrm.fupin.base.CommonPresenter;
import com.jkrm.fupin.bean.AppUploadVideoResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.ResponseBean;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.bean.request.AppUploadVideoRequestBean;
import com.jkrm.fupin.bean.request.UpdateRequestBean;
import com.jkrm.fupin.mvp.contracts.UpdateContract;
import com.jkrm.fupin.mvp.contracts.UploadContract;

import rx.Observable;

/**
 * Created by hzw on 2018/8/6.
 */

public class UpdatePresenter extends CommonPresenter implements UpdateContract.Presenter {

    private UpdateContract.View mView;

    public UpdatePresenter(UpdateContract.View view) {
        mView = view;
    }

    @Override
    public void getLatestAppVersion() {
        Observable<ResponseBean<UpdateBean>> observable = RetrofitClient.builderRetrofit()
                .create(ApiService.class).getLatestAppVersion(UpdateRequestBean.SYSTEM_ANDROID_MOBILE);
        addIOSubscription(observable, new ApiSubscriber(new ApiCallBack<UpdateBean>() {

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
            public void onSuccess(UpdateBean bean) {
                mView.getLatestAppVersionSuccess(bean);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.getLatestAppVersionFail(msg);
//                mView.showMsg(msg);
                mView.dismissLoadingDialog();
            }
        }));
    }
}
