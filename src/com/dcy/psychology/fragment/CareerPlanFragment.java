package com.dcy.psychology.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.adapter.SimpleTextAdapter;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.db.DbManager;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.model.IdAndName;
import com.dcy.psychology.util.CalculateUtils;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.PullRefreshListView;
import com.dcy.psychology.view.PullRefreshListView.OnRefreshListener;
import com.dcy.psychology.view.dialog.ShowHolledDialog;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CareerPlanFragment extends Fragment implements OnClickListener, OnItemSelectedListener{
	private Context mContext;
	private Resources mResources;
	private EditText nickEt;
	private RadioGroup sexGroup;
	private EditText ageEt;
	private EditText mailEt;
	private EditText gradurationYearEt;
	private Spinner mIndustrySpinner;
	private Spinner mXingzuoSpinner;
	private Spinner mProvinceSpinner;
	private Spinner mCitySpinner;
	private Spinner mDiplomaSpinner;
	private Spinner mStatusSpinner;
	private Spinner mSchoolCitySpinner;
	private Spinner mUniversitySpinner;
	private Spinner mWorkingProvinceSpinner;
	private Spinner mWorkingCitySpinner;
	private Spinner mMajorSpinner;
	private ArrayList<IdAndName> mProvinceList;
	private SimpleTextAdapter mProvinceAdapter;
	private ArrayList<IdAndName> mCityList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mCityAdapter;
	private ArrayList<IdAndName> mUniversityList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mUniversityAdapter;
	private ArrayList<IdAndName> mWorkingCityList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mWorkingCityAdapter;
	private Map<String, String> infoMap = new HashMap<String, String>();
	private View rootView;
	private CustomProgressDialog mLoadingDialog;
	private final int RequestCode_Test_Hollend = 100;
	private final int RequestCode_Test_Qizhi = 101;
	private final int RequestCode_Login = 102;
	private int pageIndex = 1;
	private PullRefreshListView mListView;
	private SpecialUserListAdapter mAdapter;
	private ArrayList<SpecificUserBean> mDataList = new ArrayList<SpecificUserBean>();
	private boolean isRefresh = false;
	private InfoShared mShared;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mResources = mContext.getResources();
		mProvinceList = DbManager.getProvince();
		mLoadingDialog = new CustomProgressDialog(mContext);
		mAdapter = new SpecialUserListAdapter(mContext, mDataList);
		mShared = new InfoShared(mContext);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_career_plan_layout, null);
		mListView = (PullRefreshListView) rootView.findViewById(R.id.pull_refresh_lv);
		mListView.setScrollListener(mScrollListener);
		mListView.setonRefreshListener(mRefreshListener);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(rootView.findViewById(R.id.tv_empty_data));
		initPerfectInfoView();
		if(TextUtils.isEmpty(MyApplication.myPhoneNum))
		{
			rootView.findViewById(R.id.ll_entry).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.tv_student_entry).setOnClickListener(this);
			rootView.findViewById(R.id.tv_teacher_entry).setOnClickListener(this);
		} else 
		{
			loadListData();
		}
		return rootView;
	}
	
	private OnRefreshListener mRefreshListener = new OnRefreshListener() 
	{
		@Override
		public void onRefresh() 
		{
			mDataList.removeAll(mDataList);
			pageIndex = 1;
			mListView.onRefreshComplete();
			mLoadingDialog.show();
			new GetSpecialUserTask().execute();
			isRefresh = true;
		}
	};
	
	private OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == SCROLL_STATE_IDLE){
				if(mListView.getLastVisiblePosition() == mListView.getCount() - 1){
					if(isRefresh){
						isRefresh = false;
					} else {
						pageIndex ++;
						mLoadingDialog.show();
						new GetSpecialUserTask().execute();
					}
				}
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_student_entry:
			Intent mStudentIntent = new Intent(mContext, LoginActivity.class);
			mStudentIntent.putExtra(Constants.UserRole, Constants.RoleUser);
			startActivityForResult(mStudentIntent, RequestCode_Login);
			break;
		case R.id.tv_teacher_entry:
			Intent mTeacherIntent = new Intent(mContext, LoginActivity.class);
			mTeacherIntent.putExtra(Constants.UserRole, Constants.RoleTeacher);
			startActivityForResult(mTeacherIntent, RequestCode_Login);
			break;
		case R.id.btn_prefect:
			if(!checkInput())
			{
				return;
			}
			infoMap.put("userName", nickEt.getText().toString());
			infoMap.put("userSex", ((RadioButton)rootView.findViewById(
					sexGroup.getCheckedRadioButtonId())).getText().toString());
			infoMap.put("userAge", ageEt.getText().toString());
			infoMap.put("userEmail", mailEt.getText().toString());
			infoMap.put("constellation", mXingzuoSpinner.getSelectedItem().toString());
			infoMap.put("homeTownP", mProvinceSpinner.getSelectedItem().toString());
			infoMap.put("homeTownC", mCitySpinner.getSelectedItem().toString());
			infoMap.put("university", mUniversitySpinner.getSelectedItem().toString());
			infoMap.put("diploma", mDiplomaSpinner.getSelectedItem().toString());
			infoMap.put("major", mMajorSpinner.getSelectedItem().toString());
			infoMap.put("graduationYear", gradurationYearEt.getText().toString());
			infoMap.put("currentState", mStatusSpinner.getSelectedItem().toString());
			infoMap.put("workingCity", mWorkingProvinceSpinner.getSelectedItem().toString() + " " + 
					mWorkingCitySpinner.getSelectedItem().toString());
			infoMap.put("industry", mIndustrySpinner.getSelectedItem().toString());
			mLoadingDialog.show();
			new PrefectInfoTask().execute();
			break;
		default:
			break;
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
				rootView.findViewById(R.id.ll_complete_info).setVisibility(View.GONE);
				if(!TextUtils.isEmpty(MyApplication.myUserRole) && 
						MyApplication.myUserRole.equals(Constants.RoleTeacher)){
					rootView.findViewById(R.id.tv_help).setVisibility(View.VISIBLE);
				} else {
					mListView.setVisibility(View.VISIBLE);
					new GetSpecialUserTask().execute();
				}
				SimpleMessageDialog hollendDialog = new SimpleMessageDialog(mContext, getString(R.string.go_Test_Hollend));
				hollendDialog.setSureClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						GrowQuestionBean beanList = MyApplication.mGson.fromJson(
								Utils.loadRawString(mContext, R.raw.test_zhiye), GrowQuestionBean.class);
						Intent mIntent = new Intent(mContext, ThoughtReadingActivity.class);
						mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, beanList);
						mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, getString(R.string.Test_Hollend));
						mIntent.putExtra(ThoughtReadingUtils.DNATest, true);
						startActivityForResult(mIntent, RequestCode_Test_Hollend);
					}
				});
				hollendDialog.show();
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
		case R.id.sp_working_province:
			mWorkingCityList.removeAll(mWorkingCityList);
			mWorkingCityList.addAll(DbManager.getCity(mProvinceList.get(position).id));
			mWorkingCityAdapter.notifyDataSetChanged();
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
		case RequestCode_Login:
			if(data == null){
				return;
			} else {
				rootView.findViewById(R.id.ll_entry).setVisibility(View.GONE);
				loadListData();
			}
			break;
		case RequestCode_Test_Hollend:
			if(data == null){
				return;
			}
			HashMap<String, String> resultMap = CalculateUtils.calculateZhiyeResult(mContext, 
					data.getIntegerArrayListExtra(ThoughtReadingUtils.PointResult));
			if(resultMap == null){
				return;
			}
			ShowHolledDialog hollendDialog = new ShowHolledDialog(mContext, resultMap.get("dataResult"), resultMap.get("typeResult") + 
					"\n" + getString(R.string.go_Test_Qizhi));
			hollendDialog.setButtonString(getString(R.string.cancel), getString(R.string.ok));
