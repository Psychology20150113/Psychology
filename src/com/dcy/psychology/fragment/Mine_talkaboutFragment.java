package com.dcy.psychology.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.dcy.psychology.ChatActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.TalkingAdapter;
import com.dcy.psychology.gsonbean.ApplyInfoBean;
import com.dcy.psychology.network.NetworkApi;
import com.dcy.psychology.view.CustomProgressDialog;

public class Mine_talkaboutFragment extends Fragment implements OnItemClickListener{
	private CustomProgressDialog mLoadingDialog;
	private Context mContext;
	private ArrayList<ApplyInfoBean> dataList = new ArrayList<ApplyInfoBean>();
	private TalkingAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mLoadingDialog = new CustomProgressDialog(mContext);
		mAdapter = new TalkingAdapter(mContext, dataList);
		mLoadingDialog.show();
		NetworkApi.getAgreedList(mListener);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_talkabout, null);
		ListView mListView = (ListView) view.findViewById(R.id.lv_talkabout);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent = new Intent(mContext, ChatActivity.class);
		String chatUserName = MyApplication.getInstance().isUser() ? 
				dataList.get(position).doctorphone : dataList.get(position).userphone;
		mIntent.putExtra("userId", chatUserName);
		startActivity(mIntent);
	}
	
	private Listener<ArrayList<ApplyInfoBean>> mListener = new Listener<ArrayList<ApplyInfoBean>>() {
		public void onResponse(java.util.ArrayList<ApplyInfoBean> response) {
			mLoadingDialog.dismiss();
			if(response == null){
				return;
			}
			dataList.addAll(response);
			mAdapter.notifyDataSetChanged();
		};
	};
}
