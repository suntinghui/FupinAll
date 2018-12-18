package com.jkrm.fupin.bean;

import java.util.List;

/**
 * Created by hzw on 2018/8/9.
 */

public class HomePageListBean {

    private List<HomePageBean> latestVideoList;
    private List<HomePageBean> heatVideoList;
    private List<HomePageBean> latestAudioList;
    private List<HomePageBean> heatAudioList;

    public List<HomePageBean> getLatestVideoList() {
        return latestVideoList;
    }

    public List<HomePageBean> getHeatVideoList() {
        return heatVideoList;
    }

    public List<HomePageBean> getLatestAudioList() {
        return latestAudioList;
    }

    public List<HomePageBean> getHeatAudioList() {
        return heatAudioList;
    }
}
