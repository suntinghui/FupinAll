package com.jkrm.fupin.util;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzw on 2018/9/13.
 */

public class LocalBrowseFileUtil {

    private List<File> getLocalVideoList(Context context, boolean isVideo) {
        File file = new File(context.getExternalFilesDir(""), "");
        List<File> fileList = new ArrayList<>();
        checkFile(fileList, file, isVideo);
        return fileList;
    }

    private void checkFile(List<File> fileList, File file, boolean isVideo) {// 遍历文件，在这里是遍历sdcard
        if (file.isDirectory()) {// 判断是否是文件夹
            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
            if (files != null) {// 如果文件夹不为空
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    checkFile(fileList, f, isVideo);// 递归调用
                }
            }
        } else if (file.isFile()) {// 判断是否是文件
            if (isVideo && MyUtil.isVideoType(file.getName())) {
                fileList.add(file);
            } else if(!isVideo && MyUtil.isAudioType(file.getName())) {
                fileList.add(file);
            }
        }
    }
}
