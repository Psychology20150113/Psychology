package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.EditInfoActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.PersonalInfoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.TestArrayAdapter;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

public class PersonalInfo_PerfectActivity extends BaseActivity implements OnClickListener{
	private InfoShared mShared;
	private AsyncImageCache mCache;
	private ImageView mHeaderView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalinfo_perfect_layout);
		setTopTitle(R.string.personalinfo_perfect);
		mShared = new InfoShared(this);
		Spinner mSpinner = (Spinner) findViewById(R.id.sp_choose_sex); 
		// 建立数据源 
		String[] mItems = getResources().getStringArray(R.array.sp_choose_sex); 
		// 建立Adapter并且绑定数据源 
		ArrayAdapter<String> mAdapter=new TestArrayAdapter(PersonalInfo_PerfectActivity.this ,mItems); 
		//绑定 Adapter到控件  
		mSpinner.setAdapter(mAdapter); 
		findViewById(R.id.tv_area).setOnClickListener(this);
		mCache = AsyncImageCache.from(this);
		showCustomDialog();
		new GetInfoTask().execute();
		
		
	}
	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent(this, EditInfoActivity.class);
		switch (v.getId()) {
		case R.id.tv_area:
			mIntent.putExtra(Constants.TitleName, getString(R.string.working_city));
			mIntent.putExtra(Constants.Params, "workingCity");
			break;
		default:
			break;
		}
		startActivityForResult(mIntent, 1000);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1000:
			if(data != null){
				showCustomDialog();
				new GetInfoTask().execute();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onCropFinish(Bitmap cropBitmap) {
		mHeaderView.setImageBitmap(cropBitmap);
	}
	
	@Override
	protected void onUploadFinish(String url) {
		showCustomDialog();
		new UploadHeaderUrl().execute(url);
	}
	
	private class UploadHeaderUrl extends AsyncTask<String, Void, BasicBean>{
		String paramsUrl;
		
		@Override
		protected BasicBean doInBackground(String... params) {
			if(TextUtils.isEmpty(params[0])){
				return null;
			}
			paramsUrl = params[0];
			return Utils.updataHeaderUrl(params[0]);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			if(result.isResult()){
				mShared.setHeaderUrl(paramsUrl);
				Toast.makeText(PersonalInfo_PerfectActivity.this, 
						R.string.updata_header_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(PersonalInfo_PerfectActivity.this, 
						R.string.updata_header_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	private class GetInfoTask extends AsyncTask<Void, Void, UserInfoBean> {
		@Override
		protected UserInfoBean doInBackground(Void... params) {
			return Utils.getUserInfo(MyApplication.myPhoneNum);
		}
		
		@Override
		protected void onPostExecute(UserInfoBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			mShared.setHeaderUrl(result.UserHeadUrl);
			setInfoData(result);
		}
	}
	private void setInfoData(UserInfoBean infoBean){
		((TextView)findViewById(R.id.tv_area)).setText(infoBean.WorkingCity);
	}
		
}