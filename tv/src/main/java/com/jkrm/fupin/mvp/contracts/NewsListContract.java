package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.MoreNewsListResultBean;
import com.jkrm.fupin.bean.NewsBean;
import com.jkrm.fupin.bean.request.MoreNewsInfoRequestBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface NewsListContract {

    interface Presenter extends BasePresenter {
        void getMoreNewsInfoList(MoreNewsInfoRequestBean bean);
    }

    interface View extends BaseView {
        void getMoreNewsInfoListSuccess(MoreNewsListResultBean bean);
        void getMoreNewsInfoListFail(String msg);
    }
}
