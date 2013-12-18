package pro.chinasoft.web;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InWebView extends WebViewClient {
	
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) { 

		view.loadUrl("http://www.baidu.com"); 

		return true; 

		 } 

}
