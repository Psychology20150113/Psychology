package com.dcy.psychology.adapter;

import com.dcy.psychology.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GameSeaLevelChooseAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutParams mLevelLayoutParams;
	
	public GameSeaLevelChooseAdapter(Context context){
		this.mContext = context;
		float density = mContext.getResources().getDisplayMetrics().density;
		int size = (int)(54 * density);
		mLevelLayoutParams = new LayoutParams(size, size);
	}
	
	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = new TextView(mContext);
			convertView.setLayoutParams(mLevelLayoutParams);
			((TextView)convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
			((TextView)convertView).setGravity(Gravity.CENTER);
			((TextView)convertView).setTextColor(Color.parseColor("#003644"));
			convertView.setBackgroundResource(R.drawable.bg_game_sea_level);
		}
		((TextView) convertView).setText(String.valueOf(position + 1));
		return convertView;
	}

}
