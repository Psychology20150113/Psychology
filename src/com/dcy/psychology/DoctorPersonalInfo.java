package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.dialog.ShareMatchDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorPersonalInfo extends BaseActivity implements OnClickListener{
	private String phoneNum;
	private TextView nameTv;
	private ImageView headerView;
	private TextView achieveView;
	private TextView mInfoView;
	private AsyncImageCache mAsyncImageCache;
	private Context mContext;
	private long specialId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_info_layout);
		setTopTitle(R.string.doctor_info);
		mContext = this;
		mAsyncImageCache = AsyncImageCache.from(this);
		initView();
		phoneNum = getIntent().getStringExtra(Constants.PhoneNum);
		if(!TextUtils.isEmpty(phoneNum)){
			showCustomDialog();
			new GetInfoTask().execute();
		}
	}
	
	@Override
	public void onClick(View v) {
		showCustomDialog();
		switch (v.getId()) {
		/*case R.id.tv_item_match:
			new GetMatchTask().execute(specialId);
			break;
		case R.id.tv_item_attention:
			if(mContext.getResources().getString(R.string.attention).equals(((TextView)v).getText())){
				new FollowTask((TextView)v, false).execute(specialId);
			} else {
				new FollowTask((TextView)v, true).execute(specialId);
			}
			break;*/
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
			hideCustomDialog();
			if(result == null || result.size() == 0){
				return;
			}
			new ShareMatchDialog(mContext, result.get(0)).show();
		}
	}
	
	private class FollowTask extends AsyncTask<Long, Void, BasicBean>{
		private TextView mTextView;
		private boolean isFollowed;
		
		public FollowTask(TextView view, boolean isFollowed) {
			mTextView = view;
			this.isFollowed = isFollowed;
		}
		
		@Override
		protected BasicBean doInBackground(Long... params) {
			if(params[0] == null){
				return null;
			}
			return isFollowed ? Utils.cancelFollowSpecificUser(params[0]) : 
				Utils.followSpecificUser(params[0]);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			/*if(result.isResult()){
				if(isFollowed){
					mTextView.setText(R.string.attention);
					Toast.makeText(mContext, R.string.cancel_attention_success, Toast.LENGTH_SHORT).show();
				} else {
					mTextView.setText(R.string.cancel_attention);
					Toast.makeText(mContext, R.string.attention_success, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}*/
			if(result.isResult())
			{
				if(isFollowed)
				{
					mTextView.setText(R.string.attention);
					 Toast(R.string.cancel_attention_success);
					//toast.makeText(mContext, R.string.cancel_attention_success, toast.LENGTH_SHORT).show();
				} else 
				{
					mTextView.setText(R.string.cancel_attention);
					 Toast(R.string.attention_success);
					//toast.makeText(mContext, R.string.attention_success, toast.LENGTH_SHORT).show();//��ע��ȡ��Ի������ʾ
				}
			} else
			{
				LayoutInflater inflater = LayoutInflater.from(mContext);;
		        View layout = inflater.inflate(R.layout.toast_layout,null);
		        layout.getBackground().setAlpha(65);
		        TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
		        text.setText(result.getReason());
		        Toast toast = new Toast(mContext);
		        toast.setGravity(Gravity.CENTER, 0, 0);
		        toast.setDuration(Toast.LENGTH_SHORT);
		       // toast.makeText(mContext, R.string.cancel_attention_success, toast.LENGTH_SHORT);
		        toast.setView(layout);
		        toast.show();
				//toast.makeText(mContext, result.getReason(), toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void initView(){
		nameTv = (TextView) findViewById(R.id.tv_item_name);
		headerView = (ImageView) findViewById(R.id.iv_header);
		achieveView = (TextView) findViewById(R.id.tv_item_achieve);
		mInfoView = (TextView) findViewById(R.id.tv_doctor_info);
		/*findViewById(R.id.tv_item_attention).setOnClickListener(this);
		
		findViewById(R.id.tv_item_match).setOnClickListener(this);*/
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
	
	
	private void setInfoData(UserInfoBean result){
		nameTv.setText(result.UserName);
		achieveView.setText(result.UserAchievement);
		mAsyncImageCache.displayImage(headerView, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(result.UserHeadUrl, result.UserHeadUrl));
		mInfoView.setText(result.UserResume);
	}
	 private void Toast(int idT){
		 LayoutInflater inflater = LayoutInflater.from(mContext);;
         View layout = inflater.inflate(R.layout.toast_layout,null);
         layout.getBackground().setAlpha(65);
         TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
         text.setText(idT);
         Toast toast = new Toast(mContext);
         toast.setGravity(Gravity.CENTER, 0, 0);
         toast.setDuration(Toast.LENGTH_SHORT);
        // toast.makeText(mContext, R.string.cancel_attention_success, toast.LENGTH_SHORT);
         toast.setView(layout);
         toast.show();
	 
	 }
}
