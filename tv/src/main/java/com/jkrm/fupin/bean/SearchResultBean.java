package com.jkrm.fupin.bean;

import java.util.List;

/**
 * Created by hzw on 2018/8/14.
 */

public class SearchResultBean {


    /**
     * totalNum : 1
     * vodList : [{"classifyId":"1000008","classifyName":"自然百科","id":"c3e28f705f344c42a97662680381b0dd","imgPath":"http://www.baidu2.com","num":0,"osskey":"www.baidu.com","title":"杨钊大大大"}]
     */

    private int totalNum;
    private List<HomePageBean> vodList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<HomePageBean> getVodList() {
        return vodList;
    }

    public void setVodList(List<HomePageBean> vodList) {
        this.vodList = vodList;
    }

}
