package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/20.
 */

public class CollectionRequestBean {

    private String vid; //视频id
    private String createuser; //当前用户id

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
}
