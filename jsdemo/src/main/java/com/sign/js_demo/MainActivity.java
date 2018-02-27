package com.sign.js_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_going)
    TextView tvGoing;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_option)
    LinearLayout llOption;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.btn_call_js)
    Button btnCallJs;
    @BindView(R.id.btn_call_android)
    Button btnCallAndroid;

    private static final String MY_URL = "https://www.ybaby.com/mobile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();

        initView();
    }

    private void initView() {
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                tvStatus.setText("start...");
                Log.d("test", "start--------");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                tvStatus.setText("end...");
                Log.d("test", "end--------");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl("file:///android_asset/error.html");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("test", "progress--------" + newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
            }
        });
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        // 缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.loadUrl(MY_URL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前进
            case R.id.tv_going:
                if (webView.canGoForward()) {
                    webView.goForward();
                }
                break;
            //后退
            case R.id.tv_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                break;
            //刷新
            case R.id.tv_refresh:
                Log.d("test", "refresh--------" + webView.getUrl());
                //清除当前webview的访问记录
                webView.clearHistory();
                //此处的缓存是整个应用程序所有的webview的缓存
                //false 只清除内存缓存
                //true 清除所有的资源缓存
//                webView.clearCache(false);
                webView.loadUrl(webView.getUrl());
                break;
            case R.id.btn_call_android:
                startActivity(new Intent(this, CallAndroidActivity.class));
                break;
            case R.id.btn_call_js:
                startActivity(new Intent(this, CallJsActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListener() {
        tvGoing.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
        btnCallAndroid.setOnClickListener(this);
        btnCallJs.setOnClickListener(this);
    }
}
