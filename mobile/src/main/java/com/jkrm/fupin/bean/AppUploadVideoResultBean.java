package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/14.
 */

public class AppUploadVideoResultBean {

    /**
     * classify : 1000002
     * createtime : 1534243311046
     * createuser : 27a00fe2aea2462d883cb6a9efb62989
     * delflag : false
     * examineflag : 0
     * id : 9dcf5cfcad454c5aa27762a5f928c08f
     * imgurl : https://tianyu-bucket.oss-cn-hangzhou.aliyuncs.com/notice/imgs/0a8a9e20-6e4b-4f58-a99b-570010a0dcb0.jpg
     * inout_side : false
     * mark : false
     * metatype : video/mp4
     * name : APP测试视频重命名文件名
     * orginname : APP测试视频原文件名
     * osspath : https://tianyu-bucket.oss-cn-hangzhou.aliyuncs.com/video/girl.mp4
     * status : false
     * title : APP测试视频标题03
     * totaltime : 12:30
     */

    private String classify;
    private String createtime;
    private String createuser;
    private boolean delflag;
    private int examineflag;
    private String id;
    private String imgurl;
    private boolean inout_side;
    private boolean mark;
    private String metatype;
    private String name;
    private String orginname;
    private String osspath;
    private boolean status;
    private String title;
    private String totaltime;

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public boolean isDelflag() {
        return delflag;
    }

    public void setDelflag(boolean delflag) {
        this.delflag = delflag;
    }

    public int getExamineflag() {
        return examineflag;
    }

    public void setExamineflag(int examineflag) {
        this.examineflag = examineflag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public boolean isInout_side() {
        return inout_side;
    }

    public void setInout_side(boolean inout_side) {
        this.inout_side = inout_side;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public String getMetatype() {
        return metatype;
    }

    public void setMetatype(String metatype) {
        this.metatype = metatype;
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

    public String getOsspath() {
        return osspath;
    }

    public void setOsspath(String osspath) {
        this.osspath = osspath;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }
}
