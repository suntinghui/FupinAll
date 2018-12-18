package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface NewsContract {

    interface Presenter extends BasePresenter {
        void getNewsList();
    }

    interface View extends BaseView {
        void getNewsListSuccess(List<NewsBean> list);
        void getNewsListFail(String msg);
    }
}
