package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.GrowDetailActivity;
import com.dcy.psychology.GrowLevelChooseActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.PlatformTwoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.StudentGrowActivity;
import com.dcy.psychology.db.DbHelper;
import com.dcy.psychology.db.SqlConstants;
import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomCircleView;
import com.google.gson.reflect.TypeToken;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class StyleTwoMainFragment extends Fragment implements OnClickListener{
	private Context mContext;
	private TextView mThemeNameView;
	private TextView mLevelNameView;
	private CustomCircleView mCircleView;
	
	private GrowModelBean bean;
	private int mLevel;
	private boolean isSpecial;
	private int themeIndex;
	private float outerRatio;
	private float innerRatio;
	private String[] labelArray;
	private String[] problemArray;
	private ArrayList<ArrayList<GrowModelBean>> jsonList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		labelArray = getResources().getStringArray(R.array.label_platform_array);
		problemArray = getResources().getStringArray(R.array.problem_array);
		getNewestOpration();
		mContext.registerReceiver(mRecordChangeReceiver, new IntentFilter(Constants.ReceiverAction_CursorChange));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_style2_main_layout, null);
		mThemeNameView = (TextView)view.findViewById(R.id.theme_name_tv);
		mThemeNameView.setText(isSpecial ? problemArray[themeIndex] : labelArray[themeIndex]);
		mLevelNameView = (TextView)view.findViewById(R.id.level_name_tv);
		mLevelNameView.setText(bean == null ? "没有数据" : bean.getTitle());
		mCircleView = (CustomCircleView) view.findViewById(R.id.circle_view);
		mCircleView.setData(outerRatio, innerRatio);
		mCircleView.setOnClickListener(this);
		view.findViewById(R.id.platform_one_ll).setOnClickListener(this);
		view.findViewById(R.id.platform_two_ll).setOnClickListener(this);
		return view;
	}
	
	private void getNewestOpration(){
		DbHelper mDbHelper = new DbHelper(mContext, SqlConstants.DBName, SqlConstants.DbVersion, SqlConstants.CreateTableSql);
		Cursor mCursor = mDbHelper.query(SqlConstants.SelectNewRecordsSql);
		if(mCursor.getCount() == 0){
			Toast.makeText(mContext, "没有数据", Toast.LENGTH_LONG).show();
			return;
		}
		int itemIndex = 0;
		while (mCursor.moveToNext()) {
			isSpecial = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.LibTypeKey)) == 1;
			mLevel = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.LevelKey));
			themeIndex = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.ThemeIndexKey));
			itemIndex = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.IndexKey));
//			if(jsonList == null){
				jsonList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, isSpecial ? 
						R.raw.more_grow_train_lib : R.raw.grow_train_lib), 
						new TypeToken<ArrayList<ArrayList<GrowModelBean>>>(){}.getType());
//			}
			bean = jsonList.get(themeIndex).get(mLevel);
			break;
		}
		if(jsonList == null)
			return;
		outerRatio = ((float)mLevel)/jsonList.get(themeIndex).size();
		innerRatio = ((float)itemIndex)/bean.getCount();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mRecordChangeReceiver);
	}
	
	private BroadcastReceiver mRecordChangeReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constants.ReceiverAction_CursorChange.equals(intent.getAction())){
				getNewestOpration();
				mCircleView.setData(outerRatio, innerRatio);
				mThemeNameView.setText(isSpecial ? problemArray[themeIndex] : labelArray[themeIndex]);
				mLevelNameView.setText(bean == null ? "没有数据" : bean.getTitle());
			}
		}
		
	};
	
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.circle_view:
			if(bean == null){
				Toast.makeText(mContext, R.string.not_data_warning, Toast.LENGTH_SHORT).show();
				return;
			}
			mIntent = new Intent(mContext, GrowDetailActivity.class);
			mIntent.putExtra(Constants.GrowModelBean, bean);
			mIntent.putExtra(Constants.Level, mLevel);
			mIntent.putExtra(Constants.IsSpecial, isSpecial);
			mIntent.putExtra(Constants.ThemeIndex, themeIndex);
			break;
		case R.id.platform_one_ll:
			mIntent = new Intent(mContext, StudentGrowActivity.class);
			break;
		case R.id.platform_two_ll:
			mIntent = new Intent(mContext, PlatformTwoActivity.class);
			break;
		default:
			break;
		}
		startActivityForResult(mIntent , 0);
	};
}
