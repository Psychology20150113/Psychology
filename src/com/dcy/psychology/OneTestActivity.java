package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.view.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.os.Bundle;

public class OneTestActivity extends Activity{
	private RadarChart mChart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mChart = new RadarChart(this);
		mChart.setMarkerView(new MyMarkerView(this, R.layout.custom_marker_view));
		setContentView(mChart);
		setData();
	}
	
	 public void setData() {

	        float mult = 150;
	        int cnt = 9;

	        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

	        // IMPORTANT: In a PieChart, no values (Entry) should have the same
	        // xIndex (even if from different DataSets), since no values can be
	        // drawn above each other.
	        for (int i = 0; i < cnt -1; i++) {
	            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
	        }

	        ArrayList<String> xVals = new ArrayList<String>();

	        for (int i = 0; i < cnt; i++)
	            xVals.add(mParties[i % mParties.length]);

	        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
	        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
	        set1.setDrawFilled(true);
	        set1.setLineWidth(2f);

	        RadarData data = new RadarData(xVals, set1);
	        data.setValueTextSize(8f);
	        data.setDrawValues(false);

	        mChart.setData(data);

	        mChart.invalidate();
	    }
	 
	 private String[] mParties = new String[] {
	            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F"
	    };

}
