package com.dcy.psychology.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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

import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.LoginActivity.ChatLoginTask;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.ChatAdapter;
import com.dcy.psychology.model.ChatItemModel;
import com.dcy.psychology.util.IMManager;
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
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
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
		mManager = IMManager.getInstance();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mManager.getChatMessage(mHandler, doctorAccount);
			}
		}, 3000);
		mContext = getActivity();
		doctorAccount = mContext.getResources().getString(R.string.doctor_account);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_chat_layout, null);
		initView(view);
		return view;
	}
	
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
				mManager.getChatMessage(mHandler, doctorAccount);
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
