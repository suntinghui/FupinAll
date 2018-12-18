package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface LoginContract {

    interface Presenter extends BasePresenter {
        void login(String userName, String password);
        void loginBody(LoginRequestBean bean);
    }

    interface View extends BaseView {
        void loginSuccess(LoginBean bean);
        void loginFail(String msg);
    }
}
