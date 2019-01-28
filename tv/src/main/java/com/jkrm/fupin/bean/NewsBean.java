package com.jkrm.fupin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzw on 2018/8/20.
 */

public class NewsBean implements Serializable{

    /**
     * typeName : 新闻
     * typeId : 0059617c25194caea0f0ae35336e64fb
     * noticesList : [{"content":"测色网","createtime":"2018-07-24","id":"0c8cc44780b748448699219b81a09442","mid":"0059617c25194caea0f0ae35336e64fb","noticeFiles":[{"id":"d953c0286f614b8fb8fc703e36012d34","metatype":"png","name":"测试文件名","orginname":"测试原始文件名","osskey":"woqunianmailegebiao"}],"title":"20180724系统更新公告"},{"content":"公告测试001","createtime":"2018-07-23","id":"890c269d66d84369aa8759c7fab335a0","mid":"0059617c25194caea0f0ae35336e64fb","noticeFiles":[],"title":"20180723最新公告"}]
     */

    private String typeName;
    private String typeId;
    private List<NoticesListBean> noticesList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<NoticesListBean> getNoticesList() {
        return noticesList;
    }

    public void setNoticesList(List<NoticesListBean> noticesList) {
        this.noticesList = noticesList;
    }

    public static class NoticesListBean implements Serializable{
        /**
         * content : 测色网
         * createtime : 2018-07-24
         * id : 0c8cc44780b748448699219b81a09442
         * mid : 0059617c25194caea0f0ae35336e64fb
         * noticeFiles : [{"id":"d953c0286f614b8fb8fc703e36012d34","metatype":"png","name":"测试文件名","orginname":"测试原始文件名","osskey":"woqunianmailegebiao"}]
         * title : 20180724系统更新公告
         */

        private String content;
        private String createtime;
        private String id;
        private String mid;
        private String title;
        private List<NoticeFilesBean> noticeFiles;
        private List<ZipFilesBean> zipFiles;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<NoticeFilesBean> getNoticeFiles() {
            return noticeFiles;
        }

        public void setNoticeFiles(List<NoticeFilesBean> noticeFiles) {
            this.noticeFiles = noticeFiles;
        }

        public List<ZipFilesBean> getZipFiles() {
            return zipFiles;
        }

        public void setZipFiles(List<ZipFilesBean> zipFiles) {
            this.zipFiles = zipFiles;
        }

        public static class NoticeFilesBean implements Serializable{
            /**
             * id : d953c0286f614b8fb8fc703e36012d34
             * metatype : png
             * name : 测试文件名
             * orginname : 测试原始文件名
             * osskey : woqunianmailegebiao
             */

            private String id;
            private String metatype;
            private String name;
            private String orginname;
            private String osskey;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getOsskey() {
                return osskey;
            }

            public void setOsskey(String osskey) {
                this.osskey = osskey;
            }
        }


        public static class ZipFilesBean implements Serializable{
            /**
             * id : d953c0286f614b8fb8fc703e36012d34
             * metatype : png
             * name : 测试文件名
             * orginname : 测试原始文件名
             * osskey : woqunianmailegebiao
             */

            private String id;
            private String metatype;
            private String name;
            private String orginname;
            private String osskey;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getOsskey() {
                return osskey;
            }

            public void setOsskey(String osskey) {
                this.osskey = osskey;
            }
        }
    }
}
