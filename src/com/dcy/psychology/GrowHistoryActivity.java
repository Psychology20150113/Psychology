package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.GrowWriteAdapter;
import com.dcy.psychology.db.DbHelper;
import com.dcy.psychology.db.SqlConstants;
import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.model.GrowWriteItem;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.umeng.socialize.sso.UMSsoHandler;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class GrowHistoryActivity extends BaseActivity {
	private GrowModelBean bean;
	private DbHelper mDbHelper;
	private ArrayList<GrowWriteItem> dataList;
	private int themeIndex;
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow_history_layout);
		setTopTitle(R.string.grow_history);
		setRightView2(R.drawable.icon_share);
		mDbHelper = new DbHelper(this, SqlConstants.DBName, SqlConstants.DbVersion, SqlConstants.CreateTableSql);
		bean = (GrowModelBean) getIntent().getSerializableExtra(Constants.GrowModelBean);
		themeIndex = getIntent().getIntExtra(Constants.ThemeIndex, 0);
		level = getIntent().getIntExtra(Constants.Level, 0);
		ListView mHistoryView = (ListView) findViewById(R.id.history_lv);
		dataList = pullDbData(bean.getType(), bean.getMission());
		GrowWriteAdapter mAdapter = new GrowWriteAdapter(this, dataList);
		mHistoryView.setAdapter(mAdapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	@Override
	public void onRightView2Click() {
		showSharePopupWindow();
	}
	
	private String getShareContent(){
		if(bean == null){
			return "";
		}
		StringBuilder content = new StringBuilder();
		if(dataList != null && dataList.size() > 0){
			for(GrowWriteItem item : dataList){
				content.append(item.getIndex()).append("  ");
				if(!TextUtils.isEmpty(item.getContent()))
					content.append(item.getContent()).append("  ");
				if(!TextUtils.isEmpty(item.getDegree()))
					content.append(item.getDegree());
				content.append("\n");
			}
		}
		return bean.getMission() + "\n" + content.toString();
	}

	@Override
	protected void shareToOurs() {
		if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
			Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		final EditText editText = new EditText(this);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.think_title).
		setView(editText).
		setNegativeButton(R.string.cancel, null).
		setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(TextUtils.isEmpty(editText.getText().toString())){
					Toast.makeText(GrowHistoryActivity.this, R.string.think_title, Toast.LENGTH_SHORT).show();
					return;
				}
				new PublishCommentTask().execute(getShareContent() + editText.getText().toString());
			}
		}).show();
	}
	
	@Override
	protected void shareToCircle() {
		mShareUtils.shareToCircle(getShareContent());
	}
	
	@Override
	protected void shareToSina() {
		mShareUtils.shareToSina(getShareContent());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mShareUtils.getController().getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	private class PublishCommentTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			if(params.length < 1)
				return null;
			return Utils.publishComment(MyApplication.myPhoneNum, params[0], Constants.IdOfGrowMode[themeIndex]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(TextUtils.isEmpty(result))
				return;
			if("OK".equals(result)){
				Toast.makeText(GrowHistoryActivity.this, R.string.publish_success, Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(GrowHistoryActivity.this, R.string.publish_failed, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private ArrayList<GrowWriteItem> pullDbData(String type, String mission){
		ArrayList<GrowWriteItem> list = new ArrayList<GrowWriteItem>();
		Cursor cursor = mDbHelper.query(SqlConstants.SelectSql, mission, String.valueOf(level));
		if(cursor.getCount() == 0)
			return list;
		if(GrowModelBean.Type_Write.equals(type)){
			while (cursor.moveToNext()) {
				GrowWriteItem item = new GrowWriteItem();
				item.setIndex(cursor.getInt(
						cursor.getColumnIndex(SqlConstants.IndexKey)));
				String content = cursor.getString(
						cursor.getColumnIndex(SqlConstants.DescriptionKey));
				String[] splitContent = content.split("\\^");
				if(splitContent.length > 1){
					item.setContent(splitContent[0]);
					item.setDegree(splitContent[1]);
				}else {
					item.setContent(content);
				}
				list.add(item);
			}
		}else if(GrowModelBean.Type_SingleMission.equals(type)){
			while (cursor.moveToNext()) {
				GrowWriteItem item = new GrowWriteItem();
				item.setIndex(cursor.getInt(
						cursor.getColumnIndex(SqlConstants.IndexKey)));
				item.setDegree(cursor.getString(
						cursor.getColumnIndex(SqlConstants.DescriptionKey)));
				list.add(item);
			}
		}else if(GrowModelBean.Type_MutiWrite.equals(type) || GrowModelBean.Type_MutiMission.equals(type)){
			StringBuilder contentString = new StringBuilder();
			while (cursor.moveToNext()) {
				GrowWriteItem item = new GrowWriteItem();
				item.setIndex(cursor.getInt(
						cursor.getColumnIndex(SqlConstants.IndexKey)));
				String content = cursor.getString(
						cursor.getColumnIndex(SqlConstants.DescriptionKey));
				String[] splitContent = content.split("\\^");
				if(splitContent.length < 2)
					continue;
				for(int i = 0 ; i < splitContent.length ; i ++){
					contentString.append(splitContent[i]).append("\n");
				}
				item.setContent(contentString.substring(0, contentString.length() - 1));
				list.add(item);
				contentString.delete(0, contentString.length());
			}
		}
		cursor.close();
		return list;
	}
}
