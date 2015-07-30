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
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class IndividualDialog extends Activity{

/*	Context context;
	
	public IndividualDialog(Context context) {
		super(context);
		this.context=context;
		// TODO 自动生成的构造函数存根
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.dialog_individual_layout);
		LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.ll_individual);    
		bottomLayout.getBackground().setAlpha(60);
		
	}

}