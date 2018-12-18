package com.jkrm.fupin.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.LoginBean;
import com.jkrm.fupin.bean.request.LoginRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.LoginContract;
import com.jkrm.fupin.mvp.presenters.LoginPresenter;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.SharePreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hzw on 2018/8/12.
 */

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        mEtAccount.setNextFocusDownId(R.id.et_password);
        mEtPassword.setNextFocusDownId(R.id.btn_confirm);
    }

    @Override
    protected void initData() {
        super.initData();
        if(SharePreferencesUtil.getString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, null) != null) {
            toClass(MainActivity2.class, null, true);
        } else {
            test();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mEtAccount.getText().toString();
                String password = mEtPassword.getText().toString();
                if(TextUtils.isEmpty(userName)) {
                    showDialog("账号不可为空");
                } else if(TextUtils.isEmpty(password)) {
                    showDialog("密码不可为空");
                } else {
//                    mPresenter.login(userName, password);
                    LoginRequestBean requestBean = new LoginRequestBean(userName, password);
                    mPresenter.loginBody(requestBean);
                }
            }
        });
        mBtnConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    MyUtil.hideSoftInput(mActivity);
                }
            }
        });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void loginSuccess(LoginBean bean) {
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ID, bean.getId());
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_ACCESSTOKEN, bean.getId());
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_CHINESENAME, bean.getId());
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_PORTRAIT, bean.getId());
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_PHONE, bean.getId());
        SharePreferencesUtil.saveString(MyConstants.SharedPrefrenceXml.USER_INFO_XML, MyConstants.SharedPrefrenceKey.USER_RID, bean.getId());
        toClass(MainActivity2.class, null, true);
    }

    @Override
    public void loginFail(String msg) {
        showDialog(msg);
    }

    private void test() {
        mEtAccount.setText("13888888888");
        mEtPassword.setText("123456");
        mBtnConfirm.requestFocus();
    }
}
