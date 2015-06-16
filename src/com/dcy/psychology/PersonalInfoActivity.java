package com.dcy.psychology;

import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.content.Intent;
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
		setListener();
		showCustomDialog();
		new GetInfoTask().execute();
	}
	
	private void setListener(){
		findViewById(R.id.rl_name).setOnClickListener(this);
		findViewById(R.id.rl_mail).setOnClickListener(this);
		findViewById(R.id.rl_sex).setOnClickListener(this);
		findViewById(R.id.rl_age).setOnClickListener(this);
		findViewById(R.id.rl_xingzuo).setOnClickListener(this);
		findViewById(R.id.rl_household).setOnClickListener(this);
		findViewById(R.id.rl_working_city).setOnClickListener(this);
		findViewById(R.id.rl_diploma).setOnClickListener(this);
		findViewById(R.id.rl_school).setOnClickListener(this);
		findViewById(R.id.rl_year).setOnClickListener(this);
		findViewById(R.id.rl_state).setOnClickListener(this);
		findViewById(R.id.rl_major).setOnClickListener(this);
		findViewById(R.id.rl_industry).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_header){
			showChoosePicPopupView();
			return;
		}
		Intent mIntent = new Intent(this, EditInfoActivity.class);
		switch (v.getId()) {
		case R.id.rl_name:
			mIntent.putExtra(Constants.TitleName, getString(R.string.nick));
			mIntent.putExtra(Constants.Params, "userName");
			break;
		case R.id.rl_mail:
			mIntent.putExtra(Constants.TitleName, getString(R.string.mail));
			mIntent.putExtra(Constants.Params, "userEmail");
			break;
		case R.id.rl_sex:
			mIntent.putExtra(Constants.TitleName, getString(R.string.sex));
			mIntent.putExtra(Constants.Params, "userSex");
			break;
		case R.id.rl_age:
			mIntent.putExtra(Constants.TitleName, getString(R.string.age));
			mIntent.putExtra(Constants.Params, "userAge");
			break;
		case R.id.rl_xingzuo:
			mIntent.putExtra(Constants.TitleName, getString(R.string.xingzuo));
			mIntent.putExtra(Constants.Params, "constellation");
			break;
		case R.id.rl_household:
			mIntent.putExtra(Constants.TitleName, getString(R.string.household));
			mIntent.putExtra(Constants.Params, "home");
			break;
		case R.id.rl_working_city:
			mIntent.putExtra(Constants.TitleName, getString(R.string.working_city));
			mIntent.putExtra(Constants.Params, "workingCity");
			break;
		case R.id.rl_diploma:
			mIntent.putExtra(Constants.TitleName, getString(R.string.diploma));
			mIntent.putExtra(Constants.Params, "diploma");
			break;
		case R.id.rl_school:
			mIntent.putExtra(Constants.TitleName, getString(R.string.school));
			mIntent.putExtra(Constants.Params, "university");
			break;
		case R.id.rl_year:
			mIntent.putExtra(Constants.TitleName, getString(R.string.graduation_year));
			mIntent.putExtra(Constants.Params, "graduationYear");
			break;
		case R.id.rl_state:
			mIntent.putExtra(Constants.TitleName, getString(R.string.current_state));
			mIntent.putExtra(Constants.Params, "currentState");
			break;
		case R.id.rl_major:
			mIntent.putExtra(Constants.TitleName, getString(R.string.major));
			mIntent.putExtra(Constants.Params, "major");
			break;
		case R.id.rl_industry:
			mIntent.putExtra(Constants.TitleName, getString(R.string.industry));
			mIntent.putExtra(Constants.Params, "industry");
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
		((TextView)findViewById(R.id.tv_mail)).setText(infoBean.UserEmail);
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
		((TextView)findViewById(R.id.tv_industry)).setText(infoBean.Industry);
	}
}
