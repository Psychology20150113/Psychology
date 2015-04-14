package com.dcy.psychology.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.CommentBean;
import com.dcy.psychology.gsonbean.CommentDetailBean;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentListAdapter extends BaseAdapter implements OnClickListener{
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<CommentItem> dataList = new ArrayList<CommentListAdapter.CommentItem>();
	private CommentOprationListerer mListerer;
	
	private CustomProgressDialog mDialog;
	private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private ArrayList<CommentDetaiListAdapter> mDetailAdapterList = new ArrayList<CommentDetaiListAdapter>();
	private class CommentItem{
		public CommentBean infoItem;
		public ArrayList<CommentDetailBean> detailItems; 
	}
	
	private interface CommentOprationListerer{
		void look(int id);
	}
	
	public CommentListAdapter(Context mContext , ArrayList<CommentBean> dataList) {
		mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		for(CommentBean item : dataList){
			CommentItem dataItem = new CommentItem();
			dataItem.infoItem = item;
			this.dataList.add(dataItem);
			CommentDetaiListAdapter itemAdapter = new CommentDetaiListAdapter(mContext);
			mDetailAdapterList.add(itemAdapter);
		}
	}
	
	public void setCommentOprationListener(CommentOprationListerer listerer){
		this.mListerer = listerer;
	}
	
	public void updateList(ArrayList<CommentBean> infoItems){
		dataList.removeAll(dataList);
		for(CommentBean infoItem : infoItems){
			CommentItem commentItem = new CommentItem();
			commentItem.infoItem = infoItem;
			dataList.add(commentItem);
			CommentDetaiListAdapter itemAdapter = new CommentDetaiListAdapter(mContext);
			mDetailAdapterList.add(itemAdapter);
		}
		notifyDataSetChanged();
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
			convertView = mInflater.inflate(R.layout.item_comment_list_layout, null);
			mHolder = new Holder();
			mHolder.nameText = (TextView) convertView.findViewById(R.id.item_name_tv);
			mHolder.timeText = (TextView) convertView.findViewById(R.id.item_time_tv);
			mHolder.commentText = (TextView) convertView.findViewById(R.id.item_comment_tv);
			mHolder.lookBtn = (ImageView) convertView.findViewById(R.id.item_look_btn);
			mHolder.commentBtn = (ImageView) convertView.findViewById(R.id.item_comment_btn);
			mHolder.commentDetailList = (ListView) convertView.findViewById(R.id.item_detail_comment_lv);
			mHolder.writeLayout = (LinearLayout) convertView.findViewById(R.id.item_comment_ll);
			mHolder.writeEdit = (EditText) convertView.findViewById(R.id.item_comment_et);
			mHolder.sureBtn = (Button) convertView.findViewById(R.id.item_comment_sure_btn);
			mHolder.hideCommentLayout = (LinearLayout) convertView.findViewById(R.id.item_hide_comment_ll);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		CommentItem item = dataList.get(position);
		mHolder.nameText.setText(item.infoItem.getUserLoginName());
		mHolder.timeText.setText(item.infoItem.getCreatDate());
		mHolder.commentText.setText(item.infoItem.getHeartWeiBo());
		mHolder.lookBtn.setOnClickListener(this);
		mHolder.lookBtn.setTag(position);
		mHolder.commentBtn.setOnClickListener(this);
		mHolder.hideCommentLayout.setTag(position);
		mHolder.hideCommentLayout.setOnClickListener(this);
		if(item.detailItems != null){
			mHolder.commentDetailList.setVisibility(View.VISIBLE);
			mHolder.hideCommentLayout.setVisibility(View.VISIBLE);
			CommentDetaiListAdapter itemAdapter = mDetailAdapterList.get(position);
			itemAdapter.setData(item.detailItems);
			mHolder.commentDetailList.setAdapter(itemAdapter);
		}else {
			mHolder.commentDetailList.setVisibility(View.GONE);
			mHolder.hideCommentLayout.setVisibility(View.GONE);
		}
		mHolder.writeLayout.setVisibility(View.GONE);
		mHolder.sureBtn.setTag(position);
		mHolder.sureBtn.setOnClickListener(this);
		return convertView;
	}
	
	private class GetCommentDetailTask extends AsyncTask<Integer, Void, ArrayList<CommentDetailBean>>{
		int position = 0;
		@Override
		protected ArrayList<CommentDetailBean> doInBackground(Integer... params) {
			position = params[0];
			return Utils.getCommentDetail(dataList.get(position).infoItem.getHeartWeiBoID());
		}
		
		@Override
		protected void onPostExecute(ArrayList<CommentDetailBean> result) {
			hideCustomDialog();
			if(result == null)
				return;
			dataList.get(position).detailItems = result;
			notifyDataSetChanged();
		}
		
	}
	
	private class CommentItemTask extends AsyncTask<Object, Void, BasicBean>{
		int position;
		String content;
		int id;
		@Override
		protected BasicBean doInBackground(Object... params) {
			position = (Integer)params[0];
			content = (String)params[1];
			id = dataList.get(position).infoItem.getHeartWeiBoID();
			return Utils.commentItem(id, content);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(dataList.get(position).detailItems == null){
				showCustomDialog();
				new GetCommentDetailTask().execute(position);
				return;
			}
			CommentDetailBean newItem = new CommentDetailBean();
			newItem.setHeartWeiBoID(id);
			newItem.setReviewContent(content);
			newItem.setReviewDate(mFormat.format(Calendar.getInstance().getTime()));
			newItem.setReviewUserLoginName(MyApplication.myPhoneNum);
			dataList.get(position).detailItems.add(0, newItem);
			mDetailAdapterList.get(position).notifyDataSetChanged();
			Toast.makeText(mContext, result.isResult() ? R.string.comment_success : R.string.comment_failed, 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_look_btn:
			int position = (Integer)v.getTag();
			showCustomDialog();
			new GetCommentDetailTask().execute(position);
			break;
		case R.id.item_comment_btn:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				Toast.makeText(mContext, R.string.please_login, Toast.LENGTH_SHORT).show();
				return;
			}
			((LinearLayout)v.getParent().getParent()).findViewById(R.id.item_comment_ll).setVisibility(View.VISIBLE);
			break;
		case R.id.item_comment_sure_btn:
			LinearLayout parent = (LinearLayout) v.getParent();
			EditText content = (EditText) parent.findViewById(R.id.item_comment_et);
			if(TextUtils.isEmpty(content.getText().toString())){
				Toast.makeText(mContext, R.string.please_input, Toast.LENGTH_SHORT).show();
				return;
			}
			parent.setVisibility(View.GONE);
			showCustomDialog();
			new CommentItemTask().execute((Integer)v.getTag() , content.getText().toString());
			content.setText("");
			break;
		case R.id.item_hide_comment_ll:
			((LinearLayout) v.getParent()).findViewById(R.id.item_detail_comment_lv).setVisibility(View.GONE);
			dataList.get((Integer) v.getTag()).detailItems = null;
			v.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	private class Holder{
		TextView nameText;
		TextView timeText;
		TextView commentText;
		ImageView lookBtn;
		ImageView commentBtn;
		ListView commentDetailList;
		LinearLayout hideCommentLayout;
		LinearLayout writeLayout;
		EditText writeEdit;
		Button sureBtn;
	}
}
