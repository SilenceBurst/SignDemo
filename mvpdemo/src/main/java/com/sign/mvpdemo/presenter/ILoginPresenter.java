package com.sign.mvpdemo.presenter;
/*
 * 项目名:    MvpDemo
 * 包名       com.sign.mvpdemo.presenter
 * 文件名:    ILoginPresenter
 * 创建者:    CYS
 * 创建时间:  2017/2/22 on 11:19
 * 描述:     TODO
 */

public interface ILoginPresenter {
    void login(String name, String pwd);
    void destroy();
}
