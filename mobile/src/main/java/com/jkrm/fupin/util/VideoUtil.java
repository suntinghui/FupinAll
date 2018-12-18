package com.jkrm.fupin.util;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by hzw on 2018/8/14.
 */

public class VideoUtil {

    /**
     * 获取视频长度
     * @param filePath
     * @return
     */
    public static String getVideoDuration(String filePath) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(filePath);  //filePath 为音频文件的路径
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int duration= player.getDuration();//获取音频的时间
        MyLog.d("getVideoDuration duration: " + duration);
        player.release();//记得释放资源
        int seconds = duration / 1000;
        String durationResult = ((seconds / 60 > 9) ? (seconds / 60) : ("0" + seconds / 60)) + ":" +
                ((seconds % 60 > 9) ? (seconds % 60) : ("0" + seconds % 60));
        MyLog.d("getVideoDuration duration after: " + durationResult);
        return durationResult;
    }

}
