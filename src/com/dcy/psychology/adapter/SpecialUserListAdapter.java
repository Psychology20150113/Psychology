package com.dcy.psychology.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dcy.psychology.DoctorPersonalInfo;
import com.dcy.psychology.PersonalInfoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.dialog.ShareMatchDialog;

public class SpecialUserListAdapter extends BaseAdapter implements OnClickListener{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList;
	private CustomProgressDialog mDialog;
	private boolean canOpration = true;
	private AsyncImageCache mAsyncImageCache;
	
	public SpecialUserListAdapter(Context mContext, ArrayList<SpecificUserBean> dataList){
		this(mContext, dataList, true);
	}
	
	public SpecialUserListAdapter(Context mContext, ArrayList<SpecificUserBean> dataList, boolean canOpration){
		mInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		this.mContext = mContext;
		this.canOpration = canOpration;
		mAsyncImageCache = AsyncImageCache.from(mContext);
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_show_specific_user_layout, null);
			mHolder = new Holder();
			mHolder.headerIv = (ImageView) convertView.findViewById(R.id.iv_header);
			mHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_item_name);
			mHolder.achieveTv = (TextView) convertView.findViewById(R.id.tv_item_achieve);
			mHolder.attentionTv = (TextView) convertView.findViewById(R.id.tv_item_attention);
			mHolder.matchTv = (TextView) convertView.findViewById(R.id.tv_item_match);
			mHolder.infoLayout = convertView.findViewById(R.id.ll_item_user_info);
			mHolder.infoLayout.setOnClickListener(lookInfoListener);
			if(canOpration){
				mHolder.attentionTv.setOnClickListener(this);
				mHolder.matchTv.setOnClickListener(this);
			} else {
				convertView.findViewById(R.id.ll_item_opration).setVisibility(View.GONE);
			}
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		SpecificUserBean item = dataList.get(position);
		mAsyncImageCache.displayImage(mHolder.headerIv, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(item.SpecificUserHeadUrl, item.SpecificUserHeadUrl));
		mHolder.nameTv.setText(item.SpecificUserName);
		mHolder.achieveTv.setText(item.SpecificUserAchievement);
		mHolder.attentionTv.setText(item.IsFollow ? R.string.cancel_attention : R.string.attention);
		long specialUserID = dataList.get(position).SpecificUserID;
		mHolder.attentionTv.setTag(specialUserID);
		mHolder.matchTv.setTag(specialUserID);
		mHolder.infoLayout.setTag(item.SpecificUserPhone);
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		long specialId = (Long)v.getTag();
		showCustomDialog();
		switch (v.getId()) {
		case R.id.tv_item_attention:
			if(mContext.getResources().getString(R.string.attention).equals(((TextView)v).getText())){
				new FollowTask((TextView)v, false).execute(specialId);
			} else {
				new FollowTask((TextView)v, true).execute(specialId);
			}
			break;
		case R.id.tv_item_match:
			new GetMatchTask().execute(specialId);
			break;
		default:
			break;
		}
	}
	
	private OnClickListener lookInfoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String phoneNum = (String)v.getTag();
			Intent mIntent = new Intent(mContext, DoctorPersonalInfo.class);
			mIntent.putExtra(Constants.PhoneNum, phoneNum);
			mContext.startActivity(mIntent);
		}
	};
	
	private void showCustomDialog(){
		if(mDialog == null){
			mDialog = new CustomProgressDialog(mContext);
		}
		mDialog.show();
	}
	
	private void hideCustomDialog(){
		if(mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
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
			if(result.isResult()){
				if(isFollowed){
					mTextView.setText(R.string.attention);
					Toast.makeText(mContext, R.string.cancel_attention_success, Toast.LENGTH_SHORT).show();
				} else {
					mTextView.setText(R.string.cancel_attention);
					Toast.makeText(mContext, R.string.attention_success, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	
	private class Holder{
		ImageView headerIv;
		TextView nameTv;
		TextView achieveTv;
		TextView attentionTv;
		TextView matchTv;
		View infoLayout;
	}
}
