package com.dcy.psychology.xinzeng;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dcy.psychology.R;

public class DoctorPerfectActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_doctor_info_perfect_layout);
		RelativeLayout bottomLayout = (RelativeLayout) findViewById(R.id.rl_doctor_perfect);    
		bottomLayout.getBackground().setAlpha(230);
		
	}
}
