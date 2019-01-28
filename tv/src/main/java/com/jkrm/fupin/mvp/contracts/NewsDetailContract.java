package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.request.NewDetailRequestBean;

/**
 * Created by hzw on 2018/8/6.
 */

public interface NewsDetailContract {

    interface Presenter extends BasePresenter {
        void getNewsDetail(NewDetailRequestBean requestBean);
    }

    interface View extends BaseView {
        void getNewsDetailSuccess(NewsBean.NoticesListBean bean);
        void getNewsDetailFail(String msg);
    }
}
