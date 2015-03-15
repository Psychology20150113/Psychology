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
import android.content.Context;
import android.content.Intent;
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
	private TextView mTextView;
	private CustomCircleView mCircleView;
	
	private GrowModelBean bean;
	private int mLevel;
	private boolean isSpecial;
	private int themeIndex;
	private float outerRatio;
	private float innerRatio;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		getNewestOpration();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_style2_main_layout, null);
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
		ArrayList<ArrayList<GrowModelBean>> jsonList = null;
		int itemIndex = 0;
		while (mCursor.moveToNext()) {
			isSpecial = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.LibTypeKey)) == 1;
			mLevel = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.LevelKey));
			themeIndex = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.ThemeIndexKey));
			itemIndex = mCursor.getInt(mCursor.getColumnIndex(SqlConstants.IndexKey));
			jsonList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, isSpecial ? 
				R.raw.more_grow_train_lib : R.raw.grow_train_lib), 
				new TypeToken<ArrayList<ArrayList<GrowModelBean>>>(){}.getType());
			bean = jsonList.get(themeIndex).get(mLevel);
			break;
		}
		if(jsonList == null)
			return;
		outerRatio = ((float)mLevel)/jsonList.get(themeIndex).size();
		Log.i("mylog4", "level : " + mLevel + "size : " +jsonList.size());
		Log.i("mylog4", outerRatio + "");
		innerRatio = ((float)itemIndex)/bean.getCount();
		Log.i("mylog4", "itemIndex : " + itemIndex + "count : " +bean.getCount());
		Log.i("mylog4", innerRatio + "");
	}
	
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.circle_view:
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
