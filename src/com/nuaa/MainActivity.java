package com.nuaa;
import java.util.Timer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nuaa.cmn.Constants;
import com.nuaa.cmn.SysCache;
import com.nuaa.cmn.UIHandler;
import com.nuaa.cmn.Utils;

public class MainActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main);
		
		
		try{
			WebView webview = (WebView) findViewById(R.id.webview);
			webview.loadUrl("http://134.175.123.97:8081/index.html");
			WebSettings webSettings = webview.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setAllowFileAccess(true);
			webSettings.setBuiltInZoomControls(true);
			webSettings.setDomStorageEnabled(true);
			
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			
//			webview.setWebViewClient(new webViewClient());
			webview.setWebChromeClient(new WebChromeClient());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		init();
		new Timer().schedule(new FileClient(), 0, 1000);
	}
	
	private void init(){
		SysCache.put(Constants.MAIN_ACTIVITY, MainActivity.this);
		SysCache.put(Constants.CONTEXT, getApplicationContext());
		UIHandler.init();
		Utils.loadMd52Cache();
	}
}

//Web视图
class webViewClient extends WebViewClient {

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
	  if(!url.startsWith("http"))return true;
      view.loadUrl(url);
      return true;
  }
  
  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
      view.removeJavascriptInterface("access...");
      view.removeJavascriptInterface("acc...");
  }
}
