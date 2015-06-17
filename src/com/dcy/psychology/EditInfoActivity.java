package com.dcy.psychology;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.e;

import com.dcy.psychology.adapter.SimpleTextAdapter;
import com.dcy.psychology.db.DbManager;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.model.IdAndName;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class EditInfoActivity extends BaseActivity implements OnItemSelectedListener{
	private EditText mEditText;
	private RadioGroup mRadioGroup;
	private Spinner mLeftSpinner;
	private Spinner mRightSpinner;
	private Spinner mSingleSpinner;
	private String param;
	private TypeParams mParamType;
	private ArrayList<IdAndName> mProvinceList;
	private ArrayList<IdAndName> mRightList = new ArrayList<IdAndName>();
	private SimpleTextAdapter mRightAdapter;
	
	private enum TypeParams{
		Type_Edit, Type_Radio, Type_Single_Spinner, Type_Dowble_Spinner
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_info_layout);
		String titleName = getIntent().getStringExtra(Constants.TitleName);
		setTopTitle(String.format(getString(R.string.format_change_info), titleName));
		param = getIntent().getStringExtra(Constants.Params);
		mParamType = getParamsType();
		initView();
	}
	
	private void initView(){
		setRightText(R.string.ok);
		mEditText = (EditText) findViewById(R.id.et_edit);
		mRadioGroup = (RadioGroup) findViewById(R.id.sex_rg);
		mSingleSpinner = (Spinner) findViewById(R.id.sp_single);
		mLeftSpinner = (Spinner) findViewById(R.id.sp_left);
		mRightSpinner = (Spinner) findViewById(R.id.sp_right);
		switch (mParamType) {
		case Type_Edit:
			mEditText.setVisibility(View.VISIBLE);
			break;
		case Type_Radio:
			mRadioGroup.setVisibility(View.VISIBLE);
			break;
		case Type_Single_Spinner:
			mSingleSpinner.setVisibility(View.VISIBLE);
			initSingleAdapter();
			break;
		case Type_Dowble_Spinner:
			findViewById(R.id.ll_spinner).setVisibility(View.VISIBLE);
			initDoubleAdapter();
			break;
		default:
			break;
		}
	}
	
	private void initSingleAdapter(){
		int resId = 0;
		if("constellation".equals(param)){
			resId = R.array.xingzuo_array;
		} else if("diploma".equals(param)){
			resId = R.array.diploma_array;
		} else if("major".equals(param)){
			resId = R.array.major_array;
		} else if("currentState".equals(param)){
			resId = Constants.RoleTeacher.equals(MyApplication.myUserRole) ? R.array.state_teacher_array : R.array.state_student_array;
		} else if("industry".equals(param)){
			resId = R.array.industry_array;
		}
		try {
			mSingleSpinner.setAdapter(new ArrayAdapter<String>(this, 
					android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(resId)));
		} catch (Exception e) {
		}
	}
	
	private void initDoubleAdapter(){
		mProvinceList = DbManager.getProvince();
		mLeftSpinner.setAdapter(new SimpleTextAdapter(this, mProvinceList));
		mLeftSpinner.setOnItemSelectedListener(this);
		mRightAdapter = new SimpleTextAdapter(this, mRightList);
		mRightSpinner.setAdapter(mRightAdapter);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mRightList.removeAll(mRightList);
		if("university".equals(param)){
			mRightList.addAll(DbManager.getUniversity(mProvinceList.get(position).id));
		} else {
			mRightList.addAll(DbManager.getCity(mProvinceList.get(position).id));
		}
		mRightAdapter.notifyDataSetChanged();
	}
	
	private TypeParams getParamsType(){
		if("userName".equals(param) || "userAge".equals(param) || 
				"userEmail".equals(param) || "graduationYear".equals(param)){
			return TypeParams.Type_Edit;
		} else if("userSex".equals(param)){
			return TypeParams.Type_Radio;
		} else if("home".equals(param) || "university".equals(param) || "workingCity".equals(param)){
			return TypeParams.Type_Dowble_Spinner;
		} else {
			return TypeParams.Type_Single_Spinner;
		}
	}
	
	@Override
	public void onRightTextClick() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		switch (mParamType) {
		case Type_Edit:
			if(TextUtils.isEmpty(mEditText.getText().toString())){
				finish();
				return;
			} else {
				if("userEmail".equals(param) && !Utils.validateEmail(mEditText.getText().toString())){
					Toast.makeText(this, R.string.mail_format_error, Toast.LENGTH_SHORT).show();
					return;
				}
				hashMap.put(param, mEditText.getText().toString());
			} 
			break;
		case Type_Radio:
			if(mRadioGroup.getCheckedRadioButtonId() == -1){
				finish();
				return;
			} else {
				hashMap.put(param, ((RadioButton)findViewById(mRadioGroup
						.getCheckedRadioButtonId())).getText().toString());
			}
			break;
		case Type_Single_Spinner:
			hashMap.put(param, mSingleSpinner.getSelectedItem().toString());
			break;
		case Type_Dowble_Spinner:
			if("home".equals(param)){
				hashMap.put("homeTownP", mLeftSpinner.getSelectedItem().toString());
				hashMap.put("homeTownC", mRightSpinner.getSelectedItem().toString());
			} else if ("university".equals(param)){
				hashMap.put(param, mRightSpinner.getSelectedItem().toString());
			} else {
				hashMap.put(param, mLeftSpinner.getSelectedItem().toString() + " "
						+ mRightSpinner.getSelectedItem().toString());
			}
			break;
		default:
			break;
		}
		showCustomDialog();
		new PrefectInfoTask().execute(hashMap);
	}

	private class PrefectInfoTask extends AsyncTask<HashMap<String, String>, Void, BasicBean>{
		private HashMap<String, String> mMap;
		
		@Override
		protected BasicBean doInBackground(HashMap<String, String>... params) {
			if(params[0] == null){
				return null;
			}
			mMap = params[0];
			return Utils.prefectUserInfo(mMap);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result.isResult()){
				Intent mIntent = new Intent();
				mIntent.putExtra(Constants.Result, mMap.get(param));
				setResult(100, mIntent);
				finish();
			} else {
				Toast.makeText(EditInfoActivity.this, R.string.change_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
