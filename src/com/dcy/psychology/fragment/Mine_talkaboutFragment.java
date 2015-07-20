package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.TalkingAdapter;
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

public class Mine_talkaboutFragment extends Fragment {
	private CustomProgressDialog mLoadingDialog;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList = new ArrayList<SpecificUserBean>();
	private TalkingAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mLoadingDialog = new CustomProgressDialog(mContext);
		mAdapter = new TalkingAdapter(mContext, dataList);
		mLoadingDialog.show();
		new GetMatchestSpecialList().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_talkabout, null);
		ListView mListView = (ListView) view.findViewById(R.id.lv_talkabout);
		mListView.setAdapter(mAdapter);
		return view;
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
