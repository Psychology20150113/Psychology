package com.dcy.psychology.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dcy.psychology.LoginActivity.ChatLoginTask;
import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.ChatAdapter;
import com.dcy.psychology.model.ChatItemModel;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class ChatIMFragment extends Fragment implements OnClickListener{
	private ListView mListView;
	private EditText mEditText;
	private ChatAdapter mAdapter;
	private ArrayList<ChatItemModel> mDataList = new ArrayList<ChatItemModel>();
	private IMManager mManager;
	private Context mContext;
	private String doctorAccount;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(msg.obj == null){
					return;
				}
				ChatItemModel mModel = new ChatItemModel();
				mModel.setMine(false);
				mModel.setContext(msg.obj.toString());
				mModel.setTime(System.currentTimeMillis());
				mDataList.add(mModel);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mDataList.size());
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mManager = IMManager.getInstance();
		doctorAccount = mContext.getResources().getString(R.string.doctor_account);
		if(!TextUtils.isEmpty(MyApplication.myPhoneNum)){
        	 new LoginActivity.ChatLoginTask(mContext).execute(MyApplication.myPhoneNum, MyApplication.myPwd);
		} 
		mContext.registerReceiver(mLoginSuccessReceiver, new IntentFilter(Constants.ReceiverAction_LoginSuccess));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_chat_layout, null);
		initView(view);
		return view;
	}
	
	private class GetOnlineDoctorTask extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			return Utils.getOnlineDoctor("test");
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(TextUtils.isEmpty(result)){
				return;
			}
			try {
				JSONObject resultInfo = new JSONObject(result);
				ChatItemModel mModel = new ChatItemModel();
				mModel.setMine(false);
				mModel.setContext(resultInfo.getString("info"));
				mModel.setTime(System.currentTimeMillis());
				mDataList.add(mModel);
				mAdapter.notifyDataSetChanged();
				doctorAccount = resultInfo.getString("doctor") + "@114.215.179.130";
//				doctorAccount = "1@114.215.179.130";
				mManager.getChatMessage(mHandler, doctorAccount);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mLoginSuccessReceiver);
	}
	
	private BroadcastReceiver mLoginSuccessReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constants.ReceiverAction_LoginSuccess.equals(intent.getAction())){
				new GetOnlineDoctorTask().execute();
			}
		}
	};
	
	private void initView(View view){
		mListView = (ListView) view.findViewById(R.id.chat_lv);
		mAdapter = new ChatAdapter(mContext, mDataList);
		mListView.setAdapter(mAdapter);
		mEditText = (EditText) view.findViewById(R.id.input_et);
		view.findViewById(R.id.send_btn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			if(TextUtils.isEmpty(mEditText.getText())){
				Toast.makeText(mContext, R.string.please_input, Toast.LENGTH_SHORT).show();
				return;
			}
			if(mManager.pushChatMessage(mEditText.getText().toString())){
				MobclickAgent.onEvent(mContext, "chat");
				ChatItemModel mModel = new ChatItemModel();
				mModel.setMine(true);
				mModel.setContext(mEditText.getText().toString());
				mModel.setTime(System.currentTimeMillis());
				mDataList.add(mModel);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mDataList.size());
				mEditText.setText("");
			}else {
				if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
					Toast.makeText(mContext, R.string.please_login, Toast.LENGTH_SHORT).show();
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
					return;
				}
				if(!mManager.isLogined()){
					final CustomProgressDialog progressDialog = new CustomProgressDialog(mContext);
					progressDialog.show();
					ChatLoginTask task = new LoginActivity.ChatLoginTask(mContext);
					task.setRunnable(new Runnable() {
						@Override
						public void run() {
							progressDialog.dismiss();
						}
					});
					task.execute(MyApplication.myPhoneNum, MyApplication.myPwd);
				}
//				Toast.makeText(mContext, R.string.send_msg_failed, Toast.LENGTH_SHORT).show();
//				Builder mBuilder = new Builder(mContext);
//				mBuilder.setTitle(R.string.connect_failed)
//				.setMessage(R.string.connect_failed_msg)
//				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//					}
//				}).setNegativeButton(R.string.cancel, null).show();
			}
			break;
		default:
			break;
		}
	}
}
