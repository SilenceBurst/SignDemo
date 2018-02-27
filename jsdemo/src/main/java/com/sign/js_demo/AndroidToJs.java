package com.sign.js_demo;

import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2018/2/10.
 */

public class AndroidToJs extends Object {
    @JavascriptInterface
    public void hello(String msg) {
        System.out.println(msg);
    }
}
