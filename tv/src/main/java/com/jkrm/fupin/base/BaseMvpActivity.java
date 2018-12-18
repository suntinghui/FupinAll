package com.jkrm.fupin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by hzw on 2018/7/18.
 */
public abstract class BaseMvpActivity<T extends CommonPresenter> extends BaseActivity {
    protected T mPresenter;

    protected abstract T createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }
}
