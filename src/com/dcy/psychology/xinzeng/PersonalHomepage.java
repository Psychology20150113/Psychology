package com.dcy.psychology.xinzeng;

import org.apache.http.entity.mime.content.ContentBody;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.PersonalInfoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.view.dialog.IndividualDialog;
import com.dcy.psychology.view.dialog.ShareMatchDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class PersonalHomepage extends BaseActivity implements OnClickListener
{
	Context context;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homepage_layout);
        setTopTitle(R.string.personal_homepage);
        findViewById(R.id.ll_edit).setOnClickListener(this);
        findViewById(R.id.iv_individual).setOnClickListener(this);
        
    }

    @Override
   	public void onClick(View v) {
   		Intent mIntent = null;
   		switch (v.getId())
   		{
   		case R.id.ll_edit:
   			mIntent = new Intent(this, PersonalInfoActivity.class);
   	   		
   			startActivity(mIntent);
   			break;
   		case R.id.iv_individual:
   			mIntent = new Intent(this, IndividualDialog.class);
   			startActivity(mIntent);
   			break;
   			//new IndividualDialog(this).show();
   		}
   			
   		
   	}
}