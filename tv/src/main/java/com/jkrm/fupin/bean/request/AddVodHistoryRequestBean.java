package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/13.
 */

public class AddVodHistoryRequestBean {

    private String vid;//视频id
    private String createuser;//当前用户
    private String watchtime;//用户观看时长

    public AddVodHistoryRequestBean(String vid, String createuser, String watchtime) {
        this.vid = vid;
        this.createuser = createuser;
        this.watchtime = watchtime;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }
}
