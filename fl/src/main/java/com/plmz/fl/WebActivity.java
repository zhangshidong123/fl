package com.plmz.fl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		WebView webView = (WebView)findViewById(R.id.wv);
		WebSettings settings = webView.getSettings();
		settings.setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient());
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("utf-8");
		settings.setDomStorageEnabled(true);
		settings.setBuiltInZoomControls(false);
		settings.setSupportZoom(false);
		settings.setDisplayZoomControls(false);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		Map<String,String> head = new HashMap<String, String>();
		head.put("", "");
		
		webView.loadUrl("http://shop.yuge321.com/app/index.php?c=entry&do=shop&m=sz_yi&i=5", head);
	}
}
