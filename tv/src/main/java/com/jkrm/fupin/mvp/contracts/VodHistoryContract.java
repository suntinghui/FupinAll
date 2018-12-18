package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.VodHistoryBean;
import com.jkrm.fupin.bean.request.VodHistoryRequestBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface VodHistoryContract {

    interface Presenter extends BasePresenter {
        void getVodHistoryList(VodHistoryRequestBean bean);
    }

    interface View extends BaseView {
        void getVodHistoryListSuccess(List<VodHistoryBean> list);
        void getVodHistoryListFail(String msg);
    }
}
