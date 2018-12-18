package com.jkrm.fupin.interfaces;

/**
 * Created by hzw on 2018/8/15.
 */

public interface IVideoPrepareListener {

    void videoPrepared();
    void videoBufferingStart();
    void videoBufferingFinish();
}
