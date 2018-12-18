package com.jkrm.fupin.mvp.contracts;

import com.jkrm.fupin.base.BasePresenter;
import com.jkrm.fupin.base.BaseView;
import com.jkrm.fupin.bean.HomePageListBean;
import com.jkrm.fupin.bean.VodTypeBean;

import java.util.List;

/**
 * Created by hzw on 2018/8/6.
 */

public interface MainContract {

    interface Presenter extends BasePresenter {
        void getVodTypeList();
        void getAudioTypeList();
        void getHomePageVideoList();
    }

    interface View extends BaseView {
        void getVodTypeListSuccess(List<VodTypeBean> list);
        void getVodTypeListFail(String msg);

        void getAudioTypeListSuccess(List<VodTypeBean> list);
        void getAudioTypeListFail(String msg);

        void getHomePageVideoListSuccess(HomePageListBean bean);
        void getHomePageVideoListFail(String msg);
    }
}
