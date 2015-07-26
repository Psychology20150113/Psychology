package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.util.Constants;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowProblemActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_problem);
		setTopTitle(R.string.slide_problem);
		WebView mProblemWeb = (WebView) findViewById(R.id.web_problem);
		mProblemWeb.loadUrl(Constants.Web_Problem_Url);
		showCustomDialog();
		mProblemWeb.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				hideCustomDialog();
			}
		});
	}
}
