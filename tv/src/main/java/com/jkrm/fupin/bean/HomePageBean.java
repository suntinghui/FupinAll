package com.jkrm.fupin.bean;

import java.io.Serializable;

/**
 * Created by hzw on 2018/8/9.
 */

public class HomePageBean implements Serializable{

    /**
     * "classifyId":"1000005",
     "classifyName":"文化讲堂",
     "id":"d68ef4291f3f49fab19e6529286e16af",
     "num":1, 播放次数
     "osskey":"",
     "title":"dad"
     */

    private String classifyId;
    private String classifyName;
    private String createtime;
    private String id;
    private String imgPath;
    private String name;
    private String num;
    private String osskey;
    private String title;
    private String mark;
    private String profile;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOsskey() {
        return osskey;
    }

    public void setOsskey(String osskey) {
        this.osskey = osskey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
