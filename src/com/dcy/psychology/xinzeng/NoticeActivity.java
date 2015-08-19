package com.dcy.psychology.xinzeng;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.dcy.psychology.R;

public class NoticeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_notice_layout);
		RelativeLayout bottomLayout = (RelativeLayout) findViewById(R.id.rl_notice);    
		bottomLayout.getBackground().setAlpha(230);
		
	}
}