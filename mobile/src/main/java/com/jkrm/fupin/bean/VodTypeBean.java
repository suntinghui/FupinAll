package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/8.
 */

public class VodTypeBean {

    private String createtime;
    private boolean delflag;
    private String discribe;
    private String id;
    private boolean mark;
    private String name;
    private String updateuser;
//    private BaseFragment fragment;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public boolean isDelflag() {
        return delflag;
    }

    public void setDelflag(boolean delflag) {
        this.delflag = delflag;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

//    public BaseFragment getFragment() {
//        return fragment;
//    }
//
//    public void setFragment(BaseFragment fragment) {
//        this.fragment = fragment;
//    }

    @Override
    public String toString() {
        return "VodTypeBean{" +
                "createtime='" + createtime + '\'' +
                ", delflag='" + delflag + '\'' +
                ", discribe='" + discribe + '\'' +
                ", id='" + id + '\'' +
                ", mark='" + mark + '\'' +
                ", name='" + name + '\'' +
                ", updateuser='" + updateuser + '\'' +
                '}';
    }
}