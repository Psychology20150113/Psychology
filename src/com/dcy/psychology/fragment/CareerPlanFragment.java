package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SimpleTextAdapter;
import com.dcy.psychology.db.DbManager;
import com.dcy.psychology.model.IdAndName;
import com.dcy.psychology.util.Utils;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
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
		View view = inflater.inflate(R.layout.fragment_career_plan_layout, null);
//		if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
//			view.findViewById(R.id.ll_entry).setVisibility(View.VISIBLE);
//			view.findViewById(R.id.tv_student_entry).setOnClickListener(this);
//			view.findViewById(R.id.tv_teacher_entry).setOnClickListener(this);
//		}
		initPerfectInfoView(view);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_student_entry:
			
			break;
		case R.id.tv_teacher_entry:
			break;
		default:
			break;
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
	
	private void initPerfectInfoView(View rootView){
		nickEt = (EditText) rootView.findViewById(R.id.nick_et);
		sexGroup = (RadioGroup) rootView.findViewById(R.id.sex_rg);
		ageEt = (EditText) rootView.findViewById(R.id.age_et);
		mailEt = (EditText) rootView.findViewById(R.id.mail_et);
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
		//		findViewById(R.id.register_btn).setOnClickListener(this);
	}
	
	private boolean checkInput(){
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
