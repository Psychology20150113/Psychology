package com.dcy.psychology.xinzeng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.dcy.psychology.R;

public class DoctorChangePwdActivity extends Activity{
	private Button mBtnnext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_doctor_change_pwd_layout);
		RelativeLayout bottomLayout = (RelativeLayout) findViewById(R.id.rl_amend_initial_password);    
		bottomLayout.getBackground().setAlpha(230);
		mBtnnext =(Button) findViewById(R.id.btn_change_psw_next);
		mBtnnext.setOnClickListener(new View.OnClickListener(){ 
		      @Override 
		      public void onClick(View v){		         
		          Intent mIntent = new Intent(DoctorChangePwdActivity.this , NoticeActivity.class);
		          startActivity(mIntent); 
		          finish();
		      } 
		});
		
	}

}
