package com.dcy.psychology.view.dialog;

import com.dcy.psychology.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class SimpleMessageDialog extends Dialog implements android.view.View.OnClickListener{
	private String title;
	private String message;
	private String leftButton;
	private String rightButton;
	private View.OnClickListener mSureClickListener;
	private View.OnClickListener mCancelClickListener;
	
	public SimpleMessageDialog(Context context, String title, String message) {
		super(context, R.style.AppDialog);
		this.title = title;
		this.message = message;
	}
	
	public SimpleMessageDialog(Context context, String message){
		super(context, R.style.AppDialog);
		this.message = message;
		this.leftButton = context.getString(R.string.cancel);
	}
	
	public SimpleMessageDialog(Context context, String title, String message, 
			String leftButton, String rightButton) {
		super(context, R.style.AppDialog);
		this.title = title;
		this.message = message;
		this.leftButton = leftButton;
		this.rightButton = rightButton;
	}
	
	public void setSureClickListener(android.view.View.OnClickListener sureListener){
		this.mSureClickListener = sureListener;
	}
	
	public void setCancelClickListener(android.view.View.OnClickListener cancelListener){
		this.mCancelClickListener = cancelListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.dialog_simple_message_layout);
		if(!TextUtils.isEmpty(title)){
			((TextView)findViewById(R.id.dialog_title_tv)).setText(title);
		}
		((TextView)findViewById(R.id.dialog_message_tv)).setText(message);
		if(!TextUtils.isEmpty(leftButton)){
			findViewById(R.id.dialog_ok_tv).setVisibility(View.GONE);
			findViewById(R.id.ll_two_buttons).setVisibility(View.VISIBLE);
			TextView leftView = ((TextView)findViewById(R.id.dialog_left_tv));
			leftView.setText(leftButton);
			leftView.setOnClickListener(this);
			TextView rightView = ((TextView)findViewById(R.id.dialog_right_tv));
			if(!TextUtils.isEmpty(rightButton)){
				rightView.setText(rightButton);
			}
			rightView.setOnClickListener(this);
		} else {
			findViewById(R.id.dialog_ok_tv).setOnClickListener(this);
		}
		setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_left_tv:
			dismiss();
			if(mCancelClickListener != null){
				mCancelClickListener.onClick(v);
			}
			break;
		case R.id.dialog_right_tv:
			dismiss();
			if(mSureClickListener != null){
				mSureClickListener.onClick(v);
			}
			break;
		case R.id.dialog_ok_tv:
			dismiss();
			break;
		default:
			break;
		}
	}
}
