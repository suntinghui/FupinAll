package com.jkrm.fupin.api;

/**
 * Created by hzw on 2017/11/9.
 */

public abstract class CommonCallback<T> {

    public abstract void onStart();
    public abstract void onSuccess(T data);
    public abstract void onFailure(String msg);

}
