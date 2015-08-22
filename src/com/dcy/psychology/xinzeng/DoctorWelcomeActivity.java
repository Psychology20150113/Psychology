package com.dcy.psychology.xinzeng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcy.psychology.R;

public class DoctorWelcomeActivity extends Activity{
	private TextView mtextview;
	private Button mBtnnext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_welcome_layout);
		RelativeLayout bottomLayout = (RelativeLayout) findViewById(R.id.rl_welcome);    
		bottomLayout.getBackground().setAlpha(230);
		mtextview = (TextView) findViewById(R.id.tv_welcome);  
		mBtnnext =(Button) findViewById(R.id.btn_welcome_next);
		
		SpannableStringBuilder builder = new SpannableStringBuilder(mtextview.getText().toString()); 
		ForegroundColorSpan sp=new  ForegroundColorSpan(Color.parseColor("#ee833a")); 
		ForegroundColorSpan sp2=new  ForegroundColorSpan(Color.parseColor("#898989")); 
		builder.setSpan(sp, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		builder.setSpan(sp2, 4, builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		mtextview.setText(builder);
		mBtnnext.setOnClickListener(new View.OnClickListener(){ 
		      @Override 
		      public void onClick(View v){		         
		          Intent mIntent = new Intent(DoctorWelcomeActivity.this , DoctorChangePwdActivity.class);
		          startActivity(mIntent); 
		          finish();
		      } 
		});
		
	}

}
