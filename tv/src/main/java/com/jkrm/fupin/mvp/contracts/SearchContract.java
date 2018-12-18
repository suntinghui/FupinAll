package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.HomePageBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.SearchResultBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.bean.request.SearchBean;

import org.fourthline.cling.controlpoint.event.Search;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface SearchContract {

    interface Presenter extends BasePresenter {
        void searchVodList(SearchBean searchBean);
    }

    interface View extends BaseView {
        void searchVodListSuccess(SearchResultBean bean);
        void searchVodListFail(String msg);
    }
}
