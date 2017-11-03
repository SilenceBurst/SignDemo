package com.sign.mvpdemo.model;
/*
 * 项目名:    MvpDemo
 * 包名       com.sign.mvpdemo.model
 * 文件名:    ILoginModel
 * 创建者:    CYS
 * 创建时间:  2017/2/22 on 11:22
 * 描述:     TODO
 */

public interface ILoginModel {
    void login(String name,String pwd,LoginCallBack callBack);
    public interface LoginCallBack{
        void onNameError();
        void onPwdError();
        void onLoginSuccess();
        void onLoginFailed(String message);
    }
}
