package com.jkrm.fupin.bean;

import java.util.List;

/**
 * Created by hzw on 2018/9/6.
 */

public class MoreNewsListResultBean {

    private int totalNum;
    private List<NewsBean.NoticesListBean> noticesList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<NewsBean.NoticesListBean> getNoticesList() {
        return noticesList;
    }

    public void setNoticesList(List<NewsBean.NoticesListBean> noticesList) {
        this.noticesList = noticesList;
    }
}
