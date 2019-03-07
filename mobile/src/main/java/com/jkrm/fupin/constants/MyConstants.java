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
        public static final String BASE_URL = "http://47.92.175.158:8080/";

        public static final String UPDATE_DOWNLOAD_URL = "https://tianyu-bucket.oss-cn-hangzhou.aliyuncs.com/sxfp-version/android-version/fupin_mobile.apk";
    }

    public class SharedPrefrenceKey {
        public static final String USER_INFO_XML = "user_info_xml";
        public static final String USER_INFO_TOKEN = "field_userinfo_token";
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

    }

    public class Setting {
        public static final boolean isAllowDMS = false;
        public static final boolean isAllowRender = true;
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

    public class Upload {
        public static final String STATUS_BACK_INIT = "INIT";
        public static final String STATUS_BACK_UPLOADING = "UPLOADING";
        public static final String STATUS_BACK_SUCCESS = "SUCCESS";
        public static final String STATUS_BACK_FAIlURE = "FAIlURE";
        public static final String STATUS_BACK_CANCELED = "CANCELED";
        public static final String STATUS_BACK_PAUSING = "PAUSING";
        public static final String STATUS_BACK_PAUSED = "PAUSED";
        public static final String STATUS_BACK_DELETED = "DELETED";

        public static final String STATUS_INIT = "待上传";
        public static final String STATUS_UPLOADING = "上传中";
        public static final String STATUS_SUCCESS = "已上传";
        public static final String STATUS_FAIlURE = "上传失败";
        public static final String STATUS_CANCELED = "已取消";
        public static final String STATUS_PAUSING = "暂停中";
        public static final String STATUS_PAUSED = "已暂停";
        public static final String STATUS_DELETED = "已删除";
    }

    public class Popupwindow {
        public static final float POPUPWINDOW_DARK = 0.5f;
        public static final float POPUPWINDOW_NORMAL = 1.0f;
    }

    public class Action {
        public static final String ACTION_VIDEO_PREPARED = "videoPrepared";
        public static final String ACTION_VIDEO_BUFFERING_START = "videoBufferingStart";
        public static final String ACTION_VIDEO_BUFFERING_FINISH = "videoBufferingFinish";
        public static final String ACTION_VIDEO_ERROR = "videoError";
        public static final String ACTION_DOWNLOAD_SUCCESS = "download_success";
        public static final String ACTION_DOWNLOAD_PROGRESS = "download_progress";
        public static final String ACTION_DOWNLOAD_FAIL = "download_fail";
        public static final String ACTION_DMS_ADD = "upnp_dms_add";
        public static final String ACTION_DMS_REMOVE = "upnp_dms_remove";
        public static final String ACTION_DMS_DATA_GET_FAIL = "upnp_dms_data_get_fail";
    }
}
