package com.koolpos.download.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 文件工具类
 * @author joey.huang
 *
 */
public class FileUtil {

	public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
	
	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFileTo(File srcFile, File destFile)
			throws IOException {

		if (srcFile.isDirectory() || destFile.isDirectory())
			return false;// 判断是否是文件
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		int readLen = 0;
		byte[] buf = new byte[10240];
		while ((readLen = fis.read(buf)) != -1) {
			fos.write(buf, 0, readLen);
		}
		fos.flush();
		fos.close();
		fis.close();
		return true;

	}

	/**
	 * 删除文件(不包含目录)
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		MyLog.i(">>>删除文件了~");
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			// file.delete();
		} else {
			// SystemInfo.showToast(GrrzXrzAct.this, "文件不存在!");
		}
	}

	/**
	 * 打开SD卡上指定的文件
	 */
	public static void browseFile(String filepath, String fileName,
			Activity activity) {
		// String filepath = String.format("%s/%s/%s",
		// SystemInfo.getSdCardPath(), dirName, fileName);
		File file = new File(filepath);
		Log.i("test", "filepath: " + filepath + " ,fileName: " + fileName);
		if (file.exists()) {
			// 设置文件类型
			String type = checkFileType(fileName);
			Log.i("test", "type: " + type);
			Log.i("test", "取文件类型完成!");
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			if (!type.equals("")) {
				try {
					intent.setDataAndType(Uri.fromFile(file), type);
					activity.startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(activity,
							"文件打开异常,请到SD卡" + "MobilePlatform" + "目录中浏览！",
							Toast.LENGTH_SHORT).show();
					;
				}
			} else
				Toast.makeText(activity, "该文件类型无法识别！", Toast.LENGTH_SHORT)
						.show();
		}
	}

	/**
	 * 缓存写入文件数据data/data目录中
	 * 
	 * @param ser
	 * @param file
	 * @return
	 */
	public static boolean saveObject(Activity activity, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = activity.openFileOutput(file, activity.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 写string到文件
	 * @param str
	 */
	public static void writePiciFile(String str) {
		FileWriter writer = null;
		try {
			File f = new File(Environment.getExternalStorageDirectory()+"/pici.bin");
			if(!f.exists()) {
				f.createNewFile();
			}
			
			writer = new FileWriter(f, false);
			writer.write(str);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取文件
	 * @return
	 */
	public static String getPiciContent() {
		FileInputStream fileIS = null;
		BufferedReader buf = null;
		try {
			File f = new File(Environment.getExternalStorageDirectory()+"/pici.bin");
			if(!f.exists()) {
				f.createNewFile();
			}
			fileIS = new FileInputStream(f.getAbsolutePath());
			buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = buf.readLine();
			
			return readString;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 创建目录
	 */
	public static void createCatalog(String path) {
		File userFile = new File(path);
		if (userFile.exists()) {
			if (userFile.isFile()) {
				userFile.delete();
				userFile.mkdirs();
			}
		} else {
			userFile.mkdirs();
		}
	}

	/**
	 * 写入序列化对象文件
	 * 
	 * @param path
	 * @param obj
	 */
	public static void writeSerFile(String cachePath, Serializable obj) {
		File file = new File(cachePath);
		if (file.exists()) {
			try {
				deleteFile(file);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取序列化对象文件
	 * 
	 * @param path
	 * @param obj
	 */
	public static Serializable readSerFile(String cachePath) {
		File file = new File(cachePath);
		if (file.exists()) {
			ObjectInputStream objectInputStream = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				objectInputStream = new ObjectInputStream(fis);
				return (Serializable) objectInputStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 获取文件中指定文件的指定单位的大小(后面部分方法的总调用)
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sizeType
	 *            获取大小的类型1为B、2为KB、3为MB、4为GB
	 * @return double值的大小
	 */
	public static double getFileOrFilesSize(String filePath, int sizeType) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小", "获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
	}

	/**
	 * 获取下载 URL 的文件的大小
	 */
	public static long getFileSize(String address) {
		long filesize = 0;
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(address);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(10000);
			urlConn.setReadTimeout(10000);
			urlConn.connect();
			filesize = urlConn.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			urlConn.disconnect();
		}
		return filesize;
	}

	/**
	 * 取文件类型
	 */
	public static String checkFileType(String fileName) {
		Properties fileType = new Properties();
		Log.i("test", "取文件类型!");
		try {
			fileType.load(FileUtil.class
					.getResourceAsStream("/assets/andoirfiletype.properties"));
		} catch (IOException e) {
			Log.e("Utils", e.toString());
			e.printStackTrace();
		}
		int length = fileName.length();
		int dotPosition = fileName.lastIndexOf(".");
		String sufix = fileName.substring(dotPosition, length);
		return fileType.getProperty(sufix);
	}

	/**
	 * 调用此方法自动计算指定文件或指定文件夹的大小
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String getAutoFileOrFilesSize(String filePath) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小", "获取失败!");
		}
		return FormetFileSize(blockSize);
	}

	/**
	 * 获取指定文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}

	/**
	 * 获取指定文件夹(目录)大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSizes(File f) throws Exception {
		if(f == null) {
			return 0;
		}
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	private static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 * 
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	public static double FormetFileSize(long fileS, int sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
		case SIZETYPE_B:
			fileSizeLong = Double.valueOf(df.format((double) fileS));
			break;
		case SIZETYPE_KB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
			break;
		case SIZETYPE_MB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
			break;
		case SIZETYPE_GB:
			fileSizeLong = Double.valueOf(df
					.format((double) fileS / 1073741824));
			break;
		default:
			break;
		}
		return fileSizeLong;
	}
	
	/**
	* @Title: bytes2kb
    * @Description: TODO byte to kb or mb
    * @param @param bytes
    * @param @return
    * @return String 
    * @throws
    */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1) {
        	
        	return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();  
        return (returnValue + "KB");
    }
    
    public static void checkLogFile(boolean isProduct) {
    	if(isProduct) {
    		return;
    	}
    	File gpxfile = new File(Environment.getExternalStorageDirectory(), "KoolCustLog.txt");
		try {
			long fileSize = FileUtil.getFileSize(gpxfile);
			double currentLogFileZize = FileUtil.FormetFileSize(fileSize, FileUtil.SIZETYPE_MB);
			MyLog.i(">>>LOG 本地缓存文件大小是 currentLogFileZize: " + currentLogFileZize);
			if(currentLogFileZize > 10) {
				MyLog.e(">>>LOG 本地缓存文件大小超过 10 M, 自动删除重新创建 LOG 文件");
				FileUtil.deleteFile(gpxfile);
				gpxfile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
