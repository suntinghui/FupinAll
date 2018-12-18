package com.jkrm.fupin.constants;

/**
 * Created by hzw on 2018/7/18.
 */

public class MyConstants {

    public class Params {
        public static final String COMMON_PARAMS = "myParams";
        public static final String COMMON_PARAMS_BEAN = "myBeanParams";
    }

    public class Urls {
        /**
         * 扶贫当前测试URL基地址
         */
//        public static final String BASE_URL = "http://111.13.56.38:9999/api/";
        public static final String BASE_URL = "http://47.92.175.158:8080/";

        public static final String UPDATE_DOWNLOAD_URL = "https://tianyu-bucket.oss-cn-hangzhou.aliyuncs.com/sxfp-version/android-version/fupin_mobile.apk";
    }

    public class SharedPrefrenceXml {
        public static final String USER_INFO_XML = "user_info_xml";
        public static final String UPNP_SETTING_XML = "upnp_setting_xml";
    }

    public class SharedPrefrenceKey {
        public static final String USER_ID = "user_id";
        public static final String USER_ACCESSTOKEN = "accessToken";
        public static final String USER_CHINESENAME = "chinesename";
        public static final String USER_PHONE = "phone";
        public static final String USER_PORTRAIT = "portrait";
        public static final String USER_RID = "rid";

        public static final String USER_INFO_TOKEN = "field_userinfo_token";

        public static final String UPNP_SETTING_ALLOW_SHARE = "key_upnp_setting_allow_share";
    }

    public class DisplayUi {
        public static final float POPUPWINDOW_DARK = 0.5f;
        public static final float POPUPWINDOW_NORMAL = 1.0f;
    }

    public class RequestCode {
        public static final int BLUE_REQUEST_OPEN = 0;
    }

    public class Constant {
        public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

        public static final String RES_VIDEO_MARK = "0";
        public static final String RES_AUDIO_MARK = "1";

        public static final String DISK_DIRECTORY = "/sxfp";
        public static final String DISK_DIRECTORY_APP = "/Android/data/com.jkrm.fupin/files";

        public static final int POLLING_INTEVAL_DB_SERVICE = 60 * 5;
        public static final int POLLING_INTEVAL_MEIDA_SERVICE = 60 * 5;
        public static final int POLLING_INTEVAL_UPNP_SERVICE = 60 * 60;
        public static final int POLLING_INTEVAL_OSS_DOWNLOAD_SERVICE = 60 * 5;

    }

    public class Setting {
        public static final boolean isAllowDMS = true;
        public static final boolean isAllowRender = true;
        public static final boolean isAllowUpnp = true;

        public static final int CORNER_COMMON = 5;
    }

    public class Msg {
        public static final String NET_ERROR_COMMON = "网络异常,请稍后重试";
        public static final String NET_ERROR_SERVER = "服务器暂时无法连接";
        public static final String NET_ERROR_UNKNOWN = "暂时无法为您提供服务";
    }

    public class Code {
        public static final int CODE_SUCCESS = 0000; //成功code
        public static final int CODE_FAILURE_TOKEN = 401; //token过期
    }

    public class Popupwindow {
        public static final float POPUPWINDOW_DARK = 0.5f;
        public static final float POPUPWINDOW_NORMAL = 1.0f;
    }

    public class GridViewSpanCount {
        public static final int SPAN_COUNT = 5;
        public static final float SCALE_X = 1.0f;
        public static final float SCALE_X_BIG = 1.05f;
        public static final float SCALE_X_BIG2 = 1.2f;
        public static final float SCALE_Y = 1.0f;
        public static final float SCALE_Y_BIG = 1.05f;
        public static final float SCALE_Y_BIG2 = 1.2f;
    }

    public class Action {
        public static final String ACTION_VIDEO_PREPARED = "videoPrepared";
        public static final String ACTION_VIDEO_BUFFERING_START = "videoBufferingStart";
        public static final String ACTION_VIDEO_BUFFERING_FINISH = "videoBufferingFinish";
        public static final String ACTION_DOWNLOAD_SUCCESS = "download_success";
        public static final String ACTION_DOWNLOAD_PROGRESS = "download_progress";
        public static final String ACTION_DOWNLOAD_FAIL = "download_fail";
        public static final String ACTION_SERVICE_CHECK_DB = "serviceCheckDB";
        public static final String ACTION_SERVICE_CHECK_MEDIA = "serviceCheckMedia";
        public static final String ACTION_SERVICE_CHECK_UPNP = "serviceCheckUpnp";
        public static final String ACTION_SERVICE_OSS_DOWNLOAD = "serviceOssDownload";
    }
}
