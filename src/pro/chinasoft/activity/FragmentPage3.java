package pro.chinasoft.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentPage3 extends Fragment{
	private WebView wv=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		 View view = inflater.inflate(R.layout.fragment_3, container, false);
		 wv=(WebView)view.findViewById(R.id.webView);
		 wv.getSettings().setJavaScriptEnabled(true);  //设置WebView支持javascript
		  wv.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
		  wv.getSettings().setSupportZoom(true); //设置支持缩放
		  wv.getSettings().setBuiltInZoomControls(true);//显示缩放控件
		  wv.getSettings().setLoadWithOverviewMode(true);
		  wv.getSettings().setDefaultTextEncodingName("utf-8");
		  wv.requestFocus();
		  wv.loadUrl("http://www.baidu.com");
         return view;	
	}	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
	}

}