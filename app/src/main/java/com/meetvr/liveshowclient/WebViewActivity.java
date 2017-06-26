package com.meetvr.liveshowclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.meetvr.share.utrils.Mutils;

/**
 * Created by wzm-pc on 2016/10/22.
 */

public class WebViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        Mutils.setTitle(findViewById(R.id.title_txt),getIntent().getStringExtra("name"));

        final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
        final WebView webView = (WebView) findViewById(R.id.myWebView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if(getIntent().getIntExtra("from",0)==0){
            webView.setInitialScale(100);
        }
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               webView.loadUrl(url);
                return true;
               // return super.shouldOverrideUrlLoading(view, url);
            }
        });

        String url = getIntent().getStringExtra("url");

        webView.loadUrl(url);
       // webView.loadUrl("file:///android_asset/warning.html");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }

            }
        });
    }


}
