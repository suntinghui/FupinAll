package com.koolpos.download.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author joey.huang
 * @version 2017年8月7日
 */
public class FileDownloadThread extends Thread {
	
	private static final int CACHE_SIZE = 20 * 1024;
	/** 当前下载是否完成 */
	private boolean isCompleted = false;
	/** 当前下载文件长度 */
	private int downloadLength = 0;
	/** 文件保存路径 */
	private File file;
	/** 文件下载路径 */
	private URL downloadUrl;
	/** 当前下载线程ID */
	private int threadId;
	/** 线程下载数据长度 */
	private int blockSize;

	/**
	 * 
	 * @param url:文件下载地址
	 * @param file:文件保存路径
	 * @param blocksize:下载数据长度
	 * @param threadId:线程ID
	 */
	public FileDownloadThread(URL downloadUrl, File file, int blocksize, int threadId) {
		this.downloadUrl = downloadUrl;
		this.file = file;
		this.threadId = threadId;
		this.blockSize = blocksize;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile raf = null;
		HttpURLConnection conn = null;
		try {
			trustAllHosts();
			conn = (HttpURLConnection) downloadUrl.openConnection();
			conn.setReadTimeout(30 * 1000);
			conn.setAllowUserInteraction(true);
			int startPos = blockSize * (threadId - 1);//开始位置
			int endPos = blockSize * threadId - 1;//结束位置

			//设置当前线程下载的起点、终点
			conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
			byte[] buffer = new byte[CACHE_SIZE];
			bis = new BufferedInputStream(conn.getInputStream());
			raf = new RandomAccessFile(file, "rwd");
			raf.seek(startPos);
			int len;
			while ((len = bis.read(buffer, 0, CACHE_SIZE)) != -1) {
				raf.write(buffer, 0, len);
				downloadLength += len;
			}
			isCompleted = true;
		} catch (Exception e) {
			e.printStackTrace();
			isCompleted = true;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	 
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
	 
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}
		} };
	 
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线程文件是否下载完毕
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * 线程下载文件长度
	 */
	public int getDownloadLength() {
		return downloadLength;
	}
}
