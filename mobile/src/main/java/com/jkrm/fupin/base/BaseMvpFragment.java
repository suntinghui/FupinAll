package com.jkrm.fupin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by hzw on 2018/7/18.
 */
public abstract class BaseMvpFragment<T extends CommonPresenter> extends BaseFragment {
    protected T mPresenter;

    protected abstract T createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }
}
