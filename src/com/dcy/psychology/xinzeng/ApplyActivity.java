package com.dcy.psychology.xinzeng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.network.NetworkApi;
import com.dcy.psychology.util.Constants;

public class ApplyActivity  extends BaseActivity implements OnClickListener{
	private EditText mQuestionEt;
	private EditText mInfoEt;
	private String phoneNum;
	private String[] dayArray = new String[7];
	private String[] timeArray;
	private String[] amArray;
	private SimpleDateFormat mFormat = new SimpleDateFormat("MM月dd日");
	private SimpleDateFormat mTimeFormat = new SimpleDateFormat("yyyy MM月dd日 HH:mm");
	private LinearLayout containLayout;
	private LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_layout);
		setTopTitle(R.string.apply);
		phoneNum = getIntent().getStringExtra(Constants.PhoneNum);
		mInflater = getLayoutInflater();
		initView();
	}
	
	private void initView() {
		findViewById(R.id.btn_submit).setOnClickListener(this);
		findViewById(R.id.iv_add_time).setOnClickListener(this);
		mQuestionEt = (EditText) findViewById(R.id.et_hope);
		mInfoEt = (EditText) findViewById(R.id.et_ictroduce);
		containLayout = (LinearLayout) findViewById(R.id.ll_time_container);
		initSelectDayArray();
		timeArray = getResources().getStringArray(R.array.sp_shoose_time);
		amArray = getResources().getStringArray(R.array.sp_am);
		addItemLayout();
	}
	
	private void addItemLayout(){
		if(containLayout.getChildCount() >= 3){
			Toast.makeText(this, R.string.three_max, Toast.LENGTH_SHORT).show();
			return;
		}
		View itemView = mInflater.inflate(R.layout.layout_apply_time, null);
		Spinner daySpinner = (Spinner) itemView.findViewById(R.id.sp_choose_day);
		Spinner timeSpinner = (Spinner) itemView.findViewById(R.id.sp_choose_time);
		Spinner amSpinner = (Spinner) itemView.findViewById(R.id.sp_choose_am);
		daySpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.spinnerlayout, dayArray));
		timeSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.spinnerlayout, timeArray));
		amSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.spinnerlayout, amArray));
		containLayout.addView(itemView);
	}
	
	private void initSelectDayArray(){
		Calendar mCalendar = Calendar.getInstance();
		for(int i = 0 ; i < 7 ; i ++){
			dayArray[i] = mFormat.format(mCalendar.getTime());
			mCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
	}
	
	private long[] getItemTime(View itemLayout){
		Spinner daySpinner = (Spinner) itemLayout.findViewById(R.id.sp_choose_day);
		Spinner timeSpinner = (Spinner) itemLayout.findViewById(R.id.sp_choose_time);
		Spinner amSpinner = (Spinner) itemLayout.findViewById(R.id.sp_choose_am);
		String itemStartTime = "";
		String itemEndTime = "";
		boolean isAm = amSpinner.getSelectedItemPosition() == 0;
		switch (timeSpinner.getSelectedItemPosition()) {
			case 0:
				itemStartTime = isAm ? "00:00" : "12:00";
				itemEndTime = isAm ? "04:00" : "16:00";
				break;
			case 1:
				itemStartTime = isAm ? "04:00" : "16:00";
				itemEndTime = isAm ? "08:00" : "20:00";
				break;
			case 2:
				itemStartTime = isAm ? "08:00" : "12:00";
				itemEndTime = isAm ? "12:00" : "23:59";
				break;
			case 3:
				itemStartTime = "00:00";
				itemEndTime = "23:59";
				break;
			default:
				break;
		}
		long[] timeArray = new long[2];
		try {
			timeArray[0] = mTimeFormat.parse("2015 " + daySpinner.getSelectedItem().toString() + " " + itemStartTime).getTime();
			timeArray[1] = mTimeFormat.parse("2015 " + daySpinner.getSelectedItem().toString() + " " + itemEndTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeArray;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_time:
			addItemLayout();
			break;
		case R.id.btn_submit:
			if(!checkInput()){
				return;
			}
			StringBuilder startTimeBuilder = new StringBuilder();
			StringBuilder endTimeBuilder = new StringBuilder();
			long[] itemTime;
			for(int i = 0, n = containLayout.getChildCount(); i < n ; i ++){
				itemTime = getItemTime(containLayout.getChildAt(i));
				startTimeBuilder.append(itemTime[0]).append(",");
				endTimeBuilder.append(itemTime[1]).append(",");
			}
			showCustomDialog();
			NetworkApi.applyTalk(phoneNum, mQuestionEt.getText().toString(), mInfoEt.getText().toString(), 
					startTimeBuilder.substring(0, startTimeBuilder.length() - 1), endTimeBuilder.substring(0, endTimeBuilder.length() - 1),
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