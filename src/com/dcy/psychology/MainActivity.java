package com.dcy.psychology;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.dcy.psychology.R;
import com.dcy.psychology.fragment.TabCureFragment;
import com.dcy.psychology.fragment.TabGrowthFragment;
import com.dcy.psychology.fragment.TabMineFragment;
import com.dcy.psychology.service.DownloadApkService;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends BaseActivity implements OnClickListener {
	
	private ViewPager mViewPager;
	private List<Fragment> dataFragment = new ArrayList<Fragment>();
	
	private RadioGroup mTabRadio;
	private TextView mLoginText;
	
	private String[] mTabNameArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTopTitle(R.string.tab_one);
		mTabNameArray = mResources.getStringArray(R.array.tab_name);
		initData();
		initView();
		new CheckVersionTask().execute();
	}

	private void initData(){
		dataFragment.add(new TabGrowthFragment());
		dataFragment.add(new TabCureFragment());
		dataFragment.add(new TabMineFragment());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		for(int i=0 ; i<dataFragment.size() ; i++){
			transaction.add(R.id.main_vp, dataFragment.get(i));
			transaction.hide(dataFragment.get(i));
		}
		transaction.commit();
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.main_vp);
		mViewPager.setAdapter(new Utils.MainTabAdapter(getFragmentManager(), dataFragment));
		mViewPager.setOnPageChangeListener(mPageListener);
		mTabRadio = (RadioGroup) findViewById(R.id.tab_rg);
		mTabRadio.setOnCheckedChangeListener(mCheckedChangeListener);
		mLoginText = (TextView) findViewById(R.id.login_tv);
		mLoginText.setOnClickListener(this);
		findViewById(R.id.register_tv).setOnClickListener(this);
	}
	
	private class CheckVersionTask extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			return Utils.checkAppVersion();
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(TextUtils.isEmpty(result)){
				return;
			}
			try {
				final JSONObject resultJson = new JSONObject(result);
				if(Utils.isHighVersion(new InfoShared(MainActivity.this).getAppVersion(), resultJson.getString("NewVersion"))){
					SimpleMessageDialog mDialog = new SimpleMessageDialog(MainActivity.this, getString(R.string.have_new_version), 
							resultJson.getString("UpdateInfo"), getString(R.string.cancel_update), getString(R.string.update_now));
					mDialog.setSureClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								Intent mIntent = new Intent(MainActivity.this, DownloadApkService.class);
				                mIntent.putExtra(DownloadApkService.URL_KEY, resultJson.getString("NewVersionDownloadUrl"));
				                MainActivity.this.startService(mIntent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					mDialog.show();
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void checkCureFragment(){
		mTabRadio.check(R.id.tab_two_rb);
	}
	
	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int position = 0;
			switch (checkedId) {
				case R.id.tab_one_rb:
					position = 0;
					break;
				case R.id.tab_two_rb:
					position = 1;
					break;
				case R.id.tab_three_rb:
					position = 2;
					break;
				default:
					break;
			}
			setTopTitle(mTabNameArray[position]);
			mViewPager.setCurrentItem(position);
		}
	};

	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setTopTitle(mTabNameArray[position]);
			mTabRadio.check(getCheckedBtnId(position));
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private int getCheckedBtnId(int position){
		int id = 0;
		switch (position) {
			case 0:
				id = R.id.tab_one_rb;
				break;
			case 1:
				id = R.id.tab_two_rb;
				break;
			case 2:
				id = R.id.tab_three_rb;
				break;
			default:
				break;
		}
		return id;
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
			case R.id.login_tv:
				new Thread(){
					@Override
					public void run() {
						Utils.getLoginWeb("blue", "123456");
						//Utils.getArticleList();
					};
				}.start();
				
				break;
			case R.id.register_tv:
				mIntent = new Intent(this, LoginActivity.class);
				break;
			default:
				break;
		}
		if(mIntent != null)
			startActivity(mIntent);
	}
}
