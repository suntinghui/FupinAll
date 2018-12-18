package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.AppUploadVideoResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.AppUploadVideoRequestBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface UploadContract {

    interface Presenter extends BasePresenter {
        void getVodTypeList();
        void getAppOssUrl(String userid, String type);
        void appUploadVideo(AppUploadVideoRequestBean bean);
    }

    interface View extends BaseView {
        void getVodTypeListSuccess(List<VodTypeBean> list);
        void getVodTypeListFail(String msg);

        void getAppOssUrlSuccess(OssTokenBeanNew bean, String type);
        void getAppOssUrlFail(String msg);

        void appUploadVideoSuccess(AppUploadVideoResultBean bean);
        void appUploadVideoFail(String msg);

//        void resultFailure(String msg);
    }
}
