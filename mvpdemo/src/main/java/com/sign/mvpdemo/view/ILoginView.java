package com.sign.mvpdemo.view;
/*
 * 项目名:    MvpDemo
 * 包名       com.sign.mvpdemo.view
 * 文件名:    ILoginView
 * 创建者:    CYS
 * 创建时间:  2017/2/22 on 11:12
 * 描述:     TODO
 */

public interface ILoginView {
    void setNameError();
    void setPwdError();
    void loginSuccess();
    void showProgress(boolean whether);
    void showToast(String message);
}
