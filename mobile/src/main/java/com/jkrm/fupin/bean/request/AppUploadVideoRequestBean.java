package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/14.
 */

public class AppUploadVideoRequestBean {

    /**
     * createuser: "当前上传用户ID",
     classify:"视频分类ID",
     title:"APP测试视频标题03",
     name:"APP测试视频重命名文件名",
     orginname:"APP测试视频原文件名",
     metatype: "媒体类型 mp4,rmvb"
     imgurl:"视频封面图片OSS存放地址",
     osspath:"视频OSS存放地址",
     totaltime:"视频总时长"
     */

    private String createuser;
    private String classify;
    private String title;
    private String name;
    private String orginname;
    private String metatype;
    private String imgurl;
    private String osspath;
    private String totaltime;

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrginname() {
        return orginname;
    }

    public void setOrginname(String orginname) {
        this.orginname = orginname;
    }

    public String getMetatype() {
        return metatype;
    }

    public void setMetatype(String metatype) {
        this.metatype = metatype;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getOsspath() {
        return osspath;
    }

    public void setOsspath(String osspath) {
        this.osspath = osspath;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    @Override
    public String toString() {
        return "AppUploadVideoRequestBean{" +
                "createuser='" + createuser + '\'' +
                ", classify='" + classify + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", orginname='" + orginname + '\'' +
                ", metatype='" + metatype + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", osspath='" + osspath + '\'' +
                ", totaltime='" + totaltime + '\'' +
                '}';
    }
}
