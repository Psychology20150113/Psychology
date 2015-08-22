package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class ApplyActivity  extends BaseActivity implements OnClickListener{
	private Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_layout);
		setTopTitle(R.string.apply);
		submit=(Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);
		Spinner mSpinner1 = (Spinner) findViewById(R.id.sp_choose_time1);
		Spinner mSpinner2 = (Spinner) findViewById(R.id.sp_choose_time2);
		Spinner mSpinner3 = (Spinner) findViewById(R.id.sp_am_and_pm1);
		Spinner mSpinner4 = (Spinner) findViewById(R.id.sp_am_and_pm2);
		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.sp_shoose_time);
		String[] mItems2 = getResources().getStringArray(R.array.sp_am);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> mAdapter1=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems);
		ArrayAdapter<String> mAdapter2=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems);
		ArrayAdapter<String> mAdapter3=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems2);
		ArrayAdapter<String> mAdapter4=new ArrayAdapter<String>(this,R.layout.spinnerlayout, mItems2);
		//绑定 Adapter到控件
		mSpinner1.setAdapter(mAdapter1);
		mSpinner2.setAdapter(mAdapter2);
		mSpinner3.setAdapter(mAdapter3);
		mSpinner4.setAdapter(mAdapter4);
		
	}
	@Override
	public void onClick(View v) {
		Intent mIntent =new Intent(this, SubmitsuccessActivity.class); 
	
		startActivity(mIntent);
		// TODO 自动生成的方法存根
		
	}

		
}