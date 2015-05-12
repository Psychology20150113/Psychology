package com.dcy.psychology;

import com.dcy.psychology.util.ShareUtils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private LinearLayout rootView;
	private View mTitleView;
	private TextView mTitleText;
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private CustomProgressDialog mDialog;
	private TextView mTopRightText;
	private ImageView mLeftView;
	private ImageView mRightView;
	private PopupWindow mShareWindow;
	protected ShareUtils mShareUtils;
	
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.top_right_tv:
				onRightTextClick();
				break;
			case R.id.top_left_iv:
				onLeftViewClick();
				break;
			case R.id.top_right_iv:
				onRightViewClick();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mResources = getResources();
		rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);
		mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);
		rootView.addView(mTitleView, new LayoutParams(
				LayoutParams.MATCH_PARENT, (int)(50 * mResources.getDisplayMetrics().density)));
		initTitleView();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		mInflater.inflate(layoutResID, rootView);
		super.setContentView(rootView);
	}
	
	@Override
	public void setContentView(View view) {
		rootView.addView(view);
		super.setContentView(rootView);
	}
	
	private void initTitleView(){
		mTitleText = (TextView) mTitleView.findViewById(R.id.top_title_tv);
		mTopRightText = (TextView) mTitleView.findViewById(R.id.top_right_tv);
		mTopRightText.setOnClickListener(mClickListener);
		mLeftView = (ImageView) mTitleView.findViewById(R.id.top_left_iv);
		mLeftView.setOnClickListener(mClickListener);
		mRightView = (ImageView) mTitleView.findViewById(R.id.top_right_iv);
		mRightView.setOnClickListener(mClickListener);
	}
	
	public void setTopTitle(int resId){
		mTitleText.setText(getString(resId));
	}
	
	public void setTopTitle(String res){
		mTitleText.setText(res);
	}
	
	public void setLeftView(int resId){
		mLeftView.setImageResource(resId);
	}
	
	public void setRightText(int resId){
		mTopRightText.setText(getString(resId));
	}
	
	public void setRightText(String res){
		mTopRightText.setText(res);
	}
	
	public void setRightView(int resId){
		mRightView.setVisibility(View.VISIBLE);
		mRightView.setImageResource(resId);
	}
	
	public void hideTitleView(){
		mTitleView.setVisibility(View.GONE);
	}
	
	public void setTitleViewColor(int color){
		mTitleView.setBackgroundColor(color);
	}
	
	public void onRightTextClick(){};
	
	public void onLeftViewClick(){};
	
	public void onRightViewClick(){};
	
	protected void showCustomDialog(){
		if(mDialog == null){
			mDialog = new CustomProgressDialog(this);
		}
		mDialog.show();
	}
	
	protected void hideCustomDialog(){
		if(mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	protected void showSharePopupWindow(){
		if(mShareWindow == null){
			View shareView = mInflater.inflate(R.layout.popup_share_layout, null);
			mShareWindow = new PopupWindow(shareView, LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT);
			mShareWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
			mShareWindow.setAnimationStyle(R.style.AnimBottom);
			mShareWindow.setOutsideTouchable(true);
			shareView.findViewById(R.id.iv_share_ours).setOnClickListener(mShareClickListener);
			shareView.findViewById(R.id.iv_share_circle).setOnClickListener(mShareClickListener);
			shareView.findViewById(R.id.iv_share_sina).setOnClickListener(mShareClickListener);
		}
		mShareWindow.showAtLocation(mTitleView, Gravity.BOTTOM, 0, 0);
	}
	
	private OnClickListener mShareClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mShareUtils == null){
				mShareUtils = ShareUtils.getInstance(BaseActivity.this);
			}
			switch (v.getId()) {
			case R.id.iv_share_ours:
				shareToOurs();
				break;
			case R.id.iv_share_circle:
				shareToCircle();
				break;
			case R.id.iv_share_sina:
				shareToSina();
				break;
			default:
				break;
			}
		}
	};
	
	protected void shareToOurs(){};
	
	protected void shareToCircle(){};
	
	protected void shareToSina(){};
}
