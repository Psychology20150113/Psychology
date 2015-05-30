package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialUserListAdapter extends BaseAdapter implements OnClickListener{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList;
	private CustomProgressDialog mDialog;
	
	public SpecialUserListAdapter(Context mContext, ArrayList<SpecificUserBean> dataList){
		mInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		this.mContext = mContext;
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
			mHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_item_name);
			mHolder.achieveTv = (TextView) convertView.findViewById(R.id.tv_item_achieve);
			mHolder.speakTv = (TextView) convertView.findViewById(R.id.tv_item_speak);
			mHolder.attentionTv = (TextView) convertView.findViewById(R.id.tv_item_attention);
			mHolder.testTv = (TextView) convertView.findViewById(R.id.tv_item_test);
			mHolder.attentionTv.setOnClickListener(this);
			mHolder.testTv.setOnClickListener(this);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		SpecificUserBean item = dataList.get(position);
		mHolder.nameTv.setText(item.SpecificUserName);
		mHolder.achieveTv.setText(item.SpecificUserAchievement);
		mHolder.speakTv.setText(item.SpecificUserLifeMotto);
		long specialUserID = dataList.get(position).SpecificUserID;
		mHolder.attentionTv.setTag(specialUserID);
		mHolder.testTv.setTag(specialUserID);
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		long specialId = (Long)v.getTag();
		showCustomDialog();
		switch (v.getId()) {
		case R.id.tv_item_attention:
			new FollowTask().execute(specialId);
			break;
		case R.id.tv_item_test:
			break;
		default:
			break;
		}
	}
	
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
	
	private class GetMatchTask extends AsyncTask<Long, Void, BasicBean>{
		@Override
		protected BasicBean doInBackground(Long... params) {
			if(params[0] == null){
				return null;
			}
			return Utils.getMatchResult(params[0]);
		}
	}
	
	private class FollowTask extends AsyncTask<Long, Void, BasicBean>{
		@Override
		protected BasicBean doInBackground(Long... params) {
			if(params[0] == null){
				return null;
			}
			return Utils.followSpecificUser(params[0]);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			if(result.isResult()){
				Toast.makeText(mContext, R.string.attention_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class Holder{
		TextView nameTv;
		TextView achieveTv;
		TextView speakTv;
		TextView attentionTv;
		TextView testTv;
	}
}
