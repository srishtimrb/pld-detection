package com.sample.plantleafdiseasedetectionapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.sample.plantleafdiseasedetectionapp.ui.theme.Utils;

public class PrecautionsActivity extends BaseActivity {

    private FrameLayout hld_loader;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.precaution_screen);
        webView = findViewById(R.id.webview);
        hld_loader = findViewById(R.id.hld_loader);
        checkNetwork();
    }

    private void checkNetwork() {
        if(!isConnectingToInternet()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Internet Connection Error!!!");
            alertDialog.setMessage("Please check your connection");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    checkNetwork();
                }
            });
            alertDialog.show();
            alertDialog.setCancelable(false);
        }else{
            renderView();
        }
    }

    private void renderView() {
        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webSettings.setAllowFileAccess(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        webView.loadUrl("file:///android_asset/page.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                toggleLoaderVisibility();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }

        });

    }

    private void toggleLoaderVisibility() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hld_loader.setVisibility(View.GONE);
            }
        },1000);
    }

}
