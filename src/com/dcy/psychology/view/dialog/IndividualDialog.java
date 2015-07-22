package com.dcy.psychology.view.dialog;

import com.dcy.psychology.R;
import com.dcy.psychology.view.MyMarkerView;
import com.github.mikephil.charting.charts.RadarChart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndividualDialog extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_individual_layout);
		LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.ll_individual);    
		bottomLayout.getBackground().setAlpha(94);
		
	}

}