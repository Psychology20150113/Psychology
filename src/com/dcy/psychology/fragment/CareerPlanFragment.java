package com.dcy.psychology.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.adapter.SimpleTextAdapter;
import com.dcy.psychology.db.DbManager;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.model.IdAndName;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.PullRefreshListView;
import com.google.gson.reflect.TypeToken;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CareerPlanFragment extends Fragment implements OnClickListener, OnItemSelectedListener{
	private Context mContext;
	private Resources mResources;
	private EditText nickEt;
	private RadioGroup sexGroup;
	private EditText ageEt;
	private EditText mailEt;
	private EditText gradurationYearEt;
	private EditText workingEt;
	private EditText hobbiesEt;
	private EditText gradeEt;
	private EditText followEt;
	private Spinner mIndustrySpinner;
	private Spinner mXingzuoSpinner;
	private Spinner mProvinceSpinner;
	private Spinner mCitySpinner;
	private Spinner mDiplomaSpinner;
	private Spinner mStatusSpinner;
	private Spinner mSchoolCitySpinner;
	private Spinner mUniversitySpinner;
	private Spinner mMajorSpinner;
	private ArrayList<IdAndName> mProvinceList;
	private SimpleTextAdapter mProvinceAdapter;
	private ArrayList<IdAndName> mCityList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mCityAdapter;
	private ArrayList<IdAndName> mUniversityList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mUniversityAdapter;
	private Map<String, String> infoMap = new HashMap<String, String>();
	private View rootView;
	private CustomProgressDialog mLoadingDialog;
	private final int RequestCode_Test = 100;
	private PullRefreshListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mResources = mContext.getResources();
		mProvinceList = DbManager.getProvince();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_career_plan_layout, null);
