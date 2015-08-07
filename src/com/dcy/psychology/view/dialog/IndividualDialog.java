package com.dcy.psychology.view.dialog;

import com.dcy.psychology.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class IndividualDialog extends Activity{

/*	Context context;
	
	public IndividualDialog(Context context) {
		super(context);
		this.context=context;
		// TODO �Զ���ɵĹ��캯����
	}*/

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