package com.dcy.psychology.view.dialog;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.view.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class ShowHolledDialog extends Dialog implements android.view.View.OnClickListener{
	private RadarChart mChart;
	private Context mContext;
	private String[] careerArray;
	private String[] dataArray;
	private String result;
	private View.OnClickListener mSureClickListener;
	private String leftString;
	private String rightString;
	
	public ShowHolledDialog(Context mContext, String data, String result) {
		super(mContext, R.style.AppDialog);
		this.mContext = mContext;
		careerArray = mContext.getResources().getStringArray(R.array.zhiye_array);
		this.result = result;
		if(!TextUtils.isEmpty(data)){
			dataArray = data.split(",");
		}
	}
	
	public void setButtonString(String left, String right){
		leftString = left;
		rightString = right;
	}
	
	public void setSureClickListener(android.view.View.OnClickListener onClickListener){
		mSureClickListener = onClickListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_show_test_layout);
		mChart = (RadarChart) findViewById(R.id.dialog_chart);
		mChart.setMarkerView(new MyMarkerView(mContext, R.layout.custom_marker_view));
		mChart.setDescription("");
		mChart.getYAxis().setEnabled(false);
		mChart.getLegend().setEnabled(false);
		setData();
		((TextView)findViewById(R.id.dialog_tv_result)).setText(result);
		TextView leftButton = (TextView)findViewById(R.id.dialog_left_tv);
		if(!TextUtils.isEmpty(leftString)){
			leftButton.setText(leftString);
			leftButton.setOnClickListener(this);
			findViewById(R.id.dialog_ok_tv).setVisibility(View.GONE);
			findViewById(R.id.ll_two_buttons).setVisibility(View.VISIBLE);
			TextView rightButton = (TextView)findViewById(R.id.dialog_right_tv);
			rightButton.setText(rightString);
			rightButton.setOnClickListener(this);
		} else {
			findViewById(R.id.dialog_ok_tv).setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok_tv:
		case R.id.dialog_left_tv:
			dismiss();
			break;
		case R.id.dialog_right_tv:
			dismiss();
			if(mSureClickListener != null){
				mSureClickListener.onClick(v);
			}
			break;
		default:
			break;
		}
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
