package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.VodTypeBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface MainFragmentContract {

    interface Presenter extends BasePresenter {
        void getHomePageVideoList();
    }

    interface View extends BaseView {
        void getHomePageVideoListSuccess(HomePageListBean bean);
        void getHomePageVideoListFail(String msg);
    }
}
