package com.sign.mvpdemo.presenter;
/*
 * 项目名:    MvpDemo
 * 包名       com.sign.mvpdemo.presenter
 * 文件名:    LoginPresenter
 * 创建者:    CYS
 * 创建时间:  2017/2/22 on 11:29
 * 描述:     TODO
 */

import com.sign.mvpdemo.model.ILoginModel;
import com.sign.mvpdemo.model.LoginModel;
import com.sign.mvpdemo.view.ILoginView;

public class LoginPresenter implements ILoginPresenter, ILoginModel.LoginCallBack {
    private ILoginView view;
    private ILoginModel model;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        model = new LoginModel();
    }

    @Override
    public void onNameError() {
        if (view != null) {
            view.showProgress(false);
            view.setNameError();
        }
    }

    @Override
    public void onPwdError() {
        if (view != null) {
            view.showProgress(false);
            view.setPwdError();
        }
    }

    @Override
    public void onLoginSuccess() {
        if (view != null) {
            view.showProgress(false);
            view.loginSuccess();
        }
    }

    @Override
    public void onLoginFailed(String message) {
        if (view != null) {
            view.showProgress(false);
            view.showToast(message);
        }
    }

    @Override
    public void login(String name, String pwd) {
        if (view != null) {
            view.showProgress(true);
        }
        model.login(name, pwd, this);
    }

    @Override
    public void destroy() {
        view = null;
    }

}
