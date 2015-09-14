package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.network.NetworkApi;
import com.dcy.psychology.util.Constants;

public class ApplyActivity  extends BaseActivity implements OnClickListener{
	private Button submit;
	private EditText mQuestionEt;
	private EditText mInfoEt;
	private String phoneNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_layout);
		setTopTitle(R.string.apply);
		phoneNum = getIntent().getStringExtra(Constants.PhoneNum);
		initView();
		
	}
	private void initView() {
		submit=(Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);
		mQuestionEt = (EditText) findViewById(R.id.et_hope);
		mInfoEt = (EditText) findViewById(R.id.et_ictroduce);
		Spinner mSpinner1 = (Spinner) findViewById(R.id.sp_choose_time1);
		Spinner mSpinner2 = (Spinner) findViewById(R.id.sp_choose_time2);
		Spinner mSpinner3 = (Spinner) findViewById(R.id.sp_am_and_pm1);
		Spinner mSpinner4 = (Spinner) findViewById(R.id.sp_am_and_pm2);
		// 寤虹珛鏁版嵁婧�
		String[] mItems = getResources().getStringArray(R.array.sp_shoose_time);
		String[] mItems2 = getResources().getStringArray(R.array.sp_am);
		// 寤虹珛Adapter骞朵笖缁戝畾鏁版嵁婧�
		ArrayAdapter<String> mAdapter1=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems);
		ArrayAdapter<String> mAdapter2=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems);
		ArrayAdapter<String> mAdapter3=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems2);
		ArrayAdapter<String> mAdapter4=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems2);
		//缁戝畾 Adapter鍒版帶浠�
		mSpinner1.setAdapter(mAdapter1);
		mSpinner2.setAdapter(mAdapter2);
		mSpinner3.setAdapter(mAdapter3);
		mSpinner4.setAdapter(mAdapter4);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			if(!checkInput()){
				return;
			}
			showCustomDialog();
			NetworkApi.applyTalk(phoneNum, mQuestionEt.getText().toString(), mInfoEt.getText().toString(), "1213,123", "35235,123132",
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							hideCustomDialog();
							Intent mIntent =new Intent(ApplyActivity.this, SubmitsuccessActivity.class); 
							startActivity(mIntent);
							finish();
						}
				
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							hideCustomDialog();
							Toast.makeText(ApplyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
			break;

		default:
			break;
		}
	}

	private boolean checkInput(){
		if(TextUtils.isEmpty(mQuestionEt.getText()) || TextUtils.isEmpty(mInfoEt.getText())){
			Toast.makeText(this, R.string.please_input, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}