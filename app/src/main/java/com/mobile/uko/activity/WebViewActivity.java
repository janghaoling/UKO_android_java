package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.mobile.uko.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "";
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("webview_url");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}