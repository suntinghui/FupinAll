package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.OssListObjectResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.request.CollectionRequestBean;

/**
 * Created by hzw on 2018/8/6.
 */

public interface OssResDownloadContract {

    interface Presenter extends BasePresenter {
        void getAppOssUrl(String userid, String type);
        void listObjectsFromOss(String prefix);
    }

    interface View extends BaseView {
        void getAppOssUrlSuccess(OssTokenBeanNew bean, String type);
        void getAppOssUrlFail(String msg);

        void listObjectsFromOssSuccess(OssListObjectResultBean bean);
    }
}
