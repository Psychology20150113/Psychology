package com.dcy.psychology;

import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class PersonalInfoActivity extends BaseActivity {
	private String phoneNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_layout);
		setTopTitle(R.string.peasonal_info);
		phoneNum = getIntent().getStringExtra(Constants.PhoneNum);
		if(!TextUtils.isEmpty(phoneNum)){
			showCustomDialog();
			new GetInfoTask().execute();
		}
	}
	
	private class GetInfoTask extends AsyncTask<Void, Void, UserInfoBean> {
		@Override
		protected UserInfoBean doInBackground(Void... params) {
			return Utils.getUserInfo(phoneNum);
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
		((TextView)findViewById(R.id.tv_name)).setText(infoBean.UserName);
		((TextView)findViewById(R.id.tv_xingzuo)).setText(infoBean.Constellation);
		((TextView)findViewById(R.id.tv_household)).setText(infoBean.HomeTownP + "-" + infoBean.HomeTownC);
		((TextView)findViewById(R.id.tv_diploma)).setText(infoBean.Diploma);
		((TextView)findViewById(R.id.tv_school)).setText(infoBean.University);
		((TextView)findViewById(R.id.tv_graduation_year)).setText(infoBean.GraduationYear);
		((TextView)findViewById(R.id.tv_major)).setText(infoBean.Major);
		((TextView)findViewById(R.id.tv_state)).setText(infoBean.CurrentState);
		((TextView)findViewById(R.id.tv_working_city)).setText(infoBean.WorkingCity);
		((TextView)findViewById(R.id.tv_industry)).setText(infoBean.Industry);
		((TextView)findViewById(R.id.tv_grade)).setText(infoBean.Grade);
		((TextView)findViewById(R.id.tv_hobbies)).setText(infoBean.Hobbies);
		((TextView)findViewById(R.id.tv_follow)).setText(infoBean.Follow);
	}
}