//			SimpleMessageDialog hollendShowDialog = new SimpleMessageDialog(mContext, getString(R.string.Test_Hollend), 
//					resultMap.get("showResult") + "\n" + resultMap.get("typeResult") + "\n" + getString(R.string.go_Test_Qizhi), 
//					getString(R.string.cancel), getString(R.string.ok));
			hollendDialog.setSureClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					GrowQuestionBean beanList = MyApplication.mGson.fromJson(
							Utils.loadRawString(mContext, R.raw.test_qizhi), GrowQuestionBean.class);
					Intent mIntent = new Intent(mContext, ThoughtReadingActivity.class);
					mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, beanList);
					mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, getString(R.string.Test_Qizhi));
					mIntent.putExtra(ThoughtReadingUtils.DNATest, true);
					startActivityForResult(mIntent, RequestCode_Test_Qizhi);
				}
			});
			hollendDialog.show();
			new CalculateUtils.SaveTestResultTask(mContext, resultMap).execute();
			mShared.setHollendResult(resultMap.get("dataResult"), resultMap.get("typeResult"), resultMap.get("pointResult"));
			break;
		case RequestCode_Test_Qizhi:
			if(data == null){
				return;
			}
			HashMap<String, String> resultQiZhiMap = CalculateUtils.calculateQizhiResult(mContext, 
					data.getIntegerArrayListExtra(ThoughtReadingUtils.PointResult));
			if(resultQiZhiMap == null){
				return;
			}
			new SimpleMessageDialog(mContext, getString(R.string.Test_Qizhi), String.format(getString(R.string.mine_qizhi), resultQiZhiMap.get("typeResult"))).show();
			mShared.setQizhiResult(resultQiZhiMap.get("dataResult"), resultQiZhiMap.get("typeResult"), resultQiZhiMap.get("pointResult"));
			new CalculateUtils.SaveTestResultTask(mContext, resultQiZhiMap).execute();
			break;
		default:
			break;
		}
	}

	private void loadListData() {
		if(MyApplication.hasPrefectInfo){
			rootView.findViewById(R.id.ll_complete_info).setVisibility(View.GONE);
			if(MyApplication.myUserRole.equals(Constants.RoleTeacher)){
				rootView.findViewById(R.id.tv_help).setVisibility(View.VISIBLE);
				rootView.findViewById(R.id.tv_empty_data).setVisibility(View.GONE);
				rootView.findViewById(R.id.ll_list).setVisibility(View.GONE);
			} else {
				mLoadingDialog.show();
				new GetSpecialUserTask().execute();
				rootView.findViewById(R.id.ll_list).setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void initPerfectInfoView(){
		nickEt = (EditText) rootView.findViewById(R.id.nick_et);
		sexGroup = (RadioGroup) rootView.findViewById(R.id.sex_rg);
		ageEt = (EditText) rootView.findViewById(R.id.age_et);
		mailEt = (EditText) rootView.findViewById(R.id.mail_et);
		gradurationYearEt = (EditText) rootView.findViewById(R.id.et_graduation_year);
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
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(
						Constants.RoleTeacher.equals(MyApplication.myUserRole) ? R.array.state_teacher_array : R.array.state_student_array)));
		mSchoolCitySpinner = (Spinner) rootView.findViewById(R.id.sp_school_province);
		mSchoolCitySpinner.setAdapter(mProvinceAdapter);
		mSchoolCitySpinner.setOnItemSelectedListener(this);
		mUniversitySpinner = (Spinner) rootView.findViewById(R.id.sp_school);
		mUniversityAdapter = new SimpleTextAdapter(mContext, mUniversityList);
		mUniversitySpinner.setAdapter(mUniversityAdapter);
		mMajorSpinner = (Spinner) rootView.findViewById(R.id.sp_major);
		mMajorSpinner.setAdapter(new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.major_array)));
		mWorkingProvinceSpinner = (Spinner) rootView.findViewById(R.id.sp_working_province);
		mWorkingProvinceSpinner.setAdapter(mProvinceAdapter);
		mWorkingProvinceSpinner.setOnItemSelectedListener(this);
		mWorkingCitySpinner = (Spinner) rootView.findViewById(R.id.sp_working_city);
		mWorkingCityAdapter = new SimpleTextAdapter(mContext, mWorkingCityList);
		mWorkingCitySpinner.setAdapter(mWorkingCityAdapter);
		rootView.findViewById(R.id.btn_prefect).setOnClickListener(this);
	}
	
	private boolean checkInput(){
		if(TextUtils.isEmpty(nickEt.getText())){
			Toast.makeText(mContext, R.string.name_empty, Toast.LENGTH_SHORT).show();
			nickEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(mailEt.getText())){
			Toast.makeText(mContext, R.string.mail_empty, Toast.LENGTH_SHORT).show();
			mailEt.requestFocus();
			return false;
		}
		if(!Utils.validateEmail(mailEt.getText().toString())){
			Toast.makeText(mContext, R.string.mail_format_error, Toast.LENGTH_SHORT).show();
			mailEt.requestFocus();
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
