package com.dcy.psychology.view.dialog;

import com.dcy.psychology.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SimpleMessageDialog extends Dialog implements android.view.View.OnClickListener{
	private String title;
	private String message;
	
	
	public SimpleMessageDialog(Context context, String title, String message) {
		super(context);
		this.title = title;
		this.message = message;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.dialog_simple_message_layout);
		((TextView)findViewById(R.id.dialog_title_tv)).setText(title);
		((TextView)findViewById(R.id.dialog_message_tv)).setText(message);
		findViewById(R.id.dialog_ok_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok_tv:
			dismiss();
			break;

		default:
			break;
		}
	}
}
