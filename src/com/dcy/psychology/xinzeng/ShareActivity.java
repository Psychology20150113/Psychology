package com.dcy.psychology.xinzeng;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dcy.psychology.R;

public class ShareActivity extends Activity implements OnClickListener{
	private ImageView mfinish;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_share_layout);
		RelativeLayout bottomLayout = (RelativeLayout) findViewById(R.id.rl_share);    
		bottomLayout.getBackground().setAlpha(200);
		mfinish=(ImageView) findViewById(R.id.iv_finish);
		mfinish.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.iv_finish:
	   			finish();
	   			break;
		
		default:
			break;
		}
		
	}

}