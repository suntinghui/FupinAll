package com.jkrm.fupin.bean;

/**
 * Created by hzw on 2018/8/12.
 */

public class LoginBean {


    /**
     * accessToken : 9c62e30d9c814464a851179be9e388aa
     * chinesename : 你猜呀
     * createtime : 2018-08-01 13:27:23
     * discribe : 个人描述
     * id : 24dc7affc8284c8ea450db445dbde087
     * phone : 17798737602
     * portrait : user2.jpg
     * region : {"city":"5cccca2ac5a144cfa8c59deefae462f5","city_name":"古交市","code":"111","country":"d17b358e49974164bbc7ce0bb31f519e","country_name":"阳曲县","discribe":"阳曲镇描述","id":"808e89ed4e6a442db2a58baf7fc619e4","name":"阳曲镇","province":"a176d1d999f9491eb9fc342c6845e499","province_name":"山西省","selected":0,"town":""}
     * rid : 808e89ed4e6a442db2a58baf7fc619e4
     */

    private String accessToken;
    private String chinesename;
    private String createtime;
    private String discribe;
    private String id;
    private String phone;
    private String portrait;
    private RegionBean region;
    private String rid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public RegionBean getRegion() {
        return region;
    }

    public void setRegion(RegionBean region) {
        this.region = region;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public static class RegionBean {
        /**
         * city : 5cccca2ac5a144cfa8c59deefae462f5
         * city_name : 古交市
         * code : 111
         * country : d17b358e49974164bbc7ce0bb31f519e
         * country_name : 阳曲县
         * discribe : 阳曲镇描述
         * id : 808e89ed4e6a442db2a58baf7fc619e4
         * name : 阳曲镇
         * province : a176d1d999f9491eb9fc342c6845e499
         * province_name : 山西省
         * selected : 0
         * town :
         */

        private String city;
        private String city_name;
        private String code;
        private String country;
        private String country_name;
        private String discribe;
        private String id;
        private String name;
        private String province;
        private String province_name;
        private int selected;
        private String town;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }
    }
}
