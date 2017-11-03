package com.sign.mvpdemo.model;
/*
 * 项目名:    MvpDemo
 * 包名       com.sign.mvpdemo.model
 * 文件名:    LoginModel
 * 创建者:    CYS
 * 创建时间:  2017/2/22 on 11:25
 * 描述:     TODO
 */

import android.os.Handler;
import android.text.TextUtils;

public class LoginModel implements ILoginModel {

    @Override
    public void login(final String name, final String pwd, final LoginCallBack callBack) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(name)) {
                    callBack.onNameError();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    callBack.onPwdError();
                    return;
                }
                if ("123".equals(name) && "123".equals(pwd)) {
                    callBack.onLoginSuccess();
                } else {
                    callBack.onLoginFailed("用户名密码错误");
                }
            }
        }, 2000);
    }
}
