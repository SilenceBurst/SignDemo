package com.sign.js_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallAndroidActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_add_javascript_interface)
    Button btnAddJavascriptInterface;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.btn_should_override_url_loading)
    Button btnShouldOverrideUrlLoading;
    @BindView(R.id.btn_on_js_prompt)
    Button btnOnJsPrompt;
    private WebSettings settings;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_android);
        ButterKnife.bind(this);
        mContext = this;
        settings = webView.getSettings();
        btnAddJavascriptInterface.setOnClickListener(this);
        btnShouldOverrideUrlLoading.setOnClickListener(this);
        btnOnJsPrompt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 对象映射
             * 缺点：存在严重的漏洞
             */
            case R.id.btn_add_javascript_interface:
                //设置与js交互的权限
                settings.setJavaScriptEnabled(true);
                //将java对象映射到js对象
                webView.addJavascriptInterface(new AndroidToJs(), "test");
                webView.loadUrl("file:///android_asset/add_javascript_interface.html");
                break;
            //通过shouldOverrideUrlLoading回调拦截url
            case R.id.btn_should_override_url_loading:
                //设置与js交互的权限
                settings.setJavaScriptEnabled(true);
                //允许js弹窗
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Uri uri = Uri.parse(url);
                        /*
                         *根据协议的参数判断是否是需要的url
                         * 一般通过scheme（协议格式）和authority（协议名）判断
                         */
                        if ("js".equals(uri.getScheme()) && "webview".equals(uri.getAuthority())) {
                            Set<String> parameters = uri.getQueryParameterNames();
                            Iterator<String> iterator = parameters.iterator();
                            HashMap<String, String> map = new HashMap<>();
                            while (iterator.hasNext()) {
                                String next = iterator.next();
                                map.put(next, uri.getQueryParameter(next));
                            }

                            String result = hello(map);
                            //传递返回值给js 单引号要注意啊
                            webView.loadUrl("javascript:getResult('" + result + "')");
                            return true;
                        }
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                });
                //通过设置WebChromeClient来处理js的alert函数
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setTitle("调用Js")
                                .setMessage(message)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                                .create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        return true;
                    }
                });
                webView.loadUrl("file:///android_asset/should_override_url_loading.html");
                break;
            case R.id.btn_on_js_prompt:
                //设置与js交互的权限
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                        Uri uri = Uri.parse(message);
                        if ("js".equals(uri.getScheme()) && "webview".equals(uri.getAuthority())) {

                            result.confirm("js 调用android的方法成功了");
                            return true;
                        }
                        return super.onJsPrompt(view, url, message, defaultValue, result);
                    }
                });
                webView.loadUrl("file:///android_asset/on_js_prompt.html");
                break;
            default:
                break;
        }
    }

    public String hello(HashMap map) {
        Toast.makeText(mContext, "js调用了android的方法" + map.toString(), Toast.LENGTH_LONG).show();
        return " call android ok";
    }
}
