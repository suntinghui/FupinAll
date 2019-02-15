package com.koolpos.download.bean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.koolpos.download.constants.DownloadConstant;
import com.koolpos.download.util.MyLog;

import java.io.Serializable;
import java.util.List;

/**
 * @author joey.huang
 * @version 2017年8月7日
 */
public class DownloadBean implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 
	 * 文件名称, 可 null, 为 null 时自动截取本地保存路径"/"最后内容为名称 
	 * */
	private String fileName;
	/** 
	 * 文件大小, 可 null 
	 * */
	private int fileSize;
	/**
	 * 文件 md5, 可null
	 */
	private String fileMd5;
	/**
	 * 下载路径 URL
	 */
	private String downloadURL;
	/**
	 * 本地保存路径
	 */
	private String localSavePath;
	
	public DownloadBean(String downloadURL, String localSavePath) {
		this.downloadURL = downloadURL;
		this.localSavePath = localSavePath;
	}
	
	public DownloadBean(String downloadURL, String localSavePath, String fileMd5) {
		this.downloadURL = downloadURL;
		this.localSavePath = localSavePath;
		this.fileMd5 = fileMd5;
	}
	
	public String getFileName() {
		if(TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(localSavePath)) {
			fileName = localSavePath.substring(localSavePath.lastIndexOf("/") + 1, localSavePath.length());
			setFileName(fileName);
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getLocalSavePath() {
		return localSavePath;
	}

	public void setLocalSavePath(String localSavePath) {
		this.localSavePath = localSavePath;
	}

	/**
	 * 是否正在下载中
	 * @return
	 */
	public boolean isDownloadIng(List<DownloadBean> mDownloadList) {
		if(mDownloadList == null || mDownloadList.size() <= 0) {
			MyLog.e("download", "下载队列是空的, 必然未正在下载, fileName是: " + getFileName());
			return false;
		}
		if(mDownloadList.contains(this)) {
			MyLog.e("download", "下载队列包含该 BEAN 对象, fileName是: " + getFileName());
			return true;
		}
		for(DownloadBean mBaseDownloadBean : mDownloadList) {
			if(mBaseDownloadBean.getFileName() == getFileName()) {
				MyLog.e("download", "下载队列遍历获取到的文件 fileName 与该文件 fileName 相同, fileName是: " + getFileName());
				return true;
			}
		}
		MyLog.e("download", "下载队列不包含该文件, 文件 fileName 是: " + getFileName());
		return false;
	};
	/**
	 * 加入下载队列
	 */
	public void addToDownloadList(Context mContext) {
		Intent mIntent = new Intent();
		mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_QUENE_ADD);
		mIntent.putExtra(DownloadConstant.COMMON_PARAMS, this);
		mContext.sendBroadcast(mIntent);
	};
	/**
	 * 从下载队列移除
	 */
	public void removeFromDownloadList(Context mContext) {
		Intent mIntent = new Intent();
		mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_QUENE_REMOVE);
		mIntent.putExtra(DownloadConstant.COMMON_PARAMS, this);
		mContext.sendBroadcast(mIntent);
	};
}
