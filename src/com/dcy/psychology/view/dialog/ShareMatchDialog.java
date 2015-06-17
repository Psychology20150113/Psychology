package com.dcy.psychology.view.dialog;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.ShareUtils;

import android.app.Activity;
import android.app.Dialog; 
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShareMatchDialog extends Dialog implements android.view.View.OnClickListener{
	private SpecificUserBean itemBean;
	private ShareUtils mShareUtils;
	private Context mContext;
	private String shareUrl = "http://114.215.179.130/webservice/share.html?userphone=%s&specificuserid=%s&matchresult=%s";
	
	public ShareMatchDialog(Context context, SpecificUserBean bean) {
		super(context, R.style.AppDialog);
		this.itemBean = bean;
		mContext = context;
		mShareUtils = ShareUtils.getInstance((Activity)context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share_match);
		((TextView)findViewById(R.id.tv_dialog_result)).setText(String.valueOf(itemBean.MatchResult) + "%");
		((TextView)findViewById(R.id.tv_hollend)).setText(itemBean.SpecificUserHollendTest);
		((TextView)findViewById(R.id.tv_qizhi)).setText(itemBean.SpecificUserTemperamentTest);
		findViewById(R.id.dialog_ok_tv).setOnClickListener(this);
		findViewById(R.id.iv_share_circle).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok_tv:
			dismiss();
			break;
		case R.id.iv_share_circle:
			mShareUtils.shareToCircle(String.format
					(mContext.getString(R.string.share_format), itemBean.MatchResult, itemBean.SpecificUserName),
					String.format(shareUrl, MyApplication.myPhoneNum, itemBean.SpecificUserID, itemBean.MatchResult));
			break;
		default:
			break;
		}
	}
}
