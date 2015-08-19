package com.dcy.psychology.xinzeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.EditInfoActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.adapter.TestArrayAdapter;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;

public class PersonalInfo_PerfectActivity extends BaseActivity implements OnClickListener{
	private InfoShared mShared;
	private AsyncImageCache mCache;
	private ImageView mHeaderView;
	private EditText nickEt;
	private RadioGroup sexGroup;
	private EditText ageEt;
	private EditText mailEt;
	private Spinner mSpinner;
	private int pageIndex = 1;
	private final int RequestCode_Test_Hollend = 100;
	private final int RequestCode_Test_Qizhi = 101;
	private CustomProgressDialog mLoadingDialog;
	private SpecialUserListAdapter mAdapter;
	private Map<String, String> infoMap = new HashMap<String, String>();
	private ArrayList<SpecificUserBean> mDataList = new ArrayList<SpecificUserBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalinfo_perfect_layout);
		setTopTitle(R.string.personalinfo_perfect);
		mAdapter = new SpecialUserListAdapter(this, mDataList);
		mShared = new InfoShared(this);
		 nickEt = (EditText)findViewById(R.id.et_real_name);
		 ageEt = (EditText) findViewById(R.id.et_choose_age);
		mailEt = (EditText) findViewById(R.id.et_email);
		mSpinner = (Spinner) findViewById(R.id.sp_choose_sex); 
		mSpinner = (Spinner) findViewById(R.id.sp_choose_sex); 
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
		case R.id.btn_prefect:
			if(!checkInput())
			{
				return;
			}
			infoMap.put("userName", nickEt.getText().toString());
			
			infoMap.put("userSex", mSpinner.getSelectedItem().toString());
			infoMap.put("userAge", ageEt.getText().toString());
			infoMap.put("userEmail", mailEt.getText().toString());
			mLoadingDialog.show();
			new PrefectInfoTask().execute();
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
	private class PrefectInfoTask extends AsyncTask<Void, Void, BasicBean>{
		@Override
		protected BasicBean doInBackground(Void... params) {
			return Utils.prefectUserInfo(infoMap);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			if(mLoadingDialog != null && mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			if(result.isResult()){
				mShared.setIsPrefectInfo(true);
				MyApplication.hasPrefectInfo = true;
				//findViewById(R.id.ll_complete_info).setVisibility(View.GONE);
				if(!TextUtils.isEmpty(MyApplication.myUserRole) && 
						MyApplication.myUserRole.equals(Constants.RoleTeacher)){
					findViewById(R.id.tv_help).setVisibility(View.VISIBLE);
				} else {
					//mListView.setVisibility(View.VISIBLE);
					new GetSpecialUserTask().execute();
				}
				SimpleMessageDialog hollendDialog = new SimpleMessageDialog(PersonalInfo_PerfectActivity.this, getString(R.string.go_Test_Hollend));
				hollendDialog.setSureClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						GrowQuestionBean beanList = MyApplication.mGson.fromJson(
								Utils.loadRawString(PersonalInfo_PerfectActivity.this, R.raw.test_zhiye), GrowQuestionBean.class);
						Intent mIntent = new Intent(PersonalInfo_PerfectActivity.this, ThoughtReadingActivity.class);
						mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, beanList);
						mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, getString(R.string.Test_Hollend));
						mIntent.putExtra(ThoughtReadingUtils.DNATest, true);
						startActivityForResult(mIntent, RequestCode_Test_Hollend);
					}
				});
				hollendDialog.show();
			} else {
				Toast.makeText(PersonalInfo_PerfectActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	private class GetSpecialUserTask extends AsyncTask<Void, Void, ArrayList<SpecificUserBean>>{
		@Override
		protected ArrayList<SpecificUserBean> doInBackground(Void... params) {
			return Utils.getSpecificUserList(pageIndex);
		}
		
		@Override
		protected void onPostExecute(ArrayList<SpecificUserBean> result) {
			mLoadingDialog.dismiss();
			if(result == null){
				return;
			}
			mDataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
	private boolean checkInput(){
		if(TextUtils.isEmpty(nickEt.getText())){
			Toast.makeText(this, R.string.name_empty, Toast.LENGTH_SHORT).show();
			nickEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(mailEt.getText())){
			Toast.makeText(this, R.string.mail_empty, Toast.LENGTH_SHORT).show();
			mailEt.requestFocus();
			return false;
		}
		if(!Utils.validateEmail(mailEt.getText().toString())){
			Toast.makeText(this, R.string.mail_format_error, Toast.LENGTH_SHORT).show();
			mailEt.requestFocus();
			return false;
		}
		if(!TextUtils.isEmpty(ageEt.getText())){
			int age = Integer.valueOf(ageEt.getText().toString());
			if(age < 1 || age > 200){
				Toast.makeText(this, R.string.age_error, Toast.LENGTH_SHORT).show();
				ageEt.requestFocus();
				ageEt.setSelection(ageEt.getText().length());
				return false;
			}
		}
		return true;
	}
		
}