//		if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
//			view.findViewById(R.id.ll_entry).setVisibility(View.VISIBLE);
//			view.findViewById(R.id.tv_student_entry).setOnClickListener(this);
//			view.findViewById(R.id.tv_teacher_entry).setOnClickListener(this);
//		}
		initPerfectInfoView();
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_student_entry:
			
			break;
		case R.id.tv_teacher_entry:
			break;
		case R.id.btn_prefect:
			if(!checkInput()){
				return;
			}
			infoMap.put("userName", nickEt.getText().toString());
			infoMap.put("userSex", ((RadioButton)rootView.findViewById(
					sexGroup.getCheckedRadioButtonId())).getText().toString());
			infoMap.put("userAge", ageEt.getText().toString());
			infoMap.put("userEmail", mailEt.getText().toString());
			Log.i("mylog","xingzuo : " + mXingzuoSpinner.getSelectedItem().toString());
			infoMap.put("constellation", mXingzuoSpinner.getSelectedItem().toString());
			Log.i("mylog", "shengfen : " + mProvinceSpinner.getSelectedItem().toString());
			infoMap.put("homeTownP", mProvinceSpinner.getSelectedItem().toString());
			infoMap.put("homeTownC", mCitySpinner.getSelectedItem().toString());
			infoMap.put("university", mUniversitySpinner.getSelectedItem().toString());
			infoMap.put("diploma", mDiplomaSpinner.getSelectedItem().toString());
			infoMap.put("major", mMajorSpinner.getSelectedItem().toString());
			infoMap.put("graduationYear", gradurationYearEt.getText().toString());
			infoMap.put("currentState", mStatusSpinner.getSelectedItem().toString());
			infoMap.put("workingCity", workingEt.getText().toString());
			infoMap.put("industry", mIndustrySpinner.getSelectedItem().toString());
			infoMap.put("isClassLeader", "");
			infoMap.put("grade", "");
			infoMap.put("hobbies", hobbiesEt.getText().toString());
			infoMap.put("follow", followEt.getText().toString());
			if(mLoadingDialog == null){
				mLoadingDialog = new CustomProgressDialog(mContext);
			}
			mLoadingDialog.show();
			new PrefectInfoTask().execute();
			break;
		default:
			break;
		}
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
				GrowQuestionBean beanList = MyApplication.mGson.fromJson(
						Utils.loadRawString(mContext, R.raw.test_qizhi), GrowQuestionBean.class);
				Intent mIntent = new Intent(mContext, ThoughtReadingActivity.class);
				mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, beanList);
				mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, "DNA");
				mIntent.putExtra(ThoughtReadingUtils.DNATest, true);
				startActivityForResult(mIntent, RequestCode_Test);
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.sp_province:
			mCityList.removeAll(mCityList);
			mCityList.addAll(DbManager.getCity(mProvinceList.get(position).id));
			mCityAdapter.notifyDataSetChanged();
			break;
		case R.id.sp_school_province:
			mUniversityList.removeAll(mUniversityList);
			mUniversityList.addAll(DbManager.getUniversity(mProvinceList.get(position).id));
			mUniversityAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RequestCode_Test:
			if(data == null){
				return;
			}
			calculateZhiyeResult(data.getIntegerArrayListExtra(ThoughtReadingUtils.PointResult));
			break;

		default:
			break;
		}
	}
	
	private void calculateZhiyeResult(ArrayList<Integer> pointList){
		if(pointList == null){
			return;
		}
		rootView.findViewById(R.id.ll_result_show).setVisibility(View.VISIBLE);
		String[] typeArray = mContext.getResources().getStringArray(R.array.zhiye_array);
		int typePoint = 0;
		StringBuilder resultBuilder = new StringBuilder();
		for(int i = 0 ; i < typeArray.length; i ++){
			for(int j = 0 ; j < Constants.ZhiyeIndex[i].length; j ++){
				typePoint += pointList.get(Constants.ZhiyeIndex[i][j] - 1);
			}
			resultBuilder.append(typeArray[i]).append(typePoint).append("\n");
			typePoint = 0;
		}
		((TextView)rootView.findViewById(R.id.tv_result)).setText(resultBuilder.toString());
	}
	
	private void initPerfectInfoView(){
		nickEt = (EditText) rootView.findViewById(R.id.nick_et);
		sexGroup = (RadioGroup) rootView.findViewById(R.id.sex_rg);
		ageEt = (EditText) rootView.findViewById(R.id.age_et);
		mailEt = (EditText) rootView.findViewById(R.id.mail_et);
		gradurationYearEt = (EditText) rootView.findViewById(R.id.et_graduation_year);
		workingEt = (EditText) rootView.findViewById(R.id.et_working);
		hobbiesEt = (EditText) rootView.findViewById(R.id.et_hobbies);
		gradeEt = (EditText) rootView.findViewById(R.id.et_grade);
		followEt = (EditText) rootView.findViewById(R.id.et_follow);
		mIndustrySpinner = (Spinner) rootView.findViewById(R.id.sp_industry);
		mIndustrySpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.industry_array)));
		mXingzuoSpinner = (Spinner) rootView.findViewById(R.id.sp_xingzuo);
		mXingzuoSpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.xingzuo_array)));
		mProvinceSpinner = (Spinner) rootView.findViewById(R.id.sp_province);
		mProvinceSpinner.setOnItemSelectedListener(this);
		mProvinceAdapter = new SimpleTextAdapter(mContext, mProvinceList);
		mProvinceSpinner.setAdapter(mProvinceAdapter);
		mCitySpinner = (Spinner) rootView.findViewById(R.id.sp_city);
		mCityAdapter = new SimpleTextAdapter(mContext, mCityList);
		mCitySpinner.setAdapter(mCityAdapter);
		mDiplomaSpinner = (Spinner) rootView.findViewById(R.id.sp_diploma);
		mDiplomaSpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.diploma_array)));
		mStatusSpinner = (Spinner) rootView.findViewById(R.id.sp_state);
		mStatusSpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.state_student_array)));
		mSchoolCitySpinner = (Spinner) rootView.findViewById(R.id.sp_school_province);
		mSchoolCitySpinner.setAdapter(mProvinceAdapter);
		mSchoolCitySpinner.setOnItemSelectedListener(this);
		mUniversitySpinner = (Spinner) rootView.findViewById(R.id.sp_school);
		mUniversityAdapter = new SimpleTextAdapter(mContext, mUniversityList);
		mUniversitySpinner.setAdapter(mUniversityAdapter);
		mMajorSpinner = (Spinner) rootView.findViewById(R.id.sp_major);
		mMajorSpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.major_array)));
		rootView.findViewById(R.id.btn_prefect).setOnClickListener(this);
	}
	
	private boolean checkInput(){
		if(TextUtils.isEmpty(nickEt.getText())){
			Toast.makeText(mContext, R.string.account_empty, Toast.LENGTH_SHORT).show();
			nickEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(mailEt.getText())){
			Toast.makeText(mContext, R.string.mail_empty, Toast.LENGTH_SHORT).show();
			mailEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(gradurationYearEt.getText())){
			Toast.makeText(mContext, R.string.year_empty, Toast.LENGTH_SHORT).show();
			gradurationYearEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(workingEt.getText())){
			Toast.makeText(mContext, R.string.working_empty, Toast.LENGTH_SHORT).show();
			workingEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(ageEt.getText())){
			Toast.makeText(mContext, R.string.age_empty, Toast.LENGTH_SHORT).show();
			ageEt.requestFocus();
			return false;
		}
		if(!TextUtils.isEmpty(ageEt.getText())){
			int age = Integer.valueOf(ageEt.getText().toString());
			if(age < 1 || age > 200){
				Toast.makeText(mContext, R.string.age_error, Toast.LENGTH_SHORT).show();
				ageEt.requestFocus();
				ageEt.setSelection(ageEt.getText().length());
				return false;
			}
		}
		return true;
	}
}
