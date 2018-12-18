package com.jkrm.fupin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzw on 2018/8/20.
 */

public class CollectionInfoListBean implements Serializable{

    private List<CollectionsModelBean> collectionsModel;

    public List<CollectionsModelBean> getCollectionsModel() {
        return collectionsModel;
    }

    public void setCollectionsModel(List<CollectionsModelBean> collectionsModel) {
        this.collectionsModel = collectionsModel;
    }

    public static class CollectionsModelBean implements Serializable{
        /**
         * createtime : 1533276161000
         * createuser : 26716358c4a84f9681dd89db89aaff00
         * id : 212297d765d84ec486794172d6c022e9
         * vid : 8b085a9639124fe3bed3e4baf3e4fe1b
         * vodModel : {"classify":"1000003","id":"8b085a9639124fe3bed3e4baf3e4fe1b","imgurl":"http://www.baidu1.com","name":"a.mp4","osspath":"http://www.baidu1.com/a.mp4","title":"高金旺"}
         */

        private String createtime;
        private String createuser;
        private String id;
        private String vid;
        private VodModelBean vodModel;

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

        public VodModelBean getVodModel() {
            return vodModel;
        }

        public void setVodModel(VodModelBean vodModel) {
            this.vodModel = vodModel;
        }

        public static class VodModelBean implements Serializable{
            /**
             * classify : 1000003
             * id : 8b085a9639124fe3bed3e4baf3e4fe1b
             * imgurl : http://www.baidu1.com
             * name : a.mp4
             * osspath : http://www.baidu1.com/a.mp4
             * title : 高金旺
             */

            private String classify;
            private String id;
            private String imgurl;
            private String name;
            private String osspath;
            private String title;

            public String getClassify() {
                return classify;
            }

            public void setClassify(String classify) {
                this.classify = classify;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOsspath() {
                return osspath;
            }

            public void setOsspath(String osspath) {
                this.osspath = osspath;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
