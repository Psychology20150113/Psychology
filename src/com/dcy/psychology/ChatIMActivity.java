package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.adapter.ChatAdapter;
import com.dcy.psychology.model.ChatItemModel;
import com.dcy.psychology.util.IMManager;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatIMActivity extends BaseActivity implements OnClickListener{
	private ListView mListView;
	private EditText mEditText;
	private ChatAdapter mAdapter;
	private ArrayList<ChatItemModel> mDataList = new ArrayList<ChatItemModel>();
	private IMManager mManager;
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
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_layout);
		initView();
		setTopTitle("1@114.215.179.130");
		mManager = IMManager.getInstance();
		mManager.getChatMessage(mHandler, "1@114.215.179.130");
	}
	
	private void initView(){
		mListView = (ListView) findViewById(R.id.chat_lv);
		mAdapter = new ChatAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);
		mEditText = (EditText) findViewById(R.id.input_et);
		findViewById(R.id.send_btn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			if(TextUtils.isEmpty(mEditText.getText())){
				Toast.makeText(this, R.string.please_input, Toast.LENGTH_SHORT).show();
				return;
			}
			if(mManager.pushChatMessage(mEditText.getText().toString())){
				ChatItemModel mModel = new ChatItemModel();
				mModel.setMine(true);
				mModel.setContext(mEditText.getText().toString());
				mModel.setTime(System.currentTimeMillis());
				mDataList.add(mModel);
				mAdapter.notifyDataSetChanged();
				mEditText.setText("");
			}else {
				Toast.makeText(this, R.string.send_msg_failed, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
