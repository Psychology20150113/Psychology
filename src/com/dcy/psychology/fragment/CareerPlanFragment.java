package com.dcy.psychology.fragment;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.util.Utils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CareerPlanFragment extends Fragment implements OnClickListener{
	private Context mContext;
	private EditText nickEt;
	private RadioGroup sexGroup;
	private EditText ageEt;
	private EditText phoneEt;
	private EditText mailEt;
	private EditText pwdQuestionEt;
	private EditText pwdAnswerEt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_career_plan_layout, null);
		if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
			view.findViewById(R.id.ll_entry).setVisibility(View.VISIBLE);
			view.findViewById(R.id.tv_student_entry).setOnClickListener(this);
			view.findViewById(R.id.tv_teacher_entry).setOnClickListener(this);
		}
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
	
	private void initPerfectInfoView(View rootView){
//		accountEt = (EditText) findViewById(R.id.account_et);
//		nickEt = (EditText) findViewById(R.id.nick_et);
//		pwdEt = (EditText) findViewById(R.id.password_et);
//		surePwdEt = (EditText) findViewById(R.id.second_password_et);
//		sexGroup = (RadioGroup) findViewById(R.id.sex_rg);
//		ageEt = (EditText) findViewById(R.id.age_et);
//		phoneEt = (EditText) findViewById(R.id.phone_et);
//		mailEt = (EditText) findViewById(R.id.mail_et);
//		pwdQuestionEt = (EditText) findViewById(R.id.pwd_question_et);
//		pwdAnswerEt = (EditText) findViewById(R.id.pwd_answer_et);
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
