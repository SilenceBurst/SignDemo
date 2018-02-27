package com.sign.js_demo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class CallJsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_js);
        final WebView webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        //允许与js交互
        settings.setJavaScriptEnabled(true);
        //允许js弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("file:///android_asset/call_js.html");

        /**
         * 使用WebView的loadUrl方法调用js代码
         */
        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用javascript的callJs方法
                webView.loadUrl("javascript:callJs()");
            }
        });

        /**
         * 使用WebView的evaluateJavascript方法调用js代码
         * 比loadUrl效率更高，更简洁（该方法执行不会使页面刷新，而loadUrl会）
         * Android4.4之后才可以使用
         */
        findViewById(R.id.btn_evaluate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("javascript:callJs()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //value为js返回的结果
                        Toast.makeText(CallJsActivity.this, value, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //通过设置WebChromeClient来处理js的alert函数
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog alertDialog = new AlertDialog.Builder(CallJsActivity.this)
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
    }
}
