package com.sample.plantleafdiseasedetectionapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;

import com.sample.plantleafdiseasedetectionapp.ui.theme.Utils;

public class RealTimeDetectionActivity extends BaseActivity {

    private FrameLayout hld_loader;
    private WebView webView;
    private static final int FILE_CHOOSER_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> filePathCallback;
    private Button btn_precaution;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realtime_screen);
        webView = findViewById(R.id.webview);
        hld_loader = findViewById(R.id.hld_loader);
        btn_precaution = findViewById(R.id.btn_precaution);
        checkNetwork();

        btn_precaution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RealTimeDetectionActivity.this,PrecautionsActivity.class);
                startActivity(intent);

            }
        });
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
        webView.loadUrl(Utils.finalURL);

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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                RealTimeDetectionActivity.this.filePathCallback = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), FILE_CHOOSER_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (filePathCallback != null) {
                Uri[] results = (resultCode == RESULT_OK && data != null) ? new Uri[]{data.getData()} : null;
                filePathCallback.onReceiveValue(results);
                filePathCallback = null;
            }
        }
    }

}
