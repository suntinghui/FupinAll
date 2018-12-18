package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.CollectionInfoListBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.request.CollectionRequestBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;

/**
 * Created by hzw on 2018/8/6.
 */

public interface CollectionContract {

    interface Presenter extends BasePresenter {
        void getUserCollectionInfoList(CollectionRequestBean bean);
    }

    interface View extends BaseView {
        void getUserCollectionInfoListSuccess(CollectionInfoListBean bean);
        void getUserCollectionInfoListFail(String msg);
    }
}
