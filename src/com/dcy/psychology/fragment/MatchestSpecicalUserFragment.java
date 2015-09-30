package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.DoctorPersonalInfo2;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.Constants;
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

public class MatchestSpecicalUserFragment extends Fragment implements OnItemClickListener{
	private CustomProgressDialog mLoadingDialog;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList = new ArrayList<SpecificUserBean>();
	private SpecialUserListAdapter mAdapter;
	
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
		View view = inflater.inflate(R.layout.fragment_matchest_special_layout, null);
		ListView mListView = (ListView) view.findViewById(R.id.lv_container);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent = new Intent(mContext, DoctorPersonalInfo2.class);
		mIntent.putExtra(Constants.UserBean, dataList.get(position));
		mContext.startActivity(mIntent);
	}
	
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
