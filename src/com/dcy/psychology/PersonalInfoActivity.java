package com.dcy.psychology;

import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoActivity extends BaseActivity implements OnClickListener{
	private AsyncImageCache mCache;
	private ImageView mHeaderView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_layout);
		mHeaderView = (ImageView) findViewById(R.id.iv_header);
		mHeaderView.setOnClickListener(this);
		setTopTitle(R.string.peasonal_info);
		mCache = AsyncImageCache.from(this);
		showCustomDialog();
		new GetInfoTask().execute();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_header:
			showChoosePicPopupView();
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
		@Override
		protected BasicBean doInBackground(String... params) {
			if(TextUtils.isEmpty(params[0])){
				return null;
			}
			return Utils.updataHeaderUrl(params[0]);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			if(result.isResult()){
				Toast.makeText(PersonalInfoActivity.this, 
						R.string.updata_header_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(PersonalInfoActivity.this, 
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
			setInfoData(result);
		}
	}
	
	private void setInfoData(UserInfoBean infoBean){
		mCache.displayImage((ImageView) findViewById(R.id.iv_header), R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(infoBean.UserHeadUrl, infoBean.UserHeadUrl));
		((TextView)findViewById(R.id.tv_name)).setText(infoBean.UserName);
		((TextView)findViewById(R.id.tv_sex)).setText(infoBean.UserSex);
		((TextView)findViewById(R.id.tv_age)).setText(infoBean.UserAge);
		((TextView)findViewById(R.id.tv_xingzuo)).setText(infoBean.Constellation);
		((TextView)findViewById(R.id.tv_household)).setText(infoBean.HomeTownP + " " + infoBean.HomeTownC);
		((TextView)findViewById(R.id.tv_diploma)).setText(infoBean.Diploma);
		((TextView)findViewById(R.id.tv_school)).setText(infoBean.University);
		((TextView)findViewById(R.id.tv_graduation_year)).setText(infoBean.GraduationYear);
		((TextView)findViewById(R.id.tv_major)).setText(infoBean.Major);
		((TextView)findViewById(R.id.tv_state)).setText(infoBean.CurrentState);
		((TextView)findViewById(R.id.tv_working_city)).setText(infoBean.WorkingCity);
		((TextView)findViewById(R.id.tv_grade)).setText(infoBean.Grade);
		((TextView)findViewById(R.id.tv_hobbies)).setText(infoBean.Hobbies);
		((TextView)findViewById(R.id.tv_follow)).setText(infoBean.Follow);
	}
}
