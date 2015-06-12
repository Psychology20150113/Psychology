package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.DetailTestResultActivity;
import com.dcy.psychology.MineActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.ShowListActivity;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.view.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MineMarkFragment extends Fragment implements OnClickListener{
	private InfoShared mShared;
	private Context mContext;
	private RadarChart mChart;
	private String[] dataArray;
	private String[] careerArray;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mShared = new InfoShared(mContext);
		careerArray = mContext.getResources().getStringArray(R.array.zhiye_array);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_mark_layout, null);
		view.findViewById(R.id.tv_look_info).setOnClickListener(this);
		view.findViewById(R.id.tv_test).setOnClickListener(this);
		initZhiyeView(view);
		initQizhiView(view);
		return view;
	}

	private void initZhiyeView(View view) {
		mChart = (RadarChart) view.findViewById(R.id.view_chart);
		mChart.setMarkerView(new MyMarkerView(mContext, R.layout.custom_marker_view));
		mChart.setDescription("");
		mChart.getYAxis().setEnabled(false);
		mChart.getLegend().setEnabled(false);
		if(!TextUtils.isEmpty(mShared.getHollendResult())){
			((TextView)view.findViewById(R.id.tv_zhiye)).setText(String.format(
					getString(R.string.mine_zhiye), mShared.getHollendResult()));
			dataArray = mShared.getHollendData().split(",");
			setData();
		} else {
			view.findViewById(R.id.tv_empty_zhiye).setVisibility(View.VISIBLE);
			view.findViewById(R.id.tv_zhiye).setVisibility(View.GONE);
			mChart.setVisibility(View.GONE);
		}
	}
	
	private void initQizhiView(View view){
		if(!TextUtils.isEmpty(mShared.getQizhiResult())){
			String[] mQizhiData = mShared.getQizhiData().split(",");
			if(mQizhiData.length != 4){
				return;
			}
			((ProgressBar)view.findViewById(R.id.pb_qizhi_one)).setProgress(Integer.parseInt(mQizhiData[0]) * 100 / 15 );
			((ProgressBar)view.findViewById(R.id.pb_qizhi_two)).setProgress(Integer.parseInt(mQizhiData[1]) * 100 / 15 );
			((ProgressBar)view.findViewById(R.id.pb_qizhi_three)).setProgress(Integer.parseInt(mQizhiData[2]) * 100 / 15);
			((ProgressBar)view.findViewById(R.id.pb_qizhi_four)).setProgress(Integer.parseInt(mQizhiData[3]) * 100 / 15 );
			((TextView)view.findViewById(R.id.tv_point_one)).setText(mQizhiData[0]);
			((TextView)view.findViewById(R.id.tv_point_two)).setText(mQizhiData[1]);
			((TextView)view.findViewById(R.id.tv_point_three)).setText(mQizhiData[2]);
			((TextView)view.findViewById(R.id.tv_point_four)).setText(mQizhiData[3]);
			((TextView)view.findViewById(R.id.tv_qizhi)).setText(String.format(getString(R.string.mine_qizhi), mShared.getQizhiResult()));
		} else {
			view.findViewById(R.id.ll_qizhi_show).setVisibility(View.GONE);
			view.findViewById(R.id.tv_empty_qizhi).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.tv_look_info:
			mIntent = new Intent(mContext, DetailTestResultActivity.class);
			mIntent.putExtra(Constants.QizhiResult, mShared.getQizhiResult());
			mIntent.putExtra(Constants.ZhiyeResult, mShared.getHollendResult());
			break;
		case R.id.tv_test:
			((Activity)mContext).finish();
			mIntent = new Intent(mContext, ShowListActivity.class);
			mIntent.putExtra(Constants.ListType, Constants.DNAType);
			break;
		default:
			break;
		}
		startActivity(mIntent);
	}
	
	public void setData() {
	    if(dataArray == null){
	    	return;
	    }
	 	ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        
        for (int i = 0; i < careerArray.length; i++) {
        	yVals1.add(new Entry(Float.valueOf(dataArray[i]), i));
        }
        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);
        RadarData data = new RadarData(careerArray, set1);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.invalidate();
    }
}
