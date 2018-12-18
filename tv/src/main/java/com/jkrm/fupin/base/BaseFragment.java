package com.jkrm.fupin.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hzw on 2018/7/18.
 */
public abstract class BaseFragment extends Fragment implements BaseView, View.OnClickListener{

    protected BaseActivity mActivity;
    private Unbinder unbinder;
    protected CompositeSubscription mCompositeSubscription;
    protected String TAG = "";
    protected View view;

    protected abstract int getContentId();
    protected void initData() {}
    protected void initView() {}
    protected void initEvent() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getContentId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void showLoadingDialog() {
        ((BaseActivity) getActivity()).showLoadingDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        ((BaseActivity) getActivity()).dismissLoadingDialog();
    }

    @Override
    public void showMsg(String msg) {
        ((BaseActivity) getActivity()).showMsg(msg);
    }

    protected void showDialog(String msg) {
        ((BaseActivity) getActivity()).showDialog(msg);
    }

    @Override
    public void onClick(View view) {

    }

    public void toClass(Class<? extends Activity> classTo, Bundle bundle, boolean needFinish) {
        if (classTo == null)
            return;
        Intent intent = new Intent(mActivity, classTo);
        if(bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        if(needFinish)
            mActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
