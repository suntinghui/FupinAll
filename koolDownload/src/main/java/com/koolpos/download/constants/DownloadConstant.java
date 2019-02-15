package com.koolpos.download.constants;


/**
 * @author joey.huang
 * @version 2017年8月7日
 */
public class DownloadConstant {

	/**
	 * 下载中
	 */
	public static final String ACTION_DOWNLOAD_ING = "com.koolpos.download.ing";
	/**
	 * 下次成功(含 MD5 验证通过)
	 */
	public static final String ACTION_DOWNLOAD_SUCCESS = "com.koolpos.download.success";
	/*
	 * 下载失败(含 MD5 验证失败)
	 */
	public static final String ACTION_DOWNLOAD_FAIL = "com.koolpos.download.fail";
	/**
	 * 添加下载到队列
	 */
	public static final String ACTION_DOWNLOAD_QUENE_ADD = "com.koolpos.download_quene_add";
	/**
	 * 从队列移除
	 */
	public static final String ACTION_DOWNLOAD_QUENE_REMOVE = "com.koolpos.download_quene_remove";
	/**
	 * 传递参数 key
	 */
	public static final String COMMON_PARAMS = "download";

}
