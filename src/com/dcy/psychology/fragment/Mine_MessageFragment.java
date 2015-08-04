package com.dcy.psychology.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.MessageAdapter;
import com.dcy.psychology.model.MessageModel;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

public class Mine_MessageFragment extends Fragment{
	private CustomProgressDialog mLoadingDialog;
	private Context mContext;
	private ArrayList<MessageModel> dataList = new ArrayList<MessageModel>();
	private MessageAdapter mAdapter;
	
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mContext = getActivity();
			mLoadingDialog = new CustomProgressDialog(mContext);
			mAdapter = new MessageAdapter(mContext, dataList);
			mLoadingDialog.show();
			new GetMessageList().execute();
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_mine_message, null);
			ListView mListView = (ListView) view.findViewById(R.id.lv_mine_message);
			mListView.setAdapter(mAdapter);
			return view;
		}

	private class GetMessageList extends AsyncTask<Void, Void, ArrayList<MessageModel>>{
		@Override
		protected ArrayList<MessageModel> doInBackground(Void... params) {
			return Utils.getMessageList();
		}
		
		@Override
		protected void onPostExecute(ArrayList<MessageModel> result) {
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			dataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}

}
