package com.dcy.psychology.view.dialog;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.ShareUtils;
import com.dcy.psychology.view.MatchCircleView;

import android.app.Activity;
import android.app.Dialog; 
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareMatchDialog extends Dialog implements android.view.View.OnClickListener{
	private SpecificUserBean itemBean;
	private ShareUtils mShareUtils;
	private Context mContext;
	private String shareUrl = "http://114.215.179.130/webservice/share.html?userphone=%s&specificuserid=%s&matchresult=%s";
	private AsyncImageCache mCache;
	private String resultFormat;
	
	public ShareMatchDialog(Context context, SpecificUserBean bean) {
		super(context, R.style.AppDialog);
		this.itemBean = bean;
		mContext = context;
		resultFormat = mContext.getResources().getString(R.string.match_format);
		mCache = AsyncImageCache.from(mContext);
		mShareUtils = ShareUtils.getInstance((Activity)context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share_match);
		findViewById(R.id.iv_close_dialog).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_other_name)).setText(itemBean.SpecificUserName);
		((TextView)findViewById(R.id.tv_result)).setText(String.format(resultFormat, String.valueOf((int)itemBean.MatchResult)));
		String myHeadUrl = new InfoShared(mContext).getHeaderUrl();
		if(!TextUtils.isEmpty(myHeadUrl)){
			mCache.displayImage((ImageView)findViewById(R.id.iv_head_mine), R.drawable.ic_launcher, 
					new AsyncImageCache.NetworkImageGenerator(myHeadUrl, myHeadUrl));
		}
		mCache.displayImage((ImageView)findViewById(R.id.iv_head_teacher), R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(itemBean.SpecificUserHeadUrl, itemBean.SpecificUserHeadUrl));
		findViewById(R.id.ll_share_circle).setOnClickListener(this);
		((MatchCircleView)findViewById(R.id.view_result)).setData(itemBean.MatchResult/100);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close_dialog:
			dismiss();
			break;
		case R.id.ll_share_circle:
			mShareUtils.shareToCircle(String.format
					(mContext.getString(R.string.share_format), itemBean.MatchResult, itemBean.SpecificUserName),
					mCache.getCacheImage(itemBean.SpecificUserHeadUrl),
					String.format(shareUrl, MyApplication.myPhoneNum, itemBean.SpecificUserID, itemBean.MatchResult));
			break;
		default:
			break;
		}
	}
}
