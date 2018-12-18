package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.UpdateBean;
import com.jkrm.fupin.bean.request.UpdateRequestBean;

/**
 * Created by hzw on 2018/8/6.
 */

public interface UpdateContract {

    interface Presenter extends BasePresenter {
        void getLatestAppVersion();
    }

    interface View extends BaseView {
        void getLatestAppVersionSuccess(UpdateBean bean);
        void getLatestAppVersionFail(String msg);

    }
}
