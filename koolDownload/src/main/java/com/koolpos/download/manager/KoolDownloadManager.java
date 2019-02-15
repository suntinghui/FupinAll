package com.koolpos.download.manager;

import android.content.Context;
import android.content.Intent;

import com.koolpos.download.bean.DownloadBean;
import com.koolpos.download.bean.DownloadMsgBean;
import com.koolpos.download.constants.DownloadConstant;
import com.koolpos.download.download.DownloadThread;
import com.koolpos.download.util.MyLog;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author joey.huang
 * @version 2017年7月12日
 */
public class KoolDownloadManager {

	public static KoolDownloadManager instance;
	
	private KoolDownloadManager() {
		
	}
	
	public static KoolDownloadManager getInstance() {
		if(instance == null) {
			instance = new KoolDownloadManager();
		}
		return instance;
	}
	
	/**
	 * 无队列下载
	 * @param mContext 上下文
	 * @param mBaseDownloadBean 下载对象BEAN
	 */
	public void startDownload(Context mContext, DownloadBean mBaseDownloadBean) {
		if(mContext == null) return;
		if(mBaseDownloadBean == null) {
			DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(null, "下载对象为空，无法下载");
			Intent mIntent = new Intent();
			mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
			mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
			mContext.sendBroadcast(mIntent);
		}
		String localSavaPath = mBaseDownloadBean.getLocalSavePath();
		localSavaPath = localSavaPath.substring(0, localSavaPath.lastIndexOf(File.separator));
		File dirFile = new File(localSavaPath);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(mBaseDownloadBean.getLocalSavePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				if(mContext != null) {
					DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(mBaseDownloadBean.getFileName(), "本地保存文件创建失败，无法下载");
					Intent mIntent = new Intent();
					mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
					mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
					mContext.sendBroadcast(mIntent);
				}
				return;
			}
		}
		DownloadThread mDownloadThread = new DownloadThread(mContext, mBaseDownloadBean);
		mDownloadThread.start();
	}
	
	/**
	 * 有队列检测通过后下载文件
	 * @param mContext 上下文
	 * @param mBaseDownloadBean 下载对象BEAN
	 * @param mDownloadList 下载队列, 可为空
	 */
	public void startDownloadQuene(Context mContext, DownloadBean mBaseDownloadBean, List<DownloadBean > mDownloadList) {
		if(mContext == null) return;
		if(mBaseDownloadBean == null) {
			DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(null, "下载对象为空，无法下载");
			Intent mIntent = new Intent();
			mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
			mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
			mContext.sendBroadcast(mIntent);
		}
		if(mBaseDownloadBean.isDownloadIng(mDownloadList)) {
			MyLog.e("download", ">>>文件名称: " + mBaseDownloadBean.getFileName() + " 已在下载线程执行, 取消本次下载.");
			return;
		} 
		mBaseDownloadBean.addToDownloadList(mContext);
		String localSavaPath = mBaseDownloadBean.getLocalSavePath();
		localSavaPath = localSavaPath.substring(0, localSavaPath.lastIndexOf(File.separator));
		File dirFile = new File(localSavaPath);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(mBaseDownloadBean.getLocalSavePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				if(mContext != null) {
					DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(mBaseDownloadBean.getFileName(), "本地保存文件创建失败，无法下载");
					Intent mIntent = new Intent();
					mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
					mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
					mContext.sendBroadcast(mIntent);
				}
				return;
			}
		}
		DownloadThread mDownloadThread = new DownloadThread(mContext, mBaseDownloadBean);
		mDownloadThread.start();
	}
}
