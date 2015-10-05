package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.android.volley.Response.Listener;
import com.dcy.psychology.ApplyInfoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.adapter.TalkingAdapter;
import com.dcy.psychology.gsonbean.ApplyInfoBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.network.NetworkApi;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class AppliedFragment extends Fragment implements OnItemClickListener{
	private CustomProgressDialog mLoadingDialog;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList = new ArrayList<SpecificUserBean>();
	private ArrayList<ApplyInfoBean> applyInfoList;
	private SpecialUserListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mLoadingDialog = new CustomProgressDialog(mContext);
		mAdapter = new SpecialUserListAdapter(mContext, dataList);
		mLoadingDialog.show();
		NetworkApi.getApplyList(mListener);
//		new GetMatchestSpecialList().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_applied_layout, null);
		ListView mListView = (ListView) view.findViewById(R.id.lv_applied);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent = new Intent(mContext, ApplyInfoActivity.class);
		mIntent.putExtra("apply_id", applyInfoList.get(position).id);
		startActivity(mIntent);
	}
	
	private Listener<ArrayList<ApplyInfoBean>> mListener = new Listener<ArrayList<ApplyInfoBean>>() {
		@Override
		public void onResponse(ArrayList<ApplyInfoBean> response) {
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			if(response == null || response.size() == 0){
				return;
			}
			applyInfoList = response;
			for(ApplyInfoBean itemBean : response){
				dataList.add(SpecificUserBean.valueOf(itemBean));
			}
			mAdapter.notifyDataSetChanged();
		}
	};
	
	private class GetMatchestSpecialList extends AsyncTask<Void, Void, ArrayList<SpecificUserBean>>{
		@Override
		protected ArrayList<SpecificUserBean> doInBackground(Void... params) {
			return Utils.getMatchestSpecificUserList();
		}
		
		@Override
		protected void onPostExecute(ArrayList<SpecificUserBean> result) {
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			dataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
