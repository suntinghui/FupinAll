package com.koolpos.download.bean;

import java.io.Serializable;

/**
 * @author joey.huang
 * @version 2017年8月8日
 */
public class DownloadMsgBean implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 当前文件下载大小
	 */
	private int downloadSize;
	/**
	 * 返回信息
	 */
	private String message;

	/**
	 * 文件保存路径
	 */
	private String filePath;

	public DownloadMsgBean(String fileName) {
		super();
		this.fileName = fileName;
	}

	public DownloadMsgBean(String fileName, String message) {
		super();
		this.fileName = fileName;
		this.message = message;
	}

	public DownloadMsgBean(String fileName, int downloadSize) {
		super();
		this.fileName = fileName;
		this.downloadSize = downloadSize;
	}
	
	public DownloadMsgBean(String fileName, int downloadSize, String message) {
		super();
		this.fileName = fileName;
		this.downloadSize = downloadSize;
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getDownloadSize() {
		return downloadSize;
	}

	public void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "DownloadMsgBean{" +
				"fileName='" + fileName + '\'' +
				", downloadSize=" + downloadSize +
				", message='" + message + '\'' +
				", filePath='" + filePath + '\'' +
				'}';
	}
}
