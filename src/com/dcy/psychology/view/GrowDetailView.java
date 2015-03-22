package com.dcy.psychology.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dcy.psychology.GrowDetailActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.GrowWriteAdapter;
import com.dcy.psychology.db.DbHelper;
import com.dcy.psychology.db.SqlConstants;
import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.model.GrowWriteItem;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GrowDetailView extends LinearLayout {
	private LayoutInflater mInflater;
	private Context mContext;
	private Resources mResources;
	private DisplayMetrics dm;
	private LayoutParams mMatchParams;
	private LayoutParams mAverageParams;
	private LayoutParams mRadioButtonParams;
	
	//single mode
	private LinearLayout mCheckLayout;
	private RadioGroup mRegreeRg;
	private EditText mInputEt;
	private int writeCount;
	private String mission;
	private String type;
	private int dailyMax;
	private int historyCount;

	//mutiWrite use
	private TextView mTabLeftView;
	private TextView mTabRightView;
	
	private ArrayList<RadioGroup> mutiMissionList;
	private DbHelper mDbHelper;
	
	private String key;
	private int mLibType;
	private int themeIndex;
	private int mLevel;
	private InfoShared mShared;
	private int savedLevel;
	
	public GrowDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mShared = new InfoShared(context);
		mResources = mContext.getResources();
		mInflater = LayoutInflater.from(context);
		dm = mResources.getDisplayMetrics();
		mMatchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mRadioButtonParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(40*dm.density));
		mAverageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		mAverageParams.setMargins((int)(10*dm.density), 0, 0,(int)(10 * dm.density));
		mDbHelper = new DbHelper(mContext, SqlConstants.DBName, SqlConstants.DbVersion, SqlConstants.CreateTableSql);
	}

	public GrowDetailView(Context context) {
		this(context, null);
	}
	
	private class PublishCommentTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			if(params.length < 1)
				return null;
			return Utils.publishComment(MyApplication.myUserName, params[0], Constants.IdOfGrowMode[themeIndex]);
		}
		
		protected void onPostExecute(String result) {
			if(TextUtils.isEmpty(result))
				return;
			if("OK".equals(result)){
				Toast.makeText(mContext, R.string.publish_success, Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(mContext, R.string.publish_failed, Toast.LENGTH_SHORT).show();
			}
		};
	};

	public void setContent(GrowModelBean bean){
		type = bean.getType();
		mission = bean.getMission();
		if(TextUtils.isEmpty(mission) || TextUtils.isEmpty(type))
			return;
		dailyMax = bean.getDailyMax();
		if(GrowModelBean.Type_Link.equals(bean.getType())){
			
		}else if(GrowModelBean.Type_SingleMission.equals(bean.getType())){
			initSingleMissionLayout(bean);
		}else if(GrowModelBean.Type_Write.equals(bean.getType())){
			initWriteLayout(bean);
		}else if(GrowModelBean.Type_MutiWrite.equals(bean.getType())){
			initMutiWriteLayout(bean);
		}else if(GrowModelBean.Type_MutiMission.equals(bean.getType())){
			initMutiMissionLayout(bean);
		}
//		View thinkBtn = this.findViewById(R.id.think_btn);
//		if(thinkBtn == null)
//			return;
//		thinkBtn.setOnClickListener(mThinkClickListener);
	}
	
