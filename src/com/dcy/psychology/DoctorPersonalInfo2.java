package com.dcy.psychology;

import java.util.ArrayList;

import com.android.volley.Response.Listener;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.network.NetworkApi;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.dialog.ShareMatchDialog;
import com.dcy.psychology.xinzeng.ApplyActivity;
import com.dcy.psychology.xinzeng.ShareActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorPersonalInfo2 extends BaseActivity implements OnClickListener{
	private String phoneNum;
	private TextView nameTv;
	private ImageView headerView;
	private ImageView IvshareView;
	private ImageView Ivattention;
	private TextView gotalkView;
	private  ImageView mbackview;
	private  ImageView mshare;
	private TextView mInfoView;
	private AsyncImageCache mAsyncImageCache;
	private Context mContext;
	private long specialId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_homepage_layout);
		mContext = this;
		mAsyncImageCache = AsyncImageCache.from(this);
		initView();
		SpecificUserBean itemBean = (SpecificUserBean) getIntent().getSerializableExtra(Constants.UserBean);
		phoneNum = itemBean.SpecificUserPhone;
		specialId = itemBean.SpecificUserID;
		showCustomDialog();
		NetworkApi.getDetailInfo(specialId, mListener);
//			new GetInfoTask().execute();
	}
	
	@Override
	public void onClick(View v) {
		//showCustomDialog();
		switch (v.getId()) {
		case R.id.iv_back:
   			finish();
   			break;
		case R.id.tv_talk:
   			Intent mIntent =new Intent(this,ApplyActivity.class);
   			mIntent.putExtra(Constants.PhoneNum, phoneNum);
   			startActivity(mIntent);
   			break;
		case R.id.iv_share:
			 mIntent =new Intent(this,ShareActivity.class);
   			 startActivity(mIntent);
   			break;
   		case R.id.iv_attention:
   			if(specialId == 0){
   				return;
   			}
   			showCustomDialog();
   			new FollowTask(false).execute();
   			break;
		default:
			break;
		}
		
	}
	
	private class GetMatchTask extends AsyncTask<Long, Void, ArrayList<SpecificUserBean>>{
		@Override
		protected ArrayList<SpecificUserBean> doInBackground(Long... params) {
			if(params[0] == null){
				return null;
			}
			return Utils.getMatchResult(params[0]);
		}
		
		@Override
		protected void onPostExecute(ArrayList<SpecificUserBean> result) {
			//hideCustomDialog();
			if(result == null || result.size() == 0){
				return;
			}
			new ShareMatchDialog(mContext, result.get(0)).show();
		}
	}
	
	private class FollowTask extends AsyncTask<Void, Void, BasicBean>{
		private boolean isFollowed;
		
		public FollowTask(boolean isFollowed) {
			this.isFollowed = isFollowed;
		}
		
		@Override
		protected BasicBean doInBackground(Void... params) {
			return isFollowed ? Utils.cancelFollowSpecificUser(specialId): 
				Utils.followSpecificUser(specialId);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			if(result.isResult()){
				if(isFollowed){
					Toast.makeText(mContext, R.string.cancel_attention_success, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, R.string.attention_success, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void initView(){
		hideTitleView();
		nameTv = (TextView) findViewById(R.id.tv_item_name);
		headerView = (ImageView) findViewById(R.id.iv_header);
		mInfoView=(TextView) findViewById(R.id.tv_resume);
		mbackview=(ImageView) findViewById(R.id.iv_back);
		mbackview.setOnClickListener(this);
		mbackview.setImageResource(R.drawable.icon_back);	
		gotalkView=(TextView) findViewById(R.id.tv_talk);
		gotalkView.setVisibility(View.VISIBLE);
		gotalkView.setOnClickListener(this);
		findViewById(R.id.ll_edit).setVisibility(View.GONE);
		findViewById(R.id.ll_te_mark).setVisibility(View.VISIBLE);
		findViewById(R.id.rl_st_told).setVisibility(View.GONE);	
		findViewById(R.id.rl_te_told).setVisibility(View.VISIBLE);;
		findViewById(R.id.iv_individual).setVisibility(View.GONE);
		findViewById(R.id.iv_attentiontopic).setVisibility(View.GONE);
		IvshareView=(ImageView) findViewById(R.id.iv_share);
		Ivattention=(ImageView) findViewById(R.id.iv_attention);
		Ivattention.setOnClickListener(this);
		IvshareView.setVisibility(View.VISIBLE);
		Ivattention.setVisibility(View.VISIBLE);
		mshare=(ImageView) findViewById(R.id.iv_share);
		mshare.setOnClickListener(this);
	}
	
	private class GetInfoTask extends AsyncTask<Void, Void, UserInfoBean> {
		@Override
		protected UserInfoBean doInBackground(Void... params) {
			return Utils.getUserInfo(phoneNum);
		}
		
		@Override
		protected void onPostExecute(UserInfoBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			specialId = result.UserID;
			setInfoData(result);
		}
	}
	
	private Listener<UserInfoBean> mListener = new Listener<UserInfoBean>() {
		public void onResponse(UserInfoBean response) {
			hideCustomDialog();
			if(response == null){
				return;
			}
			setInfoData(response);
		};
	};
	
	private void setInfoData(UserInfoBean result){
		nameTv.setText(result.UserName);
		mAsyncImageCache.displayImage(headerView, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(result.UserHeadUrl, result.UserHeadUrl));
		mInfoView.setText(result.UserResume);
	}
}
