package com.dcy.psychology.view.dialog;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.SpecificUserBean;

import android.app.Dialog; 
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShareMatchDialog extends Dialog implements android.view.View.OnClickListener{
	private SpecificUserBean itemBean;
	
	public ShareMatchDialog(Context context, SpecificUserBean bean) {
		super(context, R.style.AppDialog);
		this.itemBean = bean;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share_match);
		((TextView)findViewById(R.id.tv_dialog_result)).setText(String.valueOf(itemBean.MatchResult) + "%");
		((TextView)findViewById(R.id.tv_hollend)).setText(itemBean.SpecificUserHollendTest);
		((TextView)findViewById(R.id.tv_qizhi)).setText(itemBean.SpecificUserTemperamentTest);
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
