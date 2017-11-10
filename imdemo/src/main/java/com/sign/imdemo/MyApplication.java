package com.sign.imdemo;

import android.app.Application;
import android.util.Log;

import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;

/**
 * 项目名:    IMDemo
 * 包名:      com.sign.imdemo
 * 文件名:    MyApplication
 * 创建者:    CYS
 * 创建时间:  2017/11/7 0007 on 8:57
 * 描述:     TODO
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // appKey 可以在七鱼管理系统->设置->APP接入 页面找到
        if (!Unicorn.init(this, "8bf848aab0a605166fdfb3a0b08b6845", options(),
                new GlideImageLoader(this))) {
            Log.d("test", "init error");
        }
    }

    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        options.savePowerConfig.customPush = true;
        return options;
    }
}
