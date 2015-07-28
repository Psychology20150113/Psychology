package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.util.InfoShared;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class SplashActivity extends Activity {
	private final int SplashTime = 1500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_layout);
		new Handler().postDelayed(runnable, SplashTime);
	}
	
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				PackageInfo mInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				InfoShared mShared = new InfoShared(SplashActivity.this);
				String version = mShared.getAppVersion();
				if(TextUtils.isEmpty(version) || !version.equals(mInfo.versionName)){
					mShared.setAppVersion(mInfo.versionName);
					startActivity(new Intent(SplashActivity.this, IntroduceActivity.class));
					finish();
				} else {
					startActivity(new Intent(SplashActivity.this, SlideMainActivity.class));
					finish();
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	};
}
