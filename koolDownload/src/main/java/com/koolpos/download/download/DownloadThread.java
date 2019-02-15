package com.koolpos.download.download;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.koolpos.download.bean.DownloadBean;
import com.koolpos.download.bean.DownloadMsgBean;
import com.koolpos.download.constants.DownloadConstant;
import com.koolpos.download.util.FileUtil;
import com.koolpos.download.util.Md5Utils;
import com.koolpos.download.util.MyLog;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载线程, 广播回传进度及结果
 * @author joey.huang
 * @version 2017年8月7日
 */
public class DownloadThread extends Thread {
	private Context mContext;
	private DownloadBean mBaseDownloadBean;
	private String downloadUrl;
	private String filePath;
	private String fileName;
	private int threadNum;
	private int blockSize;
	
	public DownloadThread(Context mContext, DownloadBean mBaseDownloadBean) {
		this.mContext = mContext;
		this.mBaseDownloadBean = mBaseDownloadBean;
		downloadUrl = mBaseDownloadBean.getDownloadURL();
		fileName = mBaseDownloadBean.getFileName();
		filePath = mBaseDownloadBean.getLocalSavePath();
		MyLog.d("DownloadThread filePath: " + filePath);
		if(mBaseDownloadBean == null || TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(filePath)) {
			sendDownloadFailMsg("下载参数缺失，请检测完整后下载");
			return;
		}
		threadNum = 1;
	}

	public DownloadThread(String downloadUrl, int threadNum, String fileptah) {
		this.downloadUrl = downloadUrl;
		this.threadNum = threadNum;
		this.filePath = fileptah;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(30 * 1000);
			conn.connect();
			if(conn.getResponseCode() == 302) {
				String location = conn.getHeaderField("Location");
                url  = new URL(location);
    			conn = (HttpURLConnection) url.openConnection();
    			conn.setReadTimeout(30 * 1000);
    			conn.connect();
    			threadNum = 1;
			}
			if(conn.getResponseCode() == 200) {
				// 读取下载文件总大小
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					sendDownloadFailMsg("读取文件大小失败，请检查网络！");
					return;
				}
				// 计算每条线程下载的数据长度
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
						: fileSize / threadNum + 1;
				File file = new File(filePath);
				FileDownloadThread[] threads = new FileDownloadThread[threadNum];
				for (int i = 0; i < threads.length; i++) {
					// 启动线程，分别下载每个线程需要下载的部分
					threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// 当前所有线程下载总量
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					float tempSize = (float) downloadedAllSize / (float) fileSize;
					int progress = (int) (tempSize * 100);
					sendDownloadProgressMsg(progress);
					Thread.sleep(1000);// 休息1秒后再读取下载进度
				}
				checkMd5();
			} else {
				sendDownloadFailMsg("下载失败，错误码："+conn.getResponseCode());
			}
		} catch(Exception e) {
			e.printStackTrace();
			sendDownloadFailMsg("下载异常，请检测网络");
		}
	}
	
	private void sendDownloadProgressMsg(int updateDownloadSize) {
		if(mContext == null)
			return;
		DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(fileName, updateDownloadSize);
		Intent mIntent = new Intent();
		mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_ING);
		mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
		mContext.sendBroadcast(mIntent);
	}
	
	private void sendDownloadSuccessMsg() {
		if(mContext == null)
			return;
		mBaseDownloadBean.removeFromDownloadList(mContext);
		DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(fileName);
		mDownloadMsgBean.setFilePath(filePath);
		Intent mIntent = new Intent();
		mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_SUCCESS);
		mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
		mContext.sendBroadcast(mIntent);
		
		mContext = null;
	}
	
	private void sendDownloadFailMsg(String failMsg) {
		if(mContext == null)
			return;
		DownloadMsgBean mDownloadMsgBean = new DownloadMsgBean(fileName, failMsg);
		mBaseDownloadBean.removeFromDownloadList(mContext);
		Intent mIntent = new Intent();
		mIntent.setAction(DownloadConstant.ACTION_DOWNLOAD_FAIL);
		mIntent.putExtra(DownloadConstant.COMMON_PARAMS, mDownloadMsgBean);
		mContext.sendBroadcast(mIntent);
		FileUtil.deleteFile(new File(filePath));
		
		mContext = null;
	}
	
	private void checkMd5() throws IOException {
		String serverFileMd5 = mBaseDownloadBean.getFileMd5();
		if(TextUtils.isEmpty(serverFileMd5)) {
			sendDownloadSuccessMsg();
			return;
		}
		File localFile = new File(filePath);
		String localFileMd5 = Md5Utils.getFileMD5String(localFile);
		if(!localFileMd5.equals(serverFileMd5)) {
			sendDownloadFailMsg("下载文件不完整，请重新下载！");
		} else {
			sendDownloadSuccessMsg();
		}
	}
}
