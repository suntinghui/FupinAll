package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.bean.UpdateBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface CacheFileContract {

    interface Presenter extends BasePresenter {
        void queryAllBeans();
    }

    interface View extends BaseView {
        void queryAllBeansSuccess(List<CacheFileBean> bean);
        void queryAllBeansFail(String msg);

    }
}
