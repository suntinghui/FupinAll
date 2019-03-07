export default {
  "baseUrl": "http://47.92.175.158:8080/",

  //video
  "videoList": "api/getHomePageVideoList",
  "videoTypeList": "api/getVodTypeList",
  "audioTypeList": "api/getAudioTypeList",
  "videoSearchList": "api/searchVodListByKeyword",
  "videoCollect": "account/saveUserCollection",
  "collectVideo": "account/getUserCollectionInfoList",
  "uncollectVideo": "account/deleteUserCollection",
  "addHistory": "account/addVodHistory",
  "historyList": "account/getVodHistoryList",
  //news
  "newsTypeList": "api/getNewsTypeList",
  "newsList": "api/getMoreNewsInfoList",
  "news": "api/getNewsInfoListForApp",
  //login
  "getUserRegion": "account/getUserRegion",
  "getMobileCaptcha": "account/getMobileCaptcha",
  "mobileExistCheck": "account/mobileExistCheck",
  "register": "account/register",
  "login": "account/login",
  "modifyPasswordByMobile": "account/modifyPasswordByMobile",

  //upload
  "getOss": "commons/getApiOssUrl",
  "uploadPortrait": "account/uploadUserPortrait",

  //personal
  "saveUserMsg": "account/saveUserBaseInfo",
  "feedBack": "account/addFeedBack",
 
  




}
