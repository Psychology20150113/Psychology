package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class GetFollowUsersFragmentActivity extends Fragment {
	private ListView mListView;
	private CustomProgressDialog mLoadingDialog;
	private SpecialUserListAdapter mAdapter;
	private ArrayList<SpecificUserBean> dataList = new ArrayList<SpecificUserBean>();
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
			super.onCreate(savedInstanceState);
			mContext = getActivity();
			mLoadingDialog = new CustomProgressDialog(mContext);
			mAdapter = new SpecialUserListAdapter(mContext, dataList);
			mLoadingDialog.show();
			new GetMatchestSpecialList().execute();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.activity_get_follow_user_layout,null);
		mAdapter = new SpecialUserListAdapter(mContext, dataList);
		mListView = (ListView) view.findViewById(R.id.attention_lv);
		mListView.setAdapter(mAdapter);
		return view;
	}
	

	
	private class GetMatchestSpecialList extends AsyncTask<Void, Void, ArrayList<SpecificUserBean>>{
		@Override
		protected ArrayList<SpecificUserBean> doInBackground(Void... params) {
			return Utils.getFollowSpecificUserList();
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
