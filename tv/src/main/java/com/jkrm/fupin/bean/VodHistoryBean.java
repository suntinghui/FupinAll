package com.jkrm.fupin.bean;

import java.io.Serializable;

/**
 * Created by hzw on 2018/8/13.
 */

public class VodHistoryBean implements Serializable{


    /**
     * createtime : 1533277820000
     * id : ebb32faa1382434e89d53f4ced21a4cc
     * vid : c3e28f705f344c42a97662680381b0dd
     * vimgPath : http://www.baidu2.com
     * vossPath : www.baidu.com
     * vtitle : 杨钊大大大大
     * watchtime : 02:10:40
     */

    private String createtime;
    private String id;
    private String vid;
    private String vimgPath;
    private String vossPath;
    private String vtitle;
    private String watchtime;

    private String percent; //本地自己添加的, 播放百分比

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVimgPath() {
        return vimgPath;
    }

    public void setVimgPath(String vimgPath) {
        this.vimgPath = vimgPath;
    }

    public String getVossPath() {
        return vossPath;
    }

    public void setVossPath(String vossPath) {
        this.vossPath = vossPath;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }
}