//	private OnClickListener mThinkClickListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			final EditText editText = new EditText(mContext);
//			Builder builder = new Builder(mContext);
//			builder.setTitle(R.string.think_title).
//			setView(editText).
//			setNegativeButton(R.string.cancel, null).
//			setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					if(TextUtils.isEmpty(editText.getText().toString())){
//						Toast.makeText(mContext, R.string.think_title, Toast.LENGTH_SHORT).show();
//						return;
//					}
//					StringBuilder content = new StringBuilder();
//					if(dataList != null && dataList.size() > 0){
//						for(GrowWriteItem item : dataList){
//							content.append(item.getIndexString()).append("  ");
//							if(!TextUtils.isEmpty(item.getContent()))
//								content.append(item.getContent()).append("  ");
//							if(!TextUtils.isEmpty(item.getDegree()))
//								content.append(item.getDegree());
//							content.append("\n");
//						}
//					}
//					new PublishCommentTask().execute(mission + "\n" + content.toString() + editText.getText().toString());
//				}
//			}).show();
//		}
//	};
	
	public void saveCompeLevel(boolean isSpecial , int themeIndex , int level){
		key = String.format(mShared.ThemeFormat, String.valueOf(isSpecial) , themeIndex);
		this.mLevel = level;
		this.savedLevel = mShared.getInt(key);
		this.themeIndex = themeIndex;
		this.mLibType = isSpecial ? 1 : 0;
	}

	private void initWriteLayout(GrowModelBean bean) {
		View writeView = mInflater.inflate(R.layout.grow_detail_write_layout, null);
		writeView.setLayoutParams(mMatchParams);
		((TextView) writeView.findViewById(R.id.mission_tv)).setText(mission);
		mInputEt = (EditText) writeView.findViewById(R.id.input_et);
		mRegreeRg = (RadioGroup) writeView.findViewById(R.id.check_rg);
		mCheckLayout = (LinearLayout) writeView.findViewById(R.id.check_ll);
		writeCount = bean.getCount();
		historyCount = getHistoryCount(type, mission);
		if(historyCount < writeCount){
			if(TextUtils.isEmpty(bean.getCheckTitle())){
				mCheckLayout.setVisibility(View.GONE);
			}else {
				((TextView)writeView.findViewById(R.id.check_title_tv)).setText(bean.getCheckTitle());
				for(String item : bean.getCheckItem()){
					RadioButton itemBtn = new RadioButton(mContext);
					itemBtn.setPadding((int)(10*dm.density), 0, (int)(10*dm.density), 0);
					itemBtn.setLayoutParams(mAverageParams);
					itemBtn.setButtonDrawable(R.drawable.bg_trans_shape);
					itemBtn.setBackgroundResource(R.drawable.bg_checked_item_selector);
					itemBtn.setText(item);
					mRegreeRg.addView(itemBtn);
				}
			}
		}else {
			mInputEt.setVisibility(View.GONE);
			mCheckLayout.setVisibility(View.GONE);
		}
		writeView.findViewById(R.id.commit_btn).setOnClickListener(mWriteCommitListener);
		this.addView(writeView);
	}
	
	private OnClickListener mWriteCommitListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(checkEmptyOrFull(mInputEt))
				return;
			GrowWriteItem item = new GrowWriteItem();
			item.setContent(mInputEt.getText().toString());
			if(mCheckLayout.getVisibility() == View.VISIBLE){
				for(int i = 0 ; i < mRegreeRg.getChildCount() ; i ++){
					RadioButton child = (RadioButton)mRegreeRg.getChildAt(i);
					if(child.isChecked()){
						item.setDegree(child.getText().toString());
						break;
					}
				}
				if(TextUtils.isEmpty(item.getDegree())){
					Toast.makeText(mContext, R.string.please_choose_degree, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			item.setIndex(historyCount + 1);
			historyCount ++;
			insertDataToDb(item, type, mission);
			Toast.makeText(mContext, R.string.complete_once, Toast.LENGTH_SHORT).show();
			mInputEt.setText("");
			if(historyCount == writeCount){
				mInputEt.setVisibility(View.GONE);
				mCheckLayout.setVisibility(View.GONE);
				if(mContext instanceof GrowDetailActivity)
					((Activity)mContext).setResult(1, new Intent());
				mShared.putInt(key, mLevel + 1);
				hideSoftInput(mInputEt);
			}
		}
	};
	
	//mutiWrite
	private void initMutiWriteLayout(GrowModelBean bean){
		LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.grow_detail_muti_write_layout, null);
		view.setLayoutParams(mMatchParams);
		mTabLeftView = (TextView) view.findViewById(R.id.mission_title_left);
		mTabLeftView.setSelected(true);
		mTabRightView = (TextView) view.findViewById(R.id.mission_title_right);
		((TextView) view.findViewById(R.id.mission_tv)).setText(bean.getMission());
		mTabLeftView.setText(bean.getMissionDetail().get(0));
		mTabRightView.setText(bean.getMissionDetail().get(1));
		mInputEt = (EditText) view.findViewById(R.id.input_et);
		writeCount = bean.getCount();
		historyCount = getHistoryCount(type, mission);
		if(historyCount >= writeCount){
			mInputEt.setVisibility(View.GONE);
		}
		view.findViewById(R.id.commit_btn).setOnClickListener(mutiWriteListener);
		this.addView(view);
	}
	
	private OnClickListener mutiWriteListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(checkEmptyOrFull(mInputEt))
				return;
			if(mTabLeftView.isSelected()){
				mTabLeftView.setTag(mInputEt.getText().toString());
				mTabLeftView.setSelected(false);
				mTabRightView.setSelected(true);
				mInputEt.setText("");
				return;
			} else {
				mTabLeftView.setSelected(true);
				mTabRightView.setSelected(false);
				GrowWriteItem item = new GrowWriteItem();
				item.setIndex(historyCount + 1);
				item.setContent(String.format("%s^%s", mTabLeftView.getTag().toString(),
						mInputEt.getText().toString()));
				historyCount ++;
				insertDataToDb(item, type, mission);
				Toast.makeText(mContext, R.string.complete_once, Toast.LENGTH_SHORT).show();
				mInputEt.setText("");
			}
			if(historyCount == writeCount){
				mInputEt.setVisibility(View.GONE);
				mCheckLayout.setVisibility(View.GONE);
				if(mContext instanceof GrowDetailActivity)
					((Activity)mContext).setResult(1, new Intent());
				mShared.putInt(key, mLevel + 1);
				hideSoftInput(mInputEt);
			}
		}
	};
	
	private void initSingleMissionLayout(GrowModelBean bean){
		View view = mInflater.inflate(R.layout.grow_detail_mission_layout, null);
		view.setLayoutParams(mMatchParams);
		((TextView)view.findViewById(R.id.mission_tv)).setText(bean.getMission());
		writeCount = bean.getCount();
		mCheckLayout = (LinearLayout) view.findViewById(R.id.check_layout);
		historyCount = getHistoryCount(type, mission);
		if(historyCount < writeCount){
			((TextView) view.findViewById(R.id.check_title_tv)).setText(bean.getCheckTitle()+":");
			mRegreeRg = (RadioGroup) view.findViewById(R.id.check_rg);
			for(String checkItem : bean.getCheckItem()){
				RadioButton button = new RadioButton(mContext);
				button.setPadding((int)(10*dm.density), 0, 0, 0);
				button.setLayoutParams(mRadioButtonParams);
				button.setButtonDrawable(R.drawable.bg_trans_shape);
				button.setBackgroundResource(R.drawable.bg_checked_item_selector);
				button.setText(checkItem);
				mRegreeRg.addView(button);
			}
		}else {
			mCheckLayout.setVisibility(View.GONE);
		}
		view.findViewById(R.id.commit_btn).setOnClickListener(singleMissionListener);
		this.addView(view);
	}

	private OnClickListener singleMissionListener = new OnClickListener() {
		@SuppressWarnings("unused")
		@Override
		public void onClick(View view) {
			if(historyCount == writeCount && mContext instanceof Activity){
				((Activity)mContext).finish();
				return;
			}
			if(dailyMax > 0 && !Constants.DebugMode){
				Calendar mCalender = Calendar.getInstance();
				mCalender.set(Calendar.HOUR_OF_DAY, 0);
				mCalender.set(Calendar.MINUTE, 0);
				mCalender.set(Calendar.SECOND, 0);
				long startTime = mCalender.getTimeInMillis();
				Cursor cursor = mDbHelper.query(SqlConstants.SelectCountByTimeSql, mission ,String.valueOf(startTime), 
						String.valueOf(startTime + 24*60*60*1000));
				if(cursor.getCount() >= dailyMax){
					Toast.makeText(mContext, R.string.complete_today, Toast.LENGTH_SHORT).show();
					cursor.close();
					return;
				}
			}
			GrowWriteItem item = new GrowWriteItem();
			for(int i = 0 ; i < mRegreeRg.getChildCount() ; i ++){
				RadioButton child = (RadioButton)mRegreeRg.getChildAt(i);
				if(child.isChecked()){
					item.setDegree(child.getText().toString());
					break;
				}
			}
			if(TextUtils.isEmpty(item.getDegree())){
				Toast.makeText(mContext, R.string.please_choose_degree, Toast.LENGTH_SHORT).show();
				return;
			}
			item.setIndex(historyCount + 1);
			historyCount ++ ;
			insertDataToDb(item, type, mission);
			Toast.makeText(mContext, R.string.complete_once, Toast.LENGTH_SHORT).show();
			if(historyCount == writeCount){
				mCheckLayout.setVisibility(View.GONE);
				if(mContext instanceof GrowDetailActivity)
					((Activity)mContext).setResult(1, new Intent());
				mShared.putInt(key, mLevel + 1);
			}
		}
	};
	
	private void initMutiMissionLayout(GrowModelBean bean){
		View view = mInflater.inflate(R.layout.grow_detail_muti_mission_layout, null);
		view.setLayoutParams(mMatchParams);
		((TextView)view.findViewById(R.id.mission_tv)).setText(bean.getMission());
		writeCount = bean.getCount();
		mCheckLayout = (LinearLayout) view.findViewById(R.id.check_layout);
		historyCount = getHistoryCount(type, mission);
		if(historyCount < writeCount){
			mutiMissionList = new ArrayList<RadioGroup>();
			for(int i = 0 ; i < bean.getMissionDetail().size() ; i++){
				View itemView = mInflater.inflate(R.layout.item_mission_check_layout, null);
				if(bean.getMissionTips() != null){
					((TextView)itemView.findViewById(R.id.mission_detail_tv)).setText(String.format(
							"%d %s----%s", i+1 ,bean.getMissionTips().get(i) , bean.getMissionDetail().get(i)));
				} else {
					((TextView)itemView.findViewById(R.id.mission_detail_tv)).setText(String.format(
							"%d %s", i+1 , bean.getMissionDetail().get(i)));
				}
				((TextView)itemView.findViewById(R.id.check_title_tv)).setText(bean.getCheckTitle());
				RadioGroup itemGroup = (RadioGroup) itemView.findViewById(R.id.check_rg);
				itemGroup.setOrientation(VERTICAL);
				mutiMissionList.add(itemGroup);
				for(String checkItem : bean.getCheckItem()){
					RadioButton itemButton = new RadioButton(mContext);
					itemButton.setPadding((int)(10*dm.density), 0, 0, 0);
					itemButton.setLayoutParams(mRadioButtonParams);
					itemButton.setButtonDrawable(R.drawable.bg_trans_shape);
					itemButton.setBackgroundResource(R.drawable.bg_checked_item_selector);
					itemButton.setText(checkItem);
					itemGroup.addView(itemButton);
				}
				mCheckLayout.addView(itemView);
			}
		}else {
		}
		view.findViewById(R.id.commit_btn).setOnClickListener(mutiMissionListener);
		this.addView(view);
	}
	
	private OnClickListener mutiMissionListener = new OnClickListener() {
		@SuppressWarnings("unused")
		@Override
		public void onClick(View view) {
			if(historyCount == writeCount && mContext instanceof Activity){
				((Activity)mContext).finish();
				return;
			}
//			int goodPoint = 0;
//			boolean pass = true;
			StringBuilder content = new StringBuilder();
			for(int i = 0 ; i < mutiMissionList.size() ; i ++){
				RadioGroup groupItem = mutiMissionList.get(i);
				if(groupItem.getCheckedRadioButtonId() == -1){
					Toast.makeText(mContext, R.string.please_choose_complete, Toast.LENGTH_SHORT).show();
					return;
				}
				RadioButton checkedItem = (RadioButton)groupItem.findViewById(groupItem.getCheckedRadioButtonId());
				content.append(checkedItem.getText()).append("^");
//				if(((RadioButton)groupItem.getChildAt(0)).isChecked())
//					goodPoint ++;
//				if(((RadioButton)groupItem.getChildAt(groupItem.getChildCount() - 1)).isChecked())
//					pass = false;
			}
			if(dailyMax > 0 && !Constants.DebugMode){
				Calendar mCalender = Calendar.getInstance();
				mCalender.set(Calendar.HOUR_OF_DAY, 0);
				mCalender.set(Calendar.MINUTE, 0);
				mCalender.set(Calendar.SECOND, 0);
				long startTime = mCalender.getTimeInMillis();
				Cursor cursor = mDbHelper.query(SqlConstants.SelectCountByTimeSql, mission ,String.valueOf(startTime), 
						String.valueOf(startTime + 24*60*60*1000));
				if(cursor.getCount() >= dailyMax){
					Toast.makeText(mContext, R.string.complete_today, Toast.LENGTH_SHORT).show();
					cursor.close();
					return;
				}
			}
			GrowWriteItem item = new GrowWriteItem();
//			item.setDegree(pass && goodPoint >= mutiMissionList.size() - 1 ? 
//					mResources.getString(R.string.complete_mission) : mResources.getString(R.string.fail_mission));
			item.setContent(content.substring(0, content.length() - 1));
			item.setIndex(historyCount + 1);
			historyCount ++;
			insertDataToDb(item, type, mission);
			Toast.makeText(mContext, R.string.complete_once, Toast.LENGTH_SHORT).show();
			if(historyCount == writeCount){
				mCheckLayout.setVisibility(View.GONE);
				if(mContext instanceof GrowDetailActivity)
					((Activity)mContext).setResult(1, new Intent());
				mShared.putInt(key, mLevel + 1);
			}
		}
	};
	
	private void insertDataToDb(ArrayList<GrowWriteItem> list , String type , String mission){
		if(GrowModelBean.Type_Write.equals(type)){
			for(int i = 0 ; i < list.size() ; i ++){
				GrowWriteItem item = list.get(i);
				insertDataToDb(item, type, mission);
			}
		}
	}
	
	private void insertDataToDb(GrowWriteItem item , String type , String mission){
		ContentValues values = new ContentValues();
		values.put(SqlConstants.IndexKey, item.getIndex());
		values.put(SqlConstants.MissionKey, mission);
		if(GrowModelBean.Type_Write.equals(type)){
			if(TextUtils.isEmpty(item.getDegree())){
				values.put(SqlConstants.DescriptionKey, item.getContent());
			}else {
				values.put(SqlConstants.DescriptionKey, String.format("%s^%s", item.getContent(),item.getDegree()));
			}
		}else if(GrowModelBean.Type_SingleMission.equals(type)){
			values.put(SqlConstants.DescriptionKey, item.getDegree());
		}else if(GrowModelBean.Type_MutiWrite.equals(type) || GrowModelBean.Type_MutiMission.equals(type)){
			values.put(SqlConstants.DescriptionKey, item.getContent());
		}
		values.put(SqlConstants.MissionTypeKey, type);
		values.put(SqlConstants.LibTypeKey, mLibType);
		values.put(SqlConstants.ThemeIndexKey, themeIndex);
		values.put(SqlConstants.LevelKey, mLevel);
		values.put(SqlConstants.TimeKey, Calendar.getInstance().getTimeInMillis());
		mDbHelper.insert(SqlConstants.TableName, values);
		mContext.sendBroadcast(new Intent(Constants.ReceiverAction_CursorChange));
	}
	
	private int getHistoryCount(String type, String mission){
		Cursor cursor = mDbHelper.query(SqlConstants.SelectSql, mission);
		return cursor.getCount();
	}
	
	private ArrayList<GrowWriteItem> pullDbData(String type, String mission){
		ArrayList<GrowWriteItem> list = new ArrayList<GrowWriteItem>();
		Cursor cursor = mDbHelper.query(SqlConstants.SelectSql, mission);
		if(cursor.getCount() == 0)
			return null;
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
		}else if(GrowModelBean.Type_SingleMission.equals(type) || GrowModelBean.Type_MutiMission.equals(type)){
			while (cursor.moveToNext()) {
				GrowWriteItem item = new GrowWriteItem();
				item.setIndex(cursor.getInt(
						cursor.getColumnIndex(SqlConstants.IndexKey)));
				item.setDegree(cursor.getString(
						cursor.getColumnIndex(SqlConstants.DescriptionKey)));
				list.add(item);
			}
		}
		cursor.close();
		return list;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		mDbHelper.close();
		super.onDetachedFromWindow();
	}
	
	private boolean checkEmptyOrFull(EditText input){
//		if(data.size() == writeCount){
		if(historyCount == writeCount){
			//Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
			if(mContext instanceof Activity){
				((Activity) mContext).finish();
			}
			return true;
		}
		if(TextUtils.isEmpty(input.getText())){
			Toast.makeText(mContext, R.string.please_input, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
	
	private void hideSoftInput(EditText inputEt){
		InputMethodManager input = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		input.hideSoftInputFromWindow(inputEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
