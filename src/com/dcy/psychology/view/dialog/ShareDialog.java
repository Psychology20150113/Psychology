package com.dcy.psychology.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.dcy.psychology.R;

public class ShareDialog extends Dialog{

/*	Context context;
	
	public IndividualDialog(Context context) {
		super(context);
		this.context=context;
		// TODO �Զ���ɵĹ��캯����
	}*/

	public ShareDialog(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //�����ޱ���  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //����ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.dialog_individual_layout);
		LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.ll_individual);    
		bottomLayout.getBackground().setAlpha(60);
		
	}

